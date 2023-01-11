package com.adrienmaginot.todo.tasklist

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}