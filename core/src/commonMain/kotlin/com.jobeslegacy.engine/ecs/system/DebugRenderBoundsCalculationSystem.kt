package com.jobeslegacy.engine.ecs.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Interval
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.jobeslegacy.engine.ecs.component.*

/**
 * This system is responsible to provide a bounds rectangle of the texture used in a sprite.
 * It is used e.g. to check if the sprite is visible on the screen in .
 *
 * @author Colton Daily
 * @date 3/9/2023
 */
class DebugRenderBoundsCalculationSystem(interval: Interval) : IteratingSystem(
    family { all(GridComponent, RenderBoundsComponent, DebugRenderBoundsComponent).none(SpriteComponent) },
    interval = interval
) {
    override fun onTickEntity(entity: Entity) {
        val gridComponent = entity[GridComponent]
        val renderBoundsComponent = entity[RenderBoundsComponent]
        val debugRenderBoundsComponent = entity[DebugRenderBoundsComponent]

        var xx = gridComponent.x
        var yy = gridComponent.y

        entity.getOrNull(OffsetComponent)?.apply {
            xx += x
            yy += y
        }

        // Object has no size - it is just
        renderBoundsComponent.textureBounds.set(xx, yy, 0f, 0f)
        debugRenderBoundsComponent.objectBounds.set(xx, yy, 0f, 0f)

        // Save object origin position (also called pivot point)
        debugRenderBoundsComponent.objectOrigin.x = xx
        debugRenderBoundsComponent.objectOrigin.y = yy
    }
}
