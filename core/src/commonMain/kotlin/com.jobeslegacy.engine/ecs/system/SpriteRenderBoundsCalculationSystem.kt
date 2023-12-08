package com.jobeslegacy.engine.ecs.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Interval
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.jobeslegacy.engine.ecs.component.DebugRenderBoundsComponent
import com.jobeslegacy.engine.ecs.component.GridComponent
import com.jobeslegacy.engine.ecs.component.RenderBoundsComponent
import com.jobeslegacy.engine.ecs.component.SpriteComponent
import com.lehaine.littlekt.math.Mat3
import com.lehaine.littlekt.math.MutableVec2f
import com.lehaine.littlekt.math.Rect
import com.lehaine.littlekt.math.geom.Angle
import com.lehaine.littlekt.math.geom.normalized

/**
 * This system is responsible to provide a bounds rectangle of the texture used in a sprite.
 * It is used e.g. to check if the sprite is visible on the screen in .
 *
 * @author Colton Daily
 * @date 3/9/2023
 */
class SpriteRenderBoundsCalculationSystem(interval: Interval) : IteratingSystem(
    family { all(GridComponent, SpriteComponent, RenderBoundsComponent) },
    interval = interval
) {
    private val _bounds = Rect()

    override fun onTickEntity(entity: Entity) {
        val grid = entity[GridComponent]
        val sprite = entity[SpriteComponent]
        val renderBounds = entity[RenderBoundsComponent]
        val slice = sprite.slice

        val origWidth =
            (if (slice?.rotated == true) slice.originalHeight.toFloat() else slice?.originalWidth?.toFloat())
                ?: sprite.renderWidth
        val origHeight =
            (if (slice?.rotated == true) slice.originalWidth.toFloat() else slice?.originalHeight?.toFloat())
                ?: sprite.renderHeight

        val x = grid.x
        val y = grid.y

        // get bounds of used sprite texture
        calculateBounds(x, y,
            offsetX = slice?.offsetX ?: 0,
            offsetY = slice?.offsetY ?: 0,
            anchorX = origWidth * grid.anchorX,
            anchorY = origHeight * grid.anchorY,
            grid.scaleX,
            grid.scaleY,
            width = if (slice?.rotated == true) sprite.renderHeight else sprite.renderWidth,
            height = if (slice?.rotated == true) sprite.renderWidth else sprite.renderHeight
        )
        renderBounds.textureBounds.set(_bounds.x, _bounds.y, _bounds.width, _bounds.height)

        entity.getOrNull(DebugRenderBoundsComponent)?.apply {
            // Get rectangle for original sprite size
            calculateBounds(x, y,
                offsetX = 0,
                offsetY = 0,
                anchorX = origWidth * grid.anchorX,
                anchorY = origHeight * grid.anchorY,
                grid.scaleX,
                grid.scaleY,
                width = if (slice?.rotated == true) origHeight else origWidth,
                height = if (slice?.rotated == true) origWidth else origHeight
            )
            objectBounds.set(_bounds.x, _bounds.y, _bounds.width, _bounds.height)

            // Save object origin position (also called pivot point)
            objectOrigin.x = x
            objectOrigin.y = y
        }
    }

    private fun calculateBounds(
        x: Float,
        y: Float,
        offsetX: Int,
        offsetY: Int,
        anchorX: Float,
        anchorY: Float,
        scaleX: Float,
        scaleY: Float,
        width: Float,
        height: Float
    ) {
        _bounds.let {
            it.x = x + offsetX * scaleX - anchorX * scaleX
            it.y = y + offsetY * scaleY - anchorY * scaleY
            it.width = width * scaleX
            it.height = height * scaleY
        }
    }
}