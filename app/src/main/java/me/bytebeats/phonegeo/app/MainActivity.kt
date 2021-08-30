package me.bytebeats.phonegeo.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import me.bytebeats.phonegeo.PhoneNumberLookup
import me.bytebeats.phonegeo.algo.LookupAlgorithm

class MainActivity : AppCompatActivity() {
    private val editor by lazy { findViewById<EditText>(R.id.editor) }
    private val province by lazy { findViewById<TextView>(R.id.province) }
    private val city by lazy { findViewById<TextView>(R.id.city) }
    private val zipCode by lazy { findViewById<TextView>(R.id.zip_code) }
    private val areaCode by lazy { findViewById<TextView>(R.id.area_code) }
    private val ispText by lazy { findViewById<TextView>(R.id.isp) }
    private val lookup by lazy { findViewById<Button>(R.id.btn_look_up) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lookup.setOnClickListener {
            PhoneNumberLookup.instance().with(LookupAlgorithm.IMPL.BINARY_SEARCH).lookup(editor.text.toString())
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
    }

    companion object {
        private const val UNKNOWN = "Unknown"
    }
}