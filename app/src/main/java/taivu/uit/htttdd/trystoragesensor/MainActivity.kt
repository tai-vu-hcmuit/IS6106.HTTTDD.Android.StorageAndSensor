package taivu.uit.htttdd.trystoragesensor

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import java.math.RoundingMode

class MainActivity : AppCompatActivity(), SensorEventListener {
    var accelerateX: Float = 0f;
    var accelerateY: Float = 0f;
    var accelerateZ: Float = 0f;
    private lateinit var mSensorManager : SensorManager
    private var mAccelerator: Sensor ?= null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get count number on Storage
        val lastValues = this.getAcceleraterValuesFromStorage();
        this.viewLastValues(lastValues[0], lastValues[1], lastValues[2]);

        this.mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.mAccelerator = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null) {
            return
        }

        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val txtX = findViewById<TextView>(R.id.current_x);
            val txtY = findViewById<TextView>(R.id.current_y);
            val txtZ = findViewById<TextView>(R.id.current_z);

            this.accelerateX = event.values[0].toBigDecimal().setScale(3, RoundingMode.FLOOR).toFloat();
            this.accelerateY = event.values[1].toBigDecimal().setScale(3, RoundingMode.FLOOR).toFloat();
            this.accelerateZ = event.values[2].toBigDecimal().setScale(3, RoundingMode.FLOOR).toFloat();

            txtX.text = this.accelerateX.toString();
            txtY.text = this.accelerateY.toString();
            txtZ.text = this.accelerateZ.toString();
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.e("Test onAccuracyChanged", "Accuracy changed")
    }

    override fun onResume() {
        super.onResume()

        this.mSensorManager.registerListener(this, this.mAccelerator, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        this.mSensorManager.unregisterListener(this)
    }

    private fun viewLastValues(x: Float, y: Float, z: Float) {
        val txtX = findViewById<TextView>(R.id.accelerate_x);
        val txtY = findViewById<TextView>(R.id.accelerate_y);
        val txtZ = findViewById<TextView>(R.id.accelerate_z);

        txtX.text = x.toString();
        txtY.text = y.toString();
        txtZ.text = z.toString();
    }

    private fun getAcceleraterValuesFromStorage(): Array<Float> {
        val ds: SharedPreferences = this.getSharedPreferences("rotateDB", MODE_PRIVATE);

        val x = ds.getFloat("accelerateX", 0f);
        val y = ds.getFloat("accelerateY", 0f);
        val z = ds.getFloat("accelerateZ", 0f);

        return arrayOf(x, y, z);
    }

    private fun saveCountToStorage(x: Float, y: Float, z: Float) {
        val ds: SharedPreferences.Editor = this.getSharedPreferences("rotateDB", MODE_PRIVATE).edit();

        ds.putFloat("accelerateX", x);
        ds.putFloat("accelerateY", y);
        ds.putFloat("accelerateZ", z);
        ds.apply();
    }

    fun saveDataToStorage(view: View) {
        this.saveCountToStorage(this.accelerateX, this.accelerateY, this.accelerateZ);
        this.viewLastValues(this.accelerateX, this.accelerateY, this.accelerateZ);
    }


}