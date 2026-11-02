package com.prismium.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prismium.PrismiumClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class PrismiumConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("prismium.json");

    private boolean entityCulling = true;
    private int distantCullingDistance = 60;
    private boolean particleCulling = true;
    private int particleCullingDistance = 16;
    private int signTextDistance = 32;

    private int droppedItemDistance = 64;

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                try (Reader reader = Files.newBufferedReader(FILE)) {
                    PrismiumConfig config = GSON.fromJson(reader, PrismiumConfig.class);

                    if (config != null) {
                        PrismiumClient.entityCulling = config.entityCulling;
                        PrismiumClient.distantCullingDistance = config.distantCullingDistance;
                        PrismiumClient.particleCulling = config.particleCulling;
                        PrismiumClient.particleCullingDistance = config.particleCullingDistance;
                        PrismiumClient.signTextDistance = config.signTextDistance;
            PrismiumClient.droppedItemDistance = config.droppedItemDistance;
                    }
                }
            } else {
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            PrismiumConfig config = new PrismiumConfig();

            config.entityCulling = PrismiumClient.entityCulling;
            config.distantCullingDistance = PrismiumClient.distantCullingDistance;
            config.particleCulling = PrismiumClient.particleCulling;
            config.particleCullingDistance = PrismiumClient.particleCullingDistance;
            config.signTextDistance = PrismiumClient.signTextDistance;
        config.droppedItemDistance = PrismiumClient.droppedItemDistance;

            Files.createDirectories(FILE.getParent());

            try (Writer writer = Files.newBufferedWriter(FILE)) {
                GSON.toJson(config, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
