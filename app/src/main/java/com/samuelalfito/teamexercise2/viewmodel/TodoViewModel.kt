package com.samuelalfito.teamexercise2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import com.samuelalfito.teamexercise2.model.Todo

enum class TodoFilter { ALL, ACTIVE, COMPLETED }

class TodoViewModel : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    private val _filter = MutableStateFlow(TodoFilter.ALL)

    val todos: StateFlow<List<Todo>> = combine(_todos, _filter) { todos, filter ->
        when (filter) {
            TodoFilter.ALL -> todos
            TodoFilter.ACTIVE -> todos.filter { !it.isDone }
            TodoFilter.COMPLETED -> todos.filter { it.isDone }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, _todos.value)

    val filter: StateFlow<TodoFilter> = _filter

    fun setFilter(newFilter: TodoFilter) {
        _filter.value = newFilter
    }

    fun addTask(title: String) {
        val nextId = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Todo(id = nextId, title = title)
        _todos.value = _todos.value + newTask
    }
    fun toggleTask(id: Int) {
        _todos.value = _todos.value.map { t ->
            if (t.id == id) t.copy(isDone = !t.isDone) else t
        }
    }
    fun deleteTask(id: Int) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }
}