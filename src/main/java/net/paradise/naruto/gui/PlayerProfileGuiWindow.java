
package net.paradise.naruto.gui;

import net.paradise.naruto.NarutoModVariables;
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
public class PlayerProfileGuiWindow extends ContainerScreen<PlayerProfileGui.GuiContainerMod> {
	private World world;
	private int x, y, z;
	private PlayerEntity entity;
	private final static HashMap guistate = PlayerProfileGui.guistate;

	public PlayerProfileGuiWindow(PlayerProfileGui.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.xSize = 480;
		this.ySize = 480;
	}

	private static final ResourceLocation texture = new ResourceLocation("naruto:textures/screens/player_profile.png");

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
		this.font.drawString(ms, "Player Profile", 196, 136, -12829636);
		this.font.drawString(ms, "Player name: " + (entity.getPersistentData().getString("tagName")) + "", 36, 162, -12829636);
		this.font.drawString(ms, "Village: ", 56, 176, -12829636);
		this.font.drawString(ms, "Village Rank: ", 30, 191, -12829636);
		this.font.drawString(ms,
				"XP/nextLvl: "
						+ ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
								.orElse(new NarutoModVariables.PlayerVariables())).current_xp)
						+ " / " + ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
								.orElse(new NarutoModVariables.PlayerVariables())).xp_to_next_level)
						+ "",
				40, 206, -12829636);
		this.font.drawString(ms, "Level: " + (int) ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
				.orElse(new NarutoModVariables.PlayerVariables())).Level) + "", 66, 222, -12829636);
		this.font.drawString(ms, "BodyCount: ", 45, 238, -12829636);
		this.font.drawString(ms, "Known as: ", 50, 253, -12829636);
		this.font.drawString(ms, "Nat Release: ", 35, 268, -12829636);
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
		this.addButton(new Button(this.guiLeft + 358, this.guiTop + 333, 87, 20, new StringTextComponent("Next Page ->"), e -> {
			if (true) {
				NarutoMod.PACKET_HANDLER.sendToServer(new PlayerProfileGui.ButtonPressedMessage(0, x, y, z));
				PlayerProfileGui.handleButtonAction(entity, 0, x, y, z);
			}
		}));
		this.addButton(new Button(this.guiLeft + 29, this.guiTop + 287, 119, 20, new StringTextComponent("RESET SKILL POINTS"), e -> {
			if (true) {
				NarutoMod.PACKET_HANDLER.sendToServer(new PlayerProfileGui.ButtonPressedMessage(1, x, y, z));
				PlayerProfileGui.handleButtonAction(entity, 1, x, y, z);
			}
		}));
	}
}
