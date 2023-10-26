package com.lehaine.littlekt.fleks.util

import com.lehaine.littlekt.graph.node.annotation.SceneGraphDslMarker
import com.lehaine.littlekt.graph.node.ui.Container
import com.lehaine.littlekt.util.nanoseconds
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName
import kotlin.reflect.KMutableProperty0
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Creates an [TweenAnimation] object as a sequentialAnimation and triggers the [callback]. This object
 * contains independent animation steps which are executed in their order of being appended.
 * The [update][TweenAnimation.update] function needs to be called regularly to execute the animation steps.
 *
 * @param callback the callback that is invoked with a [Container] context in order to initialize any values
 * @return the newly created [TweenAnimation]
 */
@OptIn(ExperimentalContracts::class)
inline fun sequentialAnimation(callback: @SceneGraphDslMarker TweenAnimation.() -> Unit = {}): TweenAnimation {
    contract { callsInPlace(callback, InvocationKind.EXACTLY_ONCE) }
    return TweenAnimation().also(callback)
}

/**
 * TODO
 */
open class TweenAnimation {

    private val tweens = mutableListOf<Tween>()
    private var index = 0
    // internal time value which is incremented by update call
    private var timeProgress: Duration = 0.seconds

    data class Property<T> (
        val key: KMutableProperty0<T>,
        val start: T,
        val end: T,
        val interpolator: (Float, T, T) -> T,
        var delay: Duration? = null,
        var duration: Duration? = null,
        var easing: Easing? = null,
        var callback: (() -> Unit)? = null
    ) {
        var startTime: Duration = 0.seconds
        var endTime: Duration = 0.seconds

        fun set(ratio: Float) {
            key.set(interpolator(ratio, start, end))
        }

        fun get(): T = key.get()
    }

    class Tween(
        val properties: List<Property<*>>,
        var defaultDelay: Duration,
        var defaultDuration: Duration = 0.seconds,  // default values if they are not given for a tween
        var defaultEasing: Easing = Easing.LINEAR,
        var initialCallback: (() -> Unit)? = null,
        var finalCallback: (() -> Unit)? = null
    ) {
        var active: Boolean = false
        var endTime: Duration = 0.seconds
    }

    /**
     * Clear animation script and remove all existing [tweens][Tween].
     */
    fun clear() {
        tweens.clear()
    }

    /**
     * Add wait step [Tween] to animation sequence.
     *
     * @param duration The duration e.g. in [seconds] which the animation sequence will wait.
     */
    fun wait(duration: Duration): Tween = Tween(properties = listOf(), defaultDelay = duration).also { tweens.add(it) }

    /**
     * Create a new [Tween] animation step and add it to the sequence of tweens.
     *
     * A tween consists of a list of properties from other objects which shall be animated. Additionally, it is possible
     * to specify a default delay, duration and easing. Those values will be taken over for animating all properties
     * in this tween where delay, duration and easing are not defined explicitly.
     *
     * @param properties a list of [properties][Property] e.g. "myClass::membervariable[<animate-to-this-value>]"
     * @param defaultDelay Specify the default delay which will be assigned to all [properties][Property]. The delay is
     *        the amount of time in e.g. [seconds] before the property animation starts.
     * @param defaultDuration Specify the default duration which will be assigned to all [properties][Property]. The duration
     *        specifies how long the property animation takes until it is finished.
     * @param defaultEasing Specify the default easing which will be assigned to all [properties][Property]. The easing
     *        describes the "change" from the current value to the end value of the [Property].
     * @return the newly created [Tween] animation
     */
    fun tween(
        vararg properties: Property<*>,
        defaultDelay: Duration = 0.seconds,
        defaultDuration: Duration = 0.seconds,
        defaultEasing: Easing = Easing.LINEAR
    ): Tween =
        Tween(properties = properties.toList(), defaultDelay = defaultDelay, defaultDuration = defaultDuration, defaultEasing = defaultEasing).also { tweens.add(it) }
    fun tween(another: Tween): Tween = another.also { tweens.add(it) }

    /**
     * Add callback lambda to [Tween] which will be executed at the beginning of the [tween's][Tween] animation.
     */
    fun Tween.initialCallback(callback: (() -> Unit)? = null): Tween = this.also { initialCallback = callback }

    /**
     * Add callback lambda to [Tween] which will be executed after the [tween's][Tween] animation was finished.
     */
    fun Tween.finalCallback(callback: (() -> Unit)? = null): Tween = this.also { finalCallback = callback}

