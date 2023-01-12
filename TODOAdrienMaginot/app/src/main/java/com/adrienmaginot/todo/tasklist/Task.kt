package com.adrienmaginot.todo.tasklist

data class Task(val id : String, val title : String, val description : String = "Description") : java.io.Serializable
{

}
