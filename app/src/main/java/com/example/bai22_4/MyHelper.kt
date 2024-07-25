package com.example.bai22_4

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context: Context) :SQLiteOpenHelper(context,"TUHOCDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE TUHOCDB(_id INTEGER primary key autoincrement, user Text, email Text)")
        db?.execSQL("INSERT INTO TUHOCDB('user','email') values ('mot','motemail')")
        db?.execSQL("INSERT INTO TUHOCDB('user','email') values ('hai','haiemail')")
        db?.execSQL("INSERT INTO TUHOCDB('user','email') values ('ba','baemail')")
        db?.execSQL("INSERT INTO TUHOCDB('user','email') values ('bon','bonemail')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}