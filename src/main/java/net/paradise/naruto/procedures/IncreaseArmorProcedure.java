package net.paradise.naruto.procedures;

import net.paradise.naruto.NarutoModVariables;
import net.paradise.naruto.NarutoMod;

import net.minecraft.world.IWorld;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import java.util.Map;

public class IncreaseArmorProcedure {

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				NarutoMod.LOGGER.warn("Failed to load dependency world for procedure IncreaseArmor!");
			return false;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				NarutoMod.LOGGER.warn("Failed to load dependency entity for procedure IncreaseArmor!");
			return false;
		}
		IWorld world = (IWorld) dependencies.get("world");
		Entity entity = (Entity) dependencies.get("entity");
		if ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
				.orElse(new NarutoModVariables.PlayerVariables())).Skill_Points > 0
				&& (entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
						.orElse(new NarutoModVariables.PlayerVariables())).Armor < NarutoModVariables.WorldVariables.get(world).MAX_ARMOR) {
			{
				double _setval = ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
						.orElse(new NarutoModVariables.PlayerVariables())).Skill_Points - 1);
				entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.Skill_Points = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			{
				double _setval = ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
						.orElse(new NarutoModVariables.PlayerVariables())).Armor + 1);
				entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.Armor = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			return true;
		}
		if (entity instanceof PlayerEntity && !entity.world.isRemote()) {
			((PlayerEntity) entity).sendStatusMessage(new StringTextComponent("You cant do that action!"), (false));
		}
		return true;
	}
}
