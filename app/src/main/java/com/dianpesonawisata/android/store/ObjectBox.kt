package com.dianpesonawisata.android.store

import android.content.Context
import io.objectbox.BoxStore

class ObjectBox {

    companion object {

        private lateinit var boxStore: BoxStore

        fun init(context: Context) {
            boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
        }

        fun get(): BoxStore {
            return boxStore
        }
    }
}
