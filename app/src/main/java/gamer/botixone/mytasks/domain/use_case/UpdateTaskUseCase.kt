package gamer.botixone.mytasks.domain.use_case

import gamer.botixone.mytasks.data.local.model.TaskEntity
import gamer.botixone.mytasks.domain.repository.TaskRepository
import gamer.botixone.mytasks.domain.result.TaskResult

class UpdateTaskUseCase (
    private val taskRepository: TaskRepository
) {
    suspend fun execute(title: String, description: String, isDone: Boolean, priority: Int, status: Int, id: Int): TaskResult<TaskEntity> {
        return taskRepository.updateTask(
            title = title,
            description = description,
            isDone = isDone,
            priority = priority,
            status = status,
            id = id
        )
    }
}