package com.jobeslegacy.engine.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.componentTypeOf
import com.jobeslegacy.engine.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Generalized Animate Component Property data class. It is used for animating properties of other components
 * via the [AnimationScript] components and one of the systems in AnimateSystems.kt file.
 *
 * value:  This is set to the previous or initial value
 * change: Value with which last value needs to be changed to reach the target value of the animation step
 *
 * In case of single switch: This value is set when easing > 0.5
 */
@Serializable @SerialName("AnimateComponent")
data class AnimateComponent (
    var animateProperty: AnimateProperty,

    @Serializable(with = AnySerializer::class)
    var change: Any = Unit,
    @Serializable(with = AnySerializer::class)
    var value: Any = Unit,

    var duration: Float = 0f,                    // in seconds
    var timeProgress: Float = 0f,                // in seconds
    @Serializable(with = EasingSerializer::class)
    var easing: Easing = Easing.LINEAR           // Changing function
) : Component<AnimateComponent>, SerializeBase {
    override fun type() = animateProperty.type

    companion object {
        // TODO upate unit test for this mapping from enum to here

    // Author's note:
    // 1. Make sure new AnimationComponent names are added both here and in the AnimateProperties enum class
    // 2. Make sure the name is the same, otherwise animate systems will behave confusingly
/*
        val AnimateSpriteIsPlaying = componentTypeOf<AnimateComponent>()
        val AnimateSpriteForwardDirection = componentTypeOf<AnimateComponent>()
        val AnimateSpriteLoop = componentTypeOf<AnimateComponent>()
        val AnimateSpriteDestroyOnPlayingFinished = componentTypeOf<AnimateComponent>()
        val AnimateSpriteAnimName = componentTypeOf<AnimateComponent>()

        val AnimateAppearanceAlpha = componentTypeOf<AnimateComponent>()
        val AnimateAppearanceTint = componentTypeOf<AnimateComponent>()
        val AnimateAppearanceVisible = componentTypeOf<AnimateComponent>()

        val AnimateSpawnerNumberOfObjects = componentTypeOf<AnimateComponent>()
        val AnimateSpawnerInterval = componentTypeOf<AnimateComponent>()
        val AnimateSpawnerTimeVariation = componentTypeOf<AnimateComponent>()
        val AnimateSpawnerPositionVariation = componentTypeOf<AnimateComponent>()

        val AnimateLifeCycleHealthCounter = componentTypeOf<AnimateComponent>()
*/
        val AnimatePositionShapeX = AnimateProperty.PositionShapeX.type
        val AnimatePositionShapeY = AnimateProperty.PositionShapeY.type
        
        val AnimateMoveComponentVelocityX = AnimateProperty.MoveComponentVelocityX.type
        val AnimateMoveComponentVelocityY = AnimateProperty.MoveComponentVelocityY.type
/*
        val AnimateOffsetX = componentTypeOf<AnimateComponent>()
        val AnimateOffsetY = componentTypeOf<AnimateComponent>()

        val AnimateLayoutCenterX = componentTypeOf<AnimateComponent>()
        val AnimateLayoutCenterY = componentTypeOf<AnimateComponent>()
        val AnimateLayoutOffsetX = componentTypeOf<AnimateComponent>()
        val AnimateLayoutOffsetY = componentTypeOf<AnimateComponent>()

        val AnimateSwitchLayerVisibilityOnVariance = componentTypeOf<AnimateComponent>()
        val AnimateSwitchLayerVisibilityOffVariance = componentTypeOf<AnimateComponent>()

        val AnimateNoisyMoveTriggerChangeVariance = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveTriggerBackVariance = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveInterval = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveIntervalVariance = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveXTarget = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveYTarget = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveXVariance = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveYVariance = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveX = componentTypeOf<AnimateComponent>()
        val AnimateNoisyMoveY = componentTypeOf<AnimateComponent>()

        val AnimateSoundStartTrigger = componentTypeOf<AnimateComponent>()
        val AnimateSoundStopTrigger = componentTypeOf<AnimateComponent>()
        val AnimateSoundPosition = componentTypeOf<AnimateComponent>()
        val AnimateSoundVolume = componentTypeOf<AnimateComponent>()

        val ExecuteConfigureFunction = componentTypeOf<AnimateComponent>()
*/
    }
}

