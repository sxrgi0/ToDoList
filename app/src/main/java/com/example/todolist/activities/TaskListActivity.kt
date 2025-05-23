package com.example.todolist.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.PopupMenu
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

        adapter = TaskAdapter(taskList, { position ->
            val task = taskList[position]
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            intent.putExtra("TASK_ID", task.id)
            startActivity(intent)
        }, { position ->
            val task = taskList[position]
            task.done = !task.done
            taskDAO.update(task)
            reloadData()
        }, { position, v ->
            val task = taskList[position]

            val popup = PopupMenu(this, v)
            popup.menuInflater.inflate(R.menu.task_context_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.actionEdit -> {
                        // Respond to context menu item 1 click.
                        val intent = Intent(this, TaskActivity::class.java)
                        intent.putExtra("CATEGORY_ID", category.id)
                        intent.putExtra("TASK_ID", task.id)
                        startActivity(intent)
                        true
                    }

                    R.id.actionDelete -> {
                        // Respond to context menu item 2 click.
                        taskDAO.delete(task)
                        reloadData()
                        true
                    }

                    else -> super.onContextItemSelected(menuItem)
                }
            }
            // Show the popup menu.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true)
            }

            popup.show()
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

        reloadData()
    }

    fun reloadData(){
        taskList = taskDAO.findAllByCategory(category)
        adapter.updateItems(taskList)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.task_context_menu, menu)
    }

    // Then, to handle clicks:
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.editButton -> {
                // Respond to context menu item 1 click.
                true
            }
            R.id.actionDelete -> {
                // Respond to context menu item 2 click.
                true
            }
            else -> super.onContextItemSelected(item)
        }
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