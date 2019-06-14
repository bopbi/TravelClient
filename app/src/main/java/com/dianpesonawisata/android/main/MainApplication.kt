package com.dianpesonawisata.android.main

import android.app.Application
import com.dianpesonawisata.android.store.ObjectBox

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}
