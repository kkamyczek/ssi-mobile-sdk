package com.dxc.ssi.agent.api.impl

import com.indylib.*
import io.ktor.utils.io.core.*
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.posix.sleep
import kotlin.native.concurrent.AtomicReference
import kotlin.test.Test

fun String.nsdata(): NSData? =
    NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)

fun NSData.string(): String? =
    NSString.create(data = this, encoding = NSUTF8StringEncoding)?.toString()

@SharedImmutable
val rw = ReadWrite()

@SharedImmutable
val rw1 = ReadWrite()

@SharedImmutable
val rw2 = ReadWrite()

@SharedImmutable
val rw3 = ReadWrite()

@SharedImmutable
val rw4 = ReadWrite()

class ReadWrite {
    fun String.nsdata(): NSData? =
        NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)

    fun NSData.string(): String? =
        NSString.create(data = this, encoding = NSUTF8StringEncoding)?.toString()

    var atomic: AtomicReference<NSData?> = AtomicReference("".nsdata())
    fun read(): String {
        return atomic.value?.string()!!
    }

    fun save(text: String) {
        atomic.value = text.nsdata()
    }
}
typealias MyCallbackWallet = CPointer<CFunction<(indy_handle_t, indy_error_t) -> Unit>>?
typealias MyCallback = CPointer<CFunction<(indy_handle_t, indy_error_t, CPointer<ByteVar>?, CPointer<ByteVar>?) -> Unit>>
typealias MyCallbackWallet2 = CPointer<CFunction<(indy_handle_t, indy_error_t, indy_handle_t) -> Unit>>

class IosIndyTest {

    @Test
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
                xcommand_handle: indy_handle_t,
                err: indy_error_t,
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
            log: CPointer<out CPointed>?,
            elem: indy_u32_t,
            pointer: CPointer<ByteVar>?,
            val1: CPointer<ByteVar>?,
            val2: CPointer<ByteVar>?,
            val3: CPointer<ByteVar>?,
            number: indy_u32_t,
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
            xcommand_handle: indy_handle_t,
            err: indy_error_t,
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
            command: indy_handle_t,
            err: indy_error_t,
            handle: indy_handle_t
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
                xcommand_handle: indy_handle_t,
                err: indy_error_t,
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
            indy_create_and_store_my_did(
                commandHandle,
                wallethandle,
                didJson,
                callback
            )
            sleep(8)
            println(rw.read())


            val data =
                "{\"@type\":\"https://didcomm.org/connections/1.0/request\",\"@id\":\"0b3a5812-08b9-49ea-90fa-8c275eb970bb\",\"label\":\"Holder\",\"imageUrl\":null,\"connection\":{\"DID\":\"UjoxXVTrTEVtxiL3evhJeD\",\"DIDDoc\":{\"@context\":\"https://w3id.org/did/v1\",\"id\":\"UjoxXVTrTEVtxiL3evhJeD\",\"publicKey\":[{\"id\":\"UjoxXVTrTEVtxiL3evhJeD\",\"type\":\"Ed25519VerificationKey2018\",\"controller\":\"UjoxXVTrTEVtxiL3evhJeD\",\"publicKeyBase58\":\"G7reKBYB6ogQoWsa4o8Nz6wbSBzF9ui8DtzNZ7sXTfJW\"}],\"service\":[{\"id\":\"UjoxXVTrTEVtxiL3evhJeD;indy\",\"type\":\"IndyAgent\",\"priority\":0,\"recipientKeys\":[\"G7reKBYB6ogQoWsa4o8Nz6wbSBzF9ui8DtzNZ7sXTfJW\"],\"serviceEndpoint\":\"ws://test81564:8123\"}]}}}"
            val byteArrray = data.toByteArray()
            val recipientVk = "[\"AVGGq5RRPkF7vMqD5niJzbr5y9cwWdGGPYfygJso13GT\"]"
            val senderVk = "G7reKBYB6ogQoWsa4o8Nz6wbSBzF9ui8DtzNZ7sXTfJW"
            val commandHandle = 3

