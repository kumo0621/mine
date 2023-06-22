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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        } else if(block.getType()==Material.LAPIS_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int expAmount = config.getInt("lapis_ore.level");
            int moneyAmount = config.getInt("lapis_ore.money");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
            player.giveExp(expAmount);
        } else if(block.getType()==Material.GOLD_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int expAmount = config.getInt("gold_ore.level");
            int moneyAmount = config.getInt("gold_ore.money");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
            player.giveExp(expAmount);
        } else if(block.getType()==Material.IRON_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int expAmount = config.getInt("iron_ore.level");
            int moneyAmount = config.getInt("iron_ore.money");
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
                int up2 = RandomCount.random();
                if (up == 99) {
                    ItemStack itemStack = new ItemStack(Material.COAL, 1); // 追加するアイテムの種類と個数を指定
                    player.getInventory().addItem(itemStack);
                    player.sendMessage("ハズレ");
                }else if(up==87&&up2==87){
                    player.getInventory().addItem(Items.luckyPickaxe);
                    player.sendMessage("超レアツルハシゲット");
                }else if(up==87&&up2==2){
                    player.getInventory().addItem(Items.accessory_main);
                    player.sendMessage("マイニングのアクセサリーゲット");
                }else if(up==87&&up2==1){
                    player.getInventory().addItem(Items.accessory_speed);
                    player.sendMessage("スピードのアクセサリーゲット");
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
        meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
        item.setItemMeta(meta);
        menu.setItem(0, item);

        ItemStack item2 = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta2 = item.getItemMeta();
        meta2.setDisplayName("3000");
        meta2.setCustomModelData(3);
        meta2.addEnchant(Enchantment.DIG_SPEED, 2, true);
        item2.setItemMeta(meta2);
        menu.setItem(1, item2);

        ItemStack item3 = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta3 = item.getItemMeta();
        meta3.setDisplayName("10000");
        meta3.setCustomModelData(5);
        meta3.addEnchant(Enchantment.DIG_SPEED, 3, true);
        item3.setItemMeta(meta3);
        menu.setItem(2, item3);

        ItemStack item4 = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta4 = item.getItemMeta();
        meta4.setDisplayName("20000");
        meta4.setCustomModelData(6);
        meta4.addEnchant(Enchantment.DIG_SPEED, 4, true);
        item4.setItemMeta(meta4);
        menu.setItem(3, item4);

        ItemStack item5 = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta5 = item.getItemMeta();
        meta5.setDisplayName("40000");
        meta5.setCustomModelData(7);
        meta5.addEnchant(Enchantment.DIG_SPEED, 5, true);
        item5.setItemMeta(meta5);
        menu.setItem(4, item5);

        ItemStack item6 = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta6 = item.getItemMeta();
        meta6.setDisplayName("80000");
        meta6.setCustomModelData(8);
        meta6.addEnchant(Enchantment.DIG_SPEED, 7, true);
        item6.setItemMeta(meta6);
        menu.setItem(5, item6);

        ItemStack item7 = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta7 = item.getItemMeta();
        meta7.setDisplayName("160000");
        meta7.setCustomModelData(9);
        meta7.addEnchant(Enchantment.DIG_SPEED, 8, true);
        item7.setItemMeta(meta7);
        menu.setItem(6, item7);

        ItemStack item8 = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta meta8 = item.getItemMeta();
        meta8.setDisplayName("500000");
        meta8.setCustomModelData(10);
        meta8.addEnchant(Enchantment.DIG_SPEED, 10, true);
        item8.setItemMeta(meta8);
        menu.setItem(7, item8);
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
                    if (money >= 3000) {
                        int result = Integer.parseInt(String.valueOf(money - 3000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.hardPickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
                if (meta != null && meta.getCustomModelData() == 5) {
                    Player player = (Player) event.getWhoClicked();
                    UUID uuid = player.getUniqueId();
                    int money = getMoney(player);
                    if (money >= 10000) {
                        int result = Integer.parseInt(String.valueOf(money - 10000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level3Pickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
                if (meta != null && meta.getCustomModelData() == 6) {
                    Player player = (Player) event.getWhoClicked();
                    UUID uuid = player.getUniqueId();
                    int money = getMoney(player);
                    if (money >= 20000) {
                        int result = Integer.parseInt(String.valueOf(money - 20000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level4Pickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
                if (meta != null && meta.getCustomModelData() == 7) {
                    Player player = (Player) event.getWhoClicked();
                    UUID uuid = player.getUniqueId();
                    int money = getMoney(player);
                    if (money >= 40000) {
                        int result = Integer.parseInt(String.valueOf(money - 40000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level5Pickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
                if (meta != null && meta.getCustomModelData() == 8) {
                    Player player = (Player) event.getWhoClicked();
                    UUID uuid = player.getUniqueId();
                    int money = getMoney(player);
                    if (money >= 80000) {
                        int result = Integer.parseInt(String.valueOf(money - 80000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level6Pickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
                if (meta != null && meta.getCustomModelData() == 9) {
                    Player player = (Player) event.getWhoClicked();
                    UUID uuid = player.getUniqueId();
                    int money = getMoney(player);
                    if (money >= 160000) {
                        int result = Integer.parseInt(String.valueOf(money - 160000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level7Pickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
                if (meta != null && meta.getCustomModelData() == 10) {
                    Player player = (Player) event.getWhoClicked();
                    UUID uuid = player.getUniqueId();
                    int money = getMoney(player);
                    if (money >= 500000) {
                        int result = Integer.parseInt(String.valueOf(money - 500000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level8Pickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.STONE_PICKAXE && item.getItemMeta() != null &&
                item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 2) {
            // カスタムモデルデータが2の石のピッケルを右クリックした場合

            // 採掘速度上昇のポーションエフェクトを作成
            PotionEffect hasteEffect = new PotionEffect(PotionEffectType.FAST_DIGGING, 200, 4);
            player.sendMessage("採掘速度が上昇しました。");
            // プレイヤーにポーションエフェクトを適用
            player.addPotionEffect(hasteEffect);
        }else if (item.getType() == Material.STONE_PICKAXE && item.getItemMeta() != null &&
                    item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 10) {
                // カスタムモデルデータが2の石のピッケルを右クリックした場合

                // 採掘速度上昇のポーションエフェクトを作成
                PotionEffect hasteEffect = new PotionEffect(PotionEffectType.SPEED, 100, 9);
                player.sendMessage("移動速度が上昇しました。");
                // プレイヤーにポーションエフェクトを適用
                player.addPotionEffect(hasteEffect);
        }
    }
}