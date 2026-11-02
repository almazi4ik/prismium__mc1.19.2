package com.prismium.mixin;

import com.prismium.PrismiumClient;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(
            method = "shouldRender",
            at = @At("HEAD"),
            cancellable = true
    )
    private <E extends Entity> void prismium$entityCulling(
            E entity,
            Frustum frustum,
            double x,
            double y,
            double z,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (PrismiumClient.entityCulling) {
            if (!frustum.isVisible(entity.getBoundingBox().inflate(0.5))) {
                cir.setReturnValue(false);
                return;
            }
        }

        int distance;

        if (entity instanceof ItemEntity) {
            distance = PrismiumClient.droppedItemDistance;
        } else {
            distance = PrismiumClient.distantCullingDistance;
        }

        double dx = entity.getX() - x;
        double dy = entity.getY() - y;
        double dz = entity.getZ() - z;

        double distanceSq = dx * dx + dy * dy + dz * dz;

        if (distanceSq > (double) distance * distance) {
            cir.setReturnValue(false);
        }
    }
}
