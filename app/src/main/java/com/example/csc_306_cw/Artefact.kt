package com.example.csc_306_cw

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class Artefact() {

    private var id: Int? = null
    private var artefactName: String? = null
    private var artefactMainImage: ByteArray? = null
    private var metaData: String? = null
    private var artefactLocation: LatLng? = null

    private var paragraphs: HashMap<String, String> = HashMap<String, String>()

    private var modalities: ArrayList<Int> = ArrayList<Int>()

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String {
        return artefactName.toString()
    }

    fun setName(name: String) {
        this.artefactName = name
    }

    fun getLocation(): LatLng? {
        return artefactLocation
    }

    fun setLocation(location: LatLng) {
        this.artefactLocation = location
    }


    fun getImage(): ByteArray? {
        return artefactMainImage
    }

    fun setImage(image: ByteArray) {
        this.artefactMainImage = image
    }

    fun getMeta(): String {
        return metaData.toString()
    }

    fun setMeta(meta: String) {
        this.metaData = meta
    }

    fun getArtefactParagraphs(): HashMap<String, String> {
        return paragraphs
    }

    fun setArtefactParagraphs(paragraphs: HashMap<String, String>) {
        this.paragraphs = paragraphs
    }

    fun addParagraph(head: String, body: String) {
        paragraphs.put(head, body)
    }

    fun getArtefactModalities(): ArrayList<Int> {
        return modalities
    }

    fun setArtefactModalities(modalities: ArrayList<Int>) {
        this.modalities = modalities
    }

    fun addModalities(modality: Int) {
        modalities.add(modality)
    }

    fun paragraphsToJson(paragraphs: HashMap<String, String>): String {

        val gson = Gson()
        val json = gson.toJson(paragraphs)
        return json
    }

    fun jsonToParagraphs(json: String): HashMap<String, String> {
        val type = object : TypeToken<HashMap<String, String>>() {}.type
        val paragraphs = Gson().fromJson<HashMap<String, String>>(json, type)
        return paragraphs
    }

    fun modalitiesToJson(modalities: ArrayList<Int>): String {

        val gson = Gson()
        val json = gson.toJson(modalities)
        return json
    }

    fun jsonToModalities(json: String): ArrayList<Int> {

        val type = object : TypeToken<ArrayList<Int>>() {}.type
        val modalities = Gson().fromJson<ArrayList<Int>>(json, type)
        return modalities
    }

    fun latLngToJson(latLng: LatLng): String {
        val jsonObject = JSONObject()
        jsonObject.put("lat", latLng.latitude)
        jsonObject.put("lng", latLng.longitude)
        return jsonObject.toString()
    }

    fun jsonToLatLng(jsonString: String): LatLng? {

        val jsonObject = JSONObject(jsonString)
        val lat = jsonObject.getDouble("lat")
        val lng = jsonObject.getDouble("lng")
        return LatLng(lat, lng)
    }

}