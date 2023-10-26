package com.lehaine.littlekt.fleks.util

import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin


private const val BOUNCE_FACTOR = 1.70158f
private const val HALF_PI = PI.toFloat() / 2f

@Suppress("unused")
fun interface Easing {
    operator fun invoke(it: Float): Float
    operator fun invoke(it: Double): Double = invoke(it.toFloat()).toDouble()

    companion object {
        operator fun invoke(name: () -> String, block: (Float) -> Float): Easing {
            return object : Easing {
                override fun invoke(it: Float): Float = block(it)
                override fun toString(): String = name()
            }
        }

        private val _ALL_LIST: List<Easings> by lazy(LazyThreadSafetyMode.PUBLICATION) {
            Easings.values().toList()
        }

        val ALL_LIST: List<Easing> get() = _ALL_LIST

        /**
         * Retrieves a mapping of all standard easings defined directly in [Easing], for example "SMOOTH" -> Easing.SMOOTH.
         */
        val ALL: Map<String, Easing> by lazy(LazyThreadSafetyMode.PUBLICATION) {
            _ALL_LIST.associateBy { it.name }
        }

        // Author's note:
        // 1. Make sure new standard easings are added both here and in the Easings enum class
        // 2. Make sure the name is the same, otherwise [ALL] will return confusing results

        val SMOOTH: Easing get() = Easings.SMOOTH
        val EASE_IN_ELASTIC: Easing get() = Easings.EASE_IN_ELASTIC
        val EASE_OUT_ELASTIC: Easing get() = Easings.EASE_OUT_ELASTIC
        val EASE_OUT_BOUNCE: Easing get() = Easings.EASE_OUT_BOUNCE
        val LINEAR: Easing get() = Easings.LINEAR
        val EASE_IN: Easing get() = Easings.EASE_IN
        val EASE_OUT: Easing get() = Easings.EASE_OUT
        val EASE_IN_OUT: Easing get() = Easings.EASE_IN_OUT
        val EASE_OUT_IN: Easing get() = Easings.EASE_OUT_IN
        val EASE_IN_BACK: Easing get() = Easings.EASE_IN_BACK
        val EASE_OUT_BACK: Easing get() = Easings.EASE_OUT_BACK
        val EASE_IN_OUT_BACK: Easing get() = Easings.EASE_IN_OUT_BACK
        val EASE_OUT_IN_BACK: Easing get() = Easings.EASE_OUT_IN_BACK
        val EASE_IN_OUT_ELASTIC: Easing get() = Easings.EASE_IN_OUT_ELASTIC
        val EASE_OUT_IN_ELASTIC: Easing get() = Easings.EASE_OUT_IN_ELASTIC
        val EASE_IN_BOUNCE: Easing get() = Easings.EASE_IN_BOUNCE
        val EASE_IN_OUT_BOUNCE: Easing get() = Easings.EASE_IN_OUT_BOUNCE
        val EASE_OUT_IN_BOUNCE: Easing get() = Easings.EASE_OUT_IN_BOUNCE
        val EASE_IN_QUAD: Easing get() = Easings.EASE_IN_QUAD
        val EASE_OUT_QUAD: Easing get() = Easings.EASE_OUT_QUAD
        val EASE_IN_OUT_QUAD: Easing get() = Easings.EASE_IN_OUT_QUAD
        val EASE_SINE: Easing get() = Easings.EASE_SINE
        val EASE_CLAMP_START: Easing get() = Easings.EASE_CLAMP_START
        val EASE_CLAMP_END: Easing get() = Easings.EASE_CLAMP_END
        val EASE_CLAMP_MIDDLE: Easing get() = Easings.EASE_CLAMP_MIDDLE
    }
}

private fun combine(it: Float, start: Easing, end: Easing) =
    if (it < .5f) .5f * start(it * 2f) else .5f * end((it - .5f) * 2f) + .5f
