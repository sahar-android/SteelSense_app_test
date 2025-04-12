package ir.simyapp.steelsensetestapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import ir.simyapp.steelsensetestapp.presentation_layer.ChartViewModel
import ir.simyapp.steelsensetestapp.presentation_layer.StaticLineChartWithToggles
import ir.simyapp.steelsensetestapp.ui.theme.SteelsenseTestAppTheme

private const val TAG = "MainActivitymainscreen"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val chartViewModel by viewModels<ChartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SteelsenseTestAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(chartViewModel = chartViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(chartViewModel: ChartViewModel) {
   val result=chartViewModel.chartData.collectAsState().value
    Log.i(TAG, "MainScreen: ${result.toString()}")
    Box(Modifier.fillMaxSize()) {
        StaticLineChartWithToggles(datasets = result)
    }
}

