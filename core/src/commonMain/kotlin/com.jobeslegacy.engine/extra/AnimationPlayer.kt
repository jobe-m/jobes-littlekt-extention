package com.jobeslegacy.engine.extra

import com.jobeslegacy.engine.ecs.entity.config.nothing
import com.jobeslegacy.engine.util.Identifier
import com.lehaine.littlekt.graphics.g2d.Animation
import com.lehaine.littlekt.util.datastructure.Pool
import com.lehaine.littlekt.util.fastForEach
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Enables playback of [Animation] classes.
 * @see [Animation]
 * @param KeyFrameType the type of key frame the [Animation] class is using.
 * @author Colton Daily
 * @date 12/29/2021
 */
/*
open class AnimationPlayer<KeyFrameType> {

    var gameObjectConfigName: Identifier = nothing

    /**
     * The current playing animations. Set this by using one of the `play` methods.
     * @see [play]
     * @see [playOnce]
     * @see [playLooped]
     */
    var currentAnimation: Animation<KeyFrameType>? = null //get() = currentAnimationInstance?.anim

//    private val currentAnimationInstance: AnimationInstance<KeyFrameType>? get() = stack.firstOrNull()  TODO integrate stack later

    /**
     * The total frames the [currentAnimation] has.
     */
    val totalFrames: Int get() = currentAnimation?.totalFrames ?: 1

    /**
     * The total amount of frames played across all animations.
     */
    // TODO property for AnimationComponent
    var totalFramesPlayed = 0
        private set

    /**
     * The index of the current frame
     */
    // TODO property for AnimationComponent
    var currentFrameIdx = 0
        private set(value) {
            field = value umod totalFrames
            onFrameChange?.invoke(field)
        }

    val currentKeyFrame: KeyFrameType?
        get() = currentAnimation?.getFrame(currentFrameIdx)

    /**
     * Invoked when a frame is changed.
     */
    var onFrameChange: ((Int) -> Unit)? = null

    /**
     * Invoked when animation has finished playing.
     */
    var onAnimationFinish: (() -> Unit)? = null

    // TODO property for AnimationComponent
    private var animationRequested = false
    // TODO property for AnimationComponent
    private var numOfFramesRequested = 0
        set(value) {
            field = value
            if (value == 0) {
                stop()
//                stack.removeFirstOrNull()?.let {  TODO integrate stack later
//                    animInstancePool.free(it)
//                }
//                stack.firstOrNull()?.let { animInstance ->
//                    animInstance.anim?.let {
//                        setAnimInfo(it,
//                            animInstance.plays,
//                            animInstance.speed,
//                            animInstance.duration,
//                            animInstance.type)
//                    }
//                }
                onAnimationFinish?.invoke()
            }
        }
    // TODO property for AnimationComponent
    private var frameDisplayTime: Duration = 100.milliseconds
    // TODO property for AnimationComponent
    private var animationType = AnimationType.STANDARD
    // TODO property for AnimationComponent
    private var lastFrameTime: Duration = Duration.ZERO
    // TODO property for AnimationComponent
    private var remainingDuration: Duration = Duration.ZERO
        set(value) {
            field = value
            if (value <= Duration.ZERO) {
                stop()
//                stack.removeFirstOrNull()?.let {  TODO integrate stack later
//                    animInstancePool.free(it)
//                }
//                stack.firstOrNull()?.let { animInstance ->
//                    animInstance.anim?.let {
//                        setAnimInfo(it,
//                            animInstance.plays,
//                            animInstance.speed,
//                            animInstance.duration,
//                            animInstance.type)
//                    }
//                }
            }
        }

    private var lastAnimation: Animation<KeyFrameType>? = null
    private var lastAnimationType: AnimationType? = null

    var overlapPlaying: Boolean = false
        private set
    private val tempFrames = mutableListOf<KeyFrameType>()
    private val tempIndices = mutableListOf<Int>()
    private val tempTimes = mutableListOf<Duration>()
    private val tempAnim = Animation(tempFrames, tempIndices, tempTimes)

    // TODO integrate later when we need it
    // private val states = arrayListOf<AnimationState<KeyFrameType>>()
    //private val stack = arrayListOf<AnimationInstance<KeyFrameType>>()
    private val animInstancePool = Pool(reset = { it.reset() }, 20) {
        AnimationInstance<KeyFrameType>()
    }


    /**
     * Play a specified frame for a certain amount of frames.
     */
    fun play(frame: KeyFrameType, frameTime: Duration = 50.milliseconds, numFrames: Int = 1) {
        tempFrames.clear()
        tempIndices.clear()
        tempTimes.clear()
        tempFrames += frame
        repeat(numFrames) {
            tempIndices += 0
            tempTimes += frameTime
        }
        play(tempAnim)
    }

    /**
     * Play the specified animation an X number of times.
     * @param animation the animation to play
     * @param times the number of times to play
     * @param queue if `false` will play the animation immediately
     */
    fun play(animation: Animation<KeyFrameType>, times: Int = 1, queue: Boolean = false) {
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

        // TODO refactor this later
        currentAnimation = animation

        animationType = AnimationType.STANDARD
    }

    /**
     * Play the specified animation one time.
     * @param animation the animation to play
     */
    fun playOnce(animation: Animation<KeyFrameType>) = play(animation)

    private fun playStateAnimOnce(animation: Animation<KeyFrameType>) {
        play(animation)
//        stack.lastOrNull()?.let {  TODO integrate stack later
//            it.isStateAnim = true
//        }
    }

    /**
     * Play the specified animation as a loop.
     * @param animation the animation to play
     */
    fun playLooped(animation: Animation<KeyFrameType>) {
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

        // TODO refactor this later
        currentAnimation = animation
        animationType = AnimationType.LOOPED
    }

    private fun playStateAnimLooped(animation: Animation<KeyFrameType>) {
        playLooped(animation)
//        stack.lastOrNull()?.let {  TODO integrate stack later
//            it.isStateAnim = true
//        }
    }

    /**
     * Play the specified animation for a duration.
     * @param animation the animation to play
     * @param duration the duration to play the animation
     * @param queue if `false` will play the animation immediately
     */
    fun play(animation: Animation<KeyFrameType>, duration: Duration, queue: Boolean = false) {
        setAnimInfo(animation)
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

        // TODO refactor this later
        currentAnimation = animation

        animationType = AnimationType.DURATION
    }

    /**
     * Runs any updates for any requested animation and grabs the next frame if so.
     */
    fun update(dt: Duration) {
        // TODO integrate later when we need it
        // updateStateAnimations()

        if (animationRequested) {
            nextFrame(dt)
        }
    }
    /**
     * Starts any currently stopped animation from the beginning. This only does something when an animation is stopped with [stop].
     */
    fun start() {
        currentAnimation?.let {
            animationRequested = true
            currentFrameIdx = 0
            lastFrameTime = Duration.ZERO
        }
    }

    /**
     * Restarts the current running animation from the beginning. Is the same as invoking [stop] and then [start].
     */
    fun restart()  = start()

    /**
     * Resumes any currently stopped animation. This only does something when an animation is stopped with [stop].
     */
    fun resume() {
        currentAnimation?.let {
            animationRequested = true
        }
    }

    /**
     * Stops any running animations. Resume the current animation with [resume].
     */
    fun stop() {
        animationRequested = false
    }

    private fun nextFrame(frameTime: Duration) {
        lastFrameTime += frameTime
        if (lastFrameTime + frameTime >= frameDisplayTime) {
            when (animationType) {
                AnimationType.STANDARD -> {
                    if (numOfFramesRequested > 0) {
                        numOfFramesRequested--
                    }
                }
                AnimationType.DURATION -> {
                    remainingDuration -= lastFrameTime
                }
                AnimationType.LOOPED -> {
                    // do nothing, let it loop
                }
            }

            if (animationRequested) {
                totalFramesPlayed++
                currentFrameIdx++
                frameDisplayTime = currentAnimation?.getFrameTime(currentFrameIdx) ?: Duration.ZERO
                lastFrameTime = Duration.ZERO
            }
        }
    }

    private fun setAnimInfo(
        animation: Animation<KeyFrameType>,
        cyclesRequested: Int = 1,
        speed: Float = 1f,
        duration: Duration = Duration.INFINITE,
        type: AnimationType = AnimationType.STANDARD,
    ) {
        currentFrameIdx = 0
        frameDisplayTime = animation.getFrameTime(currentFrameIdx) * speed.toDouble()
        animationType = type
        animationRequested = true
        remainingDuration = duration
        numOfFramesRequested = cyclesRequested * totalFrames
    }

