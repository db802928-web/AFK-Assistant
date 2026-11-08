package dev.afkassistant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AfkConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("afk-assistant.json");
    public boolean minerEnabled = false;
    public boolean schedulerEnabled = false;
    public boolean useDimensionArea = true;
    public int width = 5, length = 5, height = 1;
    public int corner1X, corner1Y, corner1Z, corner2X, corner2Y, corner2Z;
    public List<String> blockIds = new ArrayList<>(List.of("minecraft:cobblestone"));
    public List<CommandEntry> commands = new ArrayList<>(List.of(new CommandEntry("/repair", 300)));
    public static AfkConfig INSTANCE = new AfkConfig();

    public static void load() {
        try {
            if (Files.exists(FILE)) INSTANCE = GSON.fromJson(Files.readString(FILE), AfkConfig.class);
            if (INSTANCE == null) INSTANCE = new AfkConfig();
            if (INSTANCE.blockIds == null) INSTANCE.blockIds = new ArrayList<>();
            if (INSTANCE.commands == null) INSTANCE.commands = new ArrayList<>();
        } catch (Exception e) {
            INSTANCE = new AfkConfig();
            System.err.println("[AFK Assistant] Config load failed: " + e.getMessage());
        }
    }
    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            System.err.println("[AFK Assistant] Config save failed: " + e.getMessage());
        }
    }
    public static class CommandEntry {
        public String command;
        public int intervalSeconds;
        public boolean enabled = true;
        public long nextRunMillis = 0;
        public CommandEntry() { this("/repair", 300); }
        public CommandEntry(String command, int seconds) {
            this.command = command;
            this.intervalSeconds = seconds;
        }
    }
}
