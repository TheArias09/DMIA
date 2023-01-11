package com.adrienmaginot.todo.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
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
        Text("title")
        Text("description")
        Button( content = {
            Text(text = "Submit")
        }, onClick = {
            val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !")
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