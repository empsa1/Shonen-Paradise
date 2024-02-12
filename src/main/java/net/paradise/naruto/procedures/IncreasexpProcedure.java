package net.paradise.naruto.procedures;

import net.paradise.naruto.NarutoModVariables;
import net.paradise.naruto.NarutoMod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.HashMap;

public class IncreasexpProcedure {
	@Mod.EventBusSubscriber
	private static class GlobalTrigger {
		@SubscribeEvent
		public static void onPlayerXPChange(PlayerXpEvent.XpChange event) {
			if (event != null && event.getEntity() != null) {
				Entity entity = event.getEntity();
				double i = entity.getPosX();
				double j = entity.getPosY();
				double k = entity.getPosZ();
				int amount = event.getAmount();
				World world = entity.world;
				Map<String, Object> dependencies = new HashMap<>();
				dependencies.put("x", i);
				dependencies.put("y", j);
				dependencies.put("z", k);
				dependencies.put("world", world);
				dependencies.put("entity", entity);
				dependencies.put("amount", amount);
				dependencies.put("event", event);
				executeProcedure(dependencies);
			}
		}
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				NarutoMod.LOGGER.warn("Failed to load dependency world for procedure Increasexp!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				NarutoMod.LOGGER.warn("Failed to load dependency entity for procedure Increasexp!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		Entity entity = (Entity) dependencies.get("entity");
		if ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
				.orElse(new NarutoModVariables.PlayerVariables())).Level < NarutoModVariables.WorldVariables.get(world).MAX_LEVEL) {
			{
				double _setval = ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
						.orElse(new NarutoModVariables.PlayerVariables())).current_xp + 1000);
				entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.current_xp = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			if ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
					.orElse(new NarutoModVariables.PlayerVariables())).current_xp >= (entity
							.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
							.orElse(new NarutoModVariables.PlayerVariables())).xp_to_next_level) {
				{
					double _setval = ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
							.orElse(new NarutoModVariables.PlayerVariables())).Level + 1);
					entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.Level = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				{
					double _setval = ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
							.orElse(new NarutoModVariables.PlayerVariables())).Skill_Points + 1);
					entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.Skill_Points = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				{
					double _setval = ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
							.orElse(new NarutoModVariables.PlayerVariables())).current_xp
							- (entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
									.orElse(new NarutoModVariables.PlayerVariables())).xp_to_next_level);
					entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.current_xp = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				{
					double _setval = ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
							.orElse(new NarutoModVariables.PlayerVariables())).xp_to_next_level
							+ (entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
									.orElse(new NarutoModVariables.PlayerVariables())).xp_to_next_level * 2);
					entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.xp_to_next_level = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				if (entity instanceof PlayerEntity && !entity.world.isRemote()) {
					((PlayerEntity) entity).sendStatusMessage(new StringTextComponent("Leveled UP!"), (true));
				}
			} else {
				if (entity instanceof PlayerEntity && !entity.world.isRemote()) {
					((PlayerEntity) entity).sendStatusMessage(
							new StringTextComponent((("CurrentXP: " + (entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
									.orElse(new NarutoModVariables.PlayerVariables())).current_xp) + ""
									+ ("/Level: " + (entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
											.orElse(new NarutoModVariables.PlayerVariables())).Level))),
							(true));
				}
			}
		}
	}
}
