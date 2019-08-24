package com.example.welcome.echo.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.welcome.echo.R


/**
 * A simple [Fragment] subclass.
 */
class AboutUsFragment : Fragment() {

    var photo:ImageView?=null
    var text:TextView?=null
    var aboutLayout:RelativeLayout?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater!!.inflate(R.layout.fragment_about_us, container, false)
        photo=view?.findViewById(R.id.developerImage)
        text=view?.findViewById(R.id.aboutText)
        aboutLayout=view?.findViewById(R.id.aboutLayout)
        activity.title="About Us"

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
    }
}// Required empty public constructor
