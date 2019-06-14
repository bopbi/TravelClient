package com.dianpesonawisata.android.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dianpesonawisata.android.R
import com.dianpesonawisata.android.contact.ContactFragment
import com.dianpesonawisata.android.home.HomeFragment
import com.dianpesonawisata.android.notification.NotificationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                viewpager.currentItem = 0
                title = getString(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_contact -> {
                viewpager.currentItem = 1
                title = getString(R.string.title_contact)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                viewpager.currentItem = 2
                title = getString(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.title_home)
        viewpager.adapter = object : FragmentStateAdapter(this.supportFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> {
                        HomeFragment()
                    }
                    1 -> {
                        ContactFragment()
                    }
                    2 -> {
                        NotificationFragment()
                    }
                    else -> {
                        Fragment()
                    }
                }
            }

            override fun getItemCount(): Int {
                return 3
            }

        }

        viewpager.isUserInputEnabled = false

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
