package com.example.radiusagentassignment.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyJsonServerResponse(
    val facilities: List<Facility>,
    val exclusions: List<List<FacilityOption>>
)