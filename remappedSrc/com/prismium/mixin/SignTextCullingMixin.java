package com.prismium.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.prismium.PrismiumClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignRenderer.class)
public class SignTextCullingMixin {

    @Inject(method = "submitSignText", at = @At("HEAD"), cancellable = true)
    private void prismium$signTextDistance(
            SignRenderState state,
            PoseStack matrices,
            SubmitNodeCollector queue,
            boolean front,
            CallbackInfo ci
    ) {
        Minecraft client = Minecraft.getInstance();

        if (client.gameRenderer.getMainCamera() == null) {
            return;
        }

        var pos = state.blockPos;
        var cameraPos = client.gameRenderer.getMainCamera().blockPosition();

        double dx = pos.getX() + 0.5 - cameraPos.getX();
        double dy = pos.getY() + 0.5 - cameraPos.getY();
        double dz = pos.getZ() + 0.5 - cameraPos.getZ();

        double maxDistance = PrismiumClient.signTextDistance;

        if (dx * dx + dy * dy + dz * dz > maxDistance * maxDistance) {
            ci.cancel();
        }
    }
}
