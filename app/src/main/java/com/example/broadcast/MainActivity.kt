package com.example.broadcast

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    private val localBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    private var count = 0

    private val receiver = object : MyReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == MyService.ACTION_LOADED) {
                val percent = intent.getIntExtra(MyService.EXTRA_PERCENT, 0)
                findViewById<ProgressBar>(R.id.progressBar).progress = percent
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            Intent(MyReceiver.ACTION_CLICKED).apply {
                putExtra(MyReceiver.COUNT, ++count)
                localBroadcastManager.sendBroadcast(this)
            }
        }

        IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(MyReceiver.ACTION_CLICKED)
            addAction(MyService.ACTION_LOADED)
            localBroadcastManager.registerReceiver(receiver, this)
        }


        Intent(this, MyService::class.java).apply {
            startService(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(receiver)
    }
}