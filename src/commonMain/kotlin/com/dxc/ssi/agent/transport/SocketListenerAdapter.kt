package com.dxc.ssi.agent.transport

import com.dxc.ssi.agent.utils.CoroutineHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

//TODO: see if we can avoid this adapter or join it with PlatformSocketListener somehow
class SocketListenerAdapter(
    //TODO: replace with CompletableDeferred?
    val socketOpenedChannel: Channel<Unit> = Channel(),
    val socketReceivedMessageChannel: Channel<String> = Channel(),
    val socketFailureChannel: Channel<Throwable> = Channel(),
    val socketClosedChannel: Channel<SocketClosureDetails> = Channel()
) {
    fun onOpened() {
        CoroutineHelper.waitForCompletion(CoroutineScope(Dispatchers.Default).async { socketOpenedChannel.send(Unit) })
    }

    fun onMessageReceived(message: String) {
        println("SocketListenerAdapter: received message: $message")
        CoroutineHelper.waitForCompletion(CoroutineScope(Dispatchers.Default).async {
            println("SocketListenerAdapter: entered async coroutine to send message: $message")
            socketReceivedMessageChannel.send(message) })
        println("SocketListenerAdapter: sent message to internal channel: $message")
    }

    fun onFailure(throwable: Throwable) {
        println("Encountered socket error: $throwable")
        CoroutineHelper.waitForCompletion(CoroutineScope(Dispatchers.Default).async { socketFailureChannel.send(throwable) })
    }

    fun onClosed(socketClosureDetails: SocketClosureDetails) {
        println("Socket closed")
        CoroutineHelper.waitForCompletion(CoroutineScope(Dispatchers.Default).async { socketClosedChannel.send(socketClosureDetails) })
    }

}