package com.prismium.mixin;

import com.prismium.PrismiumClient;
import net.minecraft.client.particle.Particle;
import com.prismium.mixin.ParticleAccessor;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleRenderer;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.SubmittableBatch;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ParticleManager.class)
public abstract class ParticleMixin {
    @Shadow @Final
    private Map<ParticleTextureSheet, ParticleRenderer<?>> particles;

    @Inject(method = "addToBatch", at = @At("HEAD"))
    private void prismium$particleDistance(
            SubmittableBatch batch,
            Frustum frustum,
            Camera camera,
            float tickProgress,
            CallbackInfo ci
    ) {
        if (!PrismiumClient.particleCulling) return;

        var pos = camera.getCameraPos();
        double max = Math.max(0, PrismiumClient.particleCullingDistance);
        double maxSq = max * max;

        for (ParticleRenderer<?> renderer : particles.values()) {
            renderer.getParticles().removeIf(obj -> {
                if (!(obj instanceof Particle particle)) return false;
                ParticleAccessor accessor = (ParticleAccessor) particle;
                double dx = accessor.prismium$getX() - pos.x;
                double dy = accessor.prismium$getY() - pos.y;
                double dz = accessor.prismium$getZ() - pos.z;
                return dx * dx + dy * dy + dz * dz > maxSq;
            });
        }
    }
}
