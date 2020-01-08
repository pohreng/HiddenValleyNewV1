package com.example.hiddenvalleyv3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.android.synthetic.main.after_login.*


class AccDatabase (context: Context): SQLiteOpenHelper(context, dbname, factory, version){

    override fun onCreate(p0: SQLiteDatabase?){
        p0?.execSQL("create table info(username varchar(15) primary key,"+
                "pass varchar(15),points integer)")
        /*p0?.execSQL("create table newUser(username varchar(15) primary key autoincrement," +
                "pass varchar(15));")
        p0?.execSQL("create table info(username varchar(15)," +
                "password varchar(15), points integer);")*/
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
    }

    fun insertUserData(username: String , pass: String){
        val db: SQLiteDatabase =  writableDatabase
        val info: ContentValues = ContentValues()

        info.put("username", username)
        info.put("pass", pass)
        info.put("points", 0)

        db.insert("info", null, info)
        db.close()
    }

    fun verifyUsername(username: String): Boolean {
        val db=  writableDatabase
        val query= "select * from info where username = '$username'"
        val cursor= db.rawQuery(query,null)
        if ( cursor.count <= 0){
            cursor.close()
            return true
        }
        cursor.close()
        return false
    }

    fun userPresent(username: String,pass: String): Boolean {
        val db=  writableDatabase
        val query= "select * from info where username = '$username' and pass = '$pass'"
        val cursor= db.rawQuery(query,null)
        if ( cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun retrieveData(username: String) : MutableList<User_details>{
        var list : MutableList<User_details> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from info where username = '$username' "
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var user = User_details()
                user.username = result.getString(result.getColumnIndex("username"))
                user.password = result.getString(result.getColumnIndex("pass"))
                user.points = result.getString(result.getColumnIndex("points")).toInt()
                list.add(user)
            }while (result.moveToNext())
        }
        result.close()
        //db.close()
        return list
    }
    fun increasePoint(username: String ,points: String){
        val db = this.writableDatabase
        val cv = ContentValues()
        var data = retrieveData(username)
        for(i in 0..(data.size-1)){
            val totalpoint = points.toInt() + data.get(i).points.toInt()
            cv.put("points", totalpoint)
            db.update("info",cv,"username = '$username' ",null)
        }
        db.close()
        }
    fun changePass(username: String, pass: String){
        val db = this.writableDatabase
        val cv = ContentValues()
        var data = retrieveData(username)
        for(i in 0..(data.size-1)){
            cv.put("pass", pass)
            db.update("info",cv,"username = '$username' ",null)
        }
        db.close()
    }
    fun resetPoint(username: String){
        val db = this.writableDatabase
        val cv = ContentValues()
        var data = retrieveData(username)
        for(i in 0..(data.size-1)){
            val totalpoint = 0
            cv.put("points", totalpoint)
            db.update("info",cv,"username = '$username' ",null)
        }
        db.close()
    }
    /*fun retrievePoints(username: String) : MutableList<User_details>{
        var list : MutableList<User_details> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from info where username = '$username' "
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var user = User_details()
                user.username = result.getString(result.getColumnIndex("username"))
                user.password = result.getString(result.getColumnIndex("pass"))
                user.points = result.getString(result.getColumnIndex("points")).toInt()
                list.add(user)
            }while (result.moveToNext())
        }
        result.close()
        //db.close()
        return list
    }    */

    companion object{

        internal val dbname = "userDB"
        internal val factory = null
        internal val version = 1
    }



}