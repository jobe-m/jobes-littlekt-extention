package com.jobeslegacy.engine.ecs.logic.collision.resolver

import com.jobeslegacy.engine.ecs.component.GridCollisionComponent
import com.jobeslegacy.engine.ecs.component.GridCollisionResultComponent
import com.jobeslegacy.engine.ecs.component.GridComponent
import com.jobeslegacy.engine.ecs.component.MoveComponent

/**
 * @author Colton Daily
 * @date 3/10/2023
 */
abstract class CollisionResolver {

    open fun resolveXCollision(
        grid: GridComponent,
        move: MoveComponent,
        collision: GridCollisionComponent,
        collisionResult: GridCollisionResultComponent
    ) = Unit

    open fun resolveYCollision(
        grid: GridComponent,
        move: MoveComponent,
        collision: GridCollisionComponent,
        collisionResult: GridCollisionResultComponent
    ) = Unit
}