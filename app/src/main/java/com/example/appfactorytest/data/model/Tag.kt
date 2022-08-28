package com.example.appfactorytest.data.model

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("url")
    var url: String,

    @SerializedName("name")
    var name: String
)