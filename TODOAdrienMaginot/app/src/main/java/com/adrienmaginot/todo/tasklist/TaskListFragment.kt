package com.adrienmaginot.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import coil.load
import com.adrienmaginot.todo.R
import com.adrienmaginot.todo.data.Api
import com.adrienmaginot.todo.databinding.FragmentTaskListBinding
import com.adrienmaginot.todo.detail.DetailActivity
import com.adrienmaginot.todo.user.UserActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class TaskListFragment : Fragment() {

    private val adapterListener: TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            viewModel.remove(task)
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
            viewModel.add(task)
        }

    private val editTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as Task
            viewModel.edit(task)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        val rootView = binding!!.root
        //val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        adapter.submitList(viewModel.tasksStateFlow.value)
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
        /* if (savedInstanceState?.containsKey("count") == true) {
            val count = savedInstanceState.getSerializable("count") as Int
            taskList = listOf()
            for (i in 1..count)
                taskList = taskList + (savedInstanceState.getSerializable("task${i}") as Task)
            refreshAdapter()
        } */

        binding?.recyclerView?.adapter = adapter
        binding?.floatingActionButton?.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.removeExtra("taskToEdit")
            createTask.launch(intent)
        }

        binding?.imageView?.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            startActivity(intent)
        }

        // Dans onViewCreated()
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est executée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                adapter.submitList(newList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    /* override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("count", taskList.count())
        for (i in 1..taskList.count())
            outState.putSerializable("task${i}", taskList[i - 1])
    } */

     /* fun addTask() {
        val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}", description = "")
        taskList = taskList + newTask
        refreshAdapter()
    } */

    /*
    fun refreshAdapter() {
        adapter.submitList(viewModel.tasksStateFlow.value)
        adapter.notifyDataSetChanged()
    } */

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            binding?.textView3?.text = user.name
            binding?.imageView?.load("https://ih1.redbubble.net/image.3485158063.7564/st,small,845x845-pad,1000x1000,f8f8f8.jpg")

        }
        // Dans onResume()
        viewModel.refresh() // on demande de rafraîchir les données sans attendre le retour directement
    }

    private val viewModel: TasksListViewModel by viewModels()

}
