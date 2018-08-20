package com.example.shawn.anjeonmingug

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service(){
    // 생성될 때 작동
    override fun onCreate() {
        super.onCreate()
    }

    // 생성되기 직전에 작동
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // 서비스가 제거될 때 작동
    override fun onDestroy() {
        super.onDestroy()
    }

}