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
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.welcome.echo.R
import com.example.welcome.echo.Songs
import com.example.welcome.echo.adapters.FavoriteAdapter
import com.example.welcome.echo.databases.EchoDatabase


/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {

    var myActivity: Activity?=null

    var noFavorites: TextView?=null
    var FavnowPlayingBottomBar: RelativeLayout?=null
    var playPauseButton: ImageButton?=null
    var songTitle: TextView?=null
    var recyclerView: RecyclerView?=null
    var trackPosition:Int=0
    var favoriteContent:EchoDatabase?=null

    var refreshList:ArrayList<Songs>?=null
    var getListFormDatabase:ArrayList<Songs>?=null

    object Statified{
        var mediaPlayer: MediaPlayer?=null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.fragment_favorite, container, false)

        activity.title="Favorites"
        noFavorites=view?.findViewById(R.id.noFavorites)
        playPauseButton=view?.findViewById(R.id.playPauseButton)
        FavnowPlayingBottomBar=view?.findViewById(R.id.hiddenBarFavoriteScreen)
        songTitle=view?.findViewById(R.id.songTitleFavScreen)
        recyclerView=view?.findViewById(R.id.favoriteRecycler)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity=context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity=activity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteContent= EchoDatabase(myActivity)
        diplay_favorites_by_searching()
        bottomBarSetup()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item=menu?.findItem(R.id.action_sort)
        item?.isVisible=false
    }


    fun getSongsFromPhone():ArrayList<Songs>{
        var arrayList= arrayListOf<Songs>()
        var contentResolver=myActivity?.contentResolver
        var songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCursor=contentResolver?.query(songUri,null,null,null,null)
        if(songCursor!=null && songCursor.moveToFirst()){
            val songId=songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle=songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist=songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData=songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val songIndex=songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)

            while(songCursor.moveToNext()){
                var currentID=songCursor.getLong(songId)
                var currentTitle=songCursor.getString(songTitle)
                var currentArtist=songCursor.getString(songArtist)
                var currentData=songCursor.getString(songData)
                var currentDate=songCursor.getLong(songIndex)
                arrayList.add(Songs(currentID,currentTitle,currentArtist,currentData,currentDate))
            }

        }
        return arrayList
    }

    fun bottomBarSetup(){
        try {
            songTitle?.setText(SongPLayingFragment.Statified.currentSongHelper?.songTitle)
            SongPLayingFragment.Statified.mediaPlayer?.setOnCompletionListener {
                songTitle?.setText(SongPLayingFragment.Statified.currentSongHelper?.songTitle)
                SongPLayingFragment.Staticated.onSongComplete()
            }
                if(SongPLayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                    FavnowPlayingBottomBar?.visibility=View.VISIBLE
                    SongPLayingFragment.Statified.currentSongHelper?.isPlaying=true
                }else{
                    FavnowPlayingBottomBar?.visibility=View.INVISIBLE
                    SongPLayingFragment.Statified.currentSongHelper?.isPlaying=false
                }
            bottomBarClickHandler()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun bottomBarClickHandler(){
        FavnowPlayingBottomBar?.setOnClickListener({
            Statified.mediaPlayer=SongPLayingFragment.Statified.mediaPlayer
            val songPlayingFragement= SongPLayingFragment()
            val args= Bundle()
            args.putString("songArtist",SongPLayingFragment.Statified.currentSongHelper?.songArtist)
            args.putString("songTitle",SongPLayingFragment.Statified.currentSongHelper?.songTitle)
            args.putString("path",SongPLayingFragment.Statified.currentSongHelper?.songPath)
            args.putInt("songId", SongPLayingFragment.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("songPosition",SongPLayingFragment.Statified.currentSongHelper?.currentPosition?.toInt() as Int)
            args.putParcelableArrayList("songData",SongPLayingFragment.Statified.fetchSongs)
            args.putString("FavBottomBar","success")
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
                SongPLayingFragment.Staticated.processInformation(SongPLayingFragment.Statified.mediaPlayer as MediaPlayer)
                SongPLayingFragment.Statified.currentSongHelper?.isPlaying=true
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })

    }

    fun diplay_favorites_by_searching(){
        if(favoriteContent?.checkSize() as Int >0) {
            refreshList = ArrayList<Songs>()
            getListFormDatabase = favoriteContent?.queryDBList()!!
            val fetchListFromDevice:ArrayList<Songs>? = getSongsFromPhone()
            if (fetchListFromDevice!=null) {
                for (i in 0..fetchListFromDevice.size - 1) {
                    for (j in 0..getListFormDatabase?.size as Int - 1) {
                        if (getListFormDatabase?.get(j)?.songId == fetchListFromDevice.get(i).songId) {
                            refreshList?.add((getListFormDatabase as ArrayList<Songs>)[j])
                        }
                    }
                }


            } else {
            }

            if(refreshList==null){
                recyclerView?.visibility=View.INVISIBLE
                noFavorites?.visibility=View.VISIBLE
            }else{
                val favoriteAdapter=FavoriteAdapter(refreshList as ArrayList<Songs>,myActivity as Context)
                val mLayoutManager= LinearLayoutManager(myActivity)
                recyclerView?.layoutManager=mLayoutManager
                recyclerView?.itemAnimator= DefaultItemAnimator()
                recyclerView?.adapter=favoriteAdapter
                recyclerView?.setHasFixedSize(true)

            }
        }else{
            recyclerView?.visibility=View.INVISIBLE
            noFavorites?.visibility=View.VISIBLE
        }

    }

}// Required empty public constructor
