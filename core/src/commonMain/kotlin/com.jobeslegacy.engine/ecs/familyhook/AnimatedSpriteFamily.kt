package com.jobeslegacy.engine.ecs.familyhook

import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.FamilyHook
import com.github.quillraven.fleks.World
import com.jobeslegacy.engine.ecs.component.AnimationComponent
import com.jobeslegacy.engine.ecs.component.SpriteComponent


/**
 * Whenever a sprite object with an animated image is created then the slice is set in [SpriteComponent].
 * This is done in [onAnimatedSpriteFamilyAdded] family hook here rather than in
 * component hook because we are sure that all needed components are set up before in the [World].
 */
fun animatedSpriteFamily(): Family = World.family { all(SpriteComponent, AnimationComponent) }

val onAnimatedSpriteFamilyAdded: FamilyHook = { entity ->
    val spriteComponent = entity[SpriteComponent]
    val animationComponent = entity[AnimationComponent]

//    val animationPlayerCache = inject<AnimationPlayerCache>("AnimationPlayerCache")
//    animationPlayerCache.resetPlayer(entity, animationComponent)
}

val onAnimatedSpriteFamilyRemoved: FamilyHook = { entity ->
    // nothing to do
}
