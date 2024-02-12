package net.paradise.naruto.procedures;

import net.paradise.naruto.potion.TaijutsuPotionEffect;
import net.paradise.naruto.NarutoModVariables;
import net.paradise.naruto.NarutoMod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.World;
import net.minecraft.potion.EffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class ExecuteTaijutsuProcedure {
	@Mod.EventBusSubscriber
	private static class GlobalTrigger {
		@SubscribeEvent
		public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
			if (event.phase == TickEvent.Phase.END) {
				Entity entity = event.player;
				World world = entity.world;
				double i = entity.getPosX();
				double j = entity.getPosY();
				double k = entity.getPosZ();
				Map<String, Object> dependencies = new HashMap<>();
				dependencies.put("x", i);
				dependencies.put("y", j);
				dependencies.put("z", k);
				dependencies.put("world", world);
				dependencies.put("entity", entity);
				dependencies.put("event", event);
				executeProcedure(dependencies);
			}
		}
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				NarutoMod.LOGGER.warn("Failed to load dependency entity for procedure ExecuteTaijutsu!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		if ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NarutoModVariables.PlayerVariables())).Taijutsu > 0
				&& ((entity instanceof LivingEntity) ? ((LivingEntity) entity).getHeldItemMainhand() : ItemStack.EMPTY).getItem() == (ItemStack.EMPTY)
						.getItem()) {
			if (!(new Object() {
				boolean check(Entity _entity) {
					if (_entity instanceof LivingEntity) {
						Collection<EffectInstance> effects = ((LivingEntity) _entity).getActivePotionEffects();
						for (EffectInstance effect : effects) {
							if (effect.getPotion() == TaijutsuPotionEffect.potion)
								return true;
						}
					}
					return false;
				}
			}.check(entity)) || new Object() {
				int check(Entity _entity) {
					if (_entity instanceof LivingEntity) {
						Collection<EffectInstance> effects = ((LivingEntity) _entity).getActivePotionEffects();
						for (EffectInstance effect : effects) {
							if (effect.getPotion() == TaijutsuPotionEffect.potion)
								return effect.getAmplifier();
						}
					}
					return 0;
				}
			}.check(entity) != (entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
					.orElse(new NarutoModVariables.PlayerVariables())).Taijutsu) {
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).removePotionEffect(TaijutsuPotionEffect.potion);
				}
				if (entity instanceof LivingEntity)
					((LivingEntity) entity).addPotionEffect(new EffectInstance(TaijutsuPotionEffect.potion, (int) 10000000,
							(int) ((entity.getCapability(NarutoModVariables.PLAYER_VARIABLES_CAPABILITY, null)
									.orElse(new NarutoModVariables.PlayerVariables())).Taijutsu - 1)));
			}
		} else {
			if (entity instanceof LivingEntity) {
				((LivingEntity) entity).removePotionEffect(TaijutsuPotionEffect.potion);
			}
		}
	}
}