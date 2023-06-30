package com.example.radiusagentassignment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.radiusagentassignment.R
import com.example.radiusagentassignment.models.Option

class OptionAdapter(
    val options: List<Option>,
    val selected: String,
    val checkOption: (id: String) -> Unit
) : RecyclerView.Adapter<OptionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionViewHolder {
        val optionView = LayoutInflater
            .from(parent.context)
            .inflate(
                    R.layout.item_option_list,
                    parent,
                    false
            )
        return OptionViewHolder(optionView, options, checkOption = checkOption)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(
        holder: OptionViewHolder,
        position: Int
    ) {
        val option = options[position]

        with(holder.optionName) {
            text = option.name
        }
        with(holder.optionIcon) {
            setImageDrawable(
                    AppCompatResources.getDrawable(
                            holder.itemView.context,
                            FACILITY_TO_ICON.getOrDefault(
                                    option.id,
                                    R.drawable.ic_launcher_foreground
                            )
                    )
            )
        }
        with(holder.optionChecked) {
            val drawableId =
                if (option.id == selected) {
                    R.drawable.baseline_radio_button_checked_24
                } else {
                    R.drawable.baseline_radio_button_unchecked_24
                }
            setImageDrawable(
                    AppCompatResources.getDrawable(
                            holder.itemView.context,
                            drawableId
                    )
            )
        }
    }
}

class OptionViewHolder(
    mView: View,
    options: List<Option>,
    checkOption: (id: String) -> Unit
) : RecyclerView.ViewHolder(mView) {
    val optionName: TextView = mView.findViewById(R.id.item_option_txt)
    val optionIcon: ImageView = mView.findViewById(R.id.item_option_img)
    val optionChecked: ImageView = mView.findViewById(R.id.item_option_check)

    init {
        mView.setOnClickListener {
            checkOption(options[layoutPosition].id)
        }
    }
}

val FACILITY_TO_ICON = mapOf(
        "1" to R.drawable.apartment,
        "2" to R.drawable.condo,
        "3" to R.drawable.boat,
        "4" to R.drawable.land,
        "5" to R.drawable.ic_launcher_foreground,
        "6" to R.drawable.rooms,
        "7" to R.drawable.no_room,
        "8" to R.drawable.ic_launcher_foreground,
        "9" to R.drawable.ic_launcher_foreground,
        "10" to R.drawable.swimming,
        "11" to R.drawable.garden,
        "12" to R.drawable.garage
)