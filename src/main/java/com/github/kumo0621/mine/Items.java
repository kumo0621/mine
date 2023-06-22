package com.github.kumo0621.mine;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

    public static ItemStack beginnerPickaxe;
    public static ItemStack rarePickaxe;
    public static ItemStack intermediatePickaxe;
    public static ItemStack hardPickaxe;
    public static ItemStack level3Pickaxe;
    public static ItemStack level4Pickaxe;
    public static ItemStack level5Pickaxe;
    public static ItemStack level6Pickaxe;
    public static ItemStack level7Pickaxe;
    public static ItemStack level8Pickaxe;
    public static ItemStack luckyPickaxe;
    public static ItemStack accessory_speed;
    public static ItemStack accessory_main;
    public static void init() {

        // アイテムの作成
        beginnerPickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        // アイテムのメタデータを取得
        ItemMeta itemMeta1 = beginnerPickaxe.getItemMeta();
        // アイテムのカスタムモデルデータを設定
        itemMeta1.setCustomModelData(1);
        // アイテムを壊れないように設定
        itemMeta1.setUnbreakable(true);
        // アイテムの名前を設定
        itemMeta1.setDisplayName("初心者のツルハシ");
        // アイテムにメタデータを適用
        beginnerPickaxe.setItemMeta(itemMeta1);

        rarePickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta2 = rarePickaxe.getItemMeta();
        itemMeta2.setCustomModelData(4);
        itemMeta2.setUnbreakable(true);
        itemMeta2.setDisplayName("レアツルハシ");
        itemMeta2.addEnchant(Enchantment.DIG_SPEED, 5, true);
        rarePickaxe.setItemMeta(itemMeta2);

        intermediatePickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta3 = intermediatePickaxe.getItemMeta();
        itemMeta3.setCustomModelData(2);
        itemMeta3.setUnbreakable(true);
        itemMeta3.setDisplayName("中級者のツルハシ");
        itemMeta3.addEnchant(Enchantment.DIG_SPEED, 1, true);
        intermediatePickaxe.setItemMeta(itemMeta3);

        hardPickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta4 = hardPickaxe.getItemMeta();
        itemMeta4.setCustomModelData(3);
        itemMeta4.setUnbreakable(true);
        itemMeta4.setDisplayName("上級者のツルハシ");
        itemMeta4.addEnchant(Enchantment.DIG_SPEED, 2, true);
        hardPickaxe.setItemMeta(itemMeta4);

        level3Pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta5 = level3Pickaxe.getItemMeta();
        itemMeta5.setCustomModelData(5);
        itemMeta5.setUnbreakable(true);
        itemMeta5.setDisplayName("ベテランのツルハシ");
        itemMeta5.addEnchant(Enchantment.DIG_SPEED, 3, true);
        level3Pickaxe.setItemMeta(itemMeta5);

        level4Pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta6 = level4Pickaxe.getItemMeta();
        itemMeta6.setCustomModelData(6);
        itemMeta6.setUnbreakable(true);
        itemMeta6.setDisplayName("すごくベテランのツルハシ");
        itemMeta6.addEnchant(Enchantment.DIG_SPEED, 4, true);
        level4Pickaxe.setItemMeta(itemMeta6);

        level5Pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta7 = level5Pickaxe.getItemMeta();
        itemMeta7.setCustomModelData(7);
        itemMeta7.setUnbreakable(true);
        itemMeta7.setDisplayName("すごくすごいベテランのツルハシ");
        itemMeta7.addEnchant(Enchantment.DIG_SPEED, 5, true);
        level5Pickaxe.setItemMeta(itemMeta7);

        level6Pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta8 = level6Pickaxe.getItemMeta();
        itemMeta8.setCustomModelData(8);
        itemMeta8.setUnbreakable(true);
        itemMeta8.setDisplayName("帝王のツルハシ");
        itemMeta8.addEnchant(Enchantment.DIG_SPEED, 7, true);
        level6Pickaxe.setItemMeta(itemMeta8);

        level7Pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta9 = level7Pickaxe.getItemMeta();
        itemMeta9.setCustomModelData(9);
        itemMeta9.setUnbreakable(true);
        itemMeta9.setDisplayName("すごい帝王のツルハシ");
        itemMeta9.addEnchant(Enchantment.DIG_SPEED, 8, true);
        level7Pickaxe.setItemMeta(itemMeta9);

        level8Pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta10 = level8Pickaxe.getItemMeta();
        itemMeta10.setCustomModelData(10);
        itemMeta10.setUnbreakable(true);
        itemMeta10.setDisplayName("冥王のツルハシ");
        itemMeta10.addEnchant(Enchantment.DIG_SPEED, 10, true);
        level8Pickaxe.setItemMeta(itemMeta10);

        level8Pickaxe = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta itemMeta11 = level8Pickaxe.getItemMeta();
        itemMeta11.setCustomModelData(3);
        itemMeta11.setUnbreakable(true);
        itemMeta11.setDisplayName("爆裂ツルハシ");
        itemMeta11.addEnchant(Enchantment.DIG_SPEED, 30, true);
        level8Pickaxe.setItemMeta(itemMeta11);

        accessory_speed = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta itemMeta12 = accessory_speed.getItemMeta();
        itemMeta12.setCustomModelData(10);
        itemMeta12.setUnbreakable(true);
        itemMeta12.setDisplayName("移動速度上昇アクセサリー（右クリック）");
        accessory_speed.setItemMeta(itemMeta12);

        accessory_main = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta itemMeta13 = accessory_main.getItemMeta();
        itemMeta13.setCustomModelData(2);
        itemMeta13.setUnbreakable(true);
        itemMeta13.setDisplayName("採掘速度上昇アクセサリー（右クリック）");
        accessory_main.setItemMeta(itemMeta13);
    }

}
