package com.josycom.mayorjay.holidayinfo.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.holidayinfo.data.model.Country
import com.josycom.mayorjay.holidayinfo.data.repository.HolidayInfoRepository
import com.josycom.mayorjay.holidayinfo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(private val repository: HolidayInfoRepository) : ViewModel() {

    private val _uiData: MutableStateFlow<Resource<List<Country>>> = MutableStateFlow(Resource.Loading())
    val uiData: StateFlow<Resource<List<Country>>> = _uiData.asStateFlow()

    private val _showPopup: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> = _showPopup.asStateFlow()

    private val _country: MutableStateFlow<String> = MutableStateFlow("")
    val country: StateFlow<String> = _country.asStateFlow()

    init {
        getCountries()
    }

    fun getCountries() {
        viewModelScope.launch {
            val countryFlow = repository.getCountries()
            countryFlow.collect { resource ->
                _uiData.emit(resource)
            }
        }
    }

    fun populateYearList(): MutableList<String> {
        val yearList = mutableListOf<String>()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val firstYear = currentYear - 50
        val lastYear = currentYear + 50
        for (year in firstYear..lastYear) {
            yearList.add(year.toString())
        }
        return yearList
    }

    fun updatePopup(value: Boolean) {
        _showPopup.tryEmit(value)
    }

    fun updateCountry(value: String) {
        _country.tryEmit(value)
        updatePopup(true)
    }
}