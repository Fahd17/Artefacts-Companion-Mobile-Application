package com.example.csc_306_cw.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.csc_306_cw.Artefact

const val DATABASE_VERSION = 5
private const val DATABASE_NAME = "artefactapp"
private const val TABLE_ARTEFACT = "artefacts"

private const val COlUMN_ID = "_id"
private const val COlUMN_ARTEFACT_NAME = "artefactName"
private const val COlUMN_ARTEFACT_MAIN_IMAGE = "artefactMainImage"
private const val COlUMN_ARTEFACT_META_DATA = "artefactMetaData"
private const val COlUMN_ARTEFACT_PARAGRAPHS = "artefactParagraphs"
private const val COlUMN_ARTEFACT_MODALITIES = "artefactModalities"


class DBManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_ARTEFACT_TABLE =
            "CREATE TABLE $TABLE_ARTEFACT($COlUMN_ID INTEGER PRIMARY KEY, $COlUMN_ARTEFACT_NAME TEXT, $COlUMN_ARTEFACT_MAIN_IMAGE INTEGER," +
                    "$COlUMN_ARTEFACT_META_DATA TEXT, $COlUMN_ARTEFACT_PARAGRAPHS TEXT, $COlUMN_ARTEFACT_MODALITIES)"
        db.execSQL(CREATE_ARTEFACT_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ARTEFACT")
        onCreate(db)
    }

    fun populateArtefactsList(): ArrayList<Artefact> {

        val sql = "SELECT * FROM $TABLE_ARTEFACT"
        val db = this.readableDatabase

        val artefactsList = arrayListOf<Artefact>()

        val cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            do {
                val id = Integer.parseInt(cursor.getString(0))
                val artefactName = cursor.getString(1)
                val artefactMainImage = Integer.parseInt(cursor.getString(2))
                val artefactMetaData = cursor.getString(3)
                val artefactParagraph = cursor.getString(4)
                val artefactModalities = cursor.getString(5)

                val artefact = Artefact()
                artefact.setId(id)
                artefact.setName(artefactName)
                artefact.setImage(artefactMainImage)
                artefact.setMeta(artefactMetaData)
                artefact.setArtefactParagraphs(artefact.jsonToParagraphs(artefactParagraph))
                artefact.setArtefactModalities(artefact.jsonToModalities(artefactModalities))

                artefactsList.add(artefact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return artefactsList
    }

    fun addArtefact (name: String, mainImage: Int, metadata: String, paragraphs: String, modalities: String) {

        val values = ContentValues()
        values.put(COlUMN_ARTEFACT_NAME, name)
        values.put(COlUMN_ARTEFACT_MAIN_IMAGE, mainImage)
        values.put(COlUMN_ARTEFACT_META_DATA, metadata)
        values.put(COlUMN_ARTEFACT_PARAGRAPHS, paragraphs)
        values.put(COlUMN_ARTEFACT_MODALITIES, modalities)

        val db = this.writableDatabase
        db.insert(TABLE_ARTEFACT, null, values)

    }

    fun deleteArtefact (id: Int){
        val query = "$COlUMN_ID = ?"
        val db = this.writableDatabase

        db.delete(TABLE_ARTEFACT, query, arrayOf(id.toString()))
    }

    fun updateArtefact (artefact: Artefact){

        val values = ContentValues()
        values.put(COlUMN_ARTEFACT_NAME, artefact.getName())
        values.put(COlUMN_ARTEFACT_MAIN_IMAGE, artefact.getImage())
        values.put(COlUMN_ARTEFACT_META_DATA, artefact.getMeta())
        values.put(COlUMN_ARTEFACT_PARAGRAPHS, artefact.paragraphsToJson(artefact.getArtefactParagraphs()))
        values.put(COlUMN_ARTEFACT_MODALITIES, artefact.modalitiesToJson(artefact.getArtefactModalities()))

        val db = this.writableDatabase

        db.update(TABLE_ARTEFACT,
            values,
            "$COlUMN_ID = ?",
            arrayOf((artefact.getId()).toString())
        )
    }

    fun findArtefact (id: Int): Artefact? {

        val query = "SELECT * FROM $TABLE_ARTEFACT WHERE $COlUMN_ID = $id"
        val db = this.writableDatabase
        var foundArtefact: Artefact? = null

        val cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()){
            val id = Integer.parseInt(cursor.getString(0))
            val artefactName = cursor.getString(1)
            val artefactMainImage = Integer.parseInt(cursor.getString(2))
            val artefactMetaData = cursor.getString(3)
            val artefactParagraph = cursor.getString(4)
            val artefactModalities = cursor.getString(5)

            foundArtefact = Artefact()
            foundArtefact.setId(id)
            foundArtefact.setName(artefactName)
            foundArtefact.setImage(artefactMainImage)
            foundArtefact.setMeta(artefactMetaData)
            foundArtefact.setArtefactParagraphs(foundArtefact.jsonToParagraphs(artefactParagraph))
            foundArtefact.setArtefactModalities(foundArtefact.jsonToModalities(artefactModalities))
        }
        cursor.close()
        return foundArtefact

    }





}