package me.bytebeats.phonegeo.data

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/30 10:57
 * @Version 1.0
 * @Description 手机号码归属地信息
 */

data class PhoneGeoInfo(val province: String, val city: String, val zipCode: String, val areaCode: String)
