package com.luneruniverse.minecraft.mod.nbteditor.packets;

import java.util.UUID;

import com.luneruniverse.minecraft.mod.nbteditor.multiversion.networking.MVPacket;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SetEntityC2SPacket implements MVPacket {
	
	public static final Identifier ID = new Identifier("nbteditor", "set_entity");
	
	private final RegistryKey<World> world;
	private final UUID uuid;
	private final Identifier id;
	private final NbtCompound nbt;
	private final boolean recreate;
	
	public SetEntityC2SPacket(RegistryKey<World> world, UUID uuid, Identifier id, NbtCompound nbt, boolean recreate) {
		this.world = world;
		this.uuid = uuid;
		this.id = id;
		this.nbt = nbt;
		this.recreate = recreate;
	}
	public SetEntityC2SPacket(PacketByteBuf payload) {
		this.world = payload.readRegistryKey(payload.<World>readRegistryRefKey());
		this.uuid = payload.readUuid();
		this.id = payload.readIdentifier();
		this.nbt = payload.readNbt();
		this.recreate = payload.readBoolean();
	}
	
	public RegistryKey<World> getWorld() {
		return world;
	}
	public UUID getUUID() {
		return uuid;
	}
	public Identifier getId() {
		return id;
	}
	public NbtCompound getNbt() {
		return nbt;
	}
	public boolean isRecreate() {
		return recreate;
	}
	
	@Override
	public void write(PacketByteBuf payload) {
		payload.writeIdentifier(world.getRegistry());
		payload.writeRegistryKey(world);
		payload.writeUuid(uuid);
		payload.writeIdentifier(id);
		payload.writeNbt(nbt);
		payload.writeBoolean(recreate);
	}
	
	@Override
	public Identifier id() {
		return ID;
	}
	
}
