package com.jobeslegacy.engine.ecs.familyhook

import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.FamilyHook
import com.github.quillraven.fleks.World
import com.jobeslegacy.engine.asset.Assets
import com.jobeslegacy.engine.ecs.component.AnimationComponent
import com.jobeslegacy.engine.ecs.component.AssetType
import com.jobeslegacy.engine.ecs.component.SpriteComponent
import com.lehaine.littlekt.graphics.g2d.getAnimation


/**
 * Whenever a sprite object with an animated image is created then the slice is set in [SpriteComponent].
 * This is done in [onAnimatedSpriteFamilyAdded] family hook here rather than in
 * component hook because we are sure that all needed components are set up before in the [World].
 */
fun animatedSpriteFamily(): Family = World.family { all(SpriteComponent, AnimationComponent) }

val onAnimatedSpriteFamilyAdded: FamilyHook = { entity ->
    val spriteComponent = entity[SpriteComponent]
    val animationComponent = entity[AnimationComponent]

    // Load all animation frames and store into ...
    val animation = Assets.atlas(spriteComponent.assetType).getAnimation(spriteComponent.imageName)

    // TODO - store animation in animation system for each entity
    //      - check what internal states of animation and/or animationplayer needs to be stored in animationComponent
}

val onAnimatedSpriteFamilyRemoved: FamilyHook = { entity ->
    entity[SpriteComponent].slice = null
}
