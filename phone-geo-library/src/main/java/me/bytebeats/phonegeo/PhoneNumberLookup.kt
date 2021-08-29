package me.bytebeats.phonegeo

import me.bytebeats.phonegeo.algo.BinarySearchAlgorithm
import me.bytebeats.phonegeo.algo.LookupAlgorithm
import me.bytebeats.phonegeo.data.PhoneNumberInfo
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/29 11:09
 * @Version 1.0
 * @Description TO-DO
 */

class PhoneNumberLookup private constructor() {
    private var srcPhoneBytes: ByteArray? = null
    private var algoType: LookupAlgorithm.IMPL = LookupAlgorithm.IMPL.BINARY_SEARCH
    private val algorithmCache = mutableMapOf<LookupAlgorithm.IMPL, WeakReference<LookupAlgorithm>?>()

    init {
        try {
            this.javaClass.classLoader?.getResourceAsStream(PHONE_GEO_DAT)?.use { geoStream ->
                ByteArrayOutputStream().use { baos ->
                    val buffer = ByteArray(1024 * 4)
                    var n = geoStream.read(buffer)
                    while (-1 != n) {
                        baos.write(buffer, 0, n)
                        n = geoStream.read(buffer)
                    }
                    srcPhoneBytes = baos.toByteArray()
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to load phone.dat")
        }
    }

    fun with(algorithm: LookupAlgorithm.IMPL): PhoneNumberLookup {
        this.algoType = algorithm
        return this
    }

    fun lookup(phoneNumber: String): PhoneNumberInfo? {
        if (srcPhoneBytes == null) {
            throw IllegalStateException("Something happens when loading phone.dat")
        }
        if (algorithmCache[algoType]?.get() == null) {
            algorithmCache[algoType] = WeakReference(
                when (algoType) {
                    LookupAlgorithm.IMPL.BINARY_SEARCH -> BinarySearchAlgorithm(srcPhoneBytes!!)
                    else -> BinarySearchAlgorithm(srcPhoneBytes!!)
                }
            )
        }
        return algorithmCache[algoType]?.get()?.lookup(phoneNumber)
    }

    companion object {
        private const val PHONE_GEO_DAT = "phone-geo/phone.dat"

        private var INSTANCE: PhoneNumberLookup? = null
        fun instance(): PhoneNumberLookup {
            if (INSTANCE == null) {
                synchronized(PhoneNumberLookup::class) {
                    if (INSTANCE == null) {
                        INSTANCE = PhoneNumberLookup()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}