package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.jobeslegacy.engine.ecs.entity.config.FireAndDustEffect
import com.jobeslegacy.engine.ecs.entity.config.nothing
import com.jobeslegacy.engine.util.Identifier
import com.jobeslegacy.engine.util.cache.GameObjectConfigCache
import com.lehaine.littlekt.graphics.g2d.Animation
import com.lehaine.littlekt.graphics.g2d.TextureSlice
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Enables playback of [Animation] classes.
 * @see [Animation]
 * // @param KeyFrameType the type of key frame the [Animation] class is using.
 * @author Marko Koschak
 * @date 1-Dec-2023
 */
@Serializable @SerialName("AnimationComponent")
data class AnimationComponent(
    /** Identifier for getting the [Animation] from GameObjectConfigCache */
    var gameObjectConfigName: Identifier = nothing,
    /** Destroy entity  */
    var destroyOnAnimationFinished: Boolean = false,

    /** The total amount of frames played across all animations */
    var totalFramesPlayed: Int = 0,
    /** The index of the current frame */
    var currentFrameIdx: Int = 0,  // Do not set directly use set function below!

    // private in animation player system
    var animationRequested: Boolean = false,
    var numOfFramesRequested: Int = 0,  //  Do not set directly use set function below!
    var frameDisplayTime: Duration = 100.milliseconds,
    var animationType: AnimationType = AnimationType.STANDARD,
    var lastFrameTime: Duration = Duration.ZERO,
    var remainingDuration: Duration = Duration.ZERO


) : Component<AnimationComponent> {
    // TODO only for debugging - remove later
    var time: Float = 0f

    /**
     * The current playing animations. Set this by using one of the `play` methods.
     * @see [play]
     * @see [playOnce]
     * @see [playLooped]
     */
    val currentAnimation: Animation<TextureSlice>? get() =
        GameObjectConfigCache.getOrNull(gameObjectConfigName)?.animation

    /**
     * The total frames the [currentAnimation] has.
     */
    val totalFrames: Int get() = currentAnimation?.totalFrames ?: 1
    /**
     * Play the specified animation an X number of times.
     * @param animation the animation to play
     * @param times the number of times to play
     * @param queue if `false` will play the animation immediately
     */
    fun play(gameObjectConfigName: Identifier, times: Int = 1, queue: Boolean = false) {
//        if (!queue && stack.isNotEmpty()) {  TODO integrate stack later
//            animInstancePool.free(stack)
//            stack.clear()
//        }
//        stack += animInstancePool.alloc().apply {
//            anim = animation
//            plays = times
//            type = AnimationType.STANDARD
//        }
//        if (!queue) {
//            currentAnimationInstance?.let { animInstance ->
//                animInstance.anim?.let {
//                    setAnimInfo(it, animInstance.plays, animInstance.speed, animInstance.duration, animInstance.type)
//                }
//            }
//        }
        this.gameObjectConfigName = gameObjectConfigName
        this.numOfFramesRequested = times * totalFrames
        animationType = AnimationType.STANDARD
    }

    /**
     * Play the specified animation one time.
     * @param animation the animation to play
     */
    fun playOnce(gameObjectConfigName: Identifier) = play(gameObjectConfigName)

//    private fun playStateAnimOnce(animation: Animation<KeyFrameType>) {
//        play(animation)
//        stack.lastOrNull()?.let {  TODO integrate stack later
//            it.isStateAnim = true
//        }
//    }

    /**
     * Play the specified animation as a loop.
     * @param animation the animation to play
     */
    fun playLooped(gameObjectConfigName: Identifier) {
//        if (stack.isNotEmpty()) {  TODO integrate stack later
//            animInstancePool.free(stack)
//            stack.clear()
//        }
//        stack += animInstancePool.alloc().apply {
//            anim = animation
//            type = AnimationType.LOOPED
//        }
//        currentAnimationInstance?.let { animInstance ->
//            animInstance.anim?.let {
//                setAnimInfo(it, animInstance.plays, animInstance.speed, animInstance.duration, animInstance.type)
//            }
//        }
        this.gameObjectConfigName = gameObjectConfigName
        animationType = AnimationType.LOOPED
    }

    /**
     * Play the specified animation for a duration.
     * @param animation the animation to play
     * @param duration the duration to play the animation
     * @param queue if `false` will play the animation immediately
     */
    fun play(gameObjectConfigName: Identifier, duration: Duration, queue: Boolean = false) {
//        setAnimInfo(animation)
//        if (!queue && stack.isNotEmpty()) {  TODO integrate stack later
//            animInstancePool.free(stack)
//            stack.clear()
//        }
//        stack += animInstancePool.alloc().apply {
//            anim = animation
//            this.duration = duration
//            type = AnimationType.DURATION
//        }
//        if (!queue) {
//            currentAnimationInstance?.let { animInstance ->
//                animInstance.anim?.let {
//                    setAnimInfo(it, animInstance.plays, animInstance.speed, animInstance.duration, animInstance.type)
//                }
//            }
//        }

        this.gameObjectConfigName = gameObjectConfigName
        remainingDuration = duration
        animationType = AnimationType.DURATION
    }

    /**
     * Starts any currently stopped animation from the beginning. This only does something when an animation is stopped with [stop].
     */
    fun start() {
//        currentAnimation?.let {
        animationRequested = true
        currentFrameIdx = 0
        lastFrameTime = Duration.ZERO
//        }
    }

    /**
     * Restarts the current running animation from the beginning. Is the same as invoking [stop] and then [start].
     */
    fun restart()  = start()

    /**
     * Resumes any currently stopped animation. This only does something when an animation is stopped with [stop].
     */
    fun resume() {
//        currentAnimation?.let {
        animationRequested = true
//        }
    }

    /**
     * Stops any running animations. Resume the current animation with [resume].
     */
    fun stop() {
        animationRequested = false
    }

    override fun type() = AnimationComponent
    companion object : ComponentType<AnimationComponent>()
}

/**
 * The playback types of animation.
 * @author Colton Daily
 * @date 11/27/2021
 */
enum class AnimationType {
    /**
     * An animation that runs from start to end an **X** amount of times.
     */
    STANDARD,

    /**
     * An animation the loops from start to end.
     */
    LOOPED,

    /**
     * An animation type the runs for a certain duration.
     */
    DURATION
}

internal infix fun Int.umod(that: Int): Int {
    val remainder = this % that
    return when {
        remainder < 0 -> remainder + that
        else -> remainder
    }
}