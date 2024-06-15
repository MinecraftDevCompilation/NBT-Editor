package com.luneruniverse.minecraft.mod.nbteditor.packets;

import com.luneruniverse.minecraft.mod.nbteditor.multiversion.networking.MVPacket;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GetBlockC2SPacket implements MVPacket {
	
	public static final Identifier ID = new Identifier("nbteditor", "get_block");
	
	private final int requestId;
	private final RegistryKey<World> world;
	private final BlockPos pos;
	
	public GetBlockC2SPacket(int requestId, RegistryKey<World> world, BlockPos pos) {
		this.requestId = requestId;
		this.world = world;
		this.pos = pos;
	}
	public GetBlockC2SPacket(PacketByteBuf payload) {
		this.requestId = payload.readInt();
		this.world = payload.readRegistryKey(payload.<World>readRegistryRefKey());
		this.pos = payload.readBlockPos();
	}
	
	public int getRequestId() {
		return requestId;
	}
	public RegistryKey<World> getWorld() {
		return world;
	}
	public BlockPos getPos() {
		return pos;
	}
	
	@Override
	public void write(PacketByteBuf payload) {
		payload.writeInt(requestId);
		payload.writeIdentifier(world.getRegistry());
		payload.writeRegistryKey(world);
		payload.writeBlockPos(pos);
	}
	
	@Override
	public Identifier id() {
		return ID;
	}
	
}
