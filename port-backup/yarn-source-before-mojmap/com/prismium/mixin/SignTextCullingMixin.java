package com.prismium.mixin;

import com.prismium.PrismiumClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.AbstractSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.SignBlockEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignBlockEntityRenderer.class)
public class SignTextCullingMixin {

    @Inject(method = "renderText", at = @At("HEAD"), cancellable = true)
    private void prismium$signTextDistance(
            SignBlockEntityRenderState state,
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            boolean front,
            CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.gameRenderer.getCamera() == null) {
            return;
        }

        var pos = state.pos;
        var cameraPos = client.gameRenderer.getCamera().getBlockPos();

        double dx = pos.getX() + 0.5 - cameraPos.getX();
        double dy = pos.getY() + 0.5 - cameraPos.getY();
        double dz = pos.getZ() + 0.5 - cameraPos.getZ();

        double maxDistance = PrismiumClient.signTextDistance;

        if (dx * dx + dy * dy + dz * dz > maxDistance * maxDistance) {
            ci.cancel();
        }
    }
}
