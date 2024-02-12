package net.paradise.naruto.procedures;

import net.paradise.naruto.NarutoModVariables;
import net.paradise.naruto.NarutoMod;

import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import java.util.Map;

public class TaijutsuEffectStartedappliedProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				NarutoMod.LOGGER.warn("Failed to load dependency entity for procedure TaijutsuEffectStartedapplied!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).removePotionEffect(Effects.STRENGTH);
		}
		if (entity instanceof LivingEntity)
			((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.STRENGTH, (int) 100000000,
					(int) ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
							.orElse(new NarutoModVariables.PlayerVariables())).Taijutsu - 1)));
	}
}
