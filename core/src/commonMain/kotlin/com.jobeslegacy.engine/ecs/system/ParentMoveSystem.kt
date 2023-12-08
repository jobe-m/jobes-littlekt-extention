package com.jobeslegacy.engine.ecs.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Interval
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.jobeslegacy.engine.ecs.component.*


class ParentMoveSystem(interval: Interval) : IteratingSystem(
    family = family { all(ParentComponent, GridComponent) },
    interval = interval
) {

    override fun onTickEntity(entity: Entity) {
        val parentGridComponent = entity[ParentComponent].entity[GridComponent]
        val gridComponent = entity[GridComponent]

        gridComponent.x = parentGridComponent.x
        gridComponent.y = parentGridComponent.y
    }
}
