package ir.simyapp.steelsensetestapp.data_layer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @Author: Sahar simyari
 * @Date: 4/11/2025
 */
class ChartRepositoryImpl @Inject constructor(private val datasetManager: DataManager):ChartRepository {
    override fun getChartData(): Flow<List<DataSet>> = flow {
        val dataset1 = datasetManager.generateDataset("Line1", 100, 0f, 100f)
        val dataset2 = datasetManager.generateDataset("Line2", 100, 0f, 200f)
        val dataset3 = datasetManager.generateDataset("Line3", 100, 0f, 300f)
        emit(listOf(dataset1, dataset2, dataset3))
    }
}