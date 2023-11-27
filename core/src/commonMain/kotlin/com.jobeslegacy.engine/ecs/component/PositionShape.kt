package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.jobeslegacy.engine.util.SerializeBase
import com.jobeslegacy.engine.ecs.entity.config.invalidEntity
import com.jobeslegacy.engine.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * This component is used to add generic object properties like position and size to an entity.
 * The data from this component will be processed e.g. by the KorgeViewSystem in the Fleks ECS.
 */
@Serializable @SerialName("PositionAndSize")
data class PositionAndSizeComponent(
    var initialized: Boolean = false,
    var x: Float = 0.0f,
    var y: Float = 0.0f,
    var width: Float = 0.0f,
    var height: Float = 0.0f,
) : Component<PositionAndSizeComponent>, SerializeBase {
    override fun type() = PositionAndSizeComponent
    companion object : ComponentType<PositionAndSizeComponent>()
}

@Serializable @SerialName("Offset")
data class OffsetComponent(
    var x: Float = 0.0f,
    var y: Float = 0.0f
) : Component<OffsetComponent>, SerializeBase {
    override fun type() = OffsetComponent
    companion object : ComponentType<OffsetComponent>()
}

@Serializable @SerialName("OffsetByFrameIndex")
data class OffsetByFrameIndexComponent(
    var entity: Entity = invalidEntity,
    var list: Map<String, List<Point>> = emptyMap()
) : Component<OffsetByFrameIndexComponent>, SerializeBase {
    override fun type() = OffsetByFrameIndexComponent
    companion object : ComponentType<OffsetByFrameIndexComponent>()
}

/**
 * Add this component to an entity together with PositionShape component to randomly change the position withing
 * the specified [xVariance] and [yVariance].
 */
@Serializable @SerialName("AutomaticMoving")
data class NoisyMoveComponent(
    // trigger variance for start moving: (1.0) - trigger immediately when possible, (0.0) - no trigger for start moving at all
    var triggerVariance: Float = 0f,
    // terminate variance for stop moving: (1.0) - always terminate previous trigger, (0.0) - triggered moving stays forever
    var terminateVariance: Float = 0f,
    var interval: Float = 0f,          // in seconds
    var intervalVariance: Float = 0f,  // in seconds
    var xTarget: Float = 0f,
    var yTarget: Float = 0f,
    var xVariance: Float = 0f,
    var yVariance: Float = 0f,

    /** Final absolute move values which are applied to the [PositionShape] component's x,y properties of the entity in [KorgeViewSystem] */
    var triggered: Boolean = false,
    var x: Float = 0f,
    var y: Float = 0f,

    // Internal runtime data
    var timeProgress: Float = 0f,
    var waitTime: Float = 0f
) : Component<NoisyMoveComponent>, SerializeBase {
    override fun type() = NoisyMoveComponent

    override fun World.onAdd(entity: Entity) {

        timeProgress = 0f
        waitTime = interval + if (intervalVariance != 0f) (-intervalVariance..intervalVariance).random() else 0f

        val startX = x
        val startY = y
        val endX = xTarget + if (xVariance != 0f) (-xVariance..xVariance).random() else 0f
        val endY = yTarget + if (yVariance != 0f) (-yVariance..yVariance).random() else 0f
// TODO
//        updateAnimateComponent(this, entity, AnimateComponentType.NoisyMoveX, value = startX, change = endX - startX, waitTime, Easing.EASE_IN_OUT)
//        updateAnimateComponent(this, entity, AnimateComponentType.NoisyMoveY, value = startY, change = endY - startY, waitTime, Easing.EASE_IN_OUT)

    }

    fun updateAnimateComponent(world: World, entity: Entity, componentProperty: TweenProperty, value: Any, change: Any = Unit, duration: Float? = null, easing: Easing? = null) = with (world) {
        entity.configure { animatedEntity ->
/* TODO
            animatedEntity.getOrAdd(componentProperty.type) { AnimateComponent(componentProperty) }.also {
                it.change = change
                it.value = value
                it.duration = duration ?: 0f
                it.timeProgress = 0f
                it.easing = easing ?: Easing.LINEAR
            }
*/
        }
    }

    companion object : ComponentType<NoisyMoveComponent>()
}

@Serializable @SerialName("PositionShape.Point")
data class Point(
    var x: Float = 0.0f,
    var y: Float = 0.0f
) : SerializeBase

@Serializable @SerialName("Motion")
data class Motion(
    var accelX: Float = 0.0f,
    var accelY: Float = 0.0f,
    var velocityX: Float = 0.0f,
    var velocityY: Float = 0.0f
) : Component<Motion>, SerializeBase {
    override fun type() = Motion
    companion object : ComponentType<Motion>()
}

@Serializable @SerialName("ParallaxMotion")
data class ParallaxMotion(
    var isScrollingHorizontally: Boolean = true,
    var speedFactor: Float = 1.0f,  // TODO put this into assets because it is static and does not change  ????
    var selfSpeedX: Float = 0.0f,
    var selfSpeedY: Float = 0.0f
) : Component<ParallaxMotion>, SerializeBase {
    override fun type() = ParallaxMotion
    companion object : ComponentType<ParallaxMotion>()
}
