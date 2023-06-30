package com.example.radiusagentassignment.models

import com.squareup.moshi.Json

data class Facility(
    @Json(name = "facility_id") val facilityId: String,
    val name: String,
    val options: List<Option>
)

data class Option(
    val name: String,
    val icon: String,
    val id: String
)