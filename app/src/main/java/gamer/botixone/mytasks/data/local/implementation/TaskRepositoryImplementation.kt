package gamer.botixone.mytasks.data.local.implementation

import gamer.botixone.mytasks.data.local.dao.TaskDao
import gamer.botixone.mytasks.data.local.model.TaskEntity
import gamer.botixone.mytasks.domain.repository.TaskRepository
import gamer.botixone.mytasks.domain.result.TaskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepositoryImplementation @Inject constructor(
    private val taskDao: TaskDao
): TaskRepository {
    override suspend fun createTask(taskEntity: TaskEntity): TaskResult<TaskEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val id = taskDao.createTask(taskEntity).toInt()
                val newMood = taskDao.fetchTaskBy(id = id)
                TaskResult.Success(newMood!!)
            }catch (e: Exception){
                TaskResult.Error(e)
            }
        }
    }

    override suspend fun updateTask(
        title: String,
        description: String,
        isDone: Boolean,
        priority: Int,
        status: Int,
        id: Int
    ): TaskResult<TaskEntity> {
        return withContext(Dispatchers.IO) {
            try {
                taskDao.updateTask(
                    title = title,
                    description = description,
                    isDone = isDone,
                    priority = priority,
                    id = id,
                    status = status
                )
                val task = taskDao.fetchTaskBy(id = id)
                TaskResult.Success(task!!)
            }
            catch (e: Exception){
                TaskResult.Error(e)
            }
        }
    }

    override suspend fun deleteTask(status: Int, id: Int): TaskResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                taskDao.deleteTask(
                    status = status,
                    id = id
                )
                TaskResult.Success(true)
            }
            catch (e: Exception){
                TaskResult.Error(e)
            }
        }
    }

    override suspend fun fetchAllTasksBy(status: Int, limit: Int,offset: Int): TaskResult<List<TaskEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val tasks = taskDao.fetchAllTasksBy(
                    status = status,
                    limit = limit,
                    offset = offset
                )
                TaskResult.Success(tasks)
            }
            catch (e: Exception){
                TaskResult.Error(e)
            }
        }
    }
}