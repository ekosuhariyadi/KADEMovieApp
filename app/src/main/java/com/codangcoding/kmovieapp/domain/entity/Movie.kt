package com.codangcoding.kmovieapp.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Movie(
    val title: String,
    @JsonProperty("vote_average") val vote: Double,
    val overview: String,
    @JsonProperty("release_date") val releaseDate: String,
    @JsonProperty("poster_path") val posterPath: String,
    @JsonProperty("backdrop_path") val backdropPath: String
)