package com.powersurge9905.taskmanager

import android.R.attr.checked
import android.R.attr.label
import android.R.attr.onClick
import android.R.attr.singleLine
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.powersurge9905.taskmanager.ui.theme.TaskManagerTheme

const val TAG = "TestingTag"

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

@Composable
fun TaskInputField(modifier: Modifier = Modifier) {
    var taskName by remember { mutableStateOf("") }
    Row{
        TextField(
            value = taskName,
            onValueChange = {
                taskName = it
            },
            label = { Text("Enter task") },
            singleLine = true
        )
        Button(onClick = { taskList }) {
            Text("Add Task")
        }
    }
}

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    taskList: List<String> = remember { mutableStateListOf("") }
) {
    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        items(taskList.size) {

        }
    }
}

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
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = taskName
        )
        Checkbox(
            checked = complete,
            onCheckedChange = onCompletionChange
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Delete, contentDescription = "Close")
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column{
            Text(text = "Task Manager")
            TaskInputField()
            TaskList()
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