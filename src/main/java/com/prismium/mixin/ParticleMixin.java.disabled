package com.prismium.mixin;

import com.prismium.PrismiumClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleManager.class)
public class ParticleMixin {
    @Redirect(
        method = "renderParticles(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/particle/ParticleTextureSheet;Ljava/util/Queue;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/particle/Particle;render(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/Camera;F)V"
        )
    )
    private static void prismium$particleDistance(
        Particle particle,
        VertexConsumer vertexConsumer,
        Camera camera,
        float tickDelta
    ) {
        if (!PrismiumClient.particleCulling) {
            particle.render(vertexConsumer, camera, tickDelta);
            return;
        }

        ParticleAccessor accessor = (ParticleAccessor) (Object) particle;
        double dx = accessor.prismium$getX() - camera.getPos().x;
        double dy = accessor.prismium$getY() - camera.getPos().y;
        double dz = accessor.prismium$getZ() - camera.getPos().z;
        double maxDistance = PrismiumClient.particleCullingDistance;

        if (dx * dx + dy * dy + dz * dz <= maxDistance * maxDistance) {
            particle.render(vertexConsumer, camera, tickDelta);
        }
    }
}
