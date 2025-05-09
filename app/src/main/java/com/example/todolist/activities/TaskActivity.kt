package com.example.todolist.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist.R
import com.example.todolist.data.Category
import com.example.todolist.data.CategoryDAO
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDAO
import com.example.todolist.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskBinding

    lateinit var category: Category
    lateinit var task: Task

    lateinit var categoryDAO: CategoryDAO
    lateinit var taskDAO: TaskDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)

        val categoryID = intent.getLongExtra("CATEGORY_ID", -1)
        category = categoryDAO.findById(categoryID)!!

        binding.saveButton.setOnClickListener {
            val title = binding.itemEditText.text.toString()
            task = Task(-1L, title, false, category)
            taskDAO.insert(task)
            finish()
        }
    }
}