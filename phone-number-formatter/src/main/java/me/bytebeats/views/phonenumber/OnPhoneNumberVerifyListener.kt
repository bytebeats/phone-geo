package me.bytebeats.views.phonenumber

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/30 17:30
 * @Version 1.0
 * @Description To check phone number is valid
 */

interface OnPhoneNumberVerifyListener {
    fun onSuccess(phoneNumber: String)
    fun onFailure()
}