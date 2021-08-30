package me.bytebeats.phonegeo.algo

import me.bytebeats.phonegeo.data.PhoneNumberInfo
import java.nio.ByteOrder

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/30 10:32
 * @Version 1.0
 * @Description Binary Search to look up data location in phone.dat file
 */

class ProspectBinarySearchAlgorithm(data: ByteArray) : BinarySearchAlgorithm(data) {
    override fun lookup(phoneNumber: String): PhoneNumberInfo? {
        if (!validPhoneNumber(phoneNumber)) {
            return null
        }
        val byteBuffer = srcByteBuffer.asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN)
        val geoId = try {
            phoneNumber.substring(0, 7).toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("phone number %s is invalid, is it numeric".format(phoneNumber))
        }
        var left = indicesStartOffset
        var right = indicesEndOffset
        val geoIdPrefix = geoId / 100_000
        var mid = indicesStartOffset + ((indicesEndOffset - indicesStartOffset) / 7 * (geoIdPrefix - 13))
        mid = alignPosition(mid)
        while (mid in left..right) {
            if (mid == right) return null
            val compare = compare(mid, geoId, byteBuffer)
            when {
                compare == 0 -> return extract(phoneNumber, mid, byteBuffer)
                mid == left -> return null
                compare > 0 -> {
                    val tmp = (left + mid) / 2
                    right = mid
                    mid = alignPosition(tmp)
                }
                else -> {
                    val tmp = (mid + right) / 2
                    left = mid
                    mid = alignPosition(tmp)
                }
            }
        }
        return null
    }
}