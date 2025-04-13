package ir.simyapp.steelsensetestapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.simyapp.steelsensetestapp.presentation_layer.AnimatedLineChartWithTogglesOnVisibility
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
                    App(chartViewModel = chartViewModel)
                }
            }
        }
    }
}
@Composable
fun App(chartViewModel: ChartViewModel) {
    val navController = rememberNavController()
    NavigationGraph(navController = navController, chartViewModel = chartViewModel)
}
@Composable
fun NavigationGraph(navController: NavHostController, chartViewModel: ChartViewModel) {
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") { MainScreen(navController,chartViewModel) }
        composable("static_chart_screen") {
            StaticLineChartWithToggles(datasets = chartViewModel.chartData.collectAsState().value)
        }
        composable("animated_chart_screen") {
            AnimatedLineChartWithTogglesOnVisibility(datasets = chartViewModel.chartData.collectAsState().value)
        }
    }
}


@Composable
fun MainScreen(navController: NavHostController,chartViewModel: ChartViewModel) {

    var btn1Click by remember{ mutableStateOf(false) }
    var btn2Click by remember{ mutableStateOf(false) }
    if(btn2Click){
        navController.navigate("animated_chart_screen") {
            launchSingleTop = true
            popUpTo("main_screen") { inclusive = false }
        }

    }
    if(btn1Click){
        navController.navigate("static_chart_screen") {
            launchSingleTop = true
            popUpTo("main_screen") { inclusive = false }
        }

    }

    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            btn1Click=!btn1Click
        }, modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 30.dp)) {
            Text(text = "StaticLineChartWithToggles", color = Color.White, fontSize = 18.sp)
        }
        
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
                         btn2Click=!btn2Click
        }, modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 30.dp)) {
            Text(text = "AnimatedLineChartWithToggles", color = Color.White, fontSize = 18.sp)
        }
    }
}

