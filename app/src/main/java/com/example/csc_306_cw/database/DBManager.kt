package com.example.csc_306_cw.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.csc_306_cw.Artefact
import com.google.android.gms.maps.model.LatLng

const val DATABASE_VERSION = 5
private const val DATABASE_NAME = "artefactapp"
private const val TABLE_ARTEFACT = "artefacts"

private const val COlUMN_ARTEFACT_ID = "_id"
private const val COlUMN_ARTEFACT_NAME = "artefactName"
private const val COlUMN_ARTEFACT_MAIN_IMAGE = "artefactMainImage"
private const val COlUMN_ARTEFACT_META_DATA = "artefactMetaData"
private const val COlUMN_ARTEFACT_PARAGRAPHS = "artefactParagraphs"
private const val COlUMN_ARTEFACT_MODALITIES = "artefactModalities"
private const val COlUMN_ARTEFACT_STATE = "artefactState"
private const val COlUMN_ARTEFACT_LOCATION = "artefactLocation"

private const val TABLE_ROLE = "roles"
private const val COlUMN_ROLE_ID = "roleId"
private const val COlUMN_IS_ADMIN = "isAdmin"

private const val TABLE_BOOKMARK = "bookmarks"
private const val COlUMN_BOOKMARK_ID = "bookmarkId"
private const val COlUMN_USER_ID = "UserId"

private const val TABLE_MODALITY = "modalities"
private const val COlUMN_MODALITY_ID = "modalityId"
private const val COlUMN_MODALITY_BODY = "modalityBody"


class DBManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_ARTEFACT_TABLE =
            "CREATE TABLE $TABLE_ARTEFACT($COlUMN_ARTEFACT_ID INTEGER PRIMARY KEY, " +
                    "$COlUMN_ARTEFACT_NAME TEXT, $COlUMN_ARTEFACT_MAIN_IMAGE BLOB," +
                    "$COlUMN_ARTEFACT_META_DATA TEXT, $COlUMN_ARTEFACT_PARAGRAPHS TEXT, " +
                    "$COlUMN_ARTEFACT_MODALITIES, $COlUMN_ARTEFACT_STATE TEXT, " +
                    "$COlUMN_USER_ID TEXT, $COlUMN_ARTEFACT_LOCATION TEXT)"
        db.execSQL(CREATE_ARTEFACT_TABLE)

        val TABLE_ROLE_CREATE =
            "CREATE TABLE $TABLE_ROLE($COlUMN_ROLE_ID INTEGER PRIMARY KEY, " +
                    "$COlUMN_USER_ID TEXT, $COlUMN_IS_ADMIN TEXT)"
        db.execSQL(TABLE_ROLE_CREATE)


        val CREATE_BOOKMARK_TABLE =
            "CREATE TABLE $TABLE_BOOKMARK($COlUMN_BOOKMARK_ID INTEGER PRIMARY KEY, $COlUMN_ARTEFACT_ID INTEGER, $COlUMN_USER_ID TEXT)"
        db.execSQL(CREATE_BOOKMARK_TABLE)

        val CREATE_MODALITY_TABLE =
            "CREATE TABLE $TABLE_MODALITY($COlUMN_MODALITY_ID INTEGER PRIMARY KEY, $COlUMN_MODALITY_BODY BLOB)"
        db.execSQL(CREATE_MODALITY_TABLE)

        Log.d("testing", "Create database")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ARTEFACT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROLE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKMARK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MODALITY")
        onCreate(db)
    }

    fun getUserBookmark(id: String): ArrayList<Artefact> {
        val db = this.readableDatabase

        val artefactsList = arrayListOf<Artefact>()

        val query = "SELECT * FROM $TABLE_ARTEFACT WHERE $COlUMN_ARTEFACT_ID IN " +
                "(SELECT $COlUMN_ARTEFACT_ID FROM $TABLE_BOOKMARK WHERE $COlUMN_USER_ID = ?)"
        val cursor = db.rawQuery(query, arrayOf(id))

        if (cursor.moveToFirst()) {
            do {
                artefactsList.add(constructArtefact(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return artefactsList
    }

    fun isAdmin (id: String): Boolean{

        val query = "SELECT * FROM $TABLE_ROLE WHERE $COlUMN_USER_ID = ?"
        val db = this.writableDatabase
        var isAmin: Boolean = false

        val cursor = db.rawQuery(query, arrayOf(id), null)

        if (cursor.moveToFirst()) {
            isAmin = cursor.getString(2).toBoolean()
        }
        cursor.close()
        return isAmin
    }

    fun registerRole(id: String, role: String){

        val values = ContentValues()
        values.put(COlUMN_USER_ID, id)
        values.put(COlUMN_IS_ADMIN, role)

        val db = this.writableDatabase
        db.insert(TABLE_ROLE, null, values)
    }

    fun changeRole(id: String, role: String) {
        val values = ContentValues()
        values.put(COlUMN_USER_ID, id)
        values.put(COlUMN_IS_ADMIN, role)

        val db = this.writableDatabase

        db.update(
            TABLE_ROLE,
            values,
            "$COlUMN_USER_ID = ?",
            arrayOf(id)
        )
    }

    fun populateUsersList(): ArrayList<String> {

        val sql = "SELECT * FROM $TABLE_ROLE"
        val db = this.readableDatabase

        val usersList = arrayListOf<String>()

        val cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            do {
                usersList.add(cursor.getString(1))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return usersList

    }

    fun addUserBookmark(userId: String, artefactId: Int?) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COlUMN_USER_ID, userId)
        values.put(COlUMN_ARTEFACT_ID, artefactId)
        db.insert(TABLE_BOOKMARK, null, values)

        db.close()
    }

    fun deleteUserBookmark(userId: String, artefactId: Int?) {
        val db = this.writableDatabase

        db.delete(
            TABLE_BOOKMARK, "$COlUMN_USER_ID = ? AND $COlUMN_ARTEFACT_ID = ?",
            arrayOf(userId, artefactId.toString())
        )

        db.close()
    }

    fun populateUserArtefactsList(userId: String): ArrayList<Artefact> {
        val sql = "SELECT * FROM $TABLE_ARTEFACT WHERE $COlUMN_ARTEFACT_STATE != ? AND $COlUMN_USER_ID = ?"

        val db = this.readableDatabase
        val artefactsList = arrayListOf<Artefact>()

        val cursor = db.rawQuery(sql, arrayOf("ready", userId), null)

        if (cursor.moveToFirst()) {
            do {
                artefactsList.add(constructArtefact(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return artefactsList
    }

    fun populateArtefactsList(state: String): ArrayList<Artefact> {
        val sql = if (state == "ready") {
            "SELECT * FROM $TABLE_ARTEFACT WHERE $COlUMN_ARTEFACT_STATE = ?"
        } else {
            "SELECT * FROM $TABLE_ARTEFACT WHERE $COlUMN_ARTEFACT_STATE != 'ready'"
        }
        val db = this.readableDatabase

        val artefactsList = arrayListOf<Artefact>()

        val cursor = if (state == "ready") {
            db.rawQuery(sql, arrayOf(state))
        } else {
            db.rawQuery(sql, null)
        }
        if (cursor.moveToFirst()) {
            do {
                artefactsList.add(constructArtefact(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return artefactsList
    }

    fun addModality (modalityImage: ByteArray?): Int{

        val values = ContentValues()
        values.put(COlUMN_MODALITY_BODY, modalityImage)

        val db = this.writableDatabase
        return db.insert(TABLE_MODALITY, null, values).toInt()

    }

    fun getModality (id: Int): ByteArray?{

        val query = "SELECT * FROM $TABLE_MODALITY WHERE $COlUMN_MODALITY_ID = $id"
        val db = this.writableDatabase
        var foundModality: ByteArray? = null

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            foundModality = cursor.getBlob(1)
        }
        cursor.close()
        return foundModality

    }

    fun addArtefact(
        name: String, mainImage: ByteArray?,
        metadata: String, paragraphs: String,
        modalities: String, state: String,
        id: String, location: String
    ) {

        Log.d("testing","add artefact" )
        val values = ContentValues()
        values.put(COlUMN_ARTEFACT_NAME, name)
        values.put(COlUMN_ARTEFACT_MAIN_IMAGE, mainImage)
        values.put(COlUMN_ARTEFACT_META_DATA, metadata)
        values.put(COlUMN_ARTEFACT_PARAGRAPHS, paragraphs)
        values.put(COlUMN_ARTEFACT_MODALITIES, modalities)
        values.put(COlUMN_ARTEFACT_STATE, state)
        values.put(COlUMN_USER_ID, id)
        values.put(COlUMN_ARTEFACT_LOCATION, location)

        val db = this.writableDatabase
        var id = db.insert(TABLE_ARTEFACT, null, values)
        Log.d("test", id.toString())

    }

    fun deleteArtefact(id: Int) {
        val query = "$COlUMN_ARTEFACT_ID = ?"
        val db = this.writableDatabase

        db.delete(TABLE_ARTEFACT, query, arrayOf(id.toString()))
    }

    fun updateArtefact(artefact: Artefact, state: String) {

        val values = ContentValues()
        values.put(COlUMN_ARTEFACT_NAME, artefact.getName())
        values.put(COlUMN_ARTEFACT_MAIN_IMAGE, artefact.getImage())
        values.put(COlUMN_ARTEFACT_META_DATA, artefact.getMeta())
        values.put(
            COlUMN_ARTEFACT_PARAGRAPHS,
            artefact.paragraphsToJson(artefact.getArtefactParagraphs())
        )
        values.put(
            COlUMN_ARTEFACT_MODALITIES,
            artefact.modalitiesToJson(artefact.getArtefactModalities())
        )
        values.put(
            COlUMN_ARTEFACT_STATE, state
        )
        values.put(
            COlUMN_ARTEFACT_LOCATION,
            artefact.latLngToJson(artefact.getLocation()!!)
        )

        val db = this.writableDatabase

        db.update(
            TABLE_ARTEFACT,
            values,
            "$COlUMN_ARTEFACT_ID = ?",
            arrayOf((artefact.getId()).toString())
        )
    }

    fun getState(id: Int): String? {
        val query = "SELECT * FROM $TABLE_ARTEFACT WHERE $COlUMN_ARTEFACT_ID = ?"
        val db = this.writableDatabase
        var foundState: String? = null
        val cursor = db.rawQuery(query, arrayOf(id.toString()), null)

        if (cursor.moveToFirst()) {
            foundState = cursor.getString(6)
        }
        cursor.close()
        return foundState
    }

    fun isArtefactPresent(id: String): Boolean {

        val query = "SELECT COUNT(*) FROM $TABLE_ARTEFACT WHERE $COlUMN_ARTEFACT_ID = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(id.toString()), null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }

    fun findArtefact(id: Int): Artefact? {

        val query = "SELECT * FROM $TABLE_ARTEFACT WHERE $COlUMN_ARTEFACT_ID = $id"
        val db = this.writableDatabase
        var foundArtefact: Artefact? = null

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            foundArtefact = constructArtefact(cursor)
        }
        cursor.close()
        return foundArtefact

    }

    private fun constructArtefact(cursor: Cursor): Artefact {
        var artefact: Artefact? = null
        val id = Integer.parseInt(cursor.getString(0))
        val artefactName = cursor.getString(1)
        val artefactMainImage = cursor.getBlob(2)
        val artefactMetaData = cursor.getString(3)
        val artefactParagraph = cursor.getString(4)
        val artefactModalities = cursor.getString(5)
        val artefactLocation = cursor.getString(8)

        artefact = Artefact()
        artefact.setId(id)
        artefact.setName(artefactName)
        artefact.setImage(artefactMainImage)
        artefact.setMeta(artefactMetaData)
        artefact.setArtefactParagraphs(artefact.jsonToParagraphs(artefactParagraph))
        artefact.setArtefactModalities(artefact.jsonToModalities(artefactModalities))
        artefact.setLocation(artefact.jsonToLatLng(artefactLocation)!!)

        return artefact
    }


}