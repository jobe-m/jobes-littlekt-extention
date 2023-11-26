package com.jobeslegacy.engine.ecs.entity.config


import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.jobeslegacy.engine.asset.Assets
import com.jobeslegacy.engine.ecs.component.*
import com.jobeslegacy.engine.util.Identifier
import com.jobeslegacy.engine.util.random


object FireAndDustEffect {

    data class Config(
        val name: String = "none",  // name of this object
        val assetType: AssetType,
        val imageName: String = "",
        val offsetX: Float = 0.0f,
        val offsetY: Float = 0.0f,
        val velocityX: Float = 0.0f,
        val velocityY: Float = 0.0f,
        val velocityVariationX: Float = 0.0f,
        val velocityVariationY: Float = 0.0f,
        val layerIndex: Int = 0,
        val fadeOutDuration: Float = 0f
    ) : ConfigBase

    // Used in component properties to specify invokable function
    val configureEffectObject = Identifier(name = "configureEffectObject")

    // Configure function which applies the config to the entity's components
    private val configureEffectObjectFct = fun(world: World, entity: Entity, config: Identifier) = with(world) {
        val effectConfig = Assets.getEntityConfig<Config>(config)
        entity.configure { entity ->
            // HINT: GridComponent is already created by the SpawnerSystem - do not override it
            entity += OffsetComponent(
                x = effectConfig.offsetX,
                y = effectConfig.offsetY
            )
            var velocityXX = effectConfig.velocityX
            var velocityYY = effectConfig.velocityY
            if (effectConfig.velocityVariationX != 0.0f) {
                velocityXX += (-effectConfig.velocityVariationX..effectConfig.velocityVariationX).random()
            }
            if (effectConfig.velocityVariationY != 0.0f) {
                velocityYY += (-effectConfig.velocityVariationY..effectConfig.velocityVariationY).random()
            }
            entity += MoveComponent(
                velocityX = velocityXX,
                velocityY = velocityYY
            )
            entity += SpriteComponent(
                assetType = effectConfig.assetType,
                imageName = effectConfig.imageName,
                layerIndex = effectConfig.layerIndex
            )
            entity += AnimationComponent(
                destroyOnPlayingFinished = true,
                isPlaying = true
            )
            entity += LifeCycleComponent()
            if (effectConfig.fadeOutDuration > 0f) {
                entity += AnimationScript(
                    tweens = listOf(
                        // Fade out effect objects
                        TweenAppearance(entity = entity, alpha = 0.0f, duration = effectConfig.fadeOutDuration)
                    )
                )
            }
            // Need to overwrite any possible existingg InfoComponent otherwise the component hook will not pick up the new name
            entity += InfoComponent(name = effectConfig.name)
        }
        entity
    }

    init {
        Invokable.register(configureEffectObject, configureEffectObjectFct)
    }
}
