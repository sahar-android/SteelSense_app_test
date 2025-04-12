package ir.simyapp.steelsensetestapp.presentation_layer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.simyapp.steelsensetestapp.data_layer.DataSet
import ir.simyapp.steelsensetestapp.domain_layer.GetChartDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Sahar simyari
 * @Date: 4/11/2025
 */
@HiltViewModel
class ChartViewModel @Inject constructor(val getChartDataUseCase: GetChartDataUseCase):ViewModel() {
    private val _chartData = MutableStateFlow<List<DataSet>>(emptyList())
    val chartData: StateFlow<List<DataSet>>
        get() = _chartData

    init {
        loadChartData()
    }

    private fun loadChartData() {
        viewModelScope.launch {
            getChartDataUseCase().collect { data ->
                _chartData.value = data
            }
        }
    }
}