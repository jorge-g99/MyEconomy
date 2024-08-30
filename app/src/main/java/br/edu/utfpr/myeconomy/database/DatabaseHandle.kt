package br.edu.utfpr.myeconomy.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.usandosqlite.entity.Register
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseHandler (context : Context) : SQLiteOpenHelper( context, DATABASE_NAME, null, DATABASE_VERSION ){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS register (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, description TEXT, value FLOAT, date DATE) ")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABLE_NAME}" )
        onCreate( db )
    }
    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "register"
        public const val COD = 0
        public const val TYPE = 1
        public const val DESCRIPTION = 2
        public const val VALUE = 3
        public const val DATE = 4
    }
    fun insert(register: Register) {
        val db = this.writableDatabase
        val registerDb = ContentValues()

        registerDb.put("type", register.type)
        registerDb.put("description", register.description)
        registerDb.put("value", register.value)
        registerDb.put("date", register.date)
        db.insert("register", null, registerDb)
    }
    fun update(register: Register) {
        val db = this.writableDatabase
        val registerDb = ContentValues()

        registerDb.put("type", register.type)
        registerDb.put("description", register.description)
        registerDb.put("value", register.value)
        registerDb.put("date", register.date)
        db.update(TABLE_NAME, registerDb, "_id=${register._id}", null)
    }
    fun delete(id : Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "_id=${id}", null)
    }
    fun find(id: Int) : Register? {
        val db = this.writableDatabase
        val register = db.query(
            TABLE_NAME,
            null,
            "_id=${id}",
            null,
            null,
            null,
            null
        )
        if (register.moveToNext() ) {
            val registerDb = Register(
                id,
                register.getString( TYPE ),
                register.getString( DESCRIPTION ),
                register.getFloat( VALUE ),
                register.getString( DATE ),
            )
            return registerDb
        } else {
            return null
        }
    }
    fun list() : MutableList<Register> {
        val db = this.writableDatabase
        val register = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        var registers = mutableListOf<Register>()
        while (register.moveToNext()) {
            val registerDb = Register(
                register.getInt( COD ),
                register.getString( TYPE ),
                register.getString( DESCRIPTION ),
                register.getFloat( VALUE ),
                register.getString( DATE ),
            )
            registers.add(registerDb)
        }

        return registers
    }

    fun cursorList() : Cursor {
        val db = this.writableDatabase

        val register = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
        )

        return register
    }
}