// TODO integrate later when we need it
    /**
     * Priority is represented by the deepest. The deepest has top priority while the shallowest has lowest.
     */
//    fun registerState(
//        anim: Animation<KeyFrameType>,
//        priority: Int,
//        loop: Boolean = true,
//        reason: () -> Boolean = { true },
//    ) {
//        removeState(anim)
//        states.add(AnimationState(anim, priority, loop, reason))
//        states.sortByDescending { priority }
//    }
//
//    fun removeState(anim: Animation<KeyFrameType>) {
//        states.find { it.anim == anim }?.also { states.remove(it) }
//    }
//
//    fun removeAllStates() {
//        states.clear()
//    }
//
//    private fun updateStateAnimations() {
//        if (states.isEmpty()) return
//
//        if (stack.isNotEmpty() && currentAnimationInstance?.isStateAnim == false) {
//            return
//        }
//
//        states.fastForEach { state ->
//            if (state.reason()) {
//                if (currentAnimation == state.anim) return
//                if (state.loop) {
//                    playStateAnimLooped(state.anim)
//                } else {
//                    playStateAnimOnce(state.anim)
//                }
//                return
//            }
//        }
//    }

    private data class AnimationInstance<KeyFrameType>(
        var anim: Animation<KeyFrameType>? = null,
        var plays: Int = 1,
        var duration: Duration = Duration.INFINITE,
        var isStateAnim: Boolean = false,
        var speed: Float = 1f,
        var type: AnimationType = AnimationType.STANDARD,
    ) {

        fun reset() {
            anim = null
            plays = 1
            duration = Duration.INFINITE
            isStateAnim = false
            speed = 1f
            type = AnimationType.STANDARD
        }
    }

// TODO integrate later when we need it
//
//    private data class AnimationState<KeyFrameType>(
//        val anim: Animation<KeyFrameType>,
//        val priority: Int,
//        val loop: Boolean,
//        val reason: () -> Boolean,
//    )
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
*/