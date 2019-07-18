package vazkii.botania.common.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import vazkii.botania.common.lib.LibMisc;

import java.util.HashSet;
import java.util.Set;

public final class PacketHandler {
	private static final String PROTOCOL = "1";
	public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(LibMisc.MOD_ID, "chan"),
			() -> PROTOCOL,
			PROTOCOL::equals,
			PROTOCOL::equals
	);

	public static void init() {
		int id = 0;
		HANDLER.registerMessage(id++, PacketBotaniaEffect.class, PacketBotaniaEffect::encode, PacketBotaniaEffect::decode, PacketBotaniaEffect.Handler::handle);
		HANDLER.registerMessage(id++, PacketLeftClick.class, PacketLeftClick::encode, PacketLeftClick::decode, PacketLeftClick::handle);
		HANDLER.registerMessage(id++, PacketDodge.class, PacketDodge::encode, PacketDodge::decode, PacketDodge::handle);
		HANDLER.registerMessage(id++, PacketJump.class, PacketJump::encode, PacketJump::decode, PacketJump::handle);
		HANDLER.registerMessage(id++, PacketItemAge.class, PacketItemAge::encode, PacketItemAge::decode, PacketItemAge::handle);
		HANDLER.registerMessage(id++, PacketSyncRecipes.class, PacketSyncRecipes::encode, PacketSyncRecipes::decode, PacketSyncRecipes::handle);
	}

	/**
	 * Send message to all within 64 blocks that have this chunk loaded
	 */
	public static void sendToNearby(World world, BlockPos pos, Object toSend) {
		if(world instanceof ServerWorld) {
			ServerWorld ws = (ServerWorld) world;

			// todo 1.14 this sends duplicates
			for (ServerPlayerEntity playerMP : ws.getPlayers()) {
				if (playerMP.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64) {
					HANDLER.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), toSend);
				}
			}

		}
	}

	public static void sendToNearby(World world, Entity e, Object toSend) {
		sendToNearby(world, new BlockPos(e), toSend);
	}

	public static void sendTo(ServerPlayerEntity playerMP, Object toSend) {
		HANDLER.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void sendToServer(Object msg) {
		HANDLER.sendToServer(msg);
	}

	private PacketHandler() {}

}
