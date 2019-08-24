package com.example.welcome.echo.adapters

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.welcome.echo.Fragments.MainscreenFragment
import com.example.welcome.echo.Fragments.SongPLayingFragment
import com.example.welcome.echo.R
import com.example.welcome.echo.Songs

/**
 * Created by Welcome on 12/16/2017.
 */
class MainScreenAdapter(_SongList: ArrayList<Songs>, _context:Context):RecyclerView.Adapter<MainScreenAdapter.MyViewHolder>(){
    var songDetails:ArrayList<Songs>?=null
    var mcontext:Context?=null

    var mediaplayer:MediaPlayer?=null
    init {
        this.songDetails=_SongList
        this.mcontext=_context
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val songObject=songDetails?.get(position)
        holder.trackTitle?.text=songObject?.songTitle
        holder.trackArtist?.text=songObject?.artist
        holder.contentHolder?.setOnClickListener({

            try{
                mediaplayer=SongPLayingFragment.Statified.mediaPlayer
                if(mediaplayer?.isPlaying as Boolean) {
                    mediaplayer?.stop()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

            val songPlayingFragment=SongPLayingFragment()
            val args=Bundle()
            args.putString("songArtist",songObject?.artist)
            args.putString("songTitle",songObject?.songTitle)
            args.putString("path",songObject?.songData)
            args.putInt("songId",songObject?.songId?.toInt() as Int)
            args.putInt("songPosition",position)
            args.putParcelableArrayList("songData",songDetails)
            songPlayingFragment.arguments=args
                (mcontext as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.details_fragment,songPlayingFragment)
                    .addToBackStack("SongPlayingFragment")
                    .commit()

        })

    }

    override fun getItemCount(): Int {
        if (songDetails==null){
            return 0
        }else{
            return (songDetails as ArrayList<Songs>).size
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder{
        val itemView=LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_mainscreenadapter,parent,false)

        return MyViewHolder(itemView)

    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var trackTitle:TextView?=null
        var trackArtist:TextView?=null
        var contentHolder:RelativeLayout?=null
        init {

            trackTitle=itemView?.findViewById(R.id.trackTitle)
            trackArtist=itemView?.findViewById(R.id.trackArtist)
            contentHolder=itemView?.findViewById(R.id.contentRow)
        }
    }

}


