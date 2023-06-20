package com.github.kumo0621.mine;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public final class Mine extends JavaPlugin implements org.bukkit.event.Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        money = loadConfig("money.yml");
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

    FileConfiguration config;
    FileConfiguration money;

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
        // config2ファイルを保存
        saveCustomConfig(money, "money.yml");
    }

    @EventHandler
    public void joinPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // プレイヤーデータからフラグを取得
        boolean isFirstJoin = money.getBoolean(uuid.toString() + ".firstJoin", true);

        if (isFirstJoin) {
            // 初回ログイン時の処理
            money.set(uuid.toString() + ".firstJoin", false);
            money.set(uuid.toString() + ".money", 0);

            // 保存処理
            try {
                money.save(new File(getDataFolder(), "money.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 初回ログイン時のメッセージを送信
            player.sendMessage("初回ログインです。所持金が初期化されました。");
        }
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getBlock();
        Material material = block.getType();
        if (config.contains("allowed-blocks") && config.getStringList("allowed-blocks").contains(material.toString())) {
            // 設定ファイルで許可されたブロックの場合の処理
            int expAmount = config.getInt("coal_level");
            int getLevel = Integer.parseInt(Objects.requireNonNull(money.getString(uuid.toString() + ".money")));
            int result = Integer.parseInt(String.valueOf(getLevel += expAmount));
            money.set(uuid.toString() + ".money", result);
            player.giveExp(expAmount);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("money")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    UUID uuid = ((Player) sender).getUniqueId();
                        int getLevel = Integer.parseInt(Objects.requireNonNull(money.getString(uuid.toString() + ".money")));
                        sender.sendMessage("あなたの所持金は「" + getLevel + "」です。");
                }
            }
        }
        if (command.getName().equals("set")) {
            if (sender instanceof Player) {
                if (args.length > 1) {
                    String playerName = args[0];
                    Player player = Bukkit.getPlayer(playerName);
                    UUID uuid = Objects.requireNonNull(player).getUniqueId();
                    money.set(uuid.toString() + ".money", args[1]);

                    sender.sendMessage(args[0]+"さんの所持金を「" + args[1] + "」にしました。");
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();

        if (action == Action.RIGHT_CLICK_BLOCK && item != null && item.getType() == Material.PAPER
                && block != null && block.getType() == Material.CHEST) {
            UUID uuid = player.getUniqueId();
            int getLevel = Integer.parseInt(Objects.requireNonNull(money.getString(uuid.toString() + ".money")));
            if(getLevel>=100) {
                int result = Integer.parseInt(String.valueOf(getLevel -= 100));
                money.set(uuid.toString() + ".money", result);
                event.setCancelled(true);
                ItemStack itemStack = new ItemStack(Material.PAPER, 1); // 追加するアイテムの種類と個数を指定
                player.getInventory().addItem(itemStack);
                player.sendMessage("ガチャチケットと交換しました。");
            }
        }
    }
}



