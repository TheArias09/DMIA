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
                    Detail(onValidate)
                }
            }
        }
    }
}

@Composable
fun Detail(onValidate: (Task) -> Unit) {
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
        val title = remember { mutableStateOf(TextFieldValue("Title")) }
        OutlinedTextField(value = title.value,
            onValueChange = { title.value = it })

       /* var newDescription: (String) -> Unit = {}
        var description : String = "description"
        var newTask by remember { mutableStateOf(Task(id = UUID.randomUUID().toString(), title = title, description = description))}
        OutlinedTextField(value = description, onValueChange = newDescription)
        */
        val description = remember { mutableStateOf(TextFieldValue("Description")) }
        OutlinedTextField(value = description.value,
            onValueChange = { description.value = it })
        Button( content = {
            Text(text = "Submit")
        }, onClick = {
            val newTask = Task(id = UUID.randomUUID().toString(), title = title.value.text, description = description.value.text)
            //newTask = newTask.copy(title = "new title")

            onValidate(newTask)

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