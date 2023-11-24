package com.jobeslegacy.engine.ecs.component

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
        val AnimateSpriteIsPlaying = AnimateProperty.
        val AnimateSpriteForwardDirection = AnimateProperty.
        val AnimateSpriteLoop = AnimateProperty.
        val AnimateSpriteDestroyOnPlayingFinished = AnimateProperty.
        val AnimateSpriteAnimName = AnimateProperty.

        val AnimateAppearanceAlpha = AnimateProperty.
        val AnimateAppearanceTint = AnimateProperty.
        val AnimateAppearanceVisible = AnimateProperty.
*/
        val AnimateSpawnerComponentNumberOfObjects = AnimateProperty.SpawnerComponentNumberOfObjects.type
        val AnimateSpawnerComponentInterval = AnimateProperty.SpawnerComponentInterval.type
        val AnimateSpawnerComponentTimeVariation = AnimateProperty.SpawnerComponentTimeVariation.type
        val AnimateSpawnerComponentPositionVariation = AnimateProperty.SpawnerComponentPositionVariation.type
/*
        val AnimateLifeCycleHealthCounter = AnimateProperty.
*/
        val AnimatePositionAndSizeComponentX = AnimateProperty.PositionAndSizeComponentX.type
        val AnimatePositionAndSizeComponentY = AnimateProperty.PositionAndSizeComponentY.type
        
        val AnimateMoveComponentVelocityX = AnimateProperty.MoveComponentVelocityX.type
        val AnimateMoveComponentVelocityY = AnimateProperty.MoveComponentVelocityY.type
/*
        val AnimateOffsetX = AnimateProperty.
        val AnimateOffsetY = AnimateProperty.

        val AnimateLayoutCenterX = AnimateProperty.
        val AnimateLayoutCenterY = AnimateProperty.
        val AnimateLayoutOffsetX = AnimateProperty.
        val AnimateLayoutOffsetY = AnimateProperty.

        val AnimateSwitchLayerVisibilityOnVariance = AnimateProperty.
        val AnimateSwitchLayerVisibilityOffVariance = AnimateProperty.

        val AnimateNoisyMoveTriggerChangeVariance = AnimateProperty.
        val AnimateNoisyMoveTriggerBackVariance = AnimateProperty.
        val AnimateNoisyMoveInterval = AnimateProperty.
        val AnimateNoisyMoveIntervalVariance = AnimateProperty.
        val AnimateNoisyMoveXTarget = AnimateProperty.
        val AnimateNoisyMoveYTarget = AnimateProperty.
        val AnimateNoisyMoveXVariance = AnimateProperty.
        val AnimateNoisyMoveYVariance = AnimateProperty.
        val AnimateNoisyMoveX = AnimateProperty.
        val AnimateNoisyMoveY = AnimateProperty.

        val AnimateSoundStartTrigger = AnimateProperty.
        val AnimateSoundStopTrigger = AnimateProperty.
        val AnimateSoundPosition = AnimateProperty.
        val AnimateSoundVolume = AnimateProperty.

        val ExecuteConfigureFunction = AnimateProperty.
*/
    }
}

//enum class AnimateComp(val type: )

/**
 * All final AnimateComponent names are organized in this enum. This is done to easily serialize the
 * animateProperty of the base AnimateComponent data class.
 */
enum class AnimateProperty(val type: ComponentType<AnimateComponent>) {
    PositionAndSizeComponentX(componentTypeOf<AnimateComponent>()),
    PositionAndSizeComponentY(componentTypeOf<AnimateComponent>()),
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
*/
    SpawnerComponentNumberOfObjects(componentTypeOf<AnimateComponent>()),
    SpawnerComponentInterval(componentTypeOf<AnimateComponent>()),
    SpawnerComponentTimeVariation(componentTypeOf<AnimateComponent>()),
    SpawnerComponentPositionVariation(componentTypeOf<AnimateComponent>()),
/*
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
