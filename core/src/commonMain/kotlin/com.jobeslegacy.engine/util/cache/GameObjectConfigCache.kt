package com.jobeslegacy.engine.util.cache

import com.jobeslegacy.engine.util.Identifier
import com.lehaine.littlekt.graphics.g2d.Animation
import com.lehaine.littlekt.graphics.g2d.TextureSlice


/**
 * This interface is used by systems to access specific basic properties of game object config.
 */
interface ConfigBase {
    val configName: Identifier
    val animation: Animation<TextureSlice>
}

object GameObjectConfigCache {
    val gameObjectConfigMap: MutableMap<String, ConfigBase> = mutableMapOf()

    fun <T : ConfigBase> add(gameObjectConfig: T) {
        gameObjectConfigMap[gameObjectConfig.configName.string] = gameObjectConfig
    }

    inline fun <reified T : ConfigBase> get(identifier: Identifier) : T {
        val config: ConfigBase = gameObjectConfigMap[identifier.string] ?: error("GameObjectConfig: No config found for id name '${identifier.string}'!")
        if (config !is T) error("GameObjectConfig: Config for '${identifier.string}' is not of type ${T::class}!")
        return config
    }

    fun getOrNull(identifier: Identifier) : ConfigBase? =
        gameObjectConfigMap[identifier.string]
}

//class GenericMapCache<T> {
//
//    private val objectMap: MutableMap<String, T> = mutableMapOf()
//
//    fun add(id: Identifier, objValue: T) {
//        objectMap[id.name] = objValue
//    }
//
//    fun get(id: Identifier): T =
//        objectMap[id.name] ?: error("GenericMapCache: No object found for id name '${id.name}'!")
//}