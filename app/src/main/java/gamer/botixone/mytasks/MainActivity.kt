package gamer.botixone.mytasks

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gamer.botixone.mytasks.presentation.ui.menu.MainScreen
import gamer.botixone.mytasks.presentation.ui.menu.TaskScreen
import gamer.botixone.mytasks.ui.theme.MyTasksTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MenuTaskKer()
            /*MyTasksTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MenuTaskKer(){
    val navBarController = rememberNavController()
    MainScreen(
        navBarController = navBarController,
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyTasksTheme {
        Greeting("Android")
    }
}