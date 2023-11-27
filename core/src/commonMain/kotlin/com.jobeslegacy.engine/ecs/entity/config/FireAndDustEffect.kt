package com.jobeslegacy.engine.ecs.entity.config


import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.jobeslegacy.engine.ecs.component.*
import com.jobeslegacy.engine.util.Identifier
import com.jobeslegacy.engine.util.cache.ConfigBase
import com.jobeslegacy.engine.util.cache.GameObjectConfigCache
import com.jobeslegacy.engine.util.random
import com.lehaine.littlekt.graphics.g2d.Animation
import com.lehaine.littlekt.graphics.g2d.TextureSlice
import com.lehaine.littlekt.log.Logger

val noAnimation = Animation<TextureSlice>(frames = emptyList(), frameIndices = emptyList(), frameTimes = emptyList())

object FireAndDustEffect {

    private val logger = Logger("FireAndDustEffect")

    data class Config(
        override val configName: Identifier,                        // name of object is mandatory
        override val animation: Animation<TextureSlice>,  // animation is mandatory

        val offsetX: Float = 0.0f,               // offset for pivot point where the object is created (useful to set the pivot point in the middle of the sprite image)
        val offsetY: Float = 0.0f,
        val velocityX: Float = 0.0f,             // velocity of the object (makes the object moving in a direction)
        val velocityY: Float = 0.0f,
        val velocityVariationX: Float = 0.0f,    // apply variance to velocity [-x...x]
        val velocityVariationY: Float = 0.0f,
        val layerIndex: Int = 0,                 // put sprite image on specific layer - higher number will make image drawing on top of images with lower numbers
        val fadeOutDuration: Float = 0f,         // let the object (sprite image) fade out

        val startAnimation: Boolean = true,
        val destroyOnAnimationFinished: Boolean = true,  // let the object destroy itself when the animation finished playing (useful for explosion objects)
        val loopAnimation: Boolean = false,
    ) : ConfigBase

    // Used in component properties to specify invokable function
    val configureEffectObject = Identifier(string = "configureEffectObject")

    // Configure function which applies the config to the entity's components
    private val configureEffectObjectFct = fun(world: World, entity: Entity, config: Identifier) = with(world) {
        val effectConfig = GameObjectConfigCache.get<Config>(config)
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
                layerIndex = effectConfig.layerIndex
            )
            if (effectConfig.startAnimation) {
                entity += AnimationComponent(
                    // animation will be taken out of the game object config
                    gameObjectConfigName = effectConfig.configName
                ).apply {
                    // TODO this needs to be integrated differently - it is currently not serializable
                    onAnimationFinish = {
                        world -= entity
                        logger.info { "anim finished, deleting entity: ${entity.id}" }
                    }
                    // TODO refactor that
                    if (effectConfig.loopAnimation) playLooped(effectConfig.configName)
                    else play(effectConfig.configName)
                    start()
                }
            }
            entity += LifeCycleComponent()
            if (effectConfig.fadeOutDuration > 0f) {
                entity += TweenScript(
                    tweens = listOf(
                        // Fade out effect objects
                        TweenAppearance(entity = entity, alpha = 0.0f, duration = effectConfig.fadeOutDuration)
                    )
                )
            }
            // Need to overwrite!!! any possible existing InfoComponent otherwise the component hook will not pick up the new name
            entity += InfoComponent(name = effectConfig.configName.string)
        }
        entity
    }

    init {
        Invokable.register(configureEffectObject, configureEffectObjectFct)
    }
}
