package gamer.botixone.mytasks.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String = "",
    var description: String? = null,
    var isDone: Boolean = false,
    val timestamp: Long = 0,
    var priority: Int = 1,
    var status: Int = 1
)