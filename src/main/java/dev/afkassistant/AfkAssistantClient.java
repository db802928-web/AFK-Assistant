package dev.afkassistant;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AfkAssistantClient implements ClientModInitializer {
    private KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        AfkConfig.load();
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.afkassistant.open", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, "category.afkassistant"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new AfkScreen());
            }
            CommandScheduler.tick(client);
            Miner.tick(client);
        });
    }
}
