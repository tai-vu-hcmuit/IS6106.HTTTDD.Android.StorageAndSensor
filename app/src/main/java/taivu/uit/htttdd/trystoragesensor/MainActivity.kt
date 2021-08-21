package taivu.uit.htttdd.trystoragesensor

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var rotatedLeftCount: Int = 0;
    var rotatedRightCount: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get count number on Storage
        val counts = this.getCountFromStorage();
        this.rotatedLeftCount = counts[0];
        this.rotatedRightCount = counts[1];

        this.viewCount(this.rotatedLeftCount, this.rotatedRightCount);
    }

    private fun viewCount(leftCount: Int, rightCount: Int) {
        val txtLeftValue = findViewById<TextView>(R.id.rotate_left_value);
        val txtRightValue = findViewById<TextView>(R.id.rotate_right_value);

        txtLeftValue.text = leftCount.toString();
        txtRightValue.text = rightCount.toString();
    }

    private fun getCountFromStorage(): Array<Int> {
        val ds: SharedPreferences = this.getSharedPreferences("rotateDB", MODE_PRIVATE);

        val leftCount = ds.getInt("rotatedLeftCount", 0);
        val rightCount = ds.getInt("rotatedRightCount", 0);

        return arrayOf(leftCount, rightCount);
    }

    private fun saveCountToStorage(leftCount: Int, rightCount: Int) {
        val ds: SharedPreferences.Editor = this.getSharedPreferences("rotateDB", MODE_PRIVATE).edit();

        ds.putInt("rotatedLeftCount", leftCount);
        ds.putInt("rotatedRightCount", rightCount);
        ds.apply();
    }

    fun clearData(view: View) {
        this.rotatedLeftCount += 5;
        this.rotatedRightCount += 3;

        this.saveCountToStorage(this.rotatedLeftCount, this.rotatedRightCount);
        this.viewCount(this.rotatedLeftCount, this.rotatedRightCount);
    }


}