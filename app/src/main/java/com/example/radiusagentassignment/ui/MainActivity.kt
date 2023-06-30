package com.example.radiusagentassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.radiusagentassignment.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: MainViewModel
    lateinit var errorTxt: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.my_list)
        recyclerView.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        )
        errorTxt = findViewById(R.id.error_txt)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    recyclerView.adapter = FacilityAdapter(
                            it.facilies,
                            it.selected
                    ) { facilityId, optionId ->
                        viewModel.setOptionForFacility(
                                facilityId,
                                optionId
                        )
                    }
                    val result = viewModel.verifySelection()
                    if (!result.valid) {
                        errorTxt.visibility = View.VISIBLE
                        errorTxt.text = result.message
                    } else {
                        errorTxt.visibility = View.GONE
                        errorTxt.text = ""
                    }
                }
            }
        }

        viewModel.updateList()
    }
}
