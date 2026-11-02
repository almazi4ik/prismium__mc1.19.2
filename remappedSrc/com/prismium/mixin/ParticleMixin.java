package com.prismium.mixin;

import com.prismium.PrismiumClient;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.ParticlesRenderState;
import com.prismium.mixin.ParticleAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ParticleEngine.class)
public abstract class ParticleMixin {
    @Shadow @Final
    private Map<ParticleRenderType, ParticleGroup<?>> particles;

    @Inject(method = "extract", at = @At("HEAD"))
    private void prismium$particleDistance(
            ParticlesRenderState batch,
            Frustum frustum,
            Camera camera,
            float tickProgress,
            CallbackInfo ci
    ) {
        if (!PrismiumClient.particleCulling) return;

        var pos = camera.position();
        double max = Math.max(0, PrismiumClient.particleCullingDistance);
        double maxSq = max * max;

        for (ParticleGroup<?> renderer : particles.values()) {
            renderer.getAll().removeIf(obj -> {
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
