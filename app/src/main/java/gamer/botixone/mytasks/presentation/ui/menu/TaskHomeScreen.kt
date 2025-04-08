package gamer.botixone.mytasks.presentation.ui.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gamer.botixone.mytasks.data.local.model.TaskEntity
import gamer.botixone.mytasks.domain.result.TaskResult
import gamer.botixone.mytasks.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskHomeScreen(
    modifier: Modifier,
    viewModel: TaskViewModel = hiltViewModel()
){
    val tasksResult by viewModel.tasks.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val tasks = (tasksResult as? TaskResult.Success)?.data ?: emptyList()

    LaunchedEffect(Unit) {
        viewModel.loadMoreTasks(status = 1)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetTasks()
        }
    }


    Box (
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        contentAlignment = Alignment.TopStart
    ){
        TaskScreen(
            tasks = tasks,
            onTaskCheckedChange = { task, isChecked ->
                viewModel.updateTask(task.copy(isDone = isChecked))
            },
            onTaskDismissed = { task ->
                viewModel.deleteTask(task)
            },
            onUndo = { task ->
                viewModel.restoreTask(task.copy(status = 1))
            },
            snackbarHostState = snackbarHostState
        )
    }
}

fun generateDummyTasks(): List<TaskEntity> {
    val now = System.currentTimeMillis()
    val oneDayMillis = 24 * 60 * 60 * 1000

    return listOf(
        TaskEntity(id = 1, title = "Comprar leche", description = "En el supermercado", isDone = false, timestamp = now, priority = 1),
        TaskEntity(id = 2, title = "Llamar al doctor", isDone = true, timestamp = now, priority = 2),
        TaskEntity(id = 3, title = "Terminar reporte", description = "Enviar antes de las 18h", isDone = false, timestamp = now - oneDayMillis, priority = 3),
        TaskEntity(id = 4, title = "Ir al gimnasio", isDone = false, timestamp = now - oneDayMillis * 2, priority = 2),
        TaskEntity(id = 5, title = "Leer libro", isDone = true, timestamp = now - oneDayMillis * 2, priority = 1),
        TaskEntity(id = 6, title = "Lavar ropa", isDone = false, timestamp = now + oneDayMillis, priority = 1),
        TaskEntity(id = 7, title = "Lavar ropa", isDone = false, timestamp = now + oneDayMillis, priority = 1),
        TaskEntity(id = 8, title = "Lavar ropa", isDone = false, timestamp = now + oneDayMillis, priority = 1),
        TaskEntity(id = 9, title = "Lavar ropa", isDone = false, timestamp = now + oneDayMillis, priority = 1),
        TaskEntity(id = 10, title = "Lavar ropa", isDone = false, timestamp = now + oneDayMillis, priority = 1),
        TaskEntity(id = 11, title = "Lavar ropa", isDone = false, timestamp = now + oneDayMillis, priority = 1),
        TaskEntity(id = 12, title = "Lavar ropa", isDone = false, timestamp = now + oneDayMillis, priority = 1)

    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ShowTaskFormScreen(){
    val snackbarHostState = remember { SnackbarHostState() }
    TaskScreen(
        tasks =  generateDummyTasks(),
        onTaskCheckedChange = { _, _ -> },
        onTaskDismissed = {},
        onUndo = {},
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(
    tasks: List<TaskEntity>,
    onTaskCheckedChange: (TaskEntity, Boolean) -> Unit,
    onTaskDismissed: (TaskEntity) -> Unit,
    onUndo: (TaskEntity) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    var recentlyDeletedTask by remember { mutableStateOf<TaskEntity?>(null) }
    var snackbarJob by remember { mutableStateOf<Job?>(null) }

    val groupedTasks = tasks.groupBy {
        Instant.ofEpochMilli(it.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            groupedTasks.forEach { (date, taskList) ->
                stickyHeader {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("EEEE, dd MMM")),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(
                    items = taskList,
                    key = { it.id }
                ) { task ->
                    var isVisible by remember { mutableStateOf(true) }

                    if (isVisible) {
                        val swipeState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    isVisible = false

                                    onTaskDismissed(task)
                                    recentlyDeletedTask = task

                                    snackbarJob?.cancel()
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Tarea eliminada",
                                            actionLabel = "Deshacer",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onUndo(task)
                                        }
                                    }

                                    true
                                } else false
                            }
                        )


                        AnimatedVisibility(
                            visible = isVisible,
                            exit = shrinkVertically(animationSpec = tween(250)) + fadeOut()
                        ) {
                            SwipeToDismissBox(
                                state = swipeState,
                                backgroundContent = {
                                    DeleteBackground()
                                },
                                content = {
                                    TaskItem(task = task) { checked ->
                                        onTaskCheckedChange(task, checked)
                                    }
                                },
                                enableDismissFromEndToStart = true,
                                enableDismissFromStartToEnd = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 15.dp)
            .background(Color.Red, shape = RoundedCornerShape(12.dp))
            .padding(end = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = Color.White
        )
    }
}


@Composable
fun TaskItem(task: TaskEntity, onCheckedChange: (Boolean) -> Unit) {
    val transition = updateTransition(targetState = task.isDone, label = "taskStateTransition")

    val backgroundColor = if (task.isDone) {
        Color(0xFFDFF5E1)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor by transition.animateColor(label = "textColor") { isDone ->
        if (isDone) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor,
                        textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                )
                task.description?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(color = textColor.copy(alpha = 0.7f))
                    )
                }
            }
        }
    }
}
