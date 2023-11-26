package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.jobeslegacy.engine.asset.Assets
import com.lehaine.littlekt.extras.renderable.Sprite
import com.lehaine.littlekt.graphics.Color
import com.lehaine.littlekt.graphics.MutableColor
import com.lehaine.littlekt.graphics.g2d.Batch
import com.lehaine.littlekt.graphics.g2d.TextureSlice

enum class AssetType{
    COMMON, WORLD, LEVEL
}

/**
 * The [Sprite] component adds visible details to an [Drawable] entity. By adding [Sprite] to an entity the entity will be
 * able to handle animations.
 */
//TODO @Serializable @SerialName("Sprite")
data class SpriteComponent(
    var assetType: AssetType = AssetType.COMMON,
    var imageName: String = "fxPixel",  // this is always available from common assets
    /**
     * Flips the current rendering of the [Sprite] horizontally.
     */
    var flipX: Boolean = false,
    /**
     * Flips the current rendering of the [Sprite] vertically.
     */
    var flipY: Boolean = false,
    /**
     * Color that is passed along to the [Batch].
     */
    var color: MutableColor = Color.WHITE.toMutableColor(),
    /** Layer order, higher numbers means sprite will be rendered on top other lesser numbers.
     */
    var layerIndex: Int = 0,
    var alpha: Float = 1.0f,
) : Component<SpriteComponent> {

    // TODO move this into Sprite system
    var slice: TextureSlice? = null

    val renderWidth: Float
        get() = if (overrideWidth) overriddenWidth else slice?.width?.toFloat() ?: 0f

    val renderHeight: Float
        get() = if (overrideHeight) overriddenHeight else slice?.height?.toFloat() ?: 0f

    var overrideWidth = false
    var overrideHeight = false

    var overriddenWidth = 0f
    var overriddenHeight = 0f


    override fun type() = SpriteComponent
    companion object : ComponentType<SpriteComponent>()

    override fun World.onAdd(entity: Entity) {
        slice = Assets.atlas(assetType).getByPrefix(imageName).slice
    }

    override fun World.onRemove(entity: Entity) {
        slice = null
    }
}