            //{"protected":"eyJlbmMiOiJ4Y2hhY2hhMjBwb2x5MTMwNV9pZXRmIiwidHlwIjoiSldNLzEuMCIsImFsZyI6IkFub25jcnlwdCIsInJlY2lwaWVudHMiOlt7ImVuY3J5cHRlZF9rZXkiOiJuQ3Z4VUlad01rOU5DNEw1SmNPYUwtODBJU1ZRcHRRUnkxTWg3ZTkzWjNxZ05ySFZmR0ExQm9zTWN2d3RtdXBlMHNEaC1LbnZXX2R2RHU2c2dsMUpINnZzNVF4OXhYMVA3ejVhcl9iYkFHdz0iLCJoZWFkZXIiOnsia2lkIjoiSDZhajRWREZLZTFUN01COU1qcGlaUDU3Q1ZaMm1XNkxrb0JFTW41ZGM0QkUifX1dfQ==","iv":"Ld2YxKTXQPV2gPMe","ciphertext":"Z0rjwlbBV1udc-fu0FxFz9wnRK383IuHlOC8tv7_pMMghBtEKwf0YtWh2e_ZWqpFHKiNwEqzBfhUFV17iVrJDyIOM7NTidzwd_wRilchmo-vqEG4T9DdhMsKvdhUP2PfSdQSxDb0Qd39GCVL6OW2ChM0rkE9Yuxh-OFC2DTB0gZUDdWdg69XTVBkL8BKtPu0n5-zAGgmqgaFmP8p92iG3iZS5oIlGc3B_wUink2l0GcVg4VDz-PZq7NLIJDLtDxX7vQ7P-4JAp_YODeACLgM03UjHQVXXzdL4-A7Vynx4zgB-oIxVnUhUeV6q6j6VnB4rBEpPOxXOHdPbn7DyNCzyI3zBkOn4vmlbCWCLReKF_TINbj8IFobJPht8kqlh_17SvDQrPNkALrTOGTT_dZgIiw41AtcMkYYfFMb6QQcS39VTYKK_6N1N81EZnSBydEPke0bgLoA93W9KWYPt1xLvxyvG8YDizXQdoFWXk99nfmVilhG3pzoDPCkyLpGR9kfsi3FlMzqIr_d9cZN7QJ_HPfIgTS1eeZAYLFeVibD12u4ZQIdLZMJeI07mqc5tKnwkzXWUY_FNqHmVlB7CGCo4MEG-wBIUcF-KLh_0lm4sSNzjTID2rc-k_gimhwPUMBm59HPcWRTUYLrpMAlOy3f6DoS5_4Sy2O4ddKSw611lyvV4_AIuWFjoeTN9StCPxYob0DuM6MqZsNNFVQNdUTYXwk9mw6kM9x9Q5lBnf684UdRFjGr4NyhJDdinYEeh0c5MEesDW_P9eJ6wL9U1tHLDXScOSFCCIbdmXVyioOc6skieKVJz15CPVCOXZRGxYGB25WRua7tSGVTXNXcIMs2xA_wz4Xdh8c-7OxC4akpecOgcen5hb_f2XxktyP-rcN4r2G0QuLh-MT3yiAP-I8vnwWWoiqemg2vYUU_cadocJwUl-9N4if1R_20_oFCMBELABRRzAmRcWfoJyH4WajoCHVa-IfMfJAe3Q9ZrhFUD4biVVbJuky3S_bBjp6YBQeti6hAUBXc6W_rZorxn7Fp-U93GDRrZwqhUrG1zzjibW2nbnfm7jYqmeOe6tAH2Q10db2YJDxM0pCspRohSI_vC4lnszsURMIoswPVwTDuBR2GTbQptaZlJ8ZH98GtlS9V2ArHH6A2UZ-UIXf2shhj745c7YvNLGRvMl7dSb8gpC1hvDMb8GjwhlqPzrb0X2xkY3OrbbVs0hJ1JQ5tEQmbvCS02nUn6HR0-RYxM4R3UCNDEaFxd5oXU1Xfv1-OmpHLXClbBSz3TFk-2eLUsipg9xXt9V3WNfLK5PPxXgjorNnv7rZK13sYSMxj3crGIFCbBSZfwPzZqVx-ZX7HPZLw_uFbZa5CK0W-rVb234tNcPS3nQZUu8mnWaw-E2_KuwcvW-rz8fLs1ywWdGaFXsm17LX6zZiyOGACzCOOvlCr-OuOOhPIE1DLMUyr47oT4YV1T16QTwgHnJlSLF53ddB0h_7fwO85Nc1rziX76MGfK236__o48NBIdCiJfTuUeA9wJG73QYGlefDVOi47x6wBcGM_tX744XjENI8tQ5XEEpb_NNlBiKzpmrvaAVDLAVwBYcmZAsph0vYOw4cGlfhTLeFDdEFEAHH5SWTVdpdiJ4mokHJmMQkCu4wIZ41wa9OX3vMpCosygW5tTqhcA9N7lPudyHsrrASXl6d73rRQsi5EhDT-eTuMhdJGI7Th-IvoHL9VvyCuB5UmBgijSJbks3hmbn90Nz0F4POt-d-nXaegakfSKEO9K2vxEYMFvGziweyiY3pgN2OzaTlw5NlGWmysAbuS-8K97LJI6IKFVqyvPGSZLiiIj4d5VVTIBGAxU2kk4RtKRXi9SemQhQQai4FbkQ8mz7LJHbtkFpHuUi5y24PXT3xl53fqqlI3pa1dug5UHZNFHYQIScTf2gTt1QmnG-4iJYmrL7RioIEoUa7BdRHurMgFElqZ__QKq9PEDDoc3z-kItBPAe_a4s1YbE_WpvvbO7QXge0tmbApuqAI4-68ZDSYhoYjIxGeJY8DqtWsSTbOMCZhmnjTjbf2bvISzBFQnPi8S4OyYrxcVMKO0OPow_ZAarY=","tag":"3EoGg7faQj1atxoHqlGxqg=="}
            val packMessageCb: CPointer<CFunction<(indy_handle_t, indy_error_t, CPointer<indy_u8_tVar>?, indy_u32_t) -> Unit>>? =
                staticCFunction(fun(
                    _: indy_handle_t,
                    er: indy_error_t,
                    data: CPointer<indy_u8_tVar>?,
                    un: indy_u32_t
                ) {
                    initRuntimeIfNeeded()
                    //rw2.save(data as ByteArray)
                    var d = data?.readBytes(5)
                    println(d)
                    return
                })
            val result = indy_pack_message(
                commandHandle,
                wallethandle,
                byteArrray.toUByteArray().refTo(0) as CValuesRef<indy_u8_tVar /* = UByteVarOf<UByte> */>?,
                byteArrray.size.toUInt(),
                recipientVk,
                senderVk,
                packMessageCb
            )
            // rw2.read()
            sleep(8)
            if (result.toInt() != 0)
                throw Exception("PackException")
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

