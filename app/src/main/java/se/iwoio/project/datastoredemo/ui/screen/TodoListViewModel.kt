package se.iwoio.project.datastoredemo.ui.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import se.iwoio.project.datastoredemo.data.TodoItem
import se.iwoio.project.datastoredemo.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private var _todoList =
        mutableStateListOf<TodoItem>()

    fun getAllToDoList(): List<TodoItem> {
        return _todoList;
    }

    fun addTodoList(todoItem: TodoItem) {
        _todoList.add(todoItem)
    }


    fun removeTodoItem(todoItem: TodoItem) {
        _todoList.remove(todoItem)
    }

    fun editTodoItem(originalTodo: TodoItem, editedTodo: TodoItem) {
        val index = _todoList.indexOf(originalTodo)
        _todoList[index] = editedTodo
    }

    fun changeTodoState(todoItem: TodoItem, value: Boolean) {
        val index = _todoList.indexOf(todoItem)

        val newTodo = todoItem.copy(
            title = todoItem.title,
            description = todoItem.description,
            createDate = todoItem.createDate,
            priority = todoItem.priority,
            isDone = value
        )

        _todoList[index] = newTodo
    }

    fun clearAllTodos() {
       _todoList.clear()
    }

    suspend fun storeOrderByTitle(
        orderByTitle: Boolean
    ) {
        if (orderByTitle) {
            _todoList.sortBy { it.title }
        } else {
            _todoList.sortBy { it.createDate }
        }
        settingsRepository.setOrderByTitle(orderByTitle)
    }

    fun getOrderByTitle() : Flow<Boolean> {
        return settingsRepository.getOrderByTitle()
    }

    suspend fun storeOrderByDesc(
        orderByDesc: Boolean
    ) {
        if (orderByDesc) {
            _todoList.sortBy { it.description }
        } else {
            _todoList.sortBy { it.createDate }
        }
        settingsRepository.setOrderByDesc(orderByDesc)
    }

    fun getOrderByDesc() : Flow<Boolean> {
        return settingsRepository.getOrderByDesc()
    }
}
