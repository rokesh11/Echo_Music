package com.example.welcome.echo.activities

import android.annotation.SuppressLint
import android.graphics.drawable.RippleDrawable
import android.app.Notification
import android.view.WindowInsets
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.welcome.echo.R
import com.example.welcome.echo.Fragments.MainscreenFragment
import com.example.welcome.echo.Fragments.SongPLayingFragment
import java.util.*

import com.example.welcome.echo.adapters.NavigationAdapter

class MainActivity : AppCompatActivity(){

    object Statified {
        var drawerLayout: DrawerLayout? = null
        var notificationManager:NotificationManager?=null
    }
    var navdrawer_contentList:ArrayList<String> = arrayListOf()
    var getImage_for_navdrawer= intArrayOf(R.drawable.navigation_allsongs,R.drawable.navigation_favorites,
            R.drawable.navigation_settings,R.drawable.navigation_aboutus)
    var trackNotificationBuilder:Notification?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        navdrawer_contentList.add("All Songs")
        navdrawer_contentList.add("Favorites")
        navdrawer_contentList.add("Settings")
        navdrawer_contentList.add("About Us")

        val navigationAdapter=NavigationAdapter(navdrawer_contentList,getImage_for_navdrawer,this)
        navigationAdapter.notifyDataSetChanged()

        Statified.drawerLayout=findViewById(R.id.drawer_layout)

        val toggle=ActionBarDrawerToggle(this@MainActivity,Statified.drawerLayout,toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)

       Statified.drawerLayout?.setDrawerListener(toggle)
        toggle.syncState()

        val mainScreenFragment= MainscreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment,mainScreenFragment,"MainScreenFragment")
                .commit()

        val navigation_recyclerView=findViewById<RecyclerView>(R.id.navigation_recycler_view)
        navigation_recyclerView.layoutManager=LinearLayoutManager(this)
        navigation_recyclerView.itemAnimator=DefaultItemAnimator()
        navigation_recyclerView.adapter=navigationAdapter
        navigation_recyclerView.setHasFixedSize(true)

        val intent=Intent(this@MainActivity,MainActivity::class.java)
        val pIntent=PendingIntent.getActivity(this@MainActivity,System.currentTimeMillis().toInt(),intent,0)

        trackNotificationBuilder=Notification.Builder(this)
                .setContentTitle("A track is playing in background")
                .setSmallIcon(R.drawable.echo_logo)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()

        Statified.notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStart() {
        super.onStart()
        try {
            Statified.notificationManager?.cancel(1129)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if (SongPLayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                Statified.notificationManager?.notify(1129,trackNotificationBuilder)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Statified.notificationManager?.cancel(1129)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}

