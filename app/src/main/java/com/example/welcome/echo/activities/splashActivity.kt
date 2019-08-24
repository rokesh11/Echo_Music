package com.example.welcome.echo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.welcome.echo.R

class splashActivity : AppCompatActivity() {

    var permissionsString = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                              Manifest.permission.RECORD_AUDIO,
                              Manifest.permission.PROCESS_OUTGOING_CALLS,
                              Manifest.permission.MODIFY_AUDIO_SETTINGS,
                              Manifest.permission.READ_PHONE_STATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(!hasPermissions(this@splashActivity, *permissionsString)){
            ActivityCompat.requestPermissions(this@splashActivity,permissionsString,113)

        }else{
            splashScreenStart()
        }

    }

    fun splashScreenStart() {
        Handler().postDelayed({
            val startAct=Intent(this@splashActivity, MainActivity::class.java)
            startActivity(startAct)
            this.finish()
        },1000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            113->{
                if(grantResults.isNotEmpty() &&  grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                                                 grantResults[1]==PackageManager.PERMISSION_GRANTED &&
                                                 grantResults[2]==PackageManager.PERMISSION_GRANTED &&
                                                 grantResults[3]==PackageManager.PERMISSION_GRANTED &&
                                                 grantResults[4]==PackageManager.PERMISSION_GRANTED){
                    splashScreenStart()
                }else{
                    Toast.makeText(this@splashActivity,"Please grant all permissions",Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                return
            }
            else->{
                Toast.makeText(this@splashActivity,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                this.finish()
                return
            }
        }
    }

    fun hasPermissions(context:Context,vararg permissions:String):Boolean{
        var hasAllPermission=true
        for(permission in permissions){
            val res=context.checkCallingOrSelfPermission(permission)
            if (res!=PackageManager.PERMISSION_GRANTED){
                hasAllPermission=false
            }
        }
        return hasAllPermission
    }

}
