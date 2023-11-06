package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.jobeslegacy.engine.ecs.logic.collision.checker.CollisionChecker

/**
 * @author Colton Daily
 * @date 3/9/2023
 */
open class GridCollisionComponent(var checker: CollisionChecker = CollisionChecker()) : Component<GridCollisionComponent> {

    override fun type(): ComponentType<GridCollisionComponent> = GridCollisionComponent

    companion object : ComponentType<GridCollisionComponent>()
}