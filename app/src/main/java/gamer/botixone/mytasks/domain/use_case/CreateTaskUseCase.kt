package gamer.botixone.mytasks.domain.use_case

import gamer.botixone.mytasks.data.local.model.TaskEntity
import gamer.botixone.mytasks.domain.repository.TaskRepository
import gamer.botixone.mytasks.domain.result.TaskResult

class CreateTaskUseCase  (
    private val taskRepository: TaskRepository
) {
    suspend fun execute(taskEntity: TaskEntity): TaskResult<TaskEntity> {
        return taskRepository.createTask(
            taskEntity = taskEntity
        )
    }
}