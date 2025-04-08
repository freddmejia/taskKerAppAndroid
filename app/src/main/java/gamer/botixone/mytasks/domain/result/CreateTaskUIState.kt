package gamer.botixone.mytasks.domain.result

sealed class CreateTaskUIState {
    object Idle : CreateTaskUIState()
    object Success : CreateTaskUIState()
    object Error: CreateTaskUIState()
}