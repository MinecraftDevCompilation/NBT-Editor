package com.luneruniverse.minecraft.mod.nbteditor.packets;

import java.util.UUID;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SetEntityC2SPacket implements FabricPacket {
	
	public static final PacketType<SetEntityC2SPacket> TYPE = PacketType.create(new Identifier("nbteditor", "set_entity"), SetEntityC2SPacket::new);
	
	private final RegistryKey<World> world;
	private final UUID uuid;
	private final NbtCompound nbt;
	
	public SetEntityC2SPacket(RegistryKey<World> world, UUID uuid, NbtCompound nbt) {
		this.world = world;
		this.uuid = uuid;
		this.nbt = nbt;
	}
	public SetEntityC2SPacket(PacketByteBuf payload) {
		this.world = payload.readRegistryKey(payload.<World>readRegistryRefKey());
		this.uuid = payload.readUuid();
		this.nbt = payload.readNbt();
	}
	
	public RegistryKey<World> getWorld() {
		return world;
	}
	public UUID getUUID() {
		return uuid;
	}
	public NbtCompound getNbt() {
		return nbt;
	}
	
	@Override
	public void write(PacketByteBuf payload) {
		payload.writeIdentifier(world.getRegistry());
		payload.writeRegistryKey(world);
		payload.writeUuid(uuid);
		payload.writeNbt(nbt);
	}
	
	@Override
	public PacketType<?> getType() {
		return TYPE;
	}
	
}