package com.astrainteractive.astrasync.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Locker<T> {
    private val writerDispatcher = Dispatchers.IO.limitedParallelism(1)
    private val set = HashSet<T>()
    suspend fun lock(obj: T) = withContext(writerDispatcher) {
        set.add(obj)
    }

    suspend fun unlock(obj: T) = withContext(writerDispatcher) {
        set.remove(obj)
    }

    suspend fun isLocked(obj: T?) = withContext(writerDispatcher) {
        set.contains(obj)
    }
}