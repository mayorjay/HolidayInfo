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

    val yearList = mutableListOf<String>()

    init {
        getCountries()
        populateYearList()
    }

    fun getCountries() {
        viewModelScope.launch {
            val countryFlow = repository.getCountries()
            countryFlow.collect { resource ->
                _uiData.emit(resource)
            }
        }
    }

    private fun populateYearList() {
        yearList.clear()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val firstYear = currentYear - 50
        val lastYear = currentYear + 50
        for (year in firstYear..lastYear) {
            yearList.add(year.toString())
        }
    }
}