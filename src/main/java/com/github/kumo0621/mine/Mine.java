package com.github.kumo0621.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public final class Mine extends JavaPlugin implements Listener {

    public static FileConfiguration config;
    public static FileConfiguration moneyData;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        moneyData = loadConfig("money.yml");
        getServer().getPluginManager().registerEvents(this, this);
        Items.init();
    }

    private FileConfiguration loadConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);

        if (!configFile.exists()) {
            saveResource(fileName, false);
        }

        return YamlConfiguration.loadConfiguration(configFile);
    }

    private void saveCustomConfig(FileConfiguration config, String fileName) {
        try {
            File configFile = new File(getDataFolder(), fileName);
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        saveConfig();
        // moneyファイルを保存
        saveCustomConfig(moneyData, "money.yml");
    }

    @EventHandler
    public void joinPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // プレイヤーデータからフラグを取得
        boolean isFirstJoin = moneyData.getBoolean(uuid + ".firstJoin", true);

        if (isFirstJoin) {
            // 初回ログイン時の処理
            moneyData.set(uuid + ".firstJoin", false);
            moneyData.set(uuid + ".money", 0);


            // プレイヤーにアイテムを与える
            player.getInventory().addItem(Items.beginnerPickaxe);

            // 初回ログイン時のメッセージを送信
            player.sendMessage("初回ログインです。所持金が初期化されました。");
        }

        // 保存処理
        try {
            moneyData.save(new File(getDataFolder(), "money.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getBlock();
        if (block.getType() == Material.COAL_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int expAmount = config.getInt("coal.level");
            int moneyAmount = config.getInt("coal.money");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
            player.giveExp(expAmount);
        } else if(block.getType()==Material.NETHERRACK) {
            // 設定ファイルで許可されたブロックの場合の処理
            int expAmount = config.getInt("netherrack.level");
            int moneyAmount = config.getInt("netherrack.money");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
            player.giveExp(expAmount);
        } else if(block.getType()==Material.STONE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int expAmount = config.getInt("stone.level");
            int moneyAmount = config.getInt("stone.money");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
            player.giveExp(expAmount);
        }
        // configファイルに保存
        saveCustomConfig(moneyData, "money.yml");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return super.onCommand(sender, command, label, args);

        switch (command.getName()) {

            case "money" -> {
                int money = getMoney(player);
                sender.sendMessage("あなたの所持金は「" + money + "」です。");
            }

            // TODO : プレイヤー名が間違っているときのメッセージを追加する
            case "set" -> {
                if (args.length > 1) {
                    String playerName = args[0];
                    Player targetPlayer = Bukkit.getPlayer(playerName);
                    UUID uuid = Objects.requireNonNull(targetPlayer).getUniqueId();
                    moneyData.set(uuid + ".money", args[1]);
                    sender.sendMessage(args[0] + "さんの所持金を「" + args[1] + "」にしました。");
                } else {
                    sender.sendMessage("数値を指定してください。");
                }
            }
        }

        // configファイルに保存
        saveCustomConfig(moneyData, "money.yml");
        return super.onCommand(sender, command, label, args);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        if (action == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.CHEST) {
            UUID uuid = player.getUniqueId();
            int money = getMoney(player);
            if (money >= 1000) {
                int result = Integer.parseInt(String.valueOf(money - 1000));
                moneyData.set(uuid + ".money", result);
                event.setCancelled(true);
                int up = RandomCount.random();
                if (up < 99) {
                    ItemStack itemStack = new ItemStack(Material.COAL, 1); // 追加するアイテムの種類と個数を指定
                    player.getInventory().addItem(itemStack);
                    player.sendMessage("ハズレ");
                } else {
                    player.getInventory().addItem(Items.rarePickaxe);
                    player.sendMessage("レアツルハシゲット");
                }
            }
        } else if (action == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.SHULKER_BOX) {
                event.setCancelled(true);
                openMenu(player);
        }
        // configファイルに保存
        saveCustomConfig(moneyData, "money.yml");
    }

    public static int getMoney(Player player) {
        return Integer.parseInt(Objects.requireNonNull(moneyData.getString(player.getUniqueId() + ".money")));
    }

    public static int getLevel(Player player) {
        return player.getLevel();
    }
    public void openMenu(Player player) {
        // メニューを開く処理

        // メニューのインベントリを作成
        Inventory menu = Bukkit.createInventory(null, 9, "強化メニュー");

        // メニューのアイテムを設定
        ItemStack item = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("500");
        meta.setCustomModelData(2);
        meta.addEnchant(Enchantment.DIG_SPEED, 10, true);
        item.setItemMeta(meta);
        menu.setItem(0, item);
        ItemStack item2 = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta2 = item.getItemMeta();
        meta2.setDisplayName("3000");
        meta2.setCustomModelData(3);
        meta2.addEnchant(Enchantment.DIG_SPEED, 30, true);
        item2.setItemMeta(meta2);
        menu.setItem(1, item2);

        //メニュー表示
        player.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // プレイヤーがメニューをクリックしたときの処理
        if (event.getView().getTitle().equals("強化メニュー")) {
            // クリックしたインベントリがメニューであるかを確認

            event.setCancelled(true); // クリックイベントをキャンセル

            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.WOODEN_PICKAXE) {
                // クリックしたアイテムがダイヤモンドの剣であるかを確認
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if (meta != null && meta.getCustomModelData() == 2) {
                    Player player = (Player) event.getWhoClicked();
                    UUID uuid = player.getUniqueId();
                    int money = getMoney(player);
                    if (money >= 500) {
                        int result = Integer.parseInt(String.valueOf(money - 500));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.intermediatePickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
                if (meta != null && meta.getCustomModelData() == 3) {
                    Player player = (Player) event.getWhoClicked();
                    UUID uuid = player.getUniqueId();
                    int money = getMoney(player);
                    if (money >= 500) {
                        int result = Integer.parseInt(String.valueOf(money - 3000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.hardPickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
            }
        }
    }
}