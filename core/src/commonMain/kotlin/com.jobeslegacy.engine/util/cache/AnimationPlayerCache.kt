package com.jobeslegacy.engine.util.cache

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.jobeslegacy.engine.ecs.component.AnimationComponent
import com.jobeslegacy.engine.util.cache.GenericObjectCache
import com.lehaine.littlekt.graphics.g2d.Animation
import com.lehaine.littlekt.graphics.g2d.TextureSlice
import com.lehaine.littlekt.log.Logger


// TODO this will not be needed
//      no caches per entity
/*
class AnimationPlayerCacheOBSOLETE : GenericObjectCache<AnimationPlayer<TextureSlice>>() {
    private val logger = Logger("AnimationPlayerCache")
    fun resetPlayer(entity: Entity, animationComponent: AnimationComponent) {
        // Add AnimationPlayer for Entity to cache if it does not yet exist
        val animPlayer: AnimationPlayer<TextureSlice> = getOrNull(entity) ?: addAndGet(entity, AnimationPlayer())
        // TODO initialize AnimationPlayer internals to use AnimationComponent properties
        // TODO Check which internal states of AnimationPlayer needs to be stored in AnimationComponent
    }

    fun configureAnimation(world: World, entity: Entity, startAnimation: Boolean, destroyOnAnimationFinished: Boolean, loopAnimation: Boolean, animation: Animation<TextureSlice>) = with(world) {
        if (startAnimation) {
            val player = get(entity)
//            player.onFrameChange = { index ->
//                logger.info { "index: $index" }
//                logger.info { "total frames: $totalFrames  - frames played: $totalFramesPlayed" }
//                logger.info { "current frame idx: $currentFrameIdx" }
//                logger.info { "time: $time" }
//            }

            if (destroyOnAnimationFinished) player.onAnimationFinish = {
                world -= entity
//                logger.info { "anim finished, deleting entity: ${entity.id}" }
            }

            if (loopAnimation) player.playLooped(animation)
            else player.play(animation)
        }
    }
}
*/
