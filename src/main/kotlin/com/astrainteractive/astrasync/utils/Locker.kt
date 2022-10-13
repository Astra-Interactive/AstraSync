package com.astrainteractive.astrasync.utils

import com.astrainteractive.astrasync.events.EventController
import kotlinx.coroutines.withContext

class Locker<T>{
    private val set = HashSet<T>()
    suspend fun lock(player: T) = withContext(EventController.writerDispatcher){
        set.add(player)
    }

    suspend fun unlock(player: T) = withContext(EventController.writerDispatcher){
        set.remove(player)
    }

    suspend fun isLocked(player: T?) = withContext(EventController.writerDispatcher){
        set.contains(player)
    }
}