package com.example.csc_306_cw

class Artefact {

    private var artefactName: String? = null
    private var artefactMainImage: Int = 0

    private var paragraphs : HashMap<String, String> = HashMap<String, String> ()

    private var modalities: ArrayList<Int> = ArrayList<Int>()

    fun getName(): String {
        return artefactName.toString()
    }

    fun setName(name: String){
        this.artefactName = name
    }

    fun getImage(): Int {
        return artefactMainImage
    }

    fun setImage(image: Int){
        this.artefactMainImage = image
    }

    fun getArtefactParagraphs (): HashMap<String, String>{
        return paragraphs
    }

    fun setArtefactParagraphs (paragraphs: HashMap<String, String>){
        this.paragraphs = paragraphs
    }

    fun addParagraph(head: String, body: String){
        paragraphs.put(head, body)
    }
    fun getArtefactModalities (): ArrayList<Int>{
        return modalities
    }

    fun setArtefactModalities (modalities: ArrayList<Int>){
        this.modalities = modalities
    }

    fun addModalities(modality: Int){
        modalities.add(modality)
    }



}