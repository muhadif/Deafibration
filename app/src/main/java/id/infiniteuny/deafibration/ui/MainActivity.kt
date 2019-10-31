package id.infiniteuny.deafibration.ui

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import id.infiniteuny.deafibration.utils.audio.calculators.AudioCalculator
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import id.infiniteuny.deafibration.R
import id.infiniteuny.deafibration.utils.audio.core.AudioCallback
import id.infiniteuny.deafibration.utils.audio.core.AudioRecorder
import id.infiniteuny.deafibration.utils.dialog.WarningDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Timestamp
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.*


const val addSec = 5

class MainActivity : AppCompatActivity() {

    private var recorder: AudioRecorder? = null
    private var audioCalculator: AudioCalculator? = null
    private var handler: Handler? = null
    lateinit var progressDialogContent : WarningDialog
    private var progressDialog: AlertDialog? = null
    private var dimissDialogTimestamp: Timestamp? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        progressDialogContent = WarningDialog(this, R.style.WarningProgressDialog)

        recorder = AudioRecorder(callback)
        audioCalculator = AudioCalculator()

        handler = Handler(Looper.getMainLooper())

        switch_mode.setOnCheckedChangeListener { view, isChecked ->
            when(isChecked){
                true -> {
                    recorder!!.start()
                }
                false -> {
                    recorder!!.stop()
                }
            }
        }

    }

    private val callback = object : AudioCallback {

        override fun onBufferAvailable(buffer: ByteArray) {
            audioCalculator!!.setBytes(buffer)
            val amplitude = audioCalculator!!.amplitude
            val decibel = audioCalculator!!.decibel
            val frequency = audioCalculator!!.frequency

            val amp = "$amplitude"
            val db = "$decibel"
            val hz = "$frequency"

            handler!!.post(Runnable {
                if(amp.toDouble() > 1000
                    && amp.toDouble() < 3000
                    && hz.toDouble() > 500
                    && hz.toDouble() < 3700
                    && db.toDouble() < -5){

                    if (progressDialog == null) {
                        val progressDialog = progressDialogContent.create()
                        this@MainActivity.progressDialog = progressDialog
                    }

                    progressDialogContent.setBottom(R.drawable.ic_sound_bottom)
                    progressDialogContent.setMessage("Ada Klakson")

                    this@MainActivity.progressDialog!!.show()
                    val startTimestamp = Timestamp(System.currentTimeMillis())

                    val cal = Calendar.getInstance()
                    cal.setTimeInMillis(startTimestamp.getTime())
                    cal.add(Calendar.SECOND, addSec)
                    dimissDialogTimestamp = Timestamp(cal.getTime().getTime())

                    Log.d("AUDIOAMP", amp)
                    Log.d("AUDIODB", db)
                    Log.d("Freq", hz)
                }


                dimissDialogTimestamp?.let {
                    val timestamp = Timestamp(System.currentTimeMillis())
                    if(timestamp.time > it!!.time){
                        progressDialog!!.dismiss()
                        dimissDialogTimestamp = null
                    }

                }


            })
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }
}
