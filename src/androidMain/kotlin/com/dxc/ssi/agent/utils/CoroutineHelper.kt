package com.dxc.ssi.agent.utils

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

actual class CoroutineHelper {
    actual companion object {
        actual fun <T> waitForCompletion(deferred: Deferred<T>):T {
            return  runBlocking {
                deferred.await()
            }
        }

        actual fun singleThreadCoroutineContext(threadName: String): CoroutineContext =
            newSingleThreadContext(threadName)
    }
}