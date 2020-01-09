package com.example.hiddenvalleyv3

import android.media.Image
import android.widget.ImageView

class Questions {
    var image : Int = 0
    var answer : String = ""

    constructor(image : Int,answer : String){
        this.answer = answer
        this.image = image
    }
}