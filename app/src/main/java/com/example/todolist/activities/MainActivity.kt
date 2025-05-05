package com.example.todolist.activities

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapters.CategoryAdapter
import com.example.todolist.data.Category
import com.example.todolist.data.CategoryDAO
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.DialogCreateCategoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var adapter: CategoryAdapter
    var categoryList: List<Category> = emptyList()

    lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryDAO = CategoryDAO(this)

        adapter = CategoryAdapter(categoryList, {
            // He hecho click en una categoria
        }, {
            // Editar
            val category= categoryList[it]
            showCategoryDialog(category)
        }, {
            // Borrar
            val category= categoryList[it]
            categoryDAO.delete(category)
            loadData()
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addCategoryButton.setOnClickListener {
            showCategoryDialog(Category(-1L,""))
        }
    }

    fun showCategoryDialog(category: Category) {
        val dialogBinding = DialogCreateCategoryBinding.inflate(layoutInflater)

        dialogBinding.titleEditText.setText(category.title)

        var dialogTitle = ""
        var dialogIcon = 0
        if(category.id != -1L){
            dialogTitle = "Edit category"
            dialogIcon = R.drawable.ic_edit
        } else {
            dialogTitle = "Create category"
            dialogIcon = R.drawable.ic_add
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(dialogTitle)
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok, { dialog, which ->
                category.title = dialogBinding.titleEditText.text.toString()
                if(category.id != -1L){
                    categoryDAO.update(category)
                } else {
                    categoryDAO.insert(category)
                }
                loadData()
            })

            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(dialogIcon)
            .show()
    }

    fun loadData(){
        categoryList = categoryDAO.findAll()
        adapter.updateItems(categoryList)
    }
}