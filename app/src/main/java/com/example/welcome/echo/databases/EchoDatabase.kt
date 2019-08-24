package com.example.welcome.echo.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.welcome.echo.Fragments.SongPLayingFragment
import com.example.welcome.echo.Songs

/**
 * Created by Welcome on 12/16/2017.
 */
class EchoDatabase:SQLiteOpenHelper{

    var _songList=ArrayList<Songs>()

    object Staticated {
        var DB_VERSION = 1
        val DB_NAME="FavoriteDatabase"
        val TABLE_NAME="FavoriteTable"
        val COLUMN_ID="SongId"
        val COLUMN_SONG_TITLE="SongTitle"
        val COLUMN_SONG_ARTIST="SongArtist"
        val COLUMN_SONG_PATH="SongPath"
    }
    override fun onCreate(sqlLiteDatabase: SQLiteDatabase?) {

        sqlLiteDatabase?.execSQL(
                "CREATE TABLE " + Staticated.TABLE_NAME + "( " + Staticated.COLUMN_ID +" INTEGER," + Staticated.COLUMN_SONG_ARTIST + " TEXT,"
                        + Staticated.COLUMN_SONG_TITLE + " TEXT," + Staticated.COLUMN_SONG_PATH + " TEXT );"
        )

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
    constructor(context: Context?) : super(context, Staticated.DB_NAME,null,Staticated.DB_VERSION)

    fun storeAsFavorite(id:Int?,artist:String?,songTitle:String?,path:String?){
        val db=this.writableDatabase
        var contentvalues=ContentValues()
        contentvalues.put(Staticated.COLUMN_ID,id)
        contentvalues.put(Staticated.COLUMN_SONG_ARTIST,artist)
        contentvalues.put(Staticated.COLUMN_SONG_TITLE,songTitle)
        contentvalues.put(Staticated.COLUMN_SONG_PATH,path)
        db.insert(Staticated.TABLE_NAME,null,contentvalues)
        db.close()
    }

    fun queryDBList():ArrayList<Songs>?{
        try {

            val db=this.readableDatabase
            val query_params="SELECT * FROM " + Staticated.TABLE_NAME
            val cSor=db.rawQuery(query_params,null)
            if(cSor.moveToFirst()){
                do {
                    val _id=cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
                    val _artist=cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_ARTIST))
                    val _title=cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_TITLE))
                    val _songPath=cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_PATH))
                    _songList.add(Songs(_id.toLong(),_title,_artist,_songPath,0))
                }while (cSor.moveToNext())
            }else{
                return null
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

        return _songList
    }

    fun checkIfIdExists(_id:Int):Boolean{
        var storeId=-1129
        val database=this.readableDatabase
        val queryparams="SELECT * FROM " + Staticated.TABLE_NAME + " WHERE SongId = '$_id'"
        val cSor =database.rawQuery(queryparams,null)
        if(cSor.moveToFirst()){
            do {
                storeId=cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
            }while (cSor.moveToNext())
        }else{
            return false
        }
        return storeId!=-1129
    }

    fun deleteFavorite(_id:Int){
        val db=this.writableDatabase
        db.delete(Staticated.TABLE_NAME,Staticated.COLUMN_ID + "=" + _id,null)
        db.close()
    }

    fun checkSize():Int{
        var counter=0
        val db=this.readableDatabase
        val query_params="SELECT * FROM "+Staticated.TABLE_NAME
        val cSor=db.rawQuery(query_params,null)
        if(cSor.moveToFirst()){
            do{
                counter=counter+1
            }while (cSor.moveToNext())
        }else{
            return 0
        }
        return counter
    }

}
