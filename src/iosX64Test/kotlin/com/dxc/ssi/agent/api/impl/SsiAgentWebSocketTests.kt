package com.dxc.ssi.agent.api.impl

import co.touchlab.stately.collections.sharedMutableListOf
import com.dxc.ssi.agent.model.messages.MessageEnvelop
import com.dxc.ssi.agent.transport.AppSocket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.posix.sleep
import kotlin.test.Test

class ApplicationApi {
    fun about(callback: (String) -> Unit) {
        GlobalScope.apply {
            launch {
                try {
                    var incomingMessagesQueue = mutableListOf<MessageEnvelop>()
                    val ap: AppSocket = AppSocket("wss://echo.websocket.org", sharedMutableListOf())
                    sleep(1)
                    ap.connect()
                    sleep(1)
                    while (ap.currentState != AppSocket.State.CONNECTED) {
                        println(ap.currentState)
                        println("waiting")
                        callback(ap.currentState.toString())
                    }

                } catch (e: Exception) {
                    println(e.message)
                    callback("Timeout")
                }

            }
        }
    }
}

class SsiAgentWebSocketTests {
    @Test
    fun basicTestRun2() {
        ApplicationApi().about { println() }
        sleep(10)
    }
}