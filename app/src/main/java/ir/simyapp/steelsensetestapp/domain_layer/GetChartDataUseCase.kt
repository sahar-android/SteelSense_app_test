package ir.simyapp.steelsensetestapp.domain_layer

import ir.simyapp.steelsensetestapp.data_layer.ChartRepository
import ir.simyapp.steelsensetestapp.data_layer.DataSet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: Sahar simyari
 * @Date: 4/11/2025
 */
class GetChartDataUseCase @Inject constructor(
    private val repository: ChartRepository
) {

    operator fun invoke():Flow<List<DataSet>>{
        return repository.getChartData()
    }
}