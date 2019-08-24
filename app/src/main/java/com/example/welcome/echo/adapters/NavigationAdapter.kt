package com.example.welcome.echo.adapters

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.welcome.echo.Fragments.*
import com.example.welcome.echo.R
import com.example.welcome.echo.activities.MainActivity

/**
 * Created by Welcome on 12/14/2017.
 */
class NavigationAdapter(_contentList:ArrayList<String>,_getImages:IntArray,_context:Context)
    : RecyclerView.Adapter<NavigationAdapter.NavViewHolder>() {

    var contentList:ArrayList<String>?=null
    var getImages:IntArray?=null
    var mContext:Context?=null

    init {
        this.contentList=_contentList
        this.getImages=_getImages
        this.mContext=_context
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NavViewHolder {
        val itemView=LayoutInflater.from(parent?.context)
                     .inflate(R.layout.row_custom_navigationdrawer,parent,false)
        val returnThis=NavViewHolder(itemView)
        return returnThis
    }

    override fun onBindViewHolder(holder: NavViewHolder?, position: Int) {

        holder?.iconGet?.setBackgroundResource(getImages?.get(position) as Int)
        holder?.textGet?.setText(contentList?.get(position))
        holder?.contentHolder?.setOnClickListener({
            if(position==0){
                val mainScreenFragment= MainscreenFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment,mainScreenFragment)
                        .commit()
            }else if(position==1){
                val favoriteFragment=FavoriteFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment,favoriteFragment)
                        .commit()
            }else if(position==2){
                val settingsFragment=SettingsFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment,settingsFragment)
                        .commit()
            }else if(position==3){
                val aboutUsFragment=AboutUsFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment,aboutUsFragment)
                        .commit()
            }
            MainActivity.Statified.drawerLayout?.closeDrawers()
        })
    }

    override fun getItemCount(): Int {

        return contentList?.size as Int
    }

    class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var iconGet:ImageView?=null
        var textGet:TextView?=null
        var contentHolder:RelativeLayout?=null
        init {
            iconGet=itemView?.findViewById(R.id.icon_navdrawer)
            textGet=itemView?.findViewById(R.id.text_navdrawer)
            contentHolder=itemView?.findViewById(R.id.navigation_content_holder)
        }
    }

}