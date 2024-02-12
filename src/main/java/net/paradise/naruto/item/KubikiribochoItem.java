
package net.paradise.naruto.item;

import net.paradise.naruto.itemgroup.NarutoParadiseItemGroup;
import net.paradise.naruto.NarutoModElements;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.SwordItem;
import net.minecraft.item.Item;
import net.minecraft.item.IItemTier;

@NarutoModElements.ModElement.Tag
public class KubikiribochoItem extends NarutoModElements.ModElement {
	@ObjectHolder("naruto:kubikiribocho")
	public static final Item block = null;

	public KubikiribochoItem(NarutoModElements instance) {
		super(instance, 46);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new SwordItem(new IItemTier() {
			public int getMaxUses() {
				return 0;
			}

			public float getEfficiency() {
				return 5f;
			}

			public float getAttackDamage() {
				return 18f;
			}

			public int getHarvestLevel() {
				return 5;
			}

			public int getEnchantability() {
				return 0;
			}

			public Ingredient getRepairMaterial() {
				return Ingredient.EMPTY;
			}
		}, 3, -3f, new Item.Properties().group(NarutoParadiseItemGroup.tab).isImmuneToFire()) {
		}.setRegistryName("kubikiribocho"));
	}
}
