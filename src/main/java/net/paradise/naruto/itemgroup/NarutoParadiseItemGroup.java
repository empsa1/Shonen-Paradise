
package net.paradise.naruto.itemgroup;

import net.paradise.naruto.item.KubikiribochoItem;
import net.paradise.naruto.NarutoModElements;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

@NarutoModElements.ModElement.Tag
public class NarutoParadiseItemGroup extends NarutoModElements.ModElement {
	public NarutoParadiseItemGroup(NarutoModElements instance) {
		super(instance, 48);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabnaruto_paradise") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(KubikiribochoItem.block);
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return true;
			}
		}.setBackgroundImageName("item_search.png");
	}

	public static ItemGroup tab;
}
