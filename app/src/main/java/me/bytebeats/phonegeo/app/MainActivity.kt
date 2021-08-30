package me.bytebeats.phonegeo.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import me.bytebeats.phonegeo.PhoneNumberLookup
import me.bytebeats.phonegeo.algo.LookupAlgorithm
import me.bytebeats.views.phonenumber.OnPhoneNumberVerifyListener
import me.bytebeats.views.phonenumber.PhoneNumberEditText
import me.bytebeats.views.phonenumber.PhoneNumberTextWatcher

class MainActivity : AppCompatActivity() {
    private val editor by lazy { findViewById<PhoneNumberEditText>(R.id.editor) }
    private val editor2 by lazy { findViewById<PhoneNumberEditText>(R.id.editor_2) }
    private val editor3 by lazy {
        findViewById<EditText>(R.id.editor_3)
    }
    private val textWatcher by lazy { PhoneNumberTextWatcher(editor3) }
    private val province by lazy { findViewById<TextView>(R.id.province) }
    private val city by lazy { findViewById<TextView>(R.id.city) }
    private val zipCode by lazy { findViewById<TextView>(R.id.zip_code) }
    private val areaCode by lazy { findViewById<TextView>(R.id.area_code) }
    private val ispText by lazy { findViewById<TextView>(R.id.isp) }
    private val lookup1 by lazy { findViewById<Button>(R.id.btn_look_up_1) }
    private val lookup2 by lazy { findViewById<Button>(R.id.btn_look_up_2) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editor.phoneNumberVerifyListener = object : OnPhoneNumberVerifyListener {
            override fun onSuccess(phoneNumber: String) {
                geo(phoneNumber)
            }

            override fun onFailure() {
                onFail()
            }
        }
        lookup1.setOnClickListener {
            editor2.verify(object : OnPhoneNumberVerifyListener {
                override fun onSuccess(phoneNumber: String) {
                    geo(phoneNumber)
                }

                override fun onFailure() {
                    onFail()
                }
            })
        }
        editor3.addTextChangedListener(textWatcher)
        lookup2.setOnClickListener {
            PhoneNumberTextWatcher.trimmedPhoneNumber(editor3, textWatcher.splitter)?.let {
                geo(it)
            }
        }
    }

    private fun geo(phoneNumber: String) {
        PhoneNumberLookup.instance().with(LookupAlgorithm.IMPL.SEQUENCE).lookup(phoneNumber)
            ?.apply {
                province.text = geoInfo.province
                city.text = geoInfo.city
                zipCode.text = geoInfo.zipCode
                areaCode.text = geoInfo.areaCode
                ispText.text = isp.carrier
            } ?: kotlin.run {
            province.text = UNKNOWN
            city.text = UNKNOWN
            zipCode.text = UNKNOWN
            areaCode.text = UNKNOWN
            ispText.text = UNKNOWN
        }
    }

    private fun onFail() {
        province.text = UNKNOWN
        city.text = UNKNOWN
        zipCode.text = UNKNOWN
        areaCode.text = UNKNOWN
        ispText.text = UNKNOWN
    }

    companion object {
        private const val UNKNOWN = "Unknown"
    }
}