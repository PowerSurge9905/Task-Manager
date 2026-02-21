package com.powersurge9905.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.powersurge9905.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                MainScreen()
            }
        }
    }
}

// Class for storing task data
data class Task(
    val id: Int,
    val name: String,
    var complete: Boolean
)

// Text input field and button
@Composable
fun TaskInputField(
    taskName: String,
    onValueChange: (String) -> Unit,
    onTaskAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        TextField(
            value = taskName,
            onValueChange = onValueChange,
            label = { Text("Enter task") },
            singleLine = true,
            modifier = Modifier.weight(1f).padding(end = 16.dp)
        )
        Button(
            onClick = onTaskAdd
        ) {
            Text("Add Task")
        }
    }
}

// List of task items
@Composable
fun TaskList(
    taskList: List<Task>,
    onCloseTask: (Task) -> Unit,
    onTaskCompleteChange: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(16.dp)
    ) {
        items(
            items = taskList,
            key = { task -> task.id }
        ) { task ->
            TaskItem(
                taskName = task.name,
                complete = task.complete,
                onCompletionChange = {
                    isChecked -> onTaskCompleteChange(task, isChecked)
                },
                onClose = { onCloseTask(task) }
            )
        }
    }
}

// Individual task items + checkbox & delete button
@Composable
fun TaskItem(
    taskName: String,
    complete: Boolean,
    onCompletionChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = complete,
            onCheckedChange = {
                isChecked -> onCompletionChange(isChecked)
            }
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = taskName,
            textDecoration = if (complete) TextDecoration.LineThrough else null,
            color = if (complete) Color.Gray else Color.Unspecified
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Delete, contentDescription = "Close")
        }
    }
}

/*
I genuinely could not figure out how to maintain state
between app destruction with just remember, this is the best
alternative solution that I could find
*/
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var taskName by rememberSaveable { mutableStateOf("") }
    val taskList = rememberSaveable(
        // Save the current taskList
        saver = listSaver(
            save = {
                it.map { task ->
                    mapOf(
                        "id" to task.id,
                        "name" to task.name,
                        "complete" to task.complete
                    )
                }
            },
            // Restore the saved task list
            restore = {
                val list = mutableStateListOf<Task>()
                it.forEach { item ->
                    val map = item as Map<*, *>
                    list.add(
                        Task(
                            map["id"] as Int,
                            map["name"] as String,
                            map["complete"] as Boolean
                        )
                    )
                }
                list
            }
        )
    ) {
        mutableStateListOf<Task>()
    }
    var taskId by rememberSaveable { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFC7C7FF)
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(
                text = "Task Manager",
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp)
            )
            TaskInputField(
                taskName = taskName,
                // Update taskName state variable
                onValueChange =  { taskName = it },
                // Check if the text input field is blank,
                // Create new task and add it to taskList if not blank
                onTaskAdd = {
                    if (taskName.isNotBlank()) {
                        taskList.add(Task(taskId, taskName, false))
                        taskName = ""
                        taskId++
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TaskList(
                taskList = taskList,
                // Deletes task from taskList
                onCloseTask = { task -> taskList.remove(task)},
                // Updates task's complete state and checkbox
                onTaskCompleteChange = {task, isChecked ->
                    val index = taskList.indexOf(task)
                    if (index != -1) {
                        taskList[index] = task.copy(complete = isChecked)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskManagerTheme {
        MainScreen()
    }
}
