package ir.simyapp.steelsensetestapp.data_layer

import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * @Author: Sahar simyari
 * @Date: 4/11/2025
 */
@Singleton
class DataManager @Inject constructor() {
    // Function to generate a dataset
    fun generateDataset(name: String, size: Int, minValue: Float, maxValue: Float): DataSet {
        val dataPoints = List(size) {
            /*DataPoint(
                timestamp = LocalDateTime.now().plusMinutes(it.toLong()), // Increment timestamp
                value = Random.nextFloat() * (maxValue - minValue) + minValue // Generate random value
            )*/
            DataPoint(
                timestamp = LocalDateTime.now().plusMinutes(it.toLong()), // Increment timestamp
                value = maxValue / size * (it + 1) // Scale values evenly within the range
            )
        }
        return DataSet(name, dataPoints)
    }

}