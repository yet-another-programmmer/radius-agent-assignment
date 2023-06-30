package com.example.radiusagentassignment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.radiusagentassignment.R
import com.example.radiusagentassignment.models.Facility

class FacilityAdapter(
    val facilities: List<Facility>,
    val selected: List<String>,
    val checkOption: (facilityId: String, optionId: String) -> Unit
) : RecyclerView.Adapter<FacilityViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacilityViewHolder {
        val facilityView = LayoutInflater
            .from(parent.context)
            .inflate(
                    R.layout.item_facility_list,
                    parent,
                    false
            )
        return FacilityViewHolder(facilityView)
    }

    override fun getItemCount(): Int {
        return facilities.size
    }

    override fun onBindViewHolder(
        holder: FacilityViewHolder,
        position: Int
    ) {
        val facility = facilities[position]

        with(holder.facilityName) {
            text = facility.name
        }
        with(holder.optionList) {
            layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
            )
            adapter = OptionAdapter(
                    facility.options,
                    selected[position]
            ) { optionId ->
                checkOption(
                        facility.facilityId,
                        optionId
                )
            }
        }
    }

}

class FacilityViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    val facilityName: TextView = mView.findViewById(R.id.item_facility_txt)
    val optionList: RecyclerView = mView.findViewById(R.id.item_facility_option_list)
}

