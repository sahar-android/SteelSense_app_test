package ir.simyapp.steelsensetestapp.data_layer

import kotlinx.coroutines.flow.Flow

/**
 * @Author: Sahar simyari
 * @Date: 4/11/2025
 */
interface ChartRepository {
    fun getChartData(): Flow<List<DataSet>>
}