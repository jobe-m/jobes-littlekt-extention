package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.jobeslegacy.engine.ecs.entity.config.invalidEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable @SerialName("SubEntities")
data class SubEntitiesComponent(
    var entities: MutableMap<String, Entity> = mutableMapOf(),
    var moveWithParent: Boolean = true
) : Component<SubEntitiesComponent> {
    override fun type() = SubEntitiesComponent
    companion object : ComponentType<SubEntitiesComponent>()

    operator fun get(name: String) : Entity = entities[name] ?: error("SubEntities: Entity with name '$name' not found!")
}

data class ParentComponent(
    var entity: Entity = invalidEntity,
    // Behavior related to parent
    var move: Boolean = true,
) : Component<ParentComponent> {
    override fun type() = ParentComponent
    companion object : ComponentType<ParentComponent>()
}