package com.codangcoding.kmovieapp.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiResponse(
    val page: Int,
    @JsonProperty("total_results") val totalResult: Int,
    @JsonProperty("total_pages") val totalPage: Int,
    @JsonProperty("results") val movies: List<Movie>
)