    /**
     * Add delay to specific [Property] animation.
     */
    fun <T> Property<T>.delay(value: Duration): Property<T> = this.also { delay = value }

    /**
     * Add duration to specific [Property] animation.
     */
    fun <T> Property<T>.duration(value: Duration): Property<T> = this.also { duration = value }

    /**
     * Add easing to specific [Property] animation.
     */
    fun <T> Property<T>.easing(value: Easing): Property<T> = this.also { easing = value }

    /**
     * Add callback to specific [Property] animation. The callback will be invoked after each update of the
     * [Property] for the duration of the animation. This is useful to trigger specific functionality of the
     * object which shall be animated.
     */
    fun <T> Property<T>.callback(value: () -> Unit): Property<T> = this.also { callback = value }

    /**
     * Append another [TweenAnimation] to this existing animation.
     */
    fun append(callback: @SceneGraphDslMarker TweenAnimation.() -> Unit = {}): TweenAnimation = this.also(callback)

    /**
     * Execute lambda function as an animation step.
     *
     * @param delay Specify delay after which the lambda is executed. Default is 0 seconds.
     * @param duration Specify duration which is the time the animation step will wait after triggering the lambda function.
     *        Default is 0 seconds.
     * @return the newly created [Tween] animation
     */
    fun execute(delay: Duration = 0.seconds, duration: Duration = 0.seconds, callback: () -> Unit): Tween =
        Tween(properties = listOf(), defaultDelay = delay, defaultDuration = duration).initialCallback(callback).also { tweens.add(it) }

    /**
     * Call this function regularly to execute the internal logic for the [TweenAnimation].
     *
     * @param dt The time since the last invoke of the update method.
     */
    fun update(dt: Duration) {
        if (index >= tweens.count()) return
        val tween = tweens[index]

        if (!tween.active) {
            // initialize new tween animation step
            tween.active = true
            tween.initialCallback?.invoke()
            tween.endTime = timeProgress + tween.defaultDelay + tween.defaultDuration

            tween.properties.forEach { property ->
                // Initialize property values
                property.startTime = timeProgress + (property.delay ?: tween.defaultDelay)
                property.endTime = property.startTime + (property.duration ?: tween.defaultDuration)
                if (property.duration == null) property.duration = tween.defaultDuration
                if (property.easing == null) property.easing = tween.defaultEasing
                // Set overall end time to the largest end time of all properties
                if (property.endTime > tween.endTime) tween.endTime = property.endTime
            }
        }

        tween.properties.forEach { property ->
            if (property.endTime <= timeProgress) {
                // Property animation is over
                property.set(ratio = 1f)
                property.callback?.invoke()
                // TODO disable updating of property?
            } else if (property.startTime <= timeProgress) {
                // Initial Property delay has passed and the actual animation of the property can start
                val progress: Float = (timeProgress - property.startTime).nanoseconds / property.duration!!.nanoseconds  // time progress / duration = values are between: (0..1)
                property.set(ratio = property.easing!!.invoke(progress))
                property.callback?.invoke()
            }
        }

        if (tween.endTime <= timeProgress) {
            // Tween animation has reached the end
            tween.finalCallback?.invoke()
            index++
        }

        timeProgress += dt
    }

    override fun toString(): String =
        "AnimationScript: tweens: ${tweens.forEach { println(it) }}"

    companion object {
        fun tween(
            vararg properties: Property<*>,
            defaultDelay: Duration = 0.seconds,
            defaultDuration: Duration = 0.seconds,
            defaultEasing: Easing = Easing.LINEAR
        ): Tween =
            Tween(properties = properties.toList(), defaultDelay = defaultDelay, defaultDuration = defaultDuration, defaultEasing = defaultEasing)
        fun <T> Property<T>.delay(value: Duration): Property<T> = this.also { delay = value }
        fun <T> Property<T>.duration(value: Duration): Property<T> = this.also { duration = value }
        fun <T> Property<T>.easing(value: Easing): Property<T> = this.also { easing = value }
    }
}

@JvmName("getFloat")
operator fun KMutableProperty0<Float>.get(end: Float) =
    TweenAnimation.Property(this, this.get(), end, ::interpolateFloat)
@JvmName("getBoolean")
operator fun KMutableProperty0<Boolean>.get(end: Boolean) =
    TweenAnimation.Property(this, this.get(), end, ::interpolateBoolean)

internal fun interpolateFloat(ratio: Float, start: Float, end: Float): Float = (end - start) * ratio + start
internal fun interpolateBoolean(ratio: Float, start: Boolean, end: Boolean): Boolean = if (ratio > 0.5f) end else start
