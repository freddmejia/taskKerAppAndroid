package gamer.botixone.mytasks.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gamer.botixone.mytasks.data.local.dao.TaskDao
import gamer.botixone.mytasks.data.local.db.DataModelDatabase
import gamer.botixone.mytasks.data.local.implementation.TaskRepositoryImplementation
import gamer.botixone.mytasks.domain.repository.TaskRepository
import gamer.botixone.mytasks.domain.use_case.CreateTaskUseCase
import gamer.botixone.mytasks.domain.use_case.DeleteTaskUseCase
import gamer.botixone.mytasks.domain.use_case.GetAllTasksByStatusUseCase
import gamer.botixone.mytasks.domain.use_case.UpdateTaskUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImplementation(taskDao = taskDao)
    }

    @Singleton
    @Provides
    fun provideCreateTaskUseCase(taskRepository: TaskRepository): CreateTaskUseCase {
        return CreateTaskUseCase(taskRepository = taskRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateTaskUseCase(taskRepository: TaskRepository): UpdateTaskUseCase {
        return UpdateTaskUseCase(taskRepository = taskRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteTaskUseCase(taskRepository: TaskRepository): DeleteTaskUseCase {
        return DeleteTaskUseCase(taskRepository = taskRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllTaskByStatusUseCase(taskRepository: TaskRepository): GetAllTasksByStatusUseCase {
        return GetAllTasksByStatusUseCase(taskRepository = taskRepository)
    }

    //Db, dao's
    @Singleton
    @Provides
    fun provideDataModelDatabase(@ApplicationContext appContext: Context) = DataModelDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideTaskDao(db: DataModelDatabase) = db.taskDao()




}