package com.github.kumo0621.mine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
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
import java.util.*;

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
            player.getInventory().addItem(Items.beginnerShovel);
            player.getInventory().addItem(Items.beginnerAxe);

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
            int moneyAmount = config.getInt("coal");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DIRT) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("dirt");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.STONE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("stone");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.LAPIS_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("lapis_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.GOLD_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("gold_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.IRON_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("iron_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepSlate");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DIAMOND_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("DIAMOND_ORE");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DIAMOND_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("DIAMOND_ORE");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.REDSTONE_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("REDSTONE_ORE");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE_COAL_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepslate_coal_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE_IRON_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepslate_iron_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE_COPPER_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepslate_copper_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.COPPER_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("copper_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE_GOLD_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepslate_gold_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE_REDSTONE_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepslate_redstone_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.EMERALD_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("emerald_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE_EMERALD_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepslate_emerald_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE_LAPIS_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepslate_lapis_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (block.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("deepslate_diamond_ore");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
        } else if (Arrays.asList(Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG,
                        Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG)
                .contains(block.getType())) {
            // 設定ファイルで許可されたブロックの場合の処理
            int moneyAmount = config.getInt("wood_log");
            int money = getMoney(player);
            int result = Integer.parseInt(String.valueOf(money + moneyAmount));
            moneyData.set(uuid + ".money", result);
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
            case "home" -> {
                Location targetLocation = new Location(player.getWorld(), -11 ,71 ,50); // B地点の座標を指定
                player.teleport(targetLocation);
            }
            case "open" -> {
                openMenu(player);
            }
        }

        // configファイルに保存
        saveCustomConfig(moneyData, "money.yml");
        return super.onCommand(sender, command, label, args);
    }

    //@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        if (action == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.CHEST) {
            UUID uuid = player.getUniqueId();
            int money = getMoney(player);
            if (money >= 500) {
                int result = Integer.parseInt(String.valueOf(money - 500));
                moneyData.set(uuid + ".money", result);
                event.setCancelled(true);
                int up = RandomCount.random();
                if (up == 99) {
                    player.getInventory().addItem(Items.rarePickaxe);
                    player.sendMessage("レアツルハシゲット");
                } else if (up == 87) {
                    player.getInventory().addItem(Items.luckyPickaxe);
                    player.sendMessage("超レアツルハシゲット");
                } else if (up == 85) {
                    player.getInventory().addItem(Items.accessory_main);
                    player.sendMessage("マイニングのアクセサリーゲット");
                } else if (up == 82) {
                    player.getInventory().addItem(Items.accessory_speed);
                    player.sendMessage("スピードのアクセサリーゲット");
                } else {
                    ItemStack itemStack = new ItemStack(Material.COAL, 1); // 追加するアイテムの種類と個数を指定
                    player.getInventory().addItem(itemStack);
                    player.sendMessage("ハズレ");
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
        Inventory menu = Bukkit.createInventory(null, 18, "強化メニュー");

        // メニューのアイテムを設定
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("中級者のツルハシ");
        List<String> lore = new ArrayList<>();
        lore.add("1000Gで開放");
        meta.setLore(lore);
        meta.setCustomModelData(2);
        meta.addEnchant(Enchantment.DIG_SPEED, 11, true);
        item.setItemMeta(meta);
        menu.setItem(0, item);

        ItemStack item2 = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta2 = item.getItemMeta();
        meta2.setDisplayName("上級者のツルハシ");
        meta2.setCustomModelData(3);
        List<String> lore2 = new ArrayList<>();
        lore2.add("10000Gで開放");
        meta2.setLore(lore2);
        meta2.addEnchant(Enchantment.DIG_SPEED, 12, true);
        item2.setItemMeta(meta2);
        menu.setItem(1, item2);

        ItemStack item3 = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta3 = item.getItemMeta();
        meta3.setDisplayName("ベテランのツルハシ");
        List<String> lore3 = new ArrayList<>();
        lore3.add("30000Gで開放");
        meta3.setLore(lore3);
        meta3.setCustomModelData(5);
        meta3.addEnchant(Enchantment.DIG_SPEED, 13, true);
        item3.setItemMeta(meta3);
        menu.setItem(2, item3);

        ItemStack item4 = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta4 = item.getItemMeta();
        meta4.setDisplayName("すごくベテランのツルハシ");
        List<String> lore4 = new ArrayList<>();
        lore4.add("70000Gで開放");
        meta4.setLore(lore4);
        meta4.setCustomModelData(6);
        meta4.addEnchant(Enchantment.DIG_SPEED, 14, true);
        item4.setItemMeta(meta4);
        menu.setItem(3, item4);

        ItemStack item5 = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta5 = item.getItemMeta();
        meta5.setDisplayName("すごくすごくベテランのツルハシ");
        List<String> lore5 = new ArrayList<>();
        lore5.add("100000Gで開放");
        meta5.setLore(lore5);
        meta5.setCustomModelData(7);
        meta5.addEnchant(Enchantment.DIG_SPEED, 15, true);
        item5.setItemMeta(meta5);
        menu.setItem(4, item5);

        ItemStack item6 = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta6 = item.getItemMeta();
        meta6.setDisplayName("帝王のツルハシ");
        List<String> lore6 = new ArrayList<>();
        lore6.add("200000Gで開放");
        meta6.setLore(lore6);
        meta6.setCustomModelData(8);
        meta6.addEnchant(Enchantment.DIG_SPEED, 16, true);
        item6.setItemMeta(meta6);
        menu.setItem(5, item6);

        ItemStack item7 = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta7 = item.getItemMeta();
        meta7.setDisplayName("すごい帝王のツルハシ");
        List<String> lore7 = new ArrayList<>();
        lore7.add("400000Gで開放");
        meta7.setLore(lore7);
        meta7.setCustomModelData(9);
        meta7.addEnchant(Enchantment.DIG_SPEED, 17, true);
        item7.setItemMeta(meta7);
        menu.setItem(6, item7);

        ItemStack item8 = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta8 = item.getItemMeta();
        meta8.setDisplayName("冥王のツルハシ");
        List<String> lore8 = new ArrayList<>();
        lore8.add("500000Gで開放");
        meta8.setLore(lore8);
        meta8.setCustomModelData(10);
        meta8.addEnchant(Enchantment.DIG_SPEED, 18, true);
        item8.setItemMeta(meta8);
        menu.setItem(7, item8);
        //メニュー表示
        player.openInventory(menu);

        ItemStack item9 = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta9 = item.getItemMeta();
        meta9.setDisplayName("爆裂ツルハシ");
        List<String> lore9 = new ArrayList<>();
        lore9.add("1000000Gで開放");
        lore9.add("右クリックでTNTを召喚できる");
        lore9.add("鉄で修理できる");
        meta9.setLore(lore9);
        meta9.setCustomModelData(3);
        meta9.addEnchant(Enchantment.DIG_SPEED, 10, true);
        item9.setItemMeta(meta9);
        menu.setItem(8, item9);

        ItemStack item10 = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta10 = item.getItemMeta();
        meta10.setDisplayName("移動速度上昇アクセサリー");
        List<String> lore10 = new ArrayList<>();
        lore10.add("500000Gで開放");
        lore10.add("右クリックで移動速度が上がる");
        meta10.setLore(lore10);
        meta10.setCustomModelData(10);
        item10.setItemMeta(meta10);
        menu.setItem(9, item10);

        ItemStack item11 = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta11 = item.getItemMeta();
        meta11.setDisplayName("採掘速度上昇アクセサリー");
        List<String> lore11 = new ArrayList<>();
        lore11.add("500000Gで開放");
        lore11.add("右クリックで採掘速度が上がる");
        meta11.setLore(lore11);
        meta11.setCustomModelData(2);
        item11.setItemMeta(meta11);
        menu.setItem(10, item11);

        ItemStack item12 = new ItemStack(Material.COOKED_BEEF);
        ItemMeta meta12 = item.getItemMeta();
        meta12.setDisplayName("飯");
        List<String> lore12 = new ArrayList<>();
        lore12.add("1G");
        meta12.setLore(lore12);
        meta12.setCustomModelData(1);
        item12.setItemMeta(meta12);
        menu.setItem(11, item12);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // プレイヤーがメニューをクリックしたときの処理
        if (event.getView().getTitle().equals("強化メニュー")) {
            // クリックしたインベントリがメニューであるかを確認
            ItemStack clickedItem = event.getCurrentItem();
            event.setCancelled(true); // クリックイベントをキャンセル
            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta != null && meta.getCustomModelData() == 2) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (clickedItem.getType() == Material.DIAMOND_PICKAXE) {
                    if (money >= 1000) {
                        int result = Integer.parseInt(String.valueOf(money - 1000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.intermediatePickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                } else if (clickedItem.getType() == Material.IRON_PICKAXE) {
                    if (money >= 500000) {
                        int result = Integer.parseInt(String.valueOf(money - 500000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.accessory_main);
                        player.sendMessage("アクセサリーを買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }
            }
            if (meta != null && meta.getCustomModelData() == 5) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (money >= 30000) {
                    int result = Integer.parseInt(String.valueOf(money - 30000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level3Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            }
            if (meta != null && meta.getCustomModelData() == 6) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (money >= 70000) {
                    int result = Integer.parseInt(String.valueOf(money - 70000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level4Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            }
            if (meta != null && meta.getCustomModelData() == 7) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (money >= 100000) {
                    int result = Integer.parseInt(String.valueOf(money - 100000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level5Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            }
            if (meta != null && meta.getCustomModelData() == 8) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (money >= 200000) {
                    int result = Integer.parseInt(String.valueOf(money - 200000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level6Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            }
            if (meta != null && meta.getCustomModelData() == 9) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (money >= 400000) {
                    int result = Integer.parseInt(String.valueOf(money - 400000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level7Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            }
            if (meta != null && meta.getCustomModelData() == 10) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (clickedItem.getType() == Material.DIAMOND_PICKAXE) {
                    if (money >= 500000) {
                        int result = Integer.parseInt(String.valueOf(money - 500000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level8Pickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                } else if (clickedItem.getType() == Material.IRON_PICKAXE) {
                    if (money >= 500000) {
                        int result = Integer.parseInt(String.valueOf(money - 500000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.accessory_speed);
                        player.sendMessage("アクセサリーを買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }
            }
            if (meta != null && meta.getCustomModelData() == 3) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (clickedItem.getType() == Material.IRON_PICKAXE) {
                    if (money >= 1000000) {
                        int result = money - 1000000;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.luckyPickaxe);
                        player.sendMessage("爆裂ツルハシを強化しました。");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                } else if (clickedItem.getType() == Material.DIAMOND_PICKAXE) {
                    if (money >= 10000) {
                        int result = money - 10000;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.hardPickaxe);
                        player.sendMessage("冥王のツルハシを強化しました。");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }
            }
            if (meta != null && meta.getCustomModelData() == 1) {
                Player player = (Player) event.getWhoClicked();
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (clickedItem.getType() == Material.COOKED_BEEF) {
                    if (money >= 1) {
                        int result = money - 1;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.steak);
                        player.sendMessage("飯を買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }
            }
        }
    }

    HashMap<String, Long> lastUsageTimes = new HashMap<>();

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.IRON_PICKAXE && item.getItemMeta() != null &&
                item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 2) {
            // カスタムモデルデータが2の石のピッケルを右クリックした場合

            // 採掘速度上昇のポーションエフェクトを作成
            PotionEffect hasteEffect = new PotionEffect(PotionEffectType.FAST_DIGGING, 200, 4);
            player.sendMessage("採掘速度が上昇しました。");
            // プレイヤーにポーションエフェクトを適用
            player.addPotionEffect(hasteEffect);
        } else if (item.getType() == Material.IRON_PICKAXE && item.getItemMeta() != null &&
                item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 10) {
            // カスタムモデルデータが2の石のピッケルを右クリックした場合

            // 採掘速度上昇のポーションエフェクトを作成
            PotionEffect hasteEffect = new PotionEffect(PotionEffectType.SPEED, 100, 9);
            player.sendMessage("移動速度が上昇しました。");
            // プレイヤーにポーションエフェクトを適用
            player.addPotionEffect(hasteEffect);
        } else if (item.getType() == Material.IRON_PICKAXE && item.getItemMeta() != null &&
                item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 3) {
            long currentTime = System.currentTimeMillis();

            // プレイヤーの名前を取得（プレイヤーのオブジェクトから得られる名前などを使用）
            String playerName = player.getName();

            // プレイヤーが最後にこのコードを使用した時刻を取得
            Long lastUsageTime = lastUsageTimes.get(playerName);

            // 最後に使用した時刻がnull（初回使用）または3秒以上経過しているか確認
            if (lastUsageTime == null || currentTime - lastUsageTime >= 3000) {
                if (event.getAction().toString().contains("RIGHT_CLICK")) {
                    // 3秒以上経過しているか、初回使用の場合はコードを実行
                    if (item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 3) {
                        Location spawnLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(3));
                        spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.PRIMED_TNT);
                        // プレイヤーの最後の使用時刻を更新
                        lastUsageTimes.put(playerName, currentTime);
                    } else {
                        // 3秒経過していない場合は何もしないか、エラーメッセージを出力するなどの対応を行う
                        player.sendMessage("まだ使えないよ");
                    }
                }
            }
        }
    }
}