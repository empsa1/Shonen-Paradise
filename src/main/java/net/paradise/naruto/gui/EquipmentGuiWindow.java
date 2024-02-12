
package net.paradise.naruto.gui;

import net.paradise.naruto.NarutoMod;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class EquipmentGuiWindow extends ContainerScreen<EquipmentGui.GuiContainerMod> {
	private World world;
	private int x, y, z;
	private PlayerEntity entity;
	private final static HashMap guistate = EquipmentGui.guistate;

	public EquipmentGuiWindow(EquipmentGui.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.xSize = 480;
		this.ySize = 480;
	}

	private static final ResourceLocation texture = new ResourceLocation("naruto:textures/screens/equipment.png");

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.blit(ms, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeScreen();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
		this.font.drawString(ms, "Equipment", 215, 126, -12829636);
		this.font.drawString(ms, "Quickcast", 352, 158, -12829636);
		this.font.drawString(ms, "Eyes", 86, 159, -12829636);
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		super.init(minecraft, width, height);
		minecraft.keyboardListener.enableRepeatEvents(true);
		this.addButton(new Button(this.guiLeft + 212, this.guiTop + 151, 51, 20, new StringTextComponent("Kunai"), e -> {
			if (true) {
				NarutoMod.PACKET_HANDLER.sendToServer(new EquipmentGui.ButtonPressedMessage(0, x, y, z));
				EquipmentGui.handleButtonAction(entity, 0, x, y, z);
			}
		}));
		this.addButton(new Button(this.guiLeft + 205, this.guiTop + 171, 67, 20, new StringTextComponent("Shuriken"), e -> {
			if (true) {
				NarutoMod.PACKET_HANDLER.sendToServer(new EquipmentGui.ButtonPressedMessage(1, x, y, z));
				EquipmentGui.handleButtonAction(entity, 1, x, y, z);
			}
		}));
		this.addButton(new Button(this.guiLeft + 197, this.guiTop + 191, 82, 20, new StringTextComponent("FlyingKunai"), e -> {
			if (true) {
				NarutoMod.PACKET_HANDLER.sendToServer(new EquipmentGui.ButtonPressedMessage(2, x, y, z));
				EquipmentGui.handleButtonAction(entity, 2, x, y, z);
			}
		}));
		this.addButton(new Button(this.guiLeft + 209, this.guiTop + 211, 56, 20, new StringTextComponent("Senbon"), e -> {
			if (true) {
				NarutoMod.PACKET_HANDLER.sendToServer(new EquipmentGui.ButtonPressedMessage(3, x, y, z));
				EquipmentGui.handleButtonAction(entity, 3, x, y, z);
			}
		}));
		this.addButton(new Button(this.guiLeft + 29, this.guiTop + 334, 108, 20, new StringTextComponent("<- Previous Page"), e -> {
			if (true) {
				NarutoMod.PACKET_HANDLER.sendToServer(new EquipmentGui.ButtonPressedMessage(4, x, y, z));
				EquipmentGui.handleButtonAction(entity, 4, x, y, z);
			}
		}));
		this.addButton(new Button(this.guiLeft + 361, this.guiTop + 334, 87, 20, new StringTextComponent("Next Page ->"), e -> {
			if (true) {
				NarutoMod.PACKET_HANDLER.sendToServer(new EquipmentGui.ButtonPressedMessage(5, x, y, z));
				EquipmentGui.handleButtonAction(entity, 5, x, y, z);
			}
		}));
	}
}
