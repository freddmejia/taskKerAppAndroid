package gamer.botixone.mytasks.domain.result

sealed class TaskResult<out T> {
    data class Success<out T>(val data: T): TaskResult<T>()
    data class Error(val exception: Throwable) : TaskResult<Nothing>()
    object ErrorEmpty : TaskResult<Nothing>()
}