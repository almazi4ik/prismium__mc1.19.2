package com.prismium;

import com.prismium.client.PrismiumSettingsScreen;
import com.prismium.config.PrismiumConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PrismiumClient implements ClientModInitializer {

    public static boolean entityCulling = true;
    public static int distantCullingDistance = 60;

    public static boolean particleCulling = true;
    public static int particleCullingDistance = 16;

    public static int signTextDistance = 32;

    public static int droppedItemDistance = 64;


    private static KeyBinding settingsKey;

    @Override
    public void onInitializeClient() {
        PrismiumConfig.load();
        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.prismium.settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KeyBinding.Category.create(Identifier.of("prismium", "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (settingsKey.wasPressed()) {
                client.setScreen(new PrismiumSettingsScreen(client.currentScreen));
            }
        });
    }

    public static void openSettings(MinecraftClient client) {
        client.setScreen(new PrismiumSettingsScreen(client.currentScreen));
    }
}
