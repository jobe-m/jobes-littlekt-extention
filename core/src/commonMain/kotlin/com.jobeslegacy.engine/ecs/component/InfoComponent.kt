package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.jobeslegacy.engine.util.EntityByName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable @SerialName("Info")
class InfoComponent(
    var name: String = "noName",
    var entityId: Int = -1
): Component<InfoComponent> {
    override fun type() = InfoComponent

    override fun World.onAdd(entity: Entity) {
        entityId = entity.id
        EntityByName.add(name, entity)
    }

    override fun World.onRemove(entity: Entity) {
        EntityByName.remove(name)
    }

    companion object : ComponentType<InfoComponent>()
}