package com.example.appfactorytest.data.model

import com.google.gson.annotations.SerializedName


data class Image(
    @SerializedName("#text")
    var url: String,

    @SerializedName("size")
    var size: String
)