package dev.afkassistant;

import net.minecraft.client.MinecraftClient;

public final class CommandScheduler {
    private CommandScheduler() {}
    public static void tick(MinecraftClient client) {
        AfkConfig cfg = AfkConfig.INSTANCE;
        if (!cfg.schedulerEnabled || client.player == null || client.getNetworkHandler() == null) return;
        long now = System.currentTimeMillis();
        for (AfkConfig.CommandEntry entry : cfg.commands) {
            if (!entry.enabled || entry.command == null || entry.command.isBlank()) continue;
            int interval = Math.max(10, entry.intervalSeconds);
            if (entry.nextRunMillis == 0) entry.nextRunMillis = now + interval * 1000L;
            if (now >= entry.nextRunMillis) {
                String command = entry.command.trim();
                if (command.startsWith("/")) command = command.substring(1);
                if (!command.isBlank()) client.getNetworkHandler().sendChatCommand(command);
                entry.nextRunMillis = now + interval * 1000L;
            }
        }
    }
}
