package com.adrienmaginot.todo.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adrienmaginot.todo.detail.ui.theme.TODOAdrienMaginotTheme
import com.adrienmaginot.todo.tasklist.Task
import java.util.*

class DetailActivity : ComponentActivity() {

    var initialTask : Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOAdrienMaginotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val onValidate: (Task) -> Unit = {
                            task -> this.intent.putExtra("task", task)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    initialTask = intent?.getSerializableExtra("taskToEdit") as Task?

                    Detail(onValidate, this)
                }
            }
        }
    }
}

@Composable
fun Detail(onValidate: (Task) -> Unit, activity: DetailActivity) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Task Detail",
            style = MaterialTheme.typography.h2
        )

        /*var newTitle: (String) -> Unit = {}
        var title : String = "Title"
        OutlinedTextField(value = title, onValueChange = newTitle)
        */
        /*val title = remember { mutableStateOf(activity.initialTask?.title) }
        if (title.value == null){OutlinedTextField(value = "title",
            onValueChange = { title.value = it })}
        else {OutlinedTextField(value = title.value!!,
            onValueChange = { title.value = it })}
        */

       /* var newDescription: (String) -> Unit = {}
        var description : String = "description"
        var newTask by remember { mutableStateOf(Task(id = UUID.randomUUID().toString(), title = title, description = description))}
        OutlinedTextField(value = description, onValueChange = newDescription)
        */
        /*val description = remember { mutableStateOf(activity.initialTask?.description) }
        if (description.value == null){OutlinedTextField(value = "description",
            onValueChange = { description.value = it })}
        else {OutlinedTextField(value = description.value!!,
            onValueChange = { description.value = it })}
        */
        var task by remember { mutableStateOf(Task(title = "", id = UUID.randomUUID().toString(), description = ""))}
        if (activity.initialTask != null){task = activity.initialTask!!}
        OutlinedTextField(value = task.title, onValueChange = {task = task.copy(title = it)})
        OutlinedTextField(value = task.description, onValueChange = {task = task.copy(description = it)})
        Button( content = {
            Text(text = "Submit")
        }, onClick = {
            //val newTask = Task(id = UUID.randomUUID().toString(), title = title.value!!, description = description.value!!)
            //newTask = newTask.copy(title = "new title")

            onValidate(task)

        })
    }
}


@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TODOAdrienMaginotTheme {

        //Detail(onValidate)
    }
}