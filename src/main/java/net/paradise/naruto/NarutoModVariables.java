package net.paradise.naruto;

import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.world.IServerWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Direction;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public class NarutoModVariables {
	public NarutoModVariables(NarutoModElements elements) {
		elements.addNetworkMessage(WorldSavedDataSyncMessage.class, WorldSavedDataSyncMessage::buffer, WorldSavedDataSyncMessage::new,
				WorldSavedDataSyncMessage::handler);
		elements.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new,
				PlayerVariablesSyncMessage::handler);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
	}

	private void init(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(PlayerVariables.class, new PlayerVariablesStorage(), PlayerVariables::new);
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getPlayer().world.isRemote()) {
			WorldSavedData mapdata = MapVariables.get(event.getPlayer().world);
			WorldSavedData worlddata = WorldVariables.get(event.getPlayer().world);
			if (mapdata != null)
				NarutoMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
						new WorldSavedDataSyncMessage(0, mapdata));
			if (worlddata != null)
				NarutoMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
						new WorldSavedDataSyncMessage(1, worlddata));
		}
	}

	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getPlayer().world.isRemote()) {
			WorldSavedData worlddata = WorldVariables.get(event.getPlayer().world);
			if (worlddata != null)
				NarutoMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
						new WorldSavedDataSyncMessage(1, worlddata));
		}
	}

	public static class WorldVariables extends WorldSavedData {
		public static final String DATA_NAME = "naruto_worldvars";
		public double MAX_TAIJUTSU = 100.0;
		public double MAX_KENJUTSU = 100.0;
		public double MAX_GENJUTSU = 100.0;
		public double MAX_NINJUTSU = 100.0;
		public double MAX_SPEED = 100.0;
		public double MAX_CHAKRA = 100.0;
		public double MAX_HEALTH = 100.0;
		public double MAX_LEVEL = 100.0;
		public double MAX_ARMOR = 100.0;

		public WorldVariables() {
			super(DATA_NAME);
		}

		public WorldVariables(String s) {
			super(s);
		}

		@Override
		public void read(CompoundNBT nbt) {
			MAX_TAIJUTSU = nbt.getDouble("MAX_TAIJUTSU");
			MAX_KENJUTSU = nbt.getDouble("MAX_KENJUTSU");
			MAX_GENJUTSU = nbt.getDouble("MAX_GENJUTSU");
			MAX_NINJUTSU = nbt.getDouble("MAX_NINJUTSU");
			MAX_SPEED = nbt.getDouble("MAX_SPEED");
			MAX_CHAKRA = nbt.getDouble("MAX_CHAKRA");
			MAX_HEALTH = nbt.getDouble("MAX_HEALTH");
			MAX_LEVEL = nbt.getDouble("MAX_LEVEL");
			MAX_ARMOR = nbt.getDouble("MAX_ARMOR");
		}

		@Override
		public CompoundNBT write(CompoundNBT nbt) {
			nbt.putDouble("MAX_TAIJUTSU", MAX_TAIJUTSU);
			nbt.putDouble("MAX_KENJUTSU", MAX_KENJUTSU);
			nbt.putDouble("MAX_GENJUTSU", MAX_GENJUTSU);
			nbt.putDouble("MAX_NINJUTSU", MAX_NINJUTSU);
			nbt.putDouble("MAX_SPEED", MAX_SPEED);
			nbt.putDouble("MAX_CHAKRA", MAX_CHAKRA);
			nbt.putDouble("MAX_HEALTH", MAX_HEALTH);
			nbt.putDouble("MAX_LEVEL", MAX_LEVEL);
			nbt.putDouble("MAX_ARMOR", MAX_ARMOR);
			return nbt;
		}

		public void syncData(IWorld world) {
			this.markDirty();
			if (world instanceof World && !world.isRemote())
				NarutoMod.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(((World) world)::getDimensionKey),
						new WorldSavedDataSyncMessage(1, this));
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(IWorld world) {
			if (world instanceof ServerWorld) {
				return ((ServerWorld) world).getSavedData().getOrCreate(WorldVariables::new, DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends WorldSavedData {
		public static final String DATA_NAME = "naruto_mapvars";

		public MapVariables() {
			super(DATA_NAME);
		}

		public MapVariables(String s) {
			super(s);
		}

		@Override
		public void read(CompoundNBT nbt) {
		}

		@Override
		public CompoundNBT write(CompoundNBT nbt) {
			return nbt;
		}

		public void syncData(IWorld world) {
			this.markDirty();
			if (world instanceof World && !world.isRemote())
				NarutoMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new WorldSavedDataSyncMessage(0, this));
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(IWorld world) {
			if (world instanceof IServerWorld) {
				return ((IServerWorld) world).getWorld().getServer().getWorld(World.OVERWORLD).getSavedData().getOrCreate(MapVariables::new,
						DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class WorldSavedDataSyncMessage {
		public int type;
		public WorldSavedData data;

		public WorldSavedDataSyncMessage(PacketBuffer buffer) {
			this.type = buffer.readInt();
			this.data = this.type == 0 ? new MapVariables() : new WorldVariables();
			this.data.read(buffer.readCompoundTag());
		}

		public WorldSavedDataSyncMessage(int type, WorldSavedData data) {
			this.type = type;
			this.data = data;
		}

		public static void buffer(WorldSavedDataSyncMessage message, PacketBuffer buffer) {
			buffer.writeInt(message.type);
			buffer.writeCompoundTag(message.data.write(new CompoundNBT()));
		}

		public static void handler(WorldSavedDataSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer()) {
					if (message.type == 0)
						MapVariables.clientSide = (MapVariables) message.data;
					else
						WorldVariables.clientSide = (WorldVariables) message.data;
				}
			});
			context.setPacketHandled(true);
		}
	}

	@CapabilityInject(PlayerVariables.class)
	public static Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = null;

	@SubscribeEvent
	public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity && !(event.getObject() instanceof FakePlayer))
			event.addCapability(new ResourceLocation("naruto", "player_variables"), new PlayerVariablesProvider());
	}

	private static class PlayerVariablesProvider implements ICapabilitySerializable<INBT> {
		private final LazyOptional<PlayerVariables> instance = LazyOptional.of(PLAYER_VARIABLES_CAPABILITY::getDefaultInstance);

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}

		@Override
		public INBT serializeNBT() {
			return PLAYER_VARIABLES_CAPABILITY.getStorage().writeNBT(PLAYER_VARIABLES_CAPABILITY, this.instance.orElseThrow(RuntimeException::new),
					null);
		}

		@Override
		public void deserializeNBT(INBT nbt) {
			PLAYER_VARIABLES_CAPABILITY.getStorage().readNBT(PLAYER_VARIABLES_CAPABILITY, this.instance.orElseThrow(RuntimeException::new), null,
					nbt);
		}
	}

	private static class PlayerVariablesStorage implements Capability.IStorage<PlayerVariables> {
		@Override
		public INBT writeNBT(Capability<PlayerVariables> capability, PlayerVariables instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putDouble("Taijutsu", instance.Taijutsu);
			nbt.putDouble("Kenjutsu", instance.Kenjutsu);
			nbt.putDouble("Genjutsu", instance.Genjutsu);
			nbt.putDouble("Ninjutsu", instance.Ninjutsu);
			nbt.putDouble("Speed", instance.Speed);
			nbt.putDouble("Chakra", instance.Chakra);
			nbt.putDouble("Chakra_Regen", instance.Chakra_Regen);
			nbt.putDouble("Chakra_Control", instance.Chakra_Control);
			nbt.putDouble("Health", instance.Health);
			nbt.putDouble("Health_Regen", instance.Health_Regen);
			nbt.putDouble("Armor", instance.Armor);
			nbt.putDouble("Skill_Points", instance.Skill_Points);
			nbt.putDouble("Level", instance.Level);
			nbt.putDouble("xp_to_next_level", instance.xp_to_next_level);
			nbt.putDouble("current_xp", instance.current_xp);
			nbt.putDouble("Rank", instance.Rank);
			nbt.putDouble("Tools", instance.Tools);
			nbt.put("THROWABLE_TOOL", instance.THROWABLE_TOOL.write(new CompoundNBT()));
			return nbt;
		}

		@Override
		public void readNBT(Capability<PlayerVariables> capability, PlayerVariables instance, Direction side, INBT inbt) {
			CompoundNBT nbt = (CompoundNBT) inbt;
			instance.Taijutsu = nbt.getDouble("Taijutsu");
			instance.Kenjutsu = nbt.getDouble("Kenjutsu");
			instance.Genjutsu = nbt.getDouble("Genjutsu");
			instance.Ninjutsu = nbt.getDouble("Ninjutsu");
			instance.Speed = nbt.getDouble("Speed");
			instance.Chakra = nbt.getDouble("Chakra");
			instance.Chakra_Regen = nbt.getDouble("Chakra_Regen");
			instance.Chakra_Control = nbt.getDouble("Chakra_Control");
			instance.Health = nbt.getDouble("Health");
			instance.Health_Regen = nbt.getDouble("Health_Regen");
			instance.Armor = nbt.getDouble("Armor");
			instance.Skill_Points = nbt.getDouble("Skill_Points");
			instance.Level = nbt.getDouble("Level");
			instance.xp_to_next_level = nbt.getDouble("xp_to_next_level");
			instance.current_xp = nbt.getDouble("current_xp");
			instance.Rank = nbt.getDouble("Rank");
			instance.Tools = nbt.getDouble("Tools");
			instance.THROWABLE_TOOL = ItemStack.read(nbt.getCompound("THROWABLE_TOOL"));
		}
	}

	public static class PlayerVariables {
		public double Taijutsu = 0.0;
		public double Kenjutsu = 0;
		public double Genjutsu = 0;
		public double Ninjutsu = 0;
		public double Speed = 0;
		public double Chakra = 100.0;
		public double Chakra_Regen = 0;
		public double Chakra_Control = 0;
		public double Health = 0;
		public double Health_Regen = 0;
		public double Armor = 0;
		public double Skill_Points = 0;
		public double Level = 0;
		public double xp_to_next_level = 100.0;
		public double current_xp = 0;
		public double Rank = 0;
		public double Tools = 10.0;
		public ItemStack THROWABLE_TOOL = ItemStack.EMPTY;

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayerEntity)
				NarutoMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new PlayerVariablesSyncMessage(this));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getPlayer().world.isRemote())
			((PlayerVariables) event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()))
					.syncPlayerVariables(event.getPlayer());
	}

	@SubscribeEvent
	public void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		if (!event.getPlayer().world.isRemote())
			((PlayerVariables) event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()))
					.syncPlayerVariables(event.getPlayer());
	}

	@SubscribeEvent
	public void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getPlayer().world.isRemote())
			((PlayerVariables) event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()))
					.syncPlayerVariables(event.getPlayer());
	}

	@SubscribeEvent
	public void clonePlayer(PlayerEvent.Clone event) {
		PlayerVariables original = ((PlayerVariables) event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null)
				.orElse(new PlayerVariables()));
		PlayerVariables clone = ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
		if (!event.isWasDeath()) {
			clone.Taijutsu = original.Taijutsu;
			clone.Kenjutsu = original.Kenjutsu;
			clone.Genjutsu = original.Genjutsu;
			clone.Ninjutsu = original.Ninjutsu;
			clone.Speed = original.Speed;
			clone.Chakra = original.Chakra;
			clone.Chakra_Regen = original.Chakra_Regen;
			clone.Chakra_Control = original.Chakra_Control;
			clone.Health = original.Health;
			clone.Health_Regen = original.Health_Regen;
			clone.Armor = original.Armor;
			clone.Skill_Points = original.Skill_Points;
			clone.Level = original.Level;
			clone.xp_to_next_level = original.xp_to_next_level;
			clone.current_xp = original.current_xp;
			clone.Rank = original.Rank;
			clone.Tools = original.Tools;
			clone.THROWABLE_TOOL = original.THROWABLE_TOOL;
		}
	}

	public static class PlayerVariablesSyncMessage {
		public PlayerVariables data;

		public PlayerVariablesSyncMessage(PacketBuffer buffer) {
			this.data = new PlayerVariables();
			new PlayerVariablesStorage().readNBT(null, this.data, null, buffer.readCompoundTag());
		}

		public PlayerVariablesSyncMessage(PlayerVariables data) {
			this.data = data;
		}

		public static void buffer(PlayerVariablesSyncMessage message, PacketBuffer buffer) {
			buffer.writeCompoundTag((CompoundNBT) new PlayerVariablesStorage().writeNBT(null, message.data, null));
		}

		public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer()) {
					PlayerVariables variables = ((PlayerVariables) Minecraft.getInstance().player.getCapability(PLAYER_VARIABLES_CAPABILITY, null)
							.orElse(new PlayerVariables()));
					variables.Taijutsu = message.data.Taijutsu;
					variables.Kenjutsu = message.data.Kenjutsu;
					variables.Genjutsu = message.data.Genjutsu;
					variables.Ninjutsu = message.data.Ninjutsu;
					variables.Speed = message.data.Speed;
					variables.Chakra = message.data.Chakra;
					variables.Chakra_Regen = message.data.Chakra_Regen;
					variables.Chakra_Control = message.data.Chakra_Control;
					variables.Health = message.data.Health;
					variables.Health_Regen = message.data.Health_Regen;
					variables.Armor = message.data.Armor;
					variables.Skill_Points = message.data.Skill_Points;
					variables.Level = message.data.Level;
					variables.xp_to_next_level = message.data.xp_to_next_level;
					variables.current_xp = message.data.current_xp;
					variables.Rank = message.data.Rank;
					variables.Tools = message.data.Tools;
					variables.THROWABLE_TOOL = message.data.THROWABLE_TOOL;
				}
			});
			context.setPacketHandled(true);
		}
	}
}
