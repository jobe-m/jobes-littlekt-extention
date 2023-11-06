package com.jobeslegacy.engine.ecs.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.jobeslegacy.engine.ecs.component.AnimationComponent
import com.jobeslegacy.engine.ecs.component.SpriteComponent

/**
 * @author Colton Daily
 * @date 3/9/2023
 */
class AnimationSystem : IteratingSystem(family { all(SpriteComponent, AnimationComponent) }) {

    override fun onTickEntity(entity: Entity) {
        val sprite = entity[SpriteComponent]
        val animation = entity[AnimationComponent]

        sprite.slice = animation.currentAnimation?.getFrame(animation.currentFrameIdx) ?: sprite.slice
    }
}