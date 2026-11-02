package com.prismium.client;

import com.prismium.PrismiumClient;
import com.prismium.config.PrismiumConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PrismiumSettingsScreen extends Screen {

    private final Screen parent;

    public PrismiumSettingsScreen(Screen parent) {
        super(Component.literal("Prismium Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 60;

        this.addRenderableWidget(Button.builder(
                Component.literal("Entity Culling: " +
                        (PrismiumClient.entityCulling ? "ON" : "OFF")),
                button -> {
                    PrismiumClient.entityCulling = !PrismiumClient.entityCulling;
                    button.setMessage(Component.literal(
                            "Entity Culling: " +
                                    (PrismiumClient.entityCulling ? "ON" : "OFF")
                    ));
                }
        ).bounds(centerX - 100, y, 200, 20).build());

        addDistanceButtons(
                centerX, y + 30,
                "Distance: ",
                PrismiumClient.distantCullingDistance,
                () -> {
                    int[] steps = {1, 8, 16, 24, 32, 40, 48, 56, 64};
                    int current = PrismiumClient.distantCullingDistance;
                    for (int i = 0; i < steps.length; i++) {
                        if (steps[i] >= current) {
                            PrismiumClient.distantCullingDistance =
                                    steps[Math.max(0, i - 1)];
                            break;
                        }
                    }
                    if (current > 64) {
                        PrismiumClient.distantCullingDistance = 64;
                    }
                    rebuild();
                },
                () -> {
                    int[] steps = {1, 8, 16, 24, 32, 40, 48, 56, 64};
                    int current = PrismiumClient.distantCullingDistance;
                    int next = 64;
                    for (int step : steps) {
                        if (step > current) {
                            next = step;
                            break;
                        }
                    }
                    PrismiumClient.distantCullingDistance = next;
                    rebuild();
                }
        );

        this.addRenderableWidget(Button.builder(
                Component.literal("Particle Culling: " +
                        (PrismiumClient.particleCulling ? "ON" : "OFF")),
                button -> {
                    PrismiumClient.particleCulling =
                            !PrismiumClient.particleCulling;
                    button.setMessage(Component.literal(
                            "Particle Culling: " +
                                    (PrismiumClient.particleCulling ? "ON" : "OFF")
                    ));
                }
        ).bounds(centerX - 100, y + 60, 200, 20).build());


        addDistanceButtons(
                centerX, y + 120,
                "Sign Text Distance: ",
                PrismiumClient.signTextDistance,
                () -> {
                    int[] steps = {1, 8, 16, 24, 32, 40, 48, 56, 64};
                    int current = PrismiumClient.signTextDistance;
                    int previous = 1;
                    for (int step : steps) {
                        if (step >= current) break;
                        previous = step;
                    }
                    PrismiumClient.signTextDistance = previous;
                    rebuild();
                },
                () -> {
                    int[] steps = {1, 8, 16, 24, 32, 40, 48, 56, 64};
                    int current = PrismiumClient.signTextDistance;
                    int next = 64;
                    for (int step : steps) {
                        if (step > current) {
                            next = step;
                            break;
                        }
                    }
                    PrismiumClient.signTextDistance = next;
                    rebuild();
                }
        );

        addDistanceButtons(
                centerX, y + 90,
                "Particle Distance: ",
                PrismiumClient.particleCullingDistance,
                () -> {
                    PrismiumClient.particleCullingDistance =
                            switch (PrismiumClient.particleCullingDistance) {
                                case 32 -> 24;
                                case 24 -> 16;
                                case 16 -> 8;
                                case 8 -> 1;
                                default -> 1;
                            };
                    rebuild();
                },
                () -> {
                    PrismiumClient.particleCullingDistance =
                            switch (PrismiumClient.particleCullingDistance) {
                                case 1 -> 8;
                                case 8 -> 16;
                                case 16 -> 24;
                                case 24 -> 32;
                                default -> 32;
                            };
                    rebuild();
                }
        );

        addDistanceButtons(
                centerX, y + 150,
                "Dropped Item Distance: ",
                PrismiumClient.droppedItemDistance,
                () -> {
                    PrismiumClient.droppedItemDistance =
                            switch (PrismiumClient.droppedItemDistance) {
                                case 128 -> 96;
                                case 96 -> 64;
                                case 64 -> 48;
                                case 48 -> 32;
                                case 32 -> 16;
                                case 16 -> 8;
                                case 8 -> 1;
                                default -> 1;
                            };
                    rebuild();
                },
                () -> {
                    PrismiumClient.droppedItemDistance =
                            switch (PrismiumClient.droppedItemDistance) {
                                case 1 -> 8;
                                case 8 -> 16;
                                case 16 -> 32;
                                case 32 -> 48;
                                case 48 -> 64;
                                case 64 -> 96;
                                case 96 -> 128;
                                default -> 128;
                            };
                    rebuild();
                }
        );

        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> {
                    PrismiumConfig.save();
                    this.onClose();
                }
        ).bounds(centerX - 100, this.height - 35, 200, 20).build());
    }


    private void addDistanceButtons(
            int centerX,
            int y,
            String label,
            int value,
            Runnable minus,
            Runnable plus
    ) {
        this.addRenderableWidget(Button.builder(
                Component.literal("-"),
                button -> minus.run()
        ).bounds(centerX - 120, y, 40, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal(label + value + " blocks"),
                button -> {}
        ).bounds(centerX - 75, y, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("+"),
                button -> plus.run()
        ).bounds(centerX + 80, y, 40, 20).build());
    }

    private void rebuild() {
        this.clearWidgets();
        this.init();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xE6101018);

        context.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                25,
                0xFFFFFF
        );

        context.drawCenteredString(
                this.font,
                Component.literal("Performance"),
                this.width / 2,
                45,
                0xAAAAAA
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}
