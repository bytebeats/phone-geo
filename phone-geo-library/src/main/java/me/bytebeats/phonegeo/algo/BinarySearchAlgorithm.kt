package me.bytebeats.phonegeo.algo

import me.bytebeats.phonegeo.data.ISP
import me.bytebeats.phonegeo.data.PhoneGeoInfo
import me.bytebeats.phonegeo.data.PhoneNumberInfo
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/29 11:32
 * @Version 1.0
 * @Description TO-DO
 */

class BinarySearchAlgorithm(data: ByteArray) : LookupAlgorithm(data) {
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
        var mid = indicesStartOffset + (indicesEndOffset - indicesStartOffset) / 2
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

    private fun alignPosition(position: Int): Int {
        val remain = (position - indicesStartOffset) % 9
        return if ((position - indicesStartOffset) < 9) {
            position - indicesStartOffset
        } else if (remain != 0) {
            position + 9 - remain
        } else {
            position
        }
    }

    private fun compare(position: Int, key: Int, byteBuffer: ByteBuffer): Int {
        byteBuffer.position(position)
        val phoneNumberPrefix = byteBuffer.int
        return phoneNumberPrefix.compareTo(key)
    }

    private fun parseGeo(src: String): PhoneGeoInfo {
        val geos = src.split("|")
        if (geos.size < 4) {
            throw IllegalStateException("Content format error")
        }
        return PhoneGeoInfo(geos[0], geos[1], geos[2], geos[3])
    }

    private fun detectInfoLength(infoStartIndex: Int, byteBuffer: ByteBuffer): Int {
        byteBuffer.position(infoStartIndex)
        while (byteBuffer.get() != 0.toByte()) {

        }
        val infoEndIndex = byteBuffer.position() - 1
        byteBuffer.position(infoStartIndex)
        return infoEndIndex - infoStartIndex
    }

    private fun extract(phoneNumber: String, start: Int, byteBuffer: ByteBuffer): PhoneNumberInfo? {
        byteBuffer.position(start)
        val prefix = byteBuffer.int
        val infoStartIndex = byteBuffer.int
        val ispMark = byteBuffer.get()

        val isp = ISP.of(ispMark.toInt())
        val bytes = ByteArray(detectInfoLength(infoStartIndex, byteBuffer))
        byteBuffer.get(bytes)
        val geoString = String(bytes)
        val geoInfo = parseGeo(geoString)
        return PhoneNumberInfo(phoneNumber, geoInfo, isp)
    }
}