package me.bytebeats.phonegeo.data

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/30 10:25
 * @Version 1.0
 * @Description 手机号码详细信息
 */

data class PhoneNumberInfo(val number: String, val geoInfo: PhoneGeoInfo, val isp: ISP)
