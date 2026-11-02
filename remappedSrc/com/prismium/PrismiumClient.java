package com.prismium;

import com.mojang.blaze3d.platform.InputConstants;
import com.prismium.client.PrismiumSettingsScreen;
import com.prismium.config.PrismiumConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class PrismiumClient implements ClientModInitializer {

    public static boolean entityCulling = true;
    public static int distantCullingDistance = 60;

    public static boolean particleCulling = true;
    public static int particleCullingDistance = 16;

    public static int signTextDistance = 32;

    public static int droppedItemDistance = 64;


    private static KeyMapping settingsKey;

    @Override
    public void onInitializeClient() {
        PrismiumConfig.load();
        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.prismium.settings",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath("prismium", "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (settingsKey.consumeClick()) {
                client.setScreen(new PrismiumSettingsScreen(client.screen));
            }
        });
    }

    public static void openSettings(Minecraft client) {
        client.setScreen(new PrismiumSettingsScreen(client.screen));
    }
}
