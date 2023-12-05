package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.lehaine.littlekt.math.MutableVec2f
import com.lehaine.littlekt.math.Rect

/**
 * @author Colton Daily
 * @date 3/9/2023
 */
data class RenderBoundsComponent(
    val textureBounds: Rect = Rect()
) : Component<RenderBoundsComponent> {
    override fun type() = RenderBoundsComponent
    companion object : ComponentType<RenderBoundsComponent>()
}
