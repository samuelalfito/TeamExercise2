package com.samuelalfito.teamexercise2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samuelalfito.teamexercise2.viewmodel.TodoViewModel
import com.samuelalfito.teamexercise2.viewmodel.TodoFilter

@Composable
fun TodoScreen(vm: TodoViewModel = viewModel()) {
    val todos by vm.todos.collectAsState()
    val filter by vm.filter.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    var searchText by rememberSaveable { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        val tabs = listOf(TodoFilter.ALL to "Semua", TodoFilter.ACTIVE to "Aktif", TodoFilter.COMPLETED to "Selesai")
        TabRow(selectedTabIndex = tabs.indexOfFirst { it.first == filter }) {
            tabs.forEachIndexed { index, pair ->
                Tab(
                    selected = pair.first == filter,
                    onClick = { vm.setFilter(pair.first) },
                    text = { Text(pair.second) }
                )
            }
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Cari tugas...") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Tambah tugas...") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    vm.addTask(text.trim())
                    text = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) { Text("Tambah") }

        Divider()

        LazyColumn {
            items(todos.filter { it.title.contains(searchText, ignoreCase = true) }) { todo ->
                TodoItem(
                    todo = todo,
                    onToggle = { vm.toggleTask(todo.id) },
                    onDelete = { vm.deleteTask(todo.id) }
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun TodoscreenPreview() {
    TodoScreen()
}