package net.paradise.naruto.procedures;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class KubikiribochoLeftClickProcedure {
    private static final int RAYTRACE_RANGE = 20;
    private static final int DAMAGE = 20;

    public static void executeProcedure(Map<String, Object> dependencies) {
        if (!dependencies.containsKey("entity")) {
            System.err.println("Failed to load dependency entity for procedure KubikiribochoLeftClick!");
            return;
        }
        Entity entity = (Entity) dependencies.get("entity");
        if (entity instanceof LivingEntity) {
            World world = entity.world;
            Vector3d startVec = entity.getEyePosition(1.0F);
            Vector3d lookVec = entity.getLookVec();
            Vector3d endVec = startVec.add(lookVec.x * RAYTRACE_RANGE, lookVec.y * RAYTRACE_RANGE, lookVec.z * RAYTRACE_RANGE);
            AxisAlignedBB aabb = new AxisAlignedBB(startVec.x, startVec.y, startVec.z, endVec.x, endVec.y, endVec.z);

            List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(entity, aabb);
            double closestDistance = RAYTRACE_RANGE;
            Entity closestEntity = null;

            for (Entity target : entities) {
                if (target instanceof LivingEntity && !target.isSpectator()) {
                    AxisAlignedBB targetAABB = target.getBoundingBox().grow(target.getCollisionBorderSize());
                    if (targetAABB.intersects(aabb)) {
                        double distance = startVec.squareDistanceTo(target.getPositionVec());
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestEntity = target;
                        }
                    }
                }
            }

            if (closestEntity instanceof LivingEntity) {
                ((LivingEntity) closestEntity).attackEntityFrom(DamageSource.GENERIC, DAMAGE);
            }
        }
    }
}