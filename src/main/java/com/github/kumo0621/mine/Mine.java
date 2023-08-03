package com.github.kumo0621.mine;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class Mine extends JavaPlugin implements Listener {
    public static FileConfiguration config;
    public static FileConfiguration moneyData;
    private HashMap<Material, Integer> itemPrices = new HashMap<>();
    private final Map<Material, ItemStack> appraisedItems = new HashMap<>();
    private HashMap<String, Long> lastUserTimes = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        moneyData = loadConfig("money.yml");
        getServer().getPluginManager().registerEvents(this, this);
        Items.init();
        itemPrices.put(Material.DIRT, 5);
        itemPrices.put(Material.STONE, 5);
        itemPrices.put(Material.COAL, 15);
        itemPrices.put(Material.LAPIS_LAZULI, 17);
        itemPrices.put(Material.GOLD_INGOT, 30);
        itemPrices.put(Material.IRON_INGOT, 14);
        itemPrices.put(Material.DEEPSLATE, 5);
        itemPrices.put(Material.DIAMOND, 20);
        itemPrices.put(Material.REDSTONE_WIRE, 6);
        itemPrices.put(Material.DEEPSLATE_COAL_ORE, 15);
        itemPrices.put(Material.DEEPSLATE_IRON_ORE, 15);
        itemPrices.put(Material.DEEPSLATE_COPPER_ORE, 4);
        itemPrices.put(Material.COPPER_ORE, 4);
        itemPrices.put(Material.DEEPSLATE_GOLD_ORE, 30);
        itemPrices.put(Material.DEEPSLATE_REDSTONE_ORE, 6);
        itemPrices.put(Material.EMERALD_ORE, 20);
        itemPrices.put(Material.DEEPSLATE_EMERALD_ORE, 20);
        itemPrices.put(Material.DEEPSLATE_LAPIS_ORE, 14);
        itemPrices.put(Material.DEEPSLATE_DIAMOND_ORE, 20);
        itemPrices.put(Material.OAK_LOG, 10);
        itemPrices.put(Material.JUNGLE_LOG, 10);
        itemPrices.put(Material.ACACIA_LOG, 10);
        itemPrices.put(Material.BIRCH_LOG, 10);
        itemPrices.put(Material.SPRUCE_LOG, 10);
        itemPrices.put(Material.COBBLED_DEEPSLATE, 5);
        itemPrices.put(Material.COBBLESTONE, 5);

        addAppraisedItem(Material.DIAMOND, 10, new ItemStack(Material.DIAMOND, 20));
        addAppraisedItem(Material.IRON_INGOT, 50, new ItemStack(Material.IRON_INGOT, 10));
        addAppraisedItem(Material.GOLD_INGOT, 40, new ItemStack(Material.GOLD_INGOT, 5));
    }

    private void addAppraisedItem(Material originalItem, int probability, ItemStack appraisedItem) {
        appraisedItems.put(originalItem, appraisedItem);
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
                double x = config.getDouble(".x");
                double y = config.getDouble(".y");
                double z = config.getDouble(".z");
                Location targetLocation = new Location(player.getWorld(), x, y, z); // B地点の座標を指定
                player.teleport(targetLocation);
            }
            case "open" -> {
                openMenu(player);
            }
            case "fly" -> {
                UUID uuid = player.getUniqueId();
                int money = getMoney(player);
                if (money >= 100) {
                    int result = Integer.parseInt(String.valueOf(money - 100));
                    moneyData.set(uuid + ".money", result);
                    if (player.getGameMode() == GameMode.CREATIVE) {
                        player.sendMessage("クリエイティブモードでは飛行は無効です！");
                        return true;
                    }

                    // 現在の飛行許可を取得
                    boolean currentFlightState = player.getAllowFlight();

                    // 空を飛ぶ許可をトグル
                    player.setAllowFlight(!currentFlightState);

                    // タイマーで10秒後に飛行許可を元に戻す
                    if (!currentFlightState) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player.isOnline()) {
                                    player.setAllowFlight(false);
                                    player.setFlying(false);
                                    player.sendMessage("空を飛ぶ機能が無効になりました。");
                                }
                            }
                        }.runTaskLater(this, 20 * 60); // 20 ticks * 10 seconds = 10 seconds
                    }

                    player.sendMessage("空を飛ぶ許可: " + !currentFlightState);
                    return true;
                } else {
                    sender.sendMessage("お金が足りません。100G必要です。");
                }
            }

        }

        // configファイルに保存
        saveCustomConfig(moneyData, "money.yml");
        return super.

                onCommand(sender, command, label, args);

    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.CHEST) {
            event.setCancelled(true);
            bell(player);
        }
    }

    private void bell(Player player) {
        Inventory playerInventory = player.getInventory();
        int totalMoneyAmount = 0;
        UUID uuid = player.getUniqueId();
        for (int slot = 0; slot < playerInventory.getSize(); slot++) {
            ItemStack currentItem = playerInventory.getItem(slot);
            if (currentItem != null && itemPrices.containsKey(currentItem.getType())) {
                totalMoneyAmount += itemPrices.get(currentItem.getType()) * currentItem.getAmount();
                playerInventory.setItem(slot, null); // アイテムをインベントリから削除
            }
        }
        int money = getMoney(player);
        int result = money + totalMoneyAmount;
        moneyData.set(uuid + ".money", result);
        player.sendMessage("所持金は、" + result + "になりました。");

        // moneyDataを保存
        saveCustomConfig(moneyData, "money.yml");

    }

    public static int getMoney(Player player) {
        return Integer.parseInt(Objects.requireNonNull(moneyData.getString(player.getUniqueId() + ".money")));
    }

    public void openMenu(Player player) {
        // メニューのインベントリを作成
        Inventory menu = Bukkit.createInventory(null, 9, "メニュー");

        ItemStack item1 = createBasicItem(Material.STONE, "ピッケル強化メニュー", "", 101);
        menu.setItem(0, item1);

        ItemStack item2 = createBasicItem(Material.DIRT, "シャベル強化メニュー", "", 101);
        menu.setItem(1, item2);

        ItemStack item3 = createBasicItem(Material.OAK_LOG, "オノ強化メニュー", "", 101);
        menu.setItem(2, item3);

        ItemStack item4 = createBasicItem(Material.ANVIL, "雑貨メニュー", "", 101);
        menu.setItem(3, item4);
        // メニュー表示
        player.openInventory(menu);
    }

    public void openPICKAXE(Player player) {
        // メニューのインベントリを作成
        Inventory menu = Bukkit.createInventory(null, 18, "ピッケル強化メニュー");

        // ツルハシの強化情報を定義
        String[] toolNames = {
                "中級者のツルハシ",
                "上級者のツルハシ",
                "ベテランのツルハシ",
                "すごくベテランのツルハシ",
                "すごくすごくベテランのツルハシ",
                "帝王のツルハシ",
                "すごい帝王のツルハシ",
                "冥王のツルハシ"
        };

        int[] customModelData = {2, 3, 5, 6, 7, 8, 9, 10};
        int[] digSpeedEnchantLevels = {0, 1, 3, 5, 7, 9, 12, 15};
        String[] prices = {
                "1000Gで開放",
                "7000Gで開放",
                "10000Gで開放",
                "20000Gで開放",
                "40000Gで開放",
                "60000Gで開放",
                "80000Gで開放",
                "100000Gで開放"
        };

        for (int i = 0; i < toolNames.length; i++) {
            ItemStack item = createEnhancedToolItem(Material.DIAMOND_PICKAXE, toolNames[i], prices[i], customModelData[i], digSpeedEnchantLevels[i]);
            menu.setItem(i, item);
        }

        // 爆裂ツルハシのアイテムを追加
        ItemStack item9 = createEnhancedToolItem(Material.IRON_PICKAXE, "爆裂ツルハシ", "1000000Gで開放", 3, 20);
        menu.setItem(8, item9);

        // 移動速度上昇アクセサリーのアイテムを追加

        // メニュー表示
        player.openInventory(menu);
    }

    public void openShovel(Player player) {
        // メニューのインベントリを作成
        Inventory menu = Bukkit.createInventory(null, 18, "シャベル強化メニュー");

        // ツルハシの強化情報を定義
        String[] toolNames = {
                "中級者のシャベル",
                "上級者のシャベル",
                "ベテランのシャベル",
                "すごくベテランのシャベル",
                "すごくすごくベテランのシャベル",
                "帝王のシャベル",
                "すごい帝王のシャベル",
                "冥王のシャベル"
        };

        int[] customModelData = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] digSpeedEnchantLevels = {0, 1, 2, 3, 4, 5, 10, 15};
        String[] prices = {
                "1000Gで開放",
                "7000Gで開放",
                "10000Gで開放",
                "20000Gで開放",
                "40000Gで開放",
                "60000Gで開放",
                "80000Gで開放",
                "100000Gで開放"
        };

        for (int i = 0; i < toolNames.length; i++) {
            ItemStack item = createEnhancedToolItem(Material.DIAMOND_SHOVEL, toolNames[i], prices[i], customModelData[i], digSpeedEnchantLevels[i]);
            menu.setItem(i, item);
        }
        // メニュー表示
        player.openInventory(menu);
    }

    public void openAxe(Player player) {
        // メニューのインベントリを作成
        Inventory menu = Bukkit.createInventory(null, 18, "オノ強化メニュー");

        // ツルハシの強化情報を定義
        String[] toolNames = {
                "中級者のオノ",
                "上級者のオノ",
                "ベテランのオノ",
                "すごくベテランのオノ",
                "すごくすごくベテランのオノ",
                "帝王のオノ",
                "すごい帝王のオノ",
                "冥王のオノ"
        };

        int[] customModelData = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] digSpeedEnchantLevels = {0, 1, 2, 3, 4, 5, 10, 15};
        String[] prices = {
                "1000Gで開放",
                "7000Gで開放",
                "10000Gで開放",
                "20000Gで開放",
                "40000Gで開放",
                "60000Gで開放",
                "80000Gで開放",
                "100000Gで開放"
        };

        for (int i = 0; i < toolNames.length; i++) {
            ItemStack item = createEnhancedToolItem(Material.DIAMOND_AXE, toolNames[i], prices[i], customModelData[i], digSpeedEnchantLevels[i]);
            menu.setItem(i, item);
        }
        // メニュー表示
        player.openInventory(menu);
    }

    public void openChest(Player player) {
        // メニューのインベントリを作成
        Inventory menu = Bukkit.createInventory(null, 18, "雑貨メニュー");

        ItemStack item10 = createAccessoryItem(Material.IRON_PICKAXE, "移動速度上昇アクセサリー", "500000Gで開放\n右クリックで移動速度が上がる", 10);
        menu.setItem(0, item10);

        // 採掘速度上昇アクセサリーのアイテムを追加
        ItemStack item11 = createAccessoryItem(Material.IRON_PICKAXE, "採掘速度上昇アクセサリー", "500000Gで開放\n右クリックで採掘速度が上がる", 2);
        menu.setItem(1, item11);

        // 飯のアイテムを追加
        ItemStack item12 = createBasicItem(Material.COOKED_BEEF, "飯", "1G", 1);
        menu.setItem(2, item12);

        // スポンジのアイテムを追加
        ItemStack item13 = createBasicItem(Material.SPONGE, "スポンジ", "1G", 1);
        menu.setItem(3, item13);

        ItemStack item14 = createBasicItem(Material.SHULKER_BOX, "貯蔵庫", "1000G", 1);
        menu.setItem(4, item14);

        ItemStack item15 = createBasicItem(Material.PAPER, "TNTを右クリックで召喚", "100G", 1);
        menu.setItem(5, item15);

        ItemStack item16 = createBasicItem(Material.PAPER, "インベントリのものを右クリックで売却する", "500G", 2);
        menu.setItem(6, item16);

        ItemStack item17 = createBasicItem(Material.PAPER, "右クリックで範囲採掘できる", "100G", 3);
        menu.setItem(7, item17);
        // メニュー表示
        player.openInventory(menu);
    }

    // ツルハシの強化アイテムを作成するメソッド
    private ItemStack createEnhancedToolItem(Material material, String displayName, String lore, int customModelData, int digSpeedEnchantLevel) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        List<String> loreList = new ArrayList<>();
        loreList.add(lore);
        meta.setLore(loreList);
        meta.setCustomModelData(customModelData);
        meta.addEnchant(Enchantment.DIG_SPEED, digSpeedEnchantLevel, true);
        item.setItemMeta(meta);
        return item;
    }

    // アクセサリーのアイテムを作成するメソッド
    private ItemStack createAccessoryItem(Material material, String displayName, String lore, int customModelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        List<String> loreList = new ArrayList<>();
        loreList.add(lore);
        meta.setLore(loreList);
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
        return item;
    }

    // 基本アイテム（食べ物や材料など）を作成するメソッド
    private ItemStack createBasicItem(Material material, String displayName, String lore, int customModelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        List<String> loreList = new ArrayList<>();
        loreList.add(lore);
        meta.setLore(loreList);
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
        return item;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int money = getMoney(player);
        // クリックしたアイテムを取得
        ItemStack clickedItem = event.getCurrentItem();
        // クリックしたアイテムがnullでないことを確認
        if (clickedItem == null) {
            return;
        }
        // アイテムのメタデータを取得
        ItemMeta meta = clickedItem.getItemMeta();
        if (event.getView().getTitle().equals("ピッケル強化メニュー")) {
            // クリックしたインベントリがメニューであるかを確認
            event.setCancelled(true);
            if (meta != null && meta.getCustomModelData() == 2) {
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
            } else if (meta != null && meta.getCustomModelData() == 5) {
                if (money >= 10000) {
                    int result = Integer.parseInt(String.valueOf(money - 10000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level3Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            } else if (meta != null && meta.getCustomModelData() == 6) {

                if (money >= 20000) {
                    int result = Integer.parseInt(String.valueOf(money - 20000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level4Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            } else if (meta != null && meta.getCustomModelData() == 7) {
                if (money >= 40000) {
                    int result = Integer.parseInt(String.valueOf(money - 40000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level5Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            } else if (meta != null && meta.getCustomModelData() == 8) {
                if (money >= 60000) {
                    int result = Integer.parseInt(String.valueOf(money - 60000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level6Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            } else if (meta != null && meta.getCustomModelData() == 9) {
                if (money >= 80000) {
                    int result = Integer.parseInt(String.valueOf(money - 80000));
                    moneyData.set(uuid + ".money", result);
                    event.setCancelled(true);
                    player.getInventory().addItem(Items.level7Pickaxe);
                    player.sendMessage("ツルハシを強化しました。");
                } else {
                    player.sendMessage("お金が足りません。");
                }
            } else if (meta != null && meta.getCustomModelData() == 10) {
                if (clickedItem.getType() == Material.DIAMOND_PICKAXE) {
                    if (money >= 100000) {
                        int result = Integer.parseInt(String.valueOf(money - 100000));
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
            } else if (meta != null && meta.getCustomModelData() == 3) {
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
                    if (money >= 7000) {
                        int result = money - 70000;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.hardPickaxe);
                        player.sendMessage("ツルハシを強化しました。");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }


            }
        } else if (event.getView().getTitle().equals("メニュー")) {
            event.setCancelled(true);
            if (meta != null && meta.getCustomModelData() == 101) {
                if (clickedItem.getType() == Material.STONE) {
                    openPICKAXE(player);
                } else if (clickedItem.getType() == Material.DIRT) {
                    openShovel(player);
                } else if (clickedItem.getType() == Material.OAK_LOG) {
                    openAxe(player);
                } else if (clickedItem.getType() == Material.ANVIL) {
                    openChest(player);
                }
            }

        } else if (event.getView().getTitle().equals("シャベル強化メニュー")) {
            event.setCancelled(true);
            if (meta != null && meta.getCustomModelData() == 1) {
                if (clickedItem.getType() == Material.DIAMOND_SHOVEL) {
                    if (money >= 1000) {
                        int result = money - 1000;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level2shovel);
                        player.sendMessage("シャベルを強化しました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }
            }
        } else if (event.getView().getTitle().equals("オノ強化メニュー")) {
            event.setCancelled(true);
            if (meta != null && meta.getCustomModelData() == 1) {
                if (clickedItem.getType() == Material.DIAMOND_AXE) {
                    if (money >= 1000) {
                        int result = money - 1000;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.level2axe);
                        player.sendMessage("シャベルを強化しました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }
            }
        } else if (event.getView().getTitle().equals("雑貨メニュー")) {
            event.setCancelled(true);
            if (meta != null && meta.getCustomModelData() == 1) {
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
                } else if (clickedItem.getType() == Material.SPONGE) {
                    if (money >= 1) {
                        int result = money - 1;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.sponge);
                        player.sendMessage("スポンジを買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                } else if (clickedItem.getType() == Material.SHULKER_BOX) {
                    if (money >= 1000) {
                        int result = money - 1000;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.SHULKER_BOX);
                        player.sendMessage("スポンジを買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                } else if (clickedItem.getType() == Material.PAPER) {
                    if (money >= 100) {
                        int result = money - 100;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.tnt);
                        player.sendMessage("券を買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }
            } else if (meta != null && meta.getCustomModelData() == 2) {
                if (clickedItem.getType() == Material.IRON_PICKAXE) {
                    if (money >= 500000) {
                        int result = Integer.parseInt(String.valueOf(money - 500000));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.accessory_main);
                        player.sendMessage("アクセサリーを買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                } else if (clickedItem.getType() == Material.PAPER) {
                    if (money >= 500) {
                        int result = money - 500;
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.ball);
                        player.sendMessage("券を買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }
                }
            } else if (meta != null && meta.getCustomModelData() == 3) {
                if (clickedItem.getType() == Material.PAPER) {
                    if (money >= 100) {
                        int result = Integer.parseInt(String.valueOf(money - 100));
                        moneyData.set(uuid + ".money", result);
                        event.setCancelled(true);
                        player.getInventory().addItem(Items.blockBreak);
                        player.sendMessage("拳を買いました");
                    } else {
                        player.sendMessage("お金が足りません。");
                    }

                }
            } else if (meta != null && meta.getCustomModelData() == 10) {
                if (clickedItem.getType() == Material.IRON_PICKAXE) {
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
        }
    }

    HashMap<String, Long> lastUsageTimes = new HashMap<>();

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction().name().contains("RIGHT_CLICK") && item.getType() != Material.AIR) {
            String playerName = player.getName();
            long currentTime = System.currentTimeMillis();

            Long lastUsageTime = lastUsageTimes.get(playerName);

            if (lastUsageTime == null || currentTime - lastUsageTime >= 1000) {
                lastUsageTimes.put(playerName, currentTime);

                if (item.getType() == Material.IRON_PICKAXE && item.hasItemMeta() &&
                        item.getItemMeta().hasCustomModelData()) {
                    int customModelData = item.getItemMeta().getCustomModelData();
                    if (customModelData == 2) {
                        PotionEffect hasteEffect = new PotionEffect(PotionEffectType.FAST_DIGGING, 200, 4);
                        player.sendMessage("採掘速度が上昇しました。");
                        player.addPotionEffect(hasteEffect);
                    } else if (customModelData == 10) {
                        PotionEffect hasteEffect = new PotionEffect(PotionEffectType.SPEED, 100, 9);
                        player.sendMessage("移動速度が上昇しました。");
                        player.addPotionEffect(hasteEffect);
                    }
                } else if (item.getType() == Material.PAPER && item.hasItemMeta()) {
                    ItemMeta itemMeta = item.getItemMeta();
                    int customModelData = itemMeta.getCustomModelData();
                    if (customModelData == 1) {
                        // 3秒以上経過しているか、初回使用の場合はコードを実行
                        Location spawnLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(3));
                        spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.PRIMED_TNT);

                        int itemCount = item.getAmount();
                        if (itemCount >= 1) {
                            item.setAmount(itemCount - 1);
                        } else {
                            player.getInventory().setItemInMainHand(null);
                        }

                    } else if (customModelData == 2) {
                        Material ancientDebrisMaterial = Material.PAPER;
                        if (item.getType() == ancientDebrisMaterial) {
                            int itemCount = item.getAmount();
                            if (itemCount >= 1) {
                                item.setAmount(itemCount - 1);
                                bell(player);
                            } else {
                                player.getInventory().setItemInMainHand(null);
                            }
                        }
                    } else if (customModelData == 3) {
                        Material ancientDebrisMaterial = Material.PAPER;
                        if (item.getType() == ancientDebrisMaterial) {
                            int itemCount = item.getAmount();
                            if (itemCount >= 1) {
                                item.setAmount(itemCount - 1);
                                Block clickedBlock = event.getClickedBlock();
                                if (clickedBlock != null) {
                                    startMiningPower(event.getPlayer(), clickedBlock);
                                }
                            } else {
                                player.getInventory().setItemInMainHand(null);
                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.STONE || event.getBlock().getType() == Material.DEEPSLATE) {
            // 1%の確率で化石をドロップする
            int randam = RandomCount.random();
            if (randam == 1) {
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.ANCIENT_DEBRIS));
            }
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.SMITHING_TABLE) {
            event.setCancelled(true);

            // 確認するアイテムの種類を指定
            Material ancientDebrisMaterial = Material.ANCIENT_DEBRIS;

            // 古代の残骸を持っているか確認
            if (item.getType() == ancientDebrisMaterial) {
                // Reduce the item count by 1
                int itemCount = item.getAmount();
                if (itemCount >= 1) {
                    item.setAmount(itemCount - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }

                // アイテムの取得結果をランダムで決定
                int randam = RandomCount.random();
                if (randam == 1) {
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND, 10));
                    player.sendMessage("古代の残骸を鑑定してダイヤモンドをゲットしました。");
                } else if (randam == 2) {
                    player.getInventory().addItem(new ItemStack(Material.COAL, 10));
                    player.sendMessage("古代の残骸を鑑定して石炭をゲットしました。");
                } else if (randam == 3) {
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 10));
                    player.sendMessage("古代の残骸を鑑定して鉄インゴットをゲットしました。");
                } else {
                    player.sendMessage("古代の残骸を鑑定しましたが、何も起こりませんでした。");
                }
            } else {
                player.sendMessage("古代の残骸を持っていません。");
            }
        }
    }

    private void startMiningPower(Player player, Block clickedBlock) {
        // 30秒間、3x3の範囲内のブロックを採掘可能にする処理を開始
        new BukkitRunnable() {
            int timer = 60;

            @Override
            public void run() {
                if (timer > 0) {
                    // プレイヤーの位置から3x3の範囲内のブロックを採掘
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                Block block = clickedBlock.getRelative(x, y, z);
                                if (block.getType() != Material.BEDROCK) {
                                    block.breakNaturally();
                                }
                            }
                        }
                    }
                    timer--;
                } else {
                    // 30秒経過後に採掘処理を終了
                    this.cancel();
                }
            }
        }.runTaskTimer(this, 0, 20); // 1秒間隔で実行
    }
}