private enum class Easings : Easing {
    SMOOTH {
        override fun invoke(it: Float): Float = it * it * (3 - 2 * it)
    },
    EASE_IN_ELASTIC {
        override fun invoke(it: Float): Float =
            if (it == 0f || it == 1f) {
                it
            } else {
                val p = 0.3f
                val s = p / 4.0f
                val inv = it - 1

                -1f * 2f.pow(10f * inv) * sin((inv - s) * (2f * PI.toFloat()) / p)
            }
    },
    EASE_OUT_ELASTIC {
        override fun invoke(it: Float): Float =
            if (it == 0f || it == 1f) {
                it
            } else {
                val p = 0.3f
                val s = p / 4.0f
                2.0f.pow(-10.0f * it) * sin((it - s) * (2.0f * PI.toFloat()) / p) + 1
            }
    },
    EASE_OUT_BOUNCE {
        override fun invoke(it: Float): Float {
            val s = 7.5625f
            val p = 2.75f
            return when {
                it < 1f / p -> s * it.pow(2f)
                it < 2f / p -> s * (it - 1.5f / p).pow(2.0f) + 0.75f
                it < 2.5f / p -> s * (it - 2.25f / p).pow(2.0f) + 0.9375f
                else -> s * (it - 2.625f / p).pow(2.0f) + 0.984375f
            }
        }
    },
    LINEAR {
        override fun invoke(it: Float): Float = it
    },
    EASE_IN {
        override fun invoke(it: Float): Float = it * it * it
    },
    EASE_OUT {
        override fun invoke(it: Float): Float =
            (it - 1f).let { inv ->
                inv * inv * inv + 1
            }
    },
    EASE_IN_OUT {
        override fun invoke(it: Float): Float = combine(it, EASE_IN, EASE_OUT)
    },
    EASE_OUT_IN {
        override fun invoke(it: Float): Float = combine(it, EASE_OUT, EASE_IN)
    },
    EASE_IN_BACK {
        override fun invoke(it: Float): Float = it.pow(2f) * ((BOUNCE_FACTOR + 1f) * it - BOUNCE_FACTOR)
    },
    EASE_OUT_BACK {
        override fun invoke(it: Float): Float =
            (it - 1f).let { inv ->
                inv.pow(2f) * ((BOUNCE_FACTOR + 1f) * inv + BOUNCE_FACTOR) + 1f
            }
    },
    EASE_IN_OUT_BACK {
        override fun invoke(it: Float): Float = combine(it, EASE_IN_BACK, EASE_OUT_BACK)
    },
    EASE_OUT_IN_BACK {
        override fun invoke(it: Float): Float = combine(it, EASE_OUT_BACK, EASE_IN_BACK)
    },
    EASE_IN_OUT_ELASTIC {
        override fun invoke(it: Float): Float = combine(it, EASE_IN_ELASTIC, EASE_OUT_ELASTIC)
    },
    EASE_OUT_IN_ELASTIC {
        override fun invoke(it: Float): Float = combine(it, EASE_OUT_ELASTIC, EASE_IN_ELASTIC)
    },
    EASE_IN_BOUNCE {
        override fun invoke(it: Float): Float = 1f - EASE_OUT_BOUNCE(1f - it)
    },
    EASE_IN_OUT_BOUNCE {
        override fun invoke(it: Float): Float = combine(it, EASE_IN_BOUNCE, EASE_OUT_BOUNCE)
    },
    EASE_OUT_IN_BOUNCE {
        override fun invoke(it: Float): Float = combine(it, EASE_OUT_BOUNCE, EASE_IN_BOUNCE)
    },
    EASE_IN_QUAD {
        override fun invoke(it: Float): Float = 1f * it * it
    },
    EASE_OUT_QUAD {
        override fun invoke(it: Float): Float = -1f * it * (it - 2)
    },
    EASE_IN_OUT_QUAD {
        override fun invoke(it: Float): Float =
            (it * 2f).let { t ->
                if (t < 1) {
                    +1f / 2 * t * t
                } else {
                    -1f / 2 * ((t - 1) * ((t - 1) - 2) - 1)
                }
            }
    },
    EASE_SINE {
        override fun invoke(it: Float): Float = sin(it * HALF_PI)
    },
    EASE_CLAMP_START {
        override fun invoke(it: Float): Float = if (it <= 0f) 0f else 1f
    },
    EASE_CLAMP_END {
        override fun invoke(it: Float): Float = if (it < 1f) 0f else 1f
    },
    EASE_CLAMP_MIDDLE {
        override fun invoke(it: Float): Float = if (it < 0.5f) 0f else 1f
    };

    override fun toString(): String = super.toString().replace('_', '-').lowercase()
}