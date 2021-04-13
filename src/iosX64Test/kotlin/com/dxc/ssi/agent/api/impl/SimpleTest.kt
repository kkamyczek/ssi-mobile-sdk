package com.dxc.ssi.agent.api.impl

import com.dxc.ssi.agent.transport.Sleeper
import com.dxc.ssi.agent.wallet.indy.model.WalletConfig
import com.dxc.ssi.agent.wallet.indy.model.WalletPassword
import com.indylib.*
import io.ktor.utils.io.core.*
import kotlinx.cinterop.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.*
import platform.posix.memcpy
import platform.posix.sleep
import kotlin.native.concurrent.AtomicInt
import kotlin.native.concurrent.AtomicReference
import kotlin.test.Test
import kotlin.test.Ignore

@SharedImmutable
val rw = ReadWrite()

class Workaround() {

    private val callbackCompleted = AtomicReference(false)
    private val atomicInt = AtomicInt(0)
    private val atomicString = AtomicReference("")

    fun setIntValue(intValue : Int) {
        this.atomicInt.value = intValue
    }

    fun getIntValue() : Int{
        return atomicInt.value
    }

    fun setStringValue(s: String) {
        atomicString.value = s
    }

    fun getStringValue(): String {
        return atomicString.value
    }

    fun resetCallbackStatus() {
        callbackCompleted.value = false
    }

    fun setCallbackCompleted() {
        callbackCompleted.value = true
    }

    fun isCallbackCompleted() : Boolean {
        return callbackCompleted.value
    }
}

@SharedImmutable
val workaround = Workaround()

class ReadWrite {
    private fun String.nsdata(): NSData? =
        NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)

    private fun NSData.string(): String? =
        NSString.create(data = this, encoding = NSUTF8StringEncoding)?.toString()

    private var atomic: AtomicReference<NSData?> = AtomicReference("".nsdata())
    fun read(): String {
        return atomic.value?.string()!!
    }

    fun save(text: String) {
        atomic.value = text.nsdata()
    }
}

@SharedImmutable
val rw1 = ReadWrite()

@SharedImmutable
val rw2 = ReadWrite()

@SharedImmutable
val rw3 = ReadWrite()

@SharedImmutable
val rw4 = ReadWrite()


typealias MyCallbackWallet = CPointer<CFunction<(indy_handle_t, indy_error_t) -> Unit>>?
typealias MyCallback = CPointer<CFunction<(indy_handle_t, indy_error_t, CPointer<ByteVar>?, CPointer<ByteVar>?) -> Unit>>
typealias MyCallbackWallet2 = CPointer<CFunction<(indy_handle_t, indy_error_t, indy_handle_t) -> Unit>>

class IosIndyTest {

