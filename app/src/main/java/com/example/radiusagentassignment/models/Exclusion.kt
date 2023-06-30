package com.example.radiusagentassignment.models

import com.squareup.moshi.Json

data class FacilityOption(
    @Json(name = "facility_id") val facilityId: String,
    @Json(name = "options_id") val optionId: String
)