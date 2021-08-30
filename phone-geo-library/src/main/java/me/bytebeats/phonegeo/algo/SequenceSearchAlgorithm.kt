package me.bytebeats.phonegeo.algo

import me.bytebeats.phonegeo.data.ISP
import me.bytebeats.phonegeo.data.PhoneGeoInfo
import me.bytebeats.phonegeo.data.PhoneNumberInfo
import java.nio.ByteOrder

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/30 10:45
 * @Version 1.0
 * @Description Sequence Search to look up data location in phone.dat file
 */

class SequenceSearchAlgorithm(data: ByteArray) : LookupAlgorithm(data) {
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
        for (i in indicesStartOffset until byteBuffer.limit() step 8 + 1) {
            byteBuffer.position(i)
            val phonePrefix = byteBuffer.int
            val indicesPhoneStart = byteBuffer.int
            val ispCode = byteBuffer.get()
            if (phonePrefix == geoId) {
                val isp = ISP.of(ispCode.toInt())
                byteBuffer.position(indicesPhoneStart)
                while (byteBuffer.get() != 0.toByte()) {
                }
                val indicesPhoneEnd = byteBuffer.position() - 1
                byteBuffer.position(indicesPhoneStart)
                val length = indicesPhoneEnd - indicesPhoneStart
                val bytes = ByteArray(length)
                byteBuffer.get(bytes, 0, length)
                val oriString = String(bytes)
                val parts = oriString.split("|")
                val phoneGeoInfo = PhoneGeoInfo(parts[0], parts[1], parts[2], parts[3])
                return PhoneNumberInfo(phoneNumber, phoneGeoInfo, isp)
            }
        }
        return null
    }
}