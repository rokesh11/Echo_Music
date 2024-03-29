package com.example.welcome.echo.adapters

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.welcome.echo.Fragments.SongPLayingFragment
import com.example.welcome.echo.R
import com.example.welcome.echo.Songs

/**
 * Created by Welcome on 12/16/2017.
 */
class FavoriteAdapter(_songDetails:ArrayList<Songs>, _context: Context): RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    var songDetails:ArrayList<Songs>?=null
    var mContext: Context?=null

    init {
        songDetails=_songDetails
        mContext=_context
    }
    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val songObject=songDetails?.get(position)
        holder?.trackTitle?.text=songObject?.songTitle
        holder?.trackArtist?.text=songObject?.artist
        holder?.contentHolder?.setOnClickListener ( {
            val songPlayingFragment= SongPLayingFragment()
            val args= Bundle()
            args.putString("songArtist",songObject?.artist)
            args.putString("songTitle",songObject?.songTitle)
            args.putString("path",songObject?.songData)
            args.putInt("songId", songObject?.songId?.toInt() as Int)
            args.putInt("songPosition",position)
            args.putParcelableArrayList("songData",songDetails)
            songPlayingFragment.arguments=args

            (mContext as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.details_fragment,songPlayingFragment)
                    .addToBackStack("SongPlayingFragmentFavorite")
                    .commit()
        } )
    }

    override fun getItemCount(): Int {
        if(songDetails==null){
            return 0
        } else{
            return(songDetails as ArrayList<Songs>).size
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_mainscreenadapter,parent,false)

        return MyViewHolder(itemView)
    }

    class MyViewHolder(view : View): RecyclerView.ViewHolder(view) {
        var trackTitle: TextView?=null
        var trackArtist: TextView?=null
        var contentHolder: RelativeLayout?=null
        init {
            trackTitle=view.findViewById<TextView>(R.id.trackTitle)
            trackArtist=view.findViewById<TextView>(R.id.trackArtist)
            contentHolder=view.findViewById<RelativeLayout>(R.id.contentRow)
        }
    }
}