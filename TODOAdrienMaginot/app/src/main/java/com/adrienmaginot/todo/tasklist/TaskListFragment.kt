package com.adrienmaginot.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.adrienmaginot.todo.R
import com.adrienmaginot.todo.data.Api
import com.adrienmaginot.todo.databinding.FragmentTaskListBinding
import com.adrienmaginot.todo.detail.DetailActivity
import kotlinx.coroutines.launch
import java.util.*

class TaskListFragment : Fragment() {

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "Description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    private val adapterListener: TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            taskList = taskList - task
            refreshAdapter()
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("taskToEdit", task)
            editTask.launch(intent)
        }
    }
    private val adapter = TaskListAdapter(adapterListener)

    private var binding: FragmentTaskListBinding? = null

    private val createTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as Task
            taskList = taskList + task
            refreshAdapter()
        }

    private val editTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as Task
            taskList = taskList.map { if (it.id == task.id) task else it }
            refreshAdapter()
        }

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

        // Restore saved taskList
        if (savedInstanceState?.containsKey("count") == true) {
            val count = savedInstanceState.getSerializable("count") as Int
            taskList = listOf()
            for (i in 1..count)
                taskList = taskList + (savedInstanceState.getSerializable("task${i}") as Task)
            refreshAdapter()
        }

        binding?.recyclerView?.adapter = adapter
        binding?.floatingActionButton?.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.removeExtra("taskToEdit")
            createTask.launch(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("count", taskList.count())
        for (i in 1..taskList.count())
            outState.putSerializable("task${i}", taskList[i - 1])
    }

    fun addTask() {
        val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
        taskList = taskList + newTask
        refreshAdapter()
    }

    fun refreshAdapter() {
        adapter.submitList(taskList)
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
        }
    }

}