//enum class AnimateComp(val type: )

/**
 * All final AnimateComponent names are organized in this enum. This is done to easily serialize the
 * animateProperty of the base AnimateComponent data class.
 */
enum class AnimateProperty(val type: ComponentType<AnimateComponent>) {
    PositionShapeX(componentTypeOf<AnimateComponent>()),
    PositionShapeY(componentTypeOf<AnimateComponent>()),
    MoveComponentVelocityX(componentTypeOf<AnimateComponent>()),
    MoveComponentVelocityY(componentTypeOf<AnimateComponent>()),

/*    SpriteIsPlaying(AnimateComponent.AnimateSpriteIsPlaying),
    SpriteForwardDirection(AnimateComponent.AnimateSpriteForwardDirection),
    SpriteLoop(AnimateComponent.AnimateSpriteLoop),
    SpriteDestroyOnPlayingFinished(AnimateComponent.AnimateSpriteDestroyOnPlayingFinished),
    SpriteAnimName(AnimateComponent.AnimateSpriteAnimName),

    AppearanceAlpha(AnimateComponent.AnimateAppearanceAlpha),
    AppearanceTint(AnimateComponent.AnimateAppearanceTint),
    AppearanceVisible(AnimateComponent.AnimateAppearanceVisible),

    SpawnerNumberOfObjects(AnimateComponent.AnimateSpawnerNumberOfObjects),
    SpawnerInterval(AnimateComponent.AnimateSpawnerInterval),
    SpawnerTimeVariation(AnimateComponent.AnimateSpawnerTimeVariation),
    SpawnerPositionVariation(AnimateComponent.AnimateSpawnerPositionVariation),

    // TODO not used yet in animation system
    LifeCycleHealthCounter(AnimateComponent.AnimateLifeCycleHealthCounter),

    PositionShapeX(AnimateComponent.AnimatePositionShapeX),
    PositionShapeY(componentTypeOf<AnimateComponent>()),

    MoveComponentVelocityX(AnimateComponent.AnimateMoveComponentVelocityX),
    MoveComponentVelocityY(AnimateComponent.AnimateMoveComponentVelocityY),

    OffsetX(AnimateComponent.AnimateOffsetX),
    OffsetY(AnimateComponent.AnimateOffsetY),

    LayoutCenterX(AnimateComponent.AnimateLayoutCenterX),
    LayoutCenterY(AnimateComponent.AnimateLayoutCenterY),
    LayoutOffsetX(AnimateComponent.AnimateLayoutOffsetX),
    LayoutOffsetY(AnimateComponent.AnimateLayoutOffsetY),

    SwitchLayerVisibilityOnVariance(AnimateComponent.AnimateSwitchLayerVisibilityOnVariance),
    SwitchLayerVisibilityOffVariance(AnimateComponent.AnimateSwitchLayerVisibilityOffVariance),

    NoisyMoveTriggerChangeVariance(AnimateComponent.AnimateNoisyMoveTriggerChangeVariance),
    NoisyMoveTriggerBackVariance(AnimateComponent.AnimateNoisyMoveTriggerBackVariance),
    NoisyMoveInterval(AnimateComponent.AnimateNoisyMoveInterval),
    NoisyMoveIntervalVariance(AnimateComponent.AnimateNoisyMoveIntervalVariance),
    NoisyMoveOffsetXTarget(AnimateComponent.AnimateNoisyMoveXTarget),
    NoisyMoveOffsetYTarget(AnimateComponent.AnimateNoisyMoveYTarget),
    NoisyMoveOffsetXVariance(AnimateComponent.AnimateNoisyMoveXVariance),
    NoisyMoveOffsetYVariance(AnimateComponent.AnimateNoisyMoveYVariance),
    NoisyMoveX(AnimateComponent.AnimateNoisyMoveX),
    NoisyMoveY(AnimateComponent.AnimateNoisyMoveY),

    SoundStartTrigger(AnimateComponent.AnimateSoundStartTrigger),
    SoundStopTrigger(AnimateComponent.AnimateSoundStopTrigger),
    SoundPosition(AnimateComponent.AnimateSoundPosition),
    SoundVolume(AnimateComponent.AnimateSoundVolume),

    ConfigureFunction(AnimateComponent.ExecuteConfigureFunction)
*/
}
