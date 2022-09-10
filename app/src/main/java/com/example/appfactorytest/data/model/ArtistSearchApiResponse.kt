package com.example.appfactorytest.data.model

import com.google.gson.annotations.SerializedName

data class ArtistSearchApiResponse(
    @SerializedName("results")
    var results: ArtistResults
)

data class ArtistResults(
    @SerializedName("artistmatches")
    var artistMatches: ArtistMatches
)

data class ArtistMatches(
    @SerializedName("artist")
    var artists: List<Artist>
)