package gamer.botixone.mytasks.presentation.ui.menu


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import gamer.botixone.mytasks.presentation.ui.task.CreateTaskScreen


enum class BottomNavPath {
    TASKS, CREATE_TASK
}


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String, val path: String) {
    data object Tasks : BottomNavItem("task", Icons.Filled.Task, "Task", BottomNavPath.TASKS.toString())
    data object CreateTask : BottomNavItem("create_task", Icons.Filled.AddTask, "CreateTask", BottomNavPath.CREATE_TASK.toString())
}

@ExperimentalMaterial3Api
@Composable
fun TabNavigationBar(title: String) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        actions = {
            /*IconButton(
                onClick = {
                    Toast.makeText(LocalContext.current, R.string.forgot_password_screen, Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notification")
            }*/
        }
    )
}

@Composable
fun BottomNavigationBar(navBarController: NavHostController) {
    val navItems = listOf(
        BottomNavItem.Tasks,
        BottomNavItem.CreateTask
    )
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
            )
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navBarController.navigate(item.path) {
                        navBarController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navBarController: NavHostController) {
    Scaffold(
        topBar = {
            TabNavigationBar(title = "TaskKer")
        },
        bottomBar = {
            BottomNavigationBar(
                navBarController = navBarController
            )
        }
    ) { paddingValues ->
        NavigationScreens(
            navBarController = navBarController,
            paddingValues = paddingValues,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreens(navBarController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navBarController, startDestination = BottomNavItem.Tasks.path) {
        composable(BottomNavItem.Tasks.path) {
            TaskHomeScreen(
                modifier = Modifier.padding(paddingValues)
            )
        }
        composable(BottomNavItem.CreateTask.path) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CreateTaskScreen(
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}