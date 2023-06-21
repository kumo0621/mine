package com.github.kumo0621.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.enchantments.Enchantment;

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
            // アイテムの作成
            ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);

            // アイテムのメタデータを取得
            ItemMeta meta = pickaxe.getItemMeta();

            // アイテムのカスタムモデルデータを設定
            meta.setCustomModelData(1);

            // アイテムを壊れないように設定
            meta.setUnbreakable(true);

            // アイテムの名前を設定
            meta.setDisplayName("初心者のツルハシ");

            // アイテムにメタデータを適用
            pickaxe.setItemMeta(meta);

            // プレイヤーにアイテムを与える
            player.getInventory().addItem(pickaxe);
        }

            // 保存処理
            try {
                moneyData.save(new File(getDataFolder(), "money.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 初回ログイン時のメッセージを送信
            player.sendMessage("初回ログインです。所持金が初期化されました。");
        }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getBlock();
        Material material = block.getType();
        if (config.contains("allowed-blocks") && config.getStringList("allowed-blocks").contains(material.toString())) {
            // 設定ファイルで許可されたブロックの場合の処理
            int expAmount = config.getInt("coal.level");
            int moneyAmount = config.getInt("coal.money");
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
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();

        if (action == Action.RIGHT_CLICK_BLOCK && item != null && block != null && block.getType() == Material.CHEST) {
            UUID uuid = player.getUniqueId();
            int money = getMoney(player);
            if (money >= 100) {
                int result = Integer.parseInt(String.valueOf(money - 100));
                moneyData.set(uuid + ".money", result);
                event.setCancelled(true);
                int up = RandomCount.random();
                if(up<99) {
                    ItemStack itemStack = new ItemStack(Material.COAL, 1); // 追加するアイテムの種類と個数を指定
                    player.getInventory().addItem(itemStack);
                    player.sendMessage("ハズレ");
                }else{
                    ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
                    ItemMeta meta = pickaxe.getItemMeta();
                    meta.setCustomModelData(4);
                    meta.setUnbreakable(true);
                    meta.setDisplayName("レアツルハシ");
                    meta.addEnchant(Enchantment.DIG_SPEED, 50, true);
                    pickaxe.setItemMeta(meta);
                    player.getInventory().addItem(pickaxe);
                    player.sendMessage("レアツルハシゲット");
                }
            }
        }else if (action == Action.RIGHT_CLICK_BLOCK && item != null && block != null && block.getType() == Material.SHULKER_BOX) {
            UUID uuid = player.getUniqueId();
            int money = getMoney(player);
            if (money >= 500) {
                int result = Integer.parseInt(String.valueOf(money - 500));
                moneyData.set(uuid + ".money", result);
                event.setCancelled(true);
                ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
                ItemMeta meta = pickaxe.getItemMeta();
                meta.setCustomModelData(2);
                meta.setUnbreakable(true);
                meta.setDisplayName("中級者のツルハシ");
                meta.addEnchant(Enchantment.DIG_SPEED, 10, true);
                pickaxe.setItemMeta(meta);
                player.getInventory().addItem(pickaxe);
                player.sendMessage("ツルハシを強化しました。");
            }
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
}



