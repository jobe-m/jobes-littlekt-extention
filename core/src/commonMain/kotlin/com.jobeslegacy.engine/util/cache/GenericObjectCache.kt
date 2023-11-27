package com.jobeslegacy.engine.util.cache

import com.github.quillraven.fleks.Entity
import kotlin.math.max


open class GenericObjectCache<T>(arraySize: Int = 64) {
    // Korge specific internal objects which we do not want to store in the components - they are accessed by entity id the same way as components
    private var arrayList: Array<Any?> = Array(arraySize) { null }

    fun addAndGet(entity: Entity, objValue: T) : T {
        if (entity.id >= arrayList.size) {
            arrayList = arrayList.copyOf(max(arrayList.size * 2, entity.id + 1))
        }
        arrayList[entity.id] = objValue
        return objValue
    }

    fun remove(entity: Entity) {
        if (arrayList.size > entity.id) {
            arrayList[entity.id] = null
        } else error("GenericObjectCache: Entity '${entity.id}' is out of range on remove!")
    }

    operator fun get(entity: Entity) : T {
        return if (arrayList.size > entity.id) {
            arrayList[entity.id] as T ?: error("GenericObjectCache: View of entity '${entity.id}' is null!")
        } else error("GenericObjectCache: Entity '${entity.id}' is out of range on get!")
    }

    fun getOrNull(entity: Entity) : T? {
        return if (arrayList.size > entity.id) arrayList[entity.id] as T?  // Cache potentially has this view. However, return value can still be null!
        else null
    }
}
