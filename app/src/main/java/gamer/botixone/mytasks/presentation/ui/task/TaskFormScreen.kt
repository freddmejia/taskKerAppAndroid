package gamer.botixone.mytasks.presentation.ui.task

import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gamer.botixone.mytasks.data.local.model.TaskEntity
import gamer.botixone.mytasks.domain.result.CreateTaskUIState
import gamer.botixone.mytasks.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/*
 HomeScreen (Lista de tareas)
 Mostrar lista de tareas usando LazyColumn.
 SwipeToDismiss (eliminar tarea con gesto).
 Mostrar Snackbar al eliminar tarea con acción Undo.
 Mostrar tareas agrupadas por día (opcional).
 Checkbox para marcar como completada.
 Animaciones suaves al cambiar estado.
 */

@Composable
fun CreateTaskScreen(
    modifier: Modifier,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val loading by viewModel.loading.observeAsState(initial = false)

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(1) }
    val isValid = title.isNotBlank()
    val uiEvent = viewModel.uiEvent

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is CreateTaskUIState.Success -> {
                    snackbarHostState.showSnackbar("¡Tarea guardada!")
                    title = ""
                    description = ""
                    priority = 1
                }

                is CreateTaskUIState.Error -> {
                    snackbarHostState.showSnackbar("Error al crear la tarea.")
                }
                else -> {}
            }
        }
    }

    Box (
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Nueva Tarea",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),

            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            PrioritySelector(selected = priority, onPriorityChange = { priority = it })

            Spacer(modifier = Modifier.height(16.dp))
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            else {
                SimpleButtonLogin(
                    text = "Guardar",
                    buttonColors = ButtonDefaults.buttonColors(),
                    enabled = isValid
                ) {
                    val task = TaskEntity(
                        title = title,
                        description = description.ifBlank { null },
                        isDone = false,
                        timestamp = System.currentTimeMillis(),
                        priority = priority
                    )
                    viewModel.createTask(task)
                }
            }

        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun SimpleButtonLogin(text: String, buttonColors: ButtonColors, enabled: Boolean = true,simpleOnClickAction: () -> Unit) {
    Button(onClick = { simpleOnClickAction() },
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        colors = buttonColors,
        enabled = enabled
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun PrioritySelector(selected: Int, onPriorityChange: (Int) -> Unit) {
    Column {
        Text(
            text = "Prioridad",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(1, 2, 3).forEach { level ->
                val label = when (level) {
                    1 -> "Baja"
                    2 -> "Media"
                    3 -> "Alta"
                    else -> level.toString()
                }
                OutlinedButton(
                    onClick = { onPriorityChange(level) },
                    border = if (selected == level) {
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    } else null
                ) {
                    Text(text = label)
                }
            }
        }
    }
}