    fun reverse(s: String): String {
        val b = s.toByteArray().reversed()
        return String(b.toByteArray())
    }

    fun convertToBase58(hash: String): String {
        val alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
        val sb = StringBuilder()
        for (r in hash) {
            var eq = 0
            for (alpha in alphabet) {
                if (alpha == r) {
                    sb.append(eq % 58)
                }
                eq++
            }
            println(r)
        }
        return reverse(sb.toString())

    }

    @Test
    fun test_indy_base58() {
        val data = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
        for (r in convertToBase58(data)) {
            println("$r ")
        }
    }


    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()


    fun ByteArray.toHex(): String {
        val result = StringBuilder()

        forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS[firstIndex])
            result.append(HEX_CHARS[secondIndex])
        }

        return result.toString()
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
                log: CPointer<out CPointed>?,
                elem: indy_u32_t,
                pointer: CPointer<ByteVar>?,
                val1: CPointer<ByteVar>?,
                val2: CPointer<ByteVar>?,
                val3: CPointer<ByteVar>?,
                number: indy_u32_t,
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
                command: indy_handle_t,
                err: indy_error_t,
                handle: indy_handle_t
            ) {
                initRuntimeIfNeeded()
                println(err)
                println("w1: " + handle)
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
                    handle: CPointer<ByteVar /* = ByteVarOf<Byte> */>?
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
                command: indy_handle_t,
                err: indy_error_t,
                handle: indy_handle_t
            ) {
                initRuntimeIfNeeded()
                println(err)
                println("w1: " + handle)
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
                    command: indy_handle_t,
                    err: indy_error_t,
                    handle: CPointer<ByteVar /* = ByteVarOf<Byte> */>?
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
            println("w1= " + walletId + " k2= " + walletId2)
            println("k1= " + key2 + " k2= " + key4)

            val senderVk = "${key2}"
            var recipientVk = "[\"GJ1SzoWzavQYfNL9XkaJdrQejfztN4XqdsiV4ct3LXKL\",\"${key4}\"]"
            println(recipientVk)

            var hex = "{}".toByteArray().toHex()
            println(hex)
            var ff = "{\"reqId\":1496822211362017764}"
            var s = ff.cstr

            println(senderVk)
            println(recipientVk)

            var transf = s as CValuesRef<indy_u8_tVar /* = UByteVarOf<UByte> */>?

            var size = ff.length.toUInt() as indy_u32_t //29
            println(transf.toString())

            println(size.toString())

            //{"protected":"eyJlbmMiOiJ4Y2hhY2hhMjBwb2x5MTMwNV9pZXRmIiwidHlwIjoiSldNLzEuMCIsImFsZyI6IkFub25jcnlwdCIsInJlY2lwaWVudHMiOlt7ImVuY3J5cHRlZF9rZXkiOiJuQ3Z4VUlad01rOU5DNEw1SmNPYUwtODBJU1ZRcHRRUnkxTWg3ZTkzWjNxZ05ySFZmR0ExQm9zTWN2d3RtdXBlMHNEaC1LbnZXX2R2RHU2c2dsMUpINnZzNVF4OXhYMVA3ejVhcl9iYkFHdz0iLCJoZWFkZXIiOnsia2lkIjoiSDZhajRWREZLZTFUN01COU1qcGlaUDU3Q1ZaMm1XNkxrb0JFTW41ZGM0QkUifX1dfQ==","iv":"Ld2YxKTXQPV2gPMe","ciphertext":"Z0rjwlbBV1udc-fu0FxFz9wnRK383IuHlOC8tv7_pMMghBtEKwf0YtWh2e_ZWqpFHKiNwEqzBfhUFV17iVrJDyIOM7NTidzwd_wRilchmo-vqEG4T9DdhMsKvdhUP2PfSdQSxDb0Qd39GCVL6OW2ChM0rkE9Yuxh-OFC2DTB0gZUDdWdg69XTVBkL8BKtPu0n5-zAGgmqgaFmP8p92iG3iZS5oIlGc3B_wUink2l0GcVg4VDz-PZq7NLIJDLtDxX7vQ7P-4JAp_YODeACLgM03UjHQVXXzdL4-A7Vynx4zgB-oIxVnUhUeV6q6j6VnB4rBEpPOxXOHdPbn7DyNCzyI3zBkOn4vmlbCWCLReKF_TINbj8IFobJPht8kqlh_17SvDQrPNkALrTOGTT_dZgIiw41AtcMkYYfFMb6QQcS39VTYKK_6N1N81EZnSBydEPke0bgLoA93W9KWYPt1xLvxyvG8YDizXQdoFWXk99nfmVilhG3pzoDPCkyLpGR9kfsi3FlMzqIr_d9cZN7QJ_HPfIgTS1eeZAYLFeVibD12u4ZQIdLZMJeI07mqc5tKnwkzXWUY_FNqHmVlB7CGCo4MEG-wBIUcF-KLh_0lm4sSNzjTID2rc-k_gimhwPUMBm59HPcWRTUYLrpMAlOy3f6DoS5_4Sy2O4ddKSw611lyvV4_AIuWFjoeTN9StCPxYob0DuM6MqZsNNFVQNdUTYXwk9mw6kM9x9Q5lBnf684UdRFjGr4NyhJDdinYEeh0c5MEesDW_P9eJ6wL9U1tHLDXScOSFCCIbdmXVyioOc6skieKVJz15CPVCOXZRGxYGB25WRua7tSGVTXNXcIMs2xA_wz4Xdh8c-7OxC4akpecOgcen5hb_f2XxktyP-rcN4r2G0QuLh-MT3yiAP-I8vnwWWoiqemg2vYUU_cadocJwUl-9N4if1R_20_oFCMBELABRRzAmRcWfoJyH4WajoCHVa-IfMfJAe3Q9ZrhFUD4biVVbJuky3S_bBjp6YBQeti6hAUBXc6W_rZorxn7Fp-U93GDRrZwqhUrG1zzjibW2nbnfm7jYqmeOe6tAH2Q10db2YJDxM0pCspRohSI_vC4lnszsURMIoswPVwTDuBR2GTbQptaZlJ8ZH98GtlS9V2ArHH6A2UZ-UIXf2shhj745c7YvNLGRvMl7dSb8gpC1hvDMb8GjwhlqPzrb0X2xkY3OrbbVs0hJ1JQ5tEQmbvCS02nUn6HR0-RYxM4R3UCNDEaFxd5oXU1Xfv1-OmpHLXClbBSz3TFk-2eLUsipg9xXt9V3WNfLK5PPxXgjorNnv7rZK13sYSMxj3crGIFCbBSZfwPzZqVx-ZX7HPZLw_uFbZa5CK0W-rVb234tNcPS3nQZUu8mnWaw-E2_KuwcvW-rz8fLs1ywWdGaFXsm17LX6zZiyOGACzCOOvlCr-OuOOhPIE1DLMUyr47oT4YV1T16QTwgHnJlSLF53ddB0h_7fwO85Nc1rziX76MGfK236__o48NBIdCiJfTuUeA9wJG73QYGlefDVOi47x6wBcGM_tX744XjENI8tQ5XEEpb_NNlBiKzpmrvaAVDLAVwBYcmZAsph0vYOw4cGlfhTLeFDdEFEAHH5SWTVdpdiJ4mokHJmMQkCu4wIZ41wa9OX3vMpCosygW5tTqhcA9N7lPudyHsrrASXl6d73rRQsi5EhDT-eTuMhdJGI7Th-IvoHL9VvyCuB5UmBgijSJbks3hmbn90Nz0F4POt-d-nXaegakfSKEO9K2vxEYMFvGziweyiY3pgN2OzaTlw5NlGWmysAbuS-8K97LJI6IKFVqyvPGSZLiiIj4d5VVTIBGAxU2kk4RtKRXi9SemQhQQai4FbkQ8mz7LJHbtkFpHuUi5y24PXT3xl53fqqlI3pa1dug5UHZNFHYQIScTf2gTt1QmnG-4iJYmrL7RioIEoUa7BdRHurMgFElqZ__QKq9PEDDoc3z-kItBPAe_a4s1YbE_WpvvbO7QXge0tmbApuqAI4-68ZDSYhoYjIxGeJY8DqtWsSTbOMCZhmnjTjbf2bvISzBFQnPi8S4OyYrxcVMKO0OPow_ZAarY=","tag":"3EoGg7faQj1atxoHqlGxqg=="}
            val packMessageCb: CPointer<CFunction<(indy_handle_t, indy_error_t, CPointer<indy_u8_tVar>?, indy_u32_t) -> Unit>>? =
                staticCFunction(fun(
                    _: indy_handle_t,
                    er: indy_error_t,
                    data: CPointer<indy_u8_tVar>?,
                    un: indy_u32_t
                ) {
                    initRuntimeIfNeeded()
                    //rw2.save(data as ByteArray)
                    println(er)
                    println(un)
                    //println(data)
                    //var d = data?.readBytes(un.toInt())
                    //println(d)
                    return
                })
            val result = indy_pack_message(
                9,
                walletId,
                transf,
                size,
                recipientVk,
                senderVk,
                packMessageCb
            )
            // rw2.read()
            sleep(8)
            println(result.toInt())

        }
    }
}




