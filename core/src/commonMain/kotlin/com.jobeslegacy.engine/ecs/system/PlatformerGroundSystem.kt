package com.jobeslegacy.engine.ecs.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.jobeslegacy.engine.ecs.component.GridCollisionComponent
import com.jobeslegacy.engine.ecs.component.GridComponent
import com.jobeslegacy.engine.ecs.component.MoveComponent
import com.jobeslegacy.engine.ecs.component.PlatformerComponent

/**
 * @author Colton Daily
 * @date 3/11/2023
 */
class PlatformerGroundSystem :
    IteratingSystem(family { all(PlatformerComponent, GridComponent, MoveComponent, GridCollisionComponent) }) {

    override fun onTickEntity(entity: Entity) {
        val platformer = entity[PlatformerComponent]
        val grid = entity[GridComponent]
        val move = entity[MoveComponent]
        val collision = entity[GridCollisionComponent]
        val checker = collision.checker

        platformer.onGround = platformer.groundChecker.onGround(
            move.velocityY,
            grid.cx,
            grid.cy,
            grid.xr,
            grid.yr,
            checker
        )

    }
}