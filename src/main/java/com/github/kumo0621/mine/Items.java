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
        itemMeta2.addEnchant(Enchantment.DIG_SPEED, 50, true);
        rarePickaxe.setItemMeta(itemMeta2);

        intermediatePickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta3 = intermediatePickaxe.getItemMeta();
        itemMeta3.setCustomModelData(2);
        itemMeta3.setUnbreakable(true);
        itemMeta3.setDisplayName("中級者のツルハシ");
        itemMeta3.addEnchant(Enchantment.DIG_SPEED, 10, true);
        intermediatePickaxe.setItemMeta(itemMeta3);

        hardPickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta4 = hardPickaxe.getItemMeta();
        itemMeta4.setCustomModelData(3);
        itemMeta4.setUnbreakable(true);
        itemMeta4.setDisplayName("上級者のツルハシ");
        itemMeta4.addEnchant(Enchantment.DIG_SPEED, 30, true);
        hardPickaxe.setItemMeta(itemMeta4);
    }

}
