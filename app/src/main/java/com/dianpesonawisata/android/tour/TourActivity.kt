package com.dianpesonawisata.android.tour

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dianpesonawisata.android.constant.IntentConstant.Companion.TOUR_ID
import com.dianpesonawisata.android.extension.setContentFragment

class TourActivity: AppCompatActivity() {

    companion object {
        fun open(context: Context, tourId: Int) {
            val intent = Intent(context, TourActivity::class.java)
            intent.putExtra(TOUR_ID, tourId)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val tourFragment = TourFragment()
        tourFragment.arguments = intent.extras
        setContentFragment(savedInstanceState, tourFragment)
    }
}
