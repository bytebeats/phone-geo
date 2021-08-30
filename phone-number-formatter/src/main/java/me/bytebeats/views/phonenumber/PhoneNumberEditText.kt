package me.bytebeats.views.phonenumber

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/30 16:51
 * @Version 1.0
 * @Description TO-DO
 */

class PhoneNumberEditText : AppCompatEditText {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    var phoneNumberVerifyListener: OnPhoneNumberVerifyListener? = null
        get() = mTextWatcher.phoneNumberVerifyListener
        set(value) {
            field = value
            mTextWatcher.phoneNumberVerifyListener = field
        }

    private val mTextWatcher by lazy { PhoneNumberTextWatcher(this) }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhoneNumberEditText, defStyleAttr, 0)
        val splitterIdx = typedArray.getInt(R.styleable.PhoneNumberEditText_splitter, 0)
        mTextWatcher.splitter = Splitter.values()[splitterIdx]
        typedArray.recycle()
        addTextChangedListener(mTextWatcher)
        isFocusable = true
        isEnabled = true
        isFocusableInTouchMode = true
    }

    val splitter: Splitter
        get() = mTextWatcher.splitter

    fun trimmedPhoneNumber(): String? = PhoneNumberTextWatcher.trimmedPhoneNumber(this)

    fun verify(onVerifyResultListener: OnPhoneNumberVerifyListener?) {
        trimmedPhoneNumber()?.let {
            if (PhoneNumberTextWatcher.validPhoneNumber(it)) {
                onVerifyResultListener?.onSuccess(it)
            } else {
                onVerifyResultListener?.onFailure()
            }
        }
    }
}