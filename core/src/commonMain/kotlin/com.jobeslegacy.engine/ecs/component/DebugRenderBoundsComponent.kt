package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.lehaine.littlekt.graphics.Color
import com.lehaine.littlekt.math.MutableVec2f
import com.lehaine.littlekt.math.Rect

/**
 * @author Colton Daily
 * @date 3/15/2023
 */
data class DebugRenderBoundsComponent(
    val objectBounds: Rect = Rect(),
    val objectOrigin: MutableVec2f = MutableVec2f(),
    var textureBoundsColor: Color = Color.YELLOW,
    var objectBoundsColor: Color = Color.DARK_RED,
    var objectOriginColor: Color = Color.GREEN
) : Component<DebugRenderBoundsComponent> {
    override fun type() = DebugRenderBoundsComponent

    companion object : ComponentType<DebugRenderBoundsComponent>()
}