package com.adrienmaginot.todo.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.adrienmaginot.todo.R
import com.adrienmaginot.todo.databinding.FragmentTaskListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TaskListFragment : Fragment() {

    private var taskList = listOf(
    Task(id = "id_1", title = "Task 1", description = "description 1"),
    Task(id = "id_2", title = "Task 2"),
    Task(id = "id_3", title = "Task 3")
    )

    private val adapter = TaskListAdapter()

    private var binding: FragmentTaskListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        val rootView = binding!!.root
        //val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        adapter.submitList(taskList)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener { addTask() }
        */

        binding?.recyclerView?.adapter = adapter
        binding?.floatingActionButton?.setOnClickListener{ addTask() }

        adapter.onClickDelete = {
            task -> taskList = taskList - task
            refreshAdapter()
        }
    }

    fun addTask()
    {
        val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
        taskList = taskList + newTask
        refreshAdapter()
    }

    fun refreshAdapter()
    {
        adapter.submitList(taskList)
        adapter.notifyDataSetChanged()
    }


}
