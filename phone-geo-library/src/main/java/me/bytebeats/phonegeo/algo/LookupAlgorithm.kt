package me.bytebeats.phonegeo.algo

import me.bytebeats.phonegeo.data.PhoneNumberInfo
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/29 11:12
 * @Version 1.0
 * @Description the algorithm of looking up phone number
 */

abstract class LookupAlgorithm(val data: ByteArray) {
    protected var srcByteBuffer: ByteBuffer = ByteBuffer.wrap(data).asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN)
    protected var indicesStartOffset = 0
    protected var indicesEndOffset = 0

    init {
        val dataVersion = srcByteBuffer.int
        indicesStartOffset = srcByteBuffer.getInt(4)
        indicesEndOffset = srcByteBuffer.capacity()
    }

    abstract fun lookup(phoneNumber: String): PhoneNumberInfo?

    protected fun validPhoneNumber(phoneNumber: String?): Boolean {
        if (phoneNumber?.length !in 7..11) {
            throw IllegalArgumentException(
                "Phone number %s is not acceptable. it's length should be between 7 and 11, both included. but actually it's %s".format(
                    phoneNumber,
                    phoneNumber?.length
                )
            )
        }
        return true
    }

    enum class IMPL {
        BINARY_SEARCH, SEQUENCE, BINARY_SEARCH_PROSPECT, BINARY_SEARCH_ANOTHER;
    }
}