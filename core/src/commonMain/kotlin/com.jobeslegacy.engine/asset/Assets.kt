package com.jobeslegacy.engine.asset

import com.jobeslegacy.engine.ecs.component.AssetType
import com.jobeslegacy.engine.ecs.entity.config.ConfigBase
import com.jobeslegacy.engine.util.Identifier
import com.jobeslegacy.engine.util.Level
import com.lehaine.littlekt.AssetProvider
import com.lehaine.littlekt.BitmapFontAssetParameter
import com.lehaine.littlekt.Context
import com.lehaine.littlekt.Disposable
import com.lehaine.littlekt.file.ldtk.LDtkMapLoader
import com.lehaine.littlekt.graphics.g2d.TextureAtlas
import com.lehaine.littlekt.graphics.g2d.font.BitmapFont
import com.lehaine.littlekt.graphics.g2d.tilemap.ldtk.LDtkWorld
import kotlin.concurrent.Volatile
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class Assets private constructor(context: Context) : Disposable {
    private val provider = AssetProvider(context)

    private val commonAtlas : TextureAtlas by provider.load(context.resourcesVfs["common.atlas.json"])
    private val worldAtlas : TextureAtlas by provider.load(context.resourcesVfs["world1.atlas.json"])  // TODO hardcoded - this needs to be dynamic
    private val levelAtlas : TextureAtlas by provider.load(context.resourcesVfs["intro.atlas.json"])  // TODO hardcoded - this needs to be dynamic

    private val mapLoader by provider.load<LDtkMapLoader>(context.resourcesVfs["level_data/world.ldtk"])
    private val map by provider.prepare { mapLoader.loadMap(true) }
    //    private val levelMap by provider.prepare { map.levels.associateBy { it.identifier } }  -- not used yet
    private val level by provider.prepare {
        // Hardcoded currently to first entry which is the Intro "level"
        Level(map.levels[0])
    }


    private val pixelFont: BitmapFont by provider.prepare {
        provider.loadSuspending<BitmapFont>(
            context.resourcesVfs["m5x7_16_outline.fnt"],
            BitmapFontAssetParameter(preloadedTextures = listOf(commonAtlas["m5x7_16_outline"].slice))  // Hardcoded - font comes from common which is always available
        ).content
    }

    val entityConfigs: MutableMap<String, ConfigBase> = mutableMapOf()



    override fun dispose() {
        map.dispose()
        mapLoader.dispose()
        levelAtlas.dispose()
        worldAtlas.dispose()
        commonAtlas.dispose()
    }

    companion object {
        @Volatile
        private var instance: Assets? = null
        val INSTANCE: Assets get() = instance ?: error("Instance has not been created!")

        fun atlas(type: AssetType) = when(type) {
            AssetType.COMMON -> INSTANCE.commonAtlas
            AssetType.WORLD -> INSTANCE.worldAtlas
            AssetType.LEVEL -> INSTANCE.levelAtlas
        }

        val map: LDtkWorld get() = INSTANCE.map
        val level: Level get() = INSTANCE.level
        val pixelFont: BitmapFont get() = INSTANCE.pixelFont
        val provider: AssetProvider get() = INSTANCE.provider

        fun <T : ConfigBase> addEntityConfig(identifier: Identifier, entityConfig: T) {
            INSTANCE.entityConfigs[identifier.name] = entityConfig
        }

        inline fun <reified T : ConfigBase> getEntityConfig(identifier: Identifier) : T {
            val config: ConfigBase = INSTANCE.entityConfigs[identifier.name] ?: error("AssetStore - getConfig: No config found for configId name '${identifier.name}'!")
            if (config !is T) error("AssetStore - getConfig: Config for '${identifier.name}' is not of type ${T::class}!")
            return config
        }

        @OptIn(ExperimentalContracts::class)
        fun createInstance(context: Context, onLoad: () -> Unit): Assets {
            contract { callsInPlace(onLoad, InvocationKind.EXACTLY_ONCE) }
            check(instance == null) { "Instance already created!" }
            val newInstance = Assets(context)
            instance = newInstance
            INSTANCE.provider.onFullyLoaded = onLoad
            context.onRender { INSTANCE.provider.update() }
            return newInstance
        }

        fun dispose() {
            instance?.dispose()
        }
    }
}
