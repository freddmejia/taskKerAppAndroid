package gamer.botixone.mytasks.domain.use_case

import gamer.botixone.mytasks.data.local.model.TaskEntity
import gamer.botixone.mytasks.domain.repository.TaskRepository
import gamer.botixone.mytasks.domain.result.TaskResult

class GetAllTasksByStatusUseCase (
    private val taskRepository: TaskRepository
) {
    suspend fun execute(status: Int, limit: Int, offset: Int): TaskResult<List<TaskEntity>> {
        return taskRepository.fetchAllTasksBy(
            status = status,
            limit = limit,
            offset = offset
        )
    }
}