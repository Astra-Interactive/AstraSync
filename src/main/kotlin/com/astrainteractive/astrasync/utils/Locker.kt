package com.astrainteractive.astrasync.utils

import com.astrainteractive.astrasync.events.EventController
import kotlinx.coroutines.withContext

class Locker<T>{
    private val set = HashSet<T>()
    suspend fun lock(obj: T) = withContext(EventController.writerDispatcher){
        set.add(obj)
    }

    suspend fun unlock(obj: T) = withContext(EventController.writerDispatcher){
        set.remove(obj)
    }

    suspend fun isLocked(obj: T?) = withContext(EventController.writerDispatcher){
        set.contains(obj)
    }
}