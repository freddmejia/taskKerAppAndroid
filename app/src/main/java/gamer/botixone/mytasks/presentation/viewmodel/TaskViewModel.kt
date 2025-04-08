package gamer.botixone.mytasks.presentation.viewmodel

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamer.botixone.mytasks.data.local.model.TaskEntity
import gamer.botixone.mytasks.domain.result.CreateTaskUIState
import gamer.botixone.mytasks.domain.result.TaskResult
import gamer.botixone.mytasks.domain.use_case.CreateTaskUseCase
import gamer.botixone.mytasks.domain.use_case.DeleteTaskUseCase
import gamer.botixone.mytasks.domain.use_case.GetAllTasksByStatusUseCase
import gamer.botixone.mytasks.domain.use_case.UpdateTaskUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getAllTasksByStatusUseCase: GetAllTasksByStatusUseCase,
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _uiEvent = MutableSharedFlow<CreateTaskUIState>()
    val uiEvent: SharedFlow<CreateTaskUIState> = _uiEvent.asSharedFlow()

    private val _tasks = MutableLiveData<TaskResult<List<TaskEntity>>>()
    val tasks: LiveData<TaskResult<List<TaskEntity>>> = _tasks

    private var currentOffset = 0
    private val limitPerPage = 5
    private val maxStored = 100

    fun createTask(task: TaskEntity) = viewModelScope.launch {
        _loading.value = true
        val resultCreated = createTaskUseCase.execute(task)

        val event = when (resultCreated) {
            is TaskResult.Success -> CreateTaskUIState.Success
            is TaskResult.Error -> CreateTaskUIState.Error
            TaskResult.ErrorEmpty -> CreateTaskUIState.Error
        }
        _uiEvent.emit(event)
        delay(800)
        _loading.value = false
    }

    fun loadMoreTasks(status: Int) = viewModelScope.launch {
        val previousList = (_tasks.value as? TaskResult.Success)?.data ?: emptyList()
        val result = getAllTasksByStatusUseCase.execute(
            status = status,
            limit = limitPerPage,
            offset = currentOffset
        )

        if (result is TaskResult.Success) {
            val combined = (previousList + result.data)
                .distinctBy { it.id }
                .takeLast(maxStored)

            _tasks.value = TaskResult.Success(combined)
            currentOffset += limitPerPage
            return@launch
        }
        if (previousList.isNotEmpty()) {
            _tasks.value = TaskResult.Success(previousList)
        }
    }

    fun updateTask(task: TaskEntity) = viewModelScope.launch {
        val updateResult = updateTaskUseCase.execute(
            title = task.title,
            description = task.description.toString(),
            isDone = task.isDone,
            priority = task.priority,
            status = task.status,
            id = task.id
        )
        if (updateResult is TaskResult.Success<*>) {
            val currentList = (_tasks.value as? TaskResult.Success)?.data ?: return@launch
            val updatedList = currentList.map {
                if (it.id == task.id) task else it
            }
            _tasks.value = TaskResult.Success(updatedList)
        }
    }

    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        val deteledResult = deleteTaskUseCase.execute(
            status = 0,
            id = task.id
        )
        var event: CreateTaskUIState = CreateTaskUIState.Idle
        when (deteledResult) {
            is TaskResult.Success -> {
                event = CreateTaskUIState.Success
                val currentList = (_tasks.value as? TaskResult.Success)?.data ?: return@launch
                val updatedList = currentList.filter { it.id != task.id }
                _tasks.value = TaskResult.Success(updatedList)
            }
            is TaskResult.Error -> CreateTaskUIState.Error
            TaskResult.ErrorEmpty -> CreateTaskUIState.Error
        }
        _uiEvent.emit(event)
    }

    fun restoreTask(task: TaskEntity) = viewModelScope.launch {
        val restored = updateTaskUseCase.execute(
            title = task.title,
            description = task.description.toString(),
            isDone = task.isDone,
            priority = task.priority,
            status = task.status,
            id = task.id
        )
        if (restored is TaskResult.Success) {
            val currentList = (_tasks.value as? TaskResult.Success)?.data ?: return@launch
            val updatedList = currentList.toMutableList().apply { add(0, task) }
            _tasks.value = TaskResult.Success(updatedList)
        }
    }


    fun resetTasks() {
        _tasks.value = TaskResult.Success(emptyList())
        currentOffset = 0
    }
}