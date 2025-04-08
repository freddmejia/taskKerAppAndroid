package gamer.botixone.mytasks.domain.use_case

import gamer.botixone.mytasks.domain.repository.TaskRepository
import gamer.botixone.mytasks.domain.result.TaskResult

class DeleteTaskUseCase (
    private val taskRepository: TaskRepository
) {
    suspend fun execute(status: Int, id: Int): TaskResult<Boolean> {
        return taskRepository.deleteTask(
            status = status,
            id = id
        )
    }
}