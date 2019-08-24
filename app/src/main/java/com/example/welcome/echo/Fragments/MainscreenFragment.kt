package com.example.welcome.echo.Fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView

import com.example.welcome.echo.R
import com.example.welcome.echo.Songs
import com.example.welcome.echo.adapters.MainScreenAdapter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class MainscreenFragment : Fragment() {

    var getSongsList: ArrayList<Songs>? = null
    var nowPlayingBottomBar: RelativeLayout? = null
    var visibleLayout: RelativeLayout? = null
    var songTitle: TextView? = null
    var playPauseButton: ImageButton? = null
    var noSongs: RelativeLayout? = null
    var recyclerView: RecyclerView? = null
    var myActivity: Activity? = null
    var _mainscreenAdapter: MainScreenAdapter? = null
    var trackPosition:Int=0

    object Statified{
        var mediaPlayer:MediaPlayer?=null
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_main_screen, container, false)

        setHasOptionsMenu(true)
        activity.title = "All Songs"
        nowPlayingBottomBar = view?.findViewById(R.id.hiddenBarMainScreen)
        noSongs = view?.findViewById(R.id.noSongs)
        playPauseButton = view?.findViewById(R.id.playPauseButton)
        visibleLayout = view?.findViewById(R.id.visibleLayout)
        songTitle = view?.findViewById(R.id.songTitlemainScreen)
        recyclerView = view?.findViewById(R.id.contentMain)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main, menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val switcher = item?.itemId
        if (switcher == R.id.action_sort_ascending) {
            val editor = myActivity?.getSharedPreferences("action_sort", Context.MODE_PRIVATE)?.edit()
            editor?.putString("action_sort_ascending", "true")
            editor?.putString("action_sort_recent", "false")
            editor?.apply()
            if (getSongsList != null) {
                Collections.sort(getSongsList, Songs.Statified.nameComparator)
            }
            _mainscreenAdapter?.notifyDataSetChanged()
            return false
        } else if (switcher == R.id.action_sort_recent) {
            val editor = myActivity?.getSharedPreferences("action_sort", Context.MODE_PRIVATE)?.edit()
            editor?.putString("action_sort_ascending", "false")
            editor?.putString("action_sort_recent", "true")
            editor?.apply()
            if (getSongsList != null) {
                Collections.sort(getSongsList, Songs.Statified.dateComparator)
            }
            _mainscreenAdapter?.notifyDataSetChanged()
            return false
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }



    fun getSongsFromPhone(): ArrayList<Songs> {
        val arrayList = ArrayList<Songs>()
        val contentResolver = myActivity?.contentResolver
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor = contentResolver?.query(songUri, null, null, null, null)
        if (songCursor != null && songCursor.moveToFirst()) {
            val songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songCursor.moveToNext()) {
                val currentId = songCursor.getLong(songId)
                val currentTitle = songCursor.getString(songTitle)
                val currentArtist = songCursor.getString(songArtist)
                val currentData = songCursor.getString(songData)
                val currentDate = songCursor.getLong(dateIndex)
                arrayList.add(Songs(currentId, currentTitle, currentArtist, currentData, currentDate))
            }

        }
        return arrayList
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getSongsList = getSongsFromPhone()
        val prefs = myActivity?.getSharedPreferences("action_sort", Context.MODE_PRIVATE)
        val action_sort_ascending = prefs?.getString("action_sort_ascending", "true")
        val action_sort_recent = prefs?.getString("action_sort_recent", "false")
        if (getSongsList == null) {
            visibleLayout?.visibility = View.INVISIBLE
            noSongs?.visibility = View.VISIBLE
        } else {
            _mainscreenAdapter = MainScreenAdapter(getSongsList as ArrayList<Songs>, myActivity as Context)
            val mLayoutManager = LinearLayoutManager(myActivity)
            recyclerView?.layoutManager = mLayoutManager
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.adapter = _mainscreenAdapter
            recyclerView?.setHasFixedSize(true)
        }

        if (getSongsList != null) {
            if (action_sort_ascending!!.equals("true", true)) {
                Collections.sort(getSongsList, Songs.Statified.nameComparator)
                _mainscreenAdapter?.notifyDataSetChanged()
                bottomBarSetup()
            } else if (action_sort_recent!!.equals("true", true)) {
                Collections.sort(getSongsList, Songs.Statified.dateComparator)
                _mainscreenAdapter?.notifyDataSetChanged()
            }
        }
        bottomBarSetup()
    }

    fun bottomBarSetup(){
        try {

            songTitle?.setText(SongPLayingFragment.Statified.currentSongHelper?.songTitle)
            SongPLayingFragment.Statified.mediaPlayer?.setOnCompletionListener {
                songTitle?.setText(SongPLayingFragment.Statified.currentSongHelper?.songTitle)
                SongPLayingFragment.Staticated.onSongComplete()
            }
                if(SongPLayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                    nowPlayingBottomBar?.visibility=View.VISIBLE
                }else{
                    nowPlayingBottomBar?.visibility=View.INVISIBLE
                }
            bottomBarClickHandler()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun bottomBarClickHandler(){
        nowPlayingBottomBar?.setOnClickListener({

            Statified.mediaPlayer=SongPLayingFragment.Statified.mediaPlayer
            val songPlayingFragement= SongPLayingFragment()
            val args= Bundle()
            args.putString("songArtist",SongPLayingFragment.Statified.currentSongHelper?.songArtist)
            args.putString("songTitle",SongPLayingFragment.Statified.currentSongHelper?.songTitle)
            args.putString("path",SongPLayingFragment.Statified.currentSongHelper?.songPath)
            args.putInt("songId", SongPLayingFragment.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("songPosition",SongPLayingFragment.Statified.currentSongHelper?.currentPosition?.toInt() as Int)
            args.putParcelableArrayList("songData",SongPLayingFragment.Statified.fetchSongs)
            args.putString("MainScreenBottomBar","success")
            songPlayingFragement.arguments=args
            fragmentManager.beginTransaction()
                    .replace(R.id.details_fragment,songPlayingFragement)
                    .addToBackStack("SongPlayingFragement")
                    .commit()
        })

        playPauseButton?.setOnClickListener({
            if(SongPLayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                SongPLayingFragment.Statified.mediaPlayer?.pause()
                trackPosition=SongPLayingFragment.Statified.mediaPlayer?.getCurrentPosition() as Int
                playPauseButton?.setBackgroundResource(R.drawable.play_icon)
            }else{
                SongPLayingFragment.Statified.mediaPlayer?.start()
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
                SongPLayingFragment.Staticated.processInformation(SongPLayingFragment.Statified.mediaPlayer as MediaPlayer)
                SongPLayingFragment.Statified.currentSongHelper?.isPlaying=true
            }
        })

    }

}
