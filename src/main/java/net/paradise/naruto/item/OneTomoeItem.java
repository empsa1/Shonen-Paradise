
package net.paradise.naruto.item;

import net.paradise.naruto.itemgroup.NarutoParadiseItemGroup;
import net.paradise.naruto.NarutoModElements;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.BlockState;

@NarutoModElements.ModElement.Tag
public class OneTomoeItem extends NarutoModElements.ModElement {
	@ObjectHolder("naruto:one_tomoe")
	public static final Item block = null;

	public OneTomoeItem(NarutoModElements instance) {
		super(instance, 56);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Item.Properties().group(NarutoParadiseItemGroup.tab).maxDamage(1000).isImmuneToFire().rarity(Rarity.RARE));
			setRegistryName("one_tomoe");
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
			return 1F;
		}
	}
}