    @Test
    @Ignore
    fun test_indy_log() {

        memScoped {
            val context: CValuesRef<*>? = null
            val enabledFn2: CValuesRef<CPointerVar<CFunction<(COpaquePointer? /* = CPointer<out CPointed>? */, indy_u32_t /* = UInt */, CPointer<ByteVar /* = ByteVarOf<Byte> */>?) -> indy_bool_t /* = Boolean */>> /* = CPointerVarOf<CPointer<CFunction<(COpaquePointer? /* = CPointer<out CPointed>? */, indy_u32_t /* = UInt */, CPointer<ByteVar /* = ByteVarOf<Byte> */>?) → indy_bool_t /* = Boolean */>>> */>? =
                null
            val logFn: CValuesRef<CPointerVar<CFunction<(COpaquePointer? /* = CPointer<out CPointed>? */, indy_u32_t /* = UInt */, CPointer<ByteVar /* = ByteVarOf<Byte> */>?, CPointer<ByteVar /* = ByteVarOf<Byte> */>?, CPointer<ByteVar /* = ByteVarOf<Byte> */>?, CPointer<ByteVar /* = ByteVarOf<Byte> */>?, indy_u32_t /* = UInt */) -> Unit>> /* = CPointerVarOf<CPointer<CFunction<(COpaquePointer? /* = CPointer<out CPointed>? */, indy_u32_t /* = UInt */, CPointer<ByteVar /* = ByteVarOf<Byte> */>?, CPointer<ByteVar /* = ByteVarOf<Byte> */>?, CPointer<ByteVar /* = ByteVarOf<Byte> */>?, CPointer<ByteVar /* = ByteVarOf<Byte> */>?, indy_u32_t /* = UInt */) → Unit>>> */>? =
                null
            val flushFn2: CValuesRef<CPointerVar<CFunction<(COpaquePointer? /* = CPointer<out CPointed>? */) -> Unit>> /* = CPointerVarOf<CPointer<CFunction<(COpaquePointer? /* = CPointer<out CPointed>? */) → Unit>>> */>? =
                null
            indy_get_logger(
                context,
                enabledFn2,
                logFn,
                flushFn2
            )
            sleep(10)
            val command = 1
            val pointer = "132"
            val config = "{\"id\":\"testWalletName${pointer}\",\"storage_type\":\"default\"}"
            val credentials = "{\"key\":\"testWalletPassword${pointer}\"}"
            val myExit_cb: MyCallbackWallet = staticCFunction(fun(
                _: indy_handle_t,
                _: indy_error_t,
            ) {
                return
            })
            indy_create_wallet(
                command,
                config,
                credentials,
                myExit_cb
            )
            sleep(10)
        }
    }

    @Test
    fun run() {
        val didJson = "{}"
        val commandHandle = 0
        val walletHandle = 0
        val callback: MyCallback = staticCFunction(fun(
            xcommand_handle: indy_handle_t,
            err: indy_error_t,
            did: CPointer<ByteVar>?,
            verkey: CPointer<ByteVar>?
        ) {
            println(did?.toKString())
            println(verkey?.toKString())
            println(xcommand_handle)
            println(err)
            return
        })
        val exitCode: indy_error_t = indy_create_and_store_my_did(
            commandHandle,
            walletHandle,
            didJson,
            callback
        )
        println(exitCode)
        sleep(5)
    }

    @Test
    fun test_indy_create_wallet() {

        val command = 1
        val pointer = "167"
        val context: CValuesRef<*>? = null
        val enabledFn: CPointer<CFunction<(
            COpaquePointer?, indy_u32_t,
            CPointer<ByteVar>?
        ) -> indy_bool_t>>? = null
        val flushFn: CPointer<CFunction<(COpaquePointer?) -> Unit>>? = null
        val myExitCallback = staticCFunction(fun(
            _: CPointer<out CPointed>?,
            _: indy_u32_t,
            _: CPointer<ByteVar>?,
            _: CPointer<ByteVar>?,
            _: CPointer<ByteVar>?,
            _: CPointer<ByteVar>?,
            _: indy_u32_t,
        ) {
            initRuntimeIfNeeded()
            return
        })
        indy_set_logger(
            context,
            enabledFn,
            myExitCallback,
            flushFn
        )
        sleep(8)
        val config = "{\"id\":\"testWalletName${pointer}\",\"storage_type\":\"default\"}"
        val credentials = "{\"key\":\"testWalletPassword${pointer}\"}"
        val myExit_cb: MyCallbackWallet = staticCFunction(fun(
            _: indy_handle_t,
            _: indy_error_t,
        ) {
            initRuntimeIfNeeded()
            return
        })
        indy_create_wallet(
            command,
            config,
            credentials,
            myExit_cb
        )
        sleep(8)
        val myExit_cb2: MyCallbackWallet2 = staticCFunction(fun(
            _: indy_handle_t,
            _: indy_error_t,
            _: indy_handle_t
        ) {
            initRuntimeIfNeeded()
            return
        })
        indy_open_wallet(
            command,
            config,
            credentials,
            myExit_cb2
        )

        sleep(8)
        val didJson = "{}"
        val commandHandle = 1
        val wallethandle = 3
        memScoped {
            val callback: MyCallback = staticCFunction(fun(
                _: indy_handle_t,
                _: indy_error_t,
                did: CPointer<ByteVar>?,
                verkey: CPointer<ByteVar>?
            ) {
                initRuntimeIfNeeded()
                val didData: String? = did?.toKString()
                val verkeyData: String? = verkey?.toKString()
                println("Print:Did:${didData} VerKey:${verkeyData}")
                rw.save("Did:${didData} VerKey:${verkeyData}")
                return
            })
            val result = indy_create_and_store_my_did(
                commandHandle,
                wallethandle,
                didJson,
                callback
            )
            sleep(8)
            println(rw.read())
            assert(result.toInt().equals(0))
        }
    }

    @Test
    fun test_indy_create_and_store_my_did() {

        val commandHandle = 1
        val walletHandle = 3
        val didJson = "{}"
        val myExit_cb2: MyCallback = staticCFunction(fun(
            xcommand_handle: indy_handle_t,
            err: indy_error_t,
            did: CPointer<ByteVar>?,
            verkey: CPointer<ByteVar>?
        ) {
            println("did")
            println(xcommand_handle)
            println(err)
            println(did?.toKString())
            println(verkey?.toKString())
            println(did!!.getRawValue())
            println(interpretCPointer<ByteVar>(did.getRawValue())?.toKString())
            return
        })
        val result2: indy_error_t = indy_create_and_store_my_did(
            commandHandle,
            walletHandle,
            didJson,
            myExit_cb2
        )
        assert(result2.toInt().equals(0))
        println(result2)
        sleep(8)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun test_indy_array() {

        memScoped {
            val pointer1 = "1"
            val config1 = "{\"id\":\"testWalletName${pointer1}\",\"storage_type\":\"default\"}"
            val credentials1 = "{\"key\":\"testWalletPassword${pointer1}\"}"

            val context: CValuesRef<*>? = null
            val enabledFn: CPointer<CFunction<(
                COpaquePointer?, indy_u32_t,
                CPointer<ByteVar>?
            ) -> indy_bool_t>>? = null
            val flushFn: CPointer<CFunction<(COpaquePointer?) -> Unit>>? = null
            val myExitCallback = staticCFunction(fun(
                _: CPointer<out CPointed>?,
                _: indy_u32_t,
                pointer: CPointer<ByteVar>?,
                val1: CPointer<ByteVar>?,
                val2: CPointer<ByteVar>?,
                val3: CPointer<ByteVar>?,
                _: indy_u32_t,
            ) {
                initRuntimeIfNeeded()
                println(pointer?.toKString())
                println(val1?.toKString())
                println(val2?.toKString())
                println(val3?.toKString())
                return
            })
            indy_set_logger(
                context,
                enabledFn,
                myExitCallback,
                flushFn
            )
            sleep(6)

            val myExit_cb1del: CPointer<CFunction<(indy_handle_t, indy_error_t) -> Unit>> = staticCFunction(fun(
                xcommand_handle: indy_handle_t,
                err: indy_error_t,
            ) {
                initRuntimeIfNeeded()
                println(xcommand_handle)
                println(err)
                return
            })
            indy_delete_wallet(
                2,
                config1,
                credentials1,
                myExit_cb1del
            )
            sleep(6)
            val myExit_cb1: CPointer<CFunction<(indy_handle_t, indy_error_t) -> Unit>> = staticCFunction(fun(
                xcommand_handle: indy_handle_t,
                err: indy_error_t,
            ) {
                initRuntimeIfNeeded()
                println(xcommand_handle)
                println(err)
                return
            })
            indy_create_wallet(
                2,
                config1,
                credentials1,
                myExit_cb1
            )
            sleep(6)

            val myExit_cbo1: MyCallbackWallet2 = staticCFunction(fun(
                _: indy_handle_t,
                err: indy_error_t,
                handle: indy_handle_t
            ) {
                initRuntimeIfNeeded()
                println(err)
                println("w1: $handle")
                val d = handle.toString()
                println(d)
                rw1.save(d!!)
                return
            })
            indy_open_wallet(
                3,
                config1,
                credentials1,
                myExit_cbo1
            )
            sleep(6)
            val walletId = rw1.read().toInt()
            println(walletId)
            val myExit_cbk1: CPointer<CFunction<(indy_handle_t /* = Int */, indy_error_t /* = UInt */, CPointer<ByteVar /* = ByteVarOf<Byte> */>?) -> Unit>>? =
                staticCFunction(fun(
                    command: indy_handle_t,
                    err: indy_error_t,
                    handle: CPointer<ByteVar>?
                ) {
                    initRuntimeIfNeeded()
                    println(err)
                    val d = handle?.toKString()
                    println(d)
                    rw2.save(d!!)
                    println(d)
                    return
                })
            indy_create_key(
                4,
                walletId,
                "{}",
                myExit_cbk1
            )
            sleep(6)
            val key2 = rw2.read()
            println(key2)
            val pointer2 = "2"
            val config2 = "{\"id\":\"testWalletName${pointer2}\",\"storage_type\":\"default\"}"
            val credentials2 = "{\"key\":\"testWalletPassword${pointer2}\"}"

            val myExit_cb2del: CPointer<CFunction<(indy_handle_t, indy_error_t) -> Unit>> = staticCFunction(fun(
                xcommand_handle: indy_handle_t,
                err: indy_error_t,
            ) {
                initRuntimeIfNeeded()
                println(xcommand_handle)
                println(err)
                return
            })
            indy_delete_wallet(
                2,
                config2,
                credentials2,
                myExit_cb2del
            )
            sleep(6)
            val myExit_cb2: CPointer<CFunction<(indy_handle_t, indy_error_t) -> Unit>> = staticCFunction(fun(
                xcommand_handle: indy_handle_t,
                err: indy_error_t,
            ) {
                initRuntimeIfNeeded()
                println(xcommand_handle)
                println(err)
                return
            })
            indy_create_wallet(
                6,
                config2,
                credentials2,
                myExit_cb2
            )
            sleep(6)

            val myExit_cbo2: MyCallbackWallet2 = staticCFunction(fun(
                _: indy_handle_t,
                err: indy_error_t,
                handle: indy_handle_t
            ) {
                initRuntimeIfNeeded()
                println(err)
                println("w2: $handle")
                val d = handle.toString()
                println(d)
                rw3.save(d!!)
                return
            })
            indy_open_wallet(
                7,
                config2,
                credentials2,
                myExit_cbo2
            )
            sleep(6)
            val walletId2 = rw3.read().toInt()
            println(walletId)
            val myExit_cbk2: CPointer<CFunction<(indy_handle_t /* = Int */, indy_error_t /* = UInt */, CPointer<ByteVar /* = ByteVarOf<Byte> */>?) -> Unit>>? =
                staticCFunction(fun(
                    _: indy_handle_t,
                    err: indy_error_t,
                    handle: CPointer<ByteVar>?
                ) {
                    initRuntimeIfNeeded()
                    println(err)
                    val d = handle?.toKString()
                    println(d)
                    rw4.save(d!!)
                    println(d)
                    return
                })
            indy_create_key(
                8,
                walletId2,
                "{}",
                myExit_cbk2
            )
            sleep(6)
            val key4 = rw4.read()

            println(key4)
            println("w1= $walletId w2= $walletId2")
            println("k1= $key2 k2= $key4")
            val recipientVk = "[\"${key4}\"]"

            val data =
                "{\"reqId\":1496822211362017764}"
            val s = data.cstr
            println(key2)
            println(recipientVk)

            val transf = s as CValuesRef<indy_u8_tVar>?

            val size = data.length.toUInt()

            val packMessageCb: CPointer<CFunction<(indy_handle_t, indy_error_t, CPointer<indy_u8_tVar>?, indy_u32_t) -> Unit>>? =
                staticCFunction(fun(
                    _: indy_handle_t,
                    _: indy_error_t,
                    data: CPointer<indy_u8_tVar>?,
                    size: indy_u32_t
                ) {
                    initRuntimeIfNeeded()
                    val byte: ByteArray = ByteArray(size.toInt()).apply {
                        usePinned {
                            memcpy(it.addressOf(0), data, size.toULong())
                        }
                    }
                    rw.save(String(byte))
                    return
                })
            val result = indy_pack_message(
                9,
                walletId,
                transf,
                size,
                recipientVk,
                key2,
                packMessageCb
            )
            sleep(3)
            println(result.toInt())
            println(result.toInt())
            sleep(3)
            println(result.toInt())
            sleep(3)
            println(result.toInt())
            sleep(3)
            println(result.toInt())
            sleep(3)
            println(rw.read())
        }
    }





    @Test
    @Ignore
    fun simpleTest() {



        val config = WalletConfig("testWallet11111")
        val password = WalletPassword("testWalletPassword")

        val walletConfigJson = Json.encodeToString(config)
        val walletPasswordJson = Json.encodeToString(password)

        val commandHandle = 1



        workaround.setIntValue( 3)
        workaround.resetCallbackStatus()
        val callback = staticCFunction<Int, UInt, Unit> { commandHandle: Int, errorCode: UInt
            ->
            initRuntimeIfNeeded()
            val strCommandHandle = commandHandle.toString()
            println("Executing callback from create_wallet: commandHandle = $strCommandHandle, errorCode = $errorCode")


            workaround.setIntValue(commandHandle)
            workaround.setStringValue(strCommandHandle)

            workaround.setCallbackCompleted()
        }


        indy_create_wallet(commandHandle, walletConfigJson, walletPasswordJson, callback)

        val commandHandle1 = waitForCallback(workaround)

        println("Callback returned $commandHandle1")


        return
        sleep(8)

        val intVal = workaround.getIntValue()
        println("reading int value from workaround = ${intVal}")

        println("after wallet creation: ${workaround.getStringValue()}")

        /*
        indy_open_wallet(command_handle: com.indylib.indy_handle_t /* = kotlin.Int */, @kotlinx.cinterop.internal.CCall.CString config: kotlin.String?, @kotlinx.cinterop.internal.CCall.CString credentials: kotlin.String?, fn: kotlinx.cinterop.CPointer<kotlinx.cinterop.CFunction<(com.indylib.indy_handle_t /* = kotlin.Int */, com.indylib.indy_error_t /* = kotlin.UInt */, com.indylib.indy_handle_t /* = kotlin.Int */) -> kotlin.Unit>>?): com.indylib.indy_error_t /* = kotlin.UInt */ { /* compiled code */ }
         */

        val commandHandle2 = 2


        val openWalletCallback =
            staticCFunction<Int, UInt, Int, Unit> { commandHandle: Int, errorCode: UInt, walletHandle: Int
                ->
                initRuntimeIfNeeded()
                println("Executing callback from open_wallet: commandHandle = $commandHandle, errorCode = $errorCode, walletHandle = $walletHandle")

            }

        indy_open_wallet(commandHandle2, walletConfigJson, walletPasswordJson, openWalletCallback)

        sleep(10)

    }

    private fun waitForCallback(workaround: Workaround): Int {
        //TODO: instead of sleep in the loop find out some proper kotlin solution, like channels, coroutines.
        //TODO: introduce timeout which is randomly increases
        //TODO: introduce some handling of command handle inside of this fun or some helper class
        while (!workaround.isCallbackCompleted()) {
            println("Callback is not completed. Sleeping...")
            Sleeper().sleep(500)
        }
        return workaround.getIntValue()

    }



    @Test
    @Ignore
   fun concurrencyTest() {
        println("Started")
       val channel = Channel<Int>()
        println("Created channel")
       runBlocking {
           println("Before sending to channel in GlobalScope")
           channel.send(1)
           channel.close()
       }
        println("After GlobalScope.launch")
       var value: Int? = 0
       runBlocking {
           println("Started run blocking")

           value = channel.receive()
       }

       println("received value = $value")

       return
   }
}



