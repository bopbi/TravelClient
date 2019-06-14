package com.dianpesonawisata.android.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dianpesonawisata.android.constant.IntentConstant.Companion.GROUPED_TOUR_ID
import com.dianpesonawisata.android.extension.setContentFragment

class GroupActivity : AppCompatActivity() {

    companion object {
        fun open(context: Context, groupedTourId: Int) {
            val intent = Intent(context, GroupActivity::class.java)
            intent.putExtra(GROUPED_TOUR_ID, groupedTourId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val groupFragment = GroupFragment()
        groupFragment.arguments = intent.extras
        setContentFragment(savedInstanceState, groupFragment)
    }
}
