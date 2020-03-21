package hu.bme.onlab.mybrowser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timer().schedule(500) {
            val intent = Intent(applicationContext,WebViewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
