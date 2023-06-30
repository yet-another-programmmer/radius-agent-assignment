package com.example.radiusagentassignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.radiusagentassignment.data.RemoteConfigRepository
import com.example.radiusagentassignment.models.Facility
import com.example.radiusagentassignment.models.FacilityOption
import com.example.radiusagentassignment.network.MyJsonServerApi
import com.example.radiusagentassignment.network.ResultWrapper
import com.example.radiusagentassignment.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val facilies: List<Facility> = emptyList(),
    val exclusions: List<List<FacilityOption>> = emptyList(),
    val selected: List<String> = emptyList()
)

class MainViewModel @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedResponse = remoteConfigRepository.getResponse()
            if (cachedResponse == null) {
                val result = safeApiCall(Dispatchers.IO) {
                    MyJsonServerApi.myService.index()
                }
                when (result) {
                    is ResultWrapper.Success -> {
                        val response = result.value
                        remoteConfigRepository.setResponse(response)
                        _uiState.update {
                            UiState(
                                    facilies = response.facilities,
                                    exclusions = response.exclusions,
                                    selected = response.facilities.map { it.options.first().id }
                            )
                        }
                    }
                    is ResultWrapper.GenericError -> {

                    }
                    is ResultWrapper.NetworkError -> {

                    }
                }
            }
        }
    }

    fun setOptionForFacility(
        facilityId: String,
        optionId: String
    ) {
        _uiState.update { last ->
            val facilityIndex = last.facilies.indexOfFirst { facilityId == it.facilityId }
            val selected = last.selected.toMutableList()
            selected[facilityIndex] = optionId
            last.copy(selected = selected)
        }
    }

    fun verifySelection(): VerificationResult {
        val state = _uiState.value
        if (state.facilies.isEmpty()) {
            return VerificationResult()
        }
        val selectedCombination = mutableListOf<FacilityOption>()
        for ((index, value) in state.selected.withIndex()) {
            selectedCombination.add(
                    FacilityOption(
                            state.facilies[index].facilityId,
                            value
                    )
            )
        }
        for (combination in state.exclusions) {
            val remainingCombinations = combination.minus(selectedCombination)
            if (remainingCombinations.isEmpty()) {
                val msg = buildString {
                    for (item in combination) {
                        val facilityId = item.facilityId
                        val optionId = item.optionId
                        val facility = state.facilies.find { it.facilityId == facilityId }
                        append(facility?.name)
                        append(": ")
                        append(facility?.options?.find { it.id == optionId }?.name)
                        append("\n")
                    }
                    append("\nInvalid option combination selected!")
                }
                return VerificationResult(
                        valid = false,
                        message = msg
                )
            }
        }
        return VerificationResult()
    }
}

data class VerificationResult(
    val valid: Boolean = true,
    val message: String = ""
)