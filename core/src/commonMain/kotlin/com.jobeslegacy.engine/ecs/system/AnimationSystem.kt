package com.jobeslegacy.engine.ecs.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.jobeslegacy.engine.ecs.component.AnimationComponent
import com.jobeslegacy.engine.ecs.component.InfoComponent
import com.jobeslegacy.engine.ecs.component.SpriteComponent
import com.lehaine.littlekt.log.Logger
import com.lehaine.littlekt.util.seconds

/**
 * @author Colton Daily
 * @date 3/9/2023
 */
class AnimationSystem : IteratingSystem(
    family { all(SpriteComponent, AnimationComponent) },
    interval = Fixed(1 / 60f)
) {
    private val logger = Logger("AnimationSystem")

    override fun onTickEntity(entity: Entity) {
        val sprite = entity[SpriteComponent]
        val animation = entity[AnimationComponent]

        // for debugging purposes
        animation.time += deltaTime

        animation.update(deltaTime.seconds)
        sprite.slice = animation.currentKeyFrame ?: sprite.slice

        entity.getOrNull(InfoComponent)?.also {
            if (it.name.equals("IntroFireBallTrails")) {
//                logger.info { "Current frame idx: ${animation.currentFrameIdx}" }
            }
        }


//        sprite.slice = animation.player.currentKeyFrame // ?: sprite.slice

    }
}