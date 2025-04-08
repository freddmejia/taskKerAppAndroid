package gamer.botixone.mytasks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gamer.botixone.mytasks.data.local.model.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createTask(taskEntity: TaskEntity): Long

    @Query("update taskentity set status = :status where id = :id")
    fun deleteTask(status: Int, id: Int)

    @Query("update taskentity set title = :title, " +
            "description = :description, isDone = :isDone, priority = :priority, status = :status where id = :id")
    fun updateTask(title: String, description: String, isDone: Boolean, priority: Int, status: Int,  id: Int)

    @Query("select * from taskentity where status = :status ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun fetchAllTasksBy(status: Int, limit: Int, offset: Int): List<TaskEntity>

    @Query("select * from taskentity where id = :id")
    fun fetchTaskBy(id: Int): TaskEntity?
}