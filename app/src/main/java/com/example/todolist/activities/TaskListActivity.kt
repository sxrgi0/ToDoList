package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapters.TaskAdapter
import com.example.todolist.data.Category
import com.example.todolist.data.CategoryDAO
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDAO
import com.example.todolist.databinding.ActivityTaskListBinding
import com.example.todolist.databinding.DialogCreateCategoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TaskListActivity : AppCompatActivity() {
    lateinit var binding: ActivityTaskListBinding

    lateinit var categoryDAO: CategoryDAO
    lateinit var category: Category

    lateinit var taskDAO: TaskDAO
    lateinit var taskList: List<Task>

    lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)

        val id = intent.getLongExtra("CATEGORY_ID", -1)
        category = categoryDAO.findById(id)!!
        taskList = emptyList()

        adapter = TaskAdapter(taskList, {
            // He hecho click en una tarea
        }, {
            // He checkeado una tarea
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        supportActionBar?.title = category.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        taskList = taskDAO.findAllByCategory(category)
        adapter.updateItems(taskList)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}