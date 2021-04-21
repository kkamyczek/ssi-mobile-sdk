import cocoapods.PocketSocket.PSWebSocket
import cocoapods.PocketSocket.PSWebSocketDelegateProtocol
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ObjCClass
import objcnames.classes.Protocol
import platform.Foundation.NSError
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.darwin.NSInteger
import platform.darwin.NSObject
import platform.darwin.NSUInteger
import platform.posix.sleep
import kotlin.test.Test

class SsiTestPocketWebSocket : PSWebSocketDelegateProtocol, NSObject() {
    private var pp: PSWebSocket? = null
    private var ipAddress = "ws://echo.websocket.org"

    @Test
    fun basicTest() {
        val url = NSURL.URLWithString(ipAddress)
        val request = NSURLRequest.requestWithURL(url!!)
        try {
            pp = PSWebSocket.clientSocketWithRequest(request)
            pp?.delegate = this
            pp?.open()
            var status = pp?.readyState
            println(status)
            while (true) {
                sleep(5)
                status = pp?.readyState
                println(status)
                pp?.send("")
                status = pp?.readyState
                println(status)
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun `class`(): ObjCClass? {
        println("1")
        return null
    }

    override fun conformsToProtocol(aProtocol: Protocol?): Boolean {
        return true
    }

    override fun description(): String? {
        return null
    }

    override fun hash(): NSUInteger {
        return 0u
    }

    override fun isEqual(`object`: Any?): Boolean {
        return true
    }

    override fun isKindOfClass(aClass: ObjCClass?): Boolean {
        return true
    }

    override fun isMemberOfClass(aClass: ObjCClass?): Boolean {
        return true
    }

    override fun isProxy(): Boolean {
        return true
    }

    override fun performSelector(aSelector: COpaquePointer?): Any? {
        TODO("Not yet implemented")
    }

    override fun performSelector(aSelector: COpaquePointer?, withObject: Any?): Any? {
        return true
    }

    override fun performSelector(aSelector: COpaquePointer?, withObject: Any?, _withObject: Any?): Any? {
        return true
    }

    override fun respondsToSelector(aSelector: COpaquePointer?): Boolean {
        return true
    }

    override fun superclass(): ObjCClass? {
        return null
    }

    override fun webSocket(webSocket: PSWebSocket?, didFailWithError: NSError?) {
        println("5")
        NSLog("5")
    }

    override fun webSocket(webSocket: PSWebSocket?, didReceiveMessage: Any?) {
        println("2")
        NSLog("2")
    }

    override fun webSocket(webSocket: PSWebSocket?, didCloseWithCode: NSInteger, reason: String?, wasClean: Boolean) {
        println("3")
        NSLog("3")
    }

    override fun webSocketDidOpen(webSocket: PSWebSocket?): kotlin.Unit {
        NSLog("4")
        println("4")
        pp?.send("{}")
        println("The websocket handshake completed and is now open!")
    }


}