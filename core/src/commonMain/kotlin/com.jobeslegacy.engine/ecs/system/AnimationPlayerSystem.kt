package com.jobeslegacy.engine.ecs.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.jobeslegacy.engine.ecs.component.*
import com.jobeslegacy.engine.ecs.component.umod
import com.jobeslegacy.engine.ecs.entity.config.FireAndDustEffect
import com.jobeslegacy.engine.util.Identifier
import com.jobeslegacy.engine.util.cache.GameObjectConfigCache
import com.lehaine.littlekt.graphics.g2d.Animation
import com.lehaine.littlekt.graphics.g2d.TextureSlice
import com.lehaine.littlekt.log.Logger
import com.lehaine.littlekt.util.seconds
import kotlin.jvm.JvmName
import kotlin.time.Duration

/**
 * @author Colton Daily
 * @date 3/9/2023
 */
class AnimationPlayerSystem : IteratingSystem(
    family { all(SpriteComponent, AnimationComponent) },
    interval = Fixed(1 / 60f)
) {
    private val logger = Logger("AnimationPlayerSystem")
    private lateinit var animation: AnimationComponent
    private var entity: Entity = Entity(0)

    override fun onTickEntity(entity: Entity) {
        val sprite = entity[SpriteComponent]
        animation = entity[AnimationComponent]
        this.entity = entity

        // for debugging purposes
        animation.time += deltaTime

        update(deltaTime.seconds)
        sprite.slice = currentKeyFrame ?: sprite.slice

        entity.getOrNull(InfoComponent)?.also {
            if (it.name == "IntroFireBallTrails") {
//                logger.info { "Current frame idx: ${animation.currentFrameIdx}" }
            }
        }
    }

//    private val currentAnimationInstance: AnimationInstance<KeyFrameType>? get() = stack.firstOrNull()  TODO integrate stack later

    private val currentKeyFrame: TextureSlice?
        get() = animation.currentAnimation?.getFrame(animation.currentFrameIdx)

    /**
     * Invoked when a frame is changed.
     */
    // TODO check how this can be converted - see below
    //      lambdas cannot be serialized
//    var onFrameChange: ((Int) -> Unit)? = null

    /**
     * Invoked when animation has finished playing.
     */
    // TODO check how we can do deletion of entities when animation finishes - this we cannot store as lambda in the component
    //      lambdas cannot be serialized
//    var onAnimationFinish: (() -> Unit)? = null

    private fun setCurrentFrameIdx(value: Int) {
        animation.currentFrameIdx = value umod animation.totalFrames
// TODO
//        onFrameChange?.invoke(field)
    }

    private fun setNumOfFramesRequested(value: Int) {
        animation.numOfFramesRequested = value
        if (value == 0) {
            animation.stop()
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

            if (animation.destroyOnAnimationFinished) {
                world -= this.entity
                logger.info { "anim finished, deleting entity: ${entity.id}" }
            }
        }
    }

    private fun setRemainingDuration(value: Duration) {
        animation.remainingDuration = value
        if (value <= Duration.ZERO) {
            animation.stop()
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

// TODO not needed - maybe move outside of AnimationComponent
//    private val tempFrames = mutableListOf<KeyFrameType>()
//    private val tempIndices = mutableListOf<Int>()
//    private val tempTimes = mutableListOf<Duration>()
//    private val tempAnim = Animation(tempFrames, tempIndices, tempTimes)

// TODO integrate later when we need it
//    private val states = arrayListOf<AnimationState<KeyFrameType>>()
//    private val stack = arrayListOf<AnimationInstance<KeyFrameType>>()
//    private val animInstancePool = Pool(reset = { it.reset() }, 20) {
//        AnimationInstance<KeyFrameType>()
//    }


    /**
     * Play a specified frame for a certain amount of frames.
     */
// TODO check if we need this functionality here or move it outside of AnimationComponent
//    fun play(frame: KeyFrameType, frameTime: Duration = 50.milliseconds, numFrames: Int = 1) {
//        tempFrames.clear()
//        tempIndices.clear()
//        tempTimes.clear()
//        tempFrames += frame
//        repeat(numFrames) {
//            tempIndices += 0
//            tempTimes += frameTime
//        }
//        play(tempAnim)
//    }

    /**
     * Runs any updates for any requested animation and grabs the next frame if so.
     */
    fun update(dt: Duration) {
        // TODO integrate later when we need it
        // updateStateAnimations()

        if (animation.animationRequested) {
            nextFrame(dt)
        }
    }

    private fun nextFrame(frameTime: Duration) {
        animation.lastFrameTime += frameTime
        if (animation.lastFrameTime + frameTime >= animation.frameDisplayTime) {
            when (animation.animationType) {
                AnimationType.STANDARD -> {
                    if (animation.numOfFramesRequested > 0) {
                        setNumOfFramesRequested(animation.numOfFramesRequested - 1)
                    }
                }
                AnimationType.DURATION -> {
                    setRemainingDuration(animation.remainingDuration - animation.lastFrameTime)
                }
                AnimationType.LOOPED -> {
                    // do nothing, let it loop
                }
            }

            if (animation.animationRequested) {
                animation.totalFramesPlayed++
                setCurrentFrameIdx(animation.currentFrameIdx + 1)
                animation.frameDisplayTime = animation.currentAnimation?.getFrameTime(animation.currentFrameIdx) ?: Duration.ZERO
                animation.lastFrameTime = Duration.ZERO
            }
        }
    }

// TODO not yet needed
//    private fun setAnimInfo(
//        animation: Animation<KeyFrameType>,
//        cyclesRequested: Int = 1,
//        speed: Float = 1f,
//        duration: Duration = Duration.INFINITE,
//        type: AnimationType = AnimationType.STANDARD,
//    ) {
//        currentFrameIdx = 0
//        frameDisplayTime = animation.getFrameTime(currentFrameIdx) * speed.toDouble()
//        animationType = type
//        animationRequested = true
//        remainingDuration = duration
//        numOfFramesRequested = cyclesRequested * totalFrames
//    }

//    private fun playStateAnimLooped(animation: Animation<KeyFrameType>) {
//        playLooped(animation)
//        stack.lastOrNull()?.let {  TODO integrate stack later
//            it.isStateAnim = true
//        }
//    }

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

//    private data class AnimationInstance<KeyFrameType>(
//        var anim: Animation<KeyFrameType>? = null,
//        var plays: Int = 1,
//        var duration: Duration = Duration.INFINITE,
//        var isStateAnim: Boolean = false,
//        var speed: Float = 1f,
//        var type: AnimationType = AnimationType.STANDARD,
//    ) {
//
//        fun reset() {
//            anim = null
//            plays = 1
//            duration = Duration.INFINITE
//            isStateAnim = false
//            speed = 1f
//            type = AnimationType.STANDARD
//        }
//    }

// TODO integrate later when we need it
//
//    private data class AnimationState<KeyFrameType>(
//        val anim: Animation<KeyFrameType>,
//        val priority: Int,
//        val loop: Boolean,
//        val reason: () -> Boolean,
//    )

}