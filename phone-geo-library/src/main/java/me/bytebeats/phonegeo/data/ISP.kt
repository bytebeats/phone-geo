package me.bytebeats.phonegeo.data

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/29 10:57
 * @Version 1.0
 * @Description ISP, 互联网服务提供商
 */

enum class ISP(val carrier: String, val code: Int) {
    CHINA_MOBILE("中国移动", 1),
    CHINA_UNICOM("中国联通", 2),
    CHINA_TELECOM("中国电信", 3),
    CHINA_UNICOM_VIRTUAL("中国联通虚拟运营商", 4),
    CHINA_TELECOM_VIRTUAL("中国电信虚拟运营商", 5),
    CHINA_MOBILE_VIRTUAL("中国移动虚拟运营商", 6),
    UNKNOWN("未知", -1);

    companion object {
        fun of(ispMark: Int): ISP {
            return values().firstOrNull { it.code == ispMark } ?: UNKNOWN
        }
    }
}