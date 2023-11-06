package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.jobeslegacy.engine.ecs.logic.collision.resolver.CollisionResolver
import com.jobeslegacy.engine.ecs.logic.collision.resolver.NoCollisionResolver

/**
 * @author Colton Daily
 * @date 3/10/2023
 */
class GridCollisionResolverComponent(var resolver: CollisionResolver = NoCollisionResolver) :
    Component<GridCollisionResolverComponent> {
    override fun type() = GridCollisionResolverComponent

    companion object : ComponentType<GridCollisionResolverComponent>()
}