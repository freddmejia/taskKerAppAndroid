package gamer.botixone.mytasks.domain.repository

import gamer.botixone.mytasks.data.local.model.TaskEntity
import gamer.botixone.mytasks.domain.result.TaskResult

interface TaskRepository {
    suspend fun createTask(taskEntity: TaskEntity): TaskResult<TaskEntity>
    suspend fun updateTask(title: String, description: String, isDone: Boolean, priority: Int, status: Int, id: Int): TaskResult<TaskEntity>
    suspend fun deleteTask(status: Int, id: Int): TaskResult<Boolean>
    suspend fun fetchAllTasksBy(status: Int, limit: Int, offset: Int): TaskResult<List<TaskEntity>>
}