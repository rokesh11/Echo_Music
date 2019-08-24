package com.example.welcome.echo.utils

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.example.welcome.echo.Fragments.SongPLayingFragment
import com.example.welcome.echo.R
import com.example.welcome.echo.activities.MainActivity

/**
 * Created by Welcome on 12/16/2017.
 */
class CaptureBroadcast:BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action==Intent.ACTION_NEW_OUTGOING_CALL) {
            try {
                MainActivity.Statified.notificationManager?.cancel(1129)
            }catch (e:Exception){
                e.printStackTrace()
            }
            try {
                if (SongPLayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                    SongPLayingFragment.Statified.mediaPlayer?.pause()
                    SongPLayingFragment.Statified.playPauseButton?.setBackgroundResource(R.drawable.play_icon)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }else{
            var tm:TelephonyManager=p0?.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            when(tm.callState){
                TelephonyManager.CALL_STATE_RINGING->{
                    try {
                        MainActivity.Statified.notificationManager?.cancel(1129)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    try {
                        if (SongPLayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                            SongPLayingFragment.Statified.mediaPlayer?.pause()
                            SongPLayingFragment.Statified.playPauseButton?.setBackgroundResource(R.drawable.play_icon)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                else->{

                }
            }
        }
    }

}