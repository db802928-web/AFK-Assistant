package dev.afkassistant;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.Registries;
import java.util.HashSet;
import java.util.Set;

public final class Miner {
    private static BlockPos target = null;
    private static int scanCursor = 0;
    private Miner() {}

    public static void stop() { target = null; }

    public static void tick(MinecraftClient client) {
        AfkConfig cfg = AfkConfig.INSTANCE;
        if (!cfg.minerEnabled || client.player == null || client.world == null ||
                client.interactionManager == null || client.currentScreen != null) {
            target = null;
            return;
        }

        Set<String> wanted = new HashSet<>(cfg.blockIds);
        if (wanted.isEmpty()) return;

        if (target != null) {
            String id = Registries.BLOCK.getId(client.world.getBlockState(target).getBlock()).toString();
            if (!wanted.contains(id) || client.world.getBlockState(target).isAir()) {
                target = null;
            } else if (client.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(target)) <= 25.0) {
                client.interactionManager.updateBlockBreakingProgress(target, Direction.UP);
                client.player.swingHand(Hand.MAIN_HAND);
                return;
            } else {
                target = null;
            }
        }

        int minX, minY, minZ, maxX, maxY, maxZ;
        if (cfg.useDimensionArea) {
            BlockPos p = client.player.getBlockPos();
            minX = p.getX() - Math.max(1, cfg.width) / 2;
            minY = p.getY() - Math.max(1, cfg.height) / 2;
            minZ = p.getZ() - Math.max(1, cfg.length) / 2;
            maxX = minX + Math.max(1, Math.min(16, cfg.width)) - 1;
            maxY = minY + Math.max(1, Math.min(8, cfg.height)) - 1;
            maxZ = minZ + Math.max(1, Math.min(16, cfg.length)) - 1;
        } else {
            minX = Math.min(cfg.corner1X, cfg.corner2X);
            minY = Math.min(cfg.corner1Y, cfg.corner2Y);
            minZ = Math.min(cfg.corner1Z, cfg.corner2Z);
            maxX = Math.max(cfg.corner1X, cfg.corner2X);
            maxY = Math.max(cfg.corner1Y, cfg.corner2Y);
            maxZ = Math.max(cfg.corner1Z, cfg.corner2Z);
        }

        int sx = maxX-minX+1, sy = maxY-minY+1, sz = maxZ-minZ+1;
        long volume = (long)sx * sy * sz;
        if (sx <= 0 || sy <= 0 || sz <= 0 || volume > 4096) return;

        for (int n = 0; n < volume; n++) {
            int i = Math.floorMod(scanCursor++, (int)volume);
            BlockPos pos = new BlockPos(minX + i % sx, minY + (i / (sx * sz)),
                    minZ + (i / sx) % sz);
            String id = Registries.BLOCK.getId(client.world.getBlockState(pos).getBlock()).toString();
            if (wanted.contains(id) &&
                    client.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(pos)) <= 25.0) {
                target = pos.toImmutable();
                client.interactionManager.attackBlock(target, Direction.UP);
                client.player.swingHand(Hand.MAIN_HAND);
                return;
            }
        }
    }
}
