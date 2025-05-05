package com.example.todolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todolist.data.Category
import com.example.todolist.databinding.ItemCategoryBinding

class CategoryAdapter(
    var items: List<Category>,
    val onItemClick: (position: Int)-> Unit,
    val onItemEdit: (position: Int)-> Unit,
    val onItemDelete: (position: Int)-> Unit
) : Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = items[position]
        holder.render(category)
        holder.itemView.setOnClickListener {
            onItemClick (position)
        }

        holder.binding.editButton.setOnClickListener {
            onItemEdit(position)
        }

        holder.binding.deleteButton.setOnClickListener {
            onItemDelete(position)
        }
    }

    fun updateItems(items: List<Category>) {
        this.items = items
        notifyDataSetChanged()
    }

}

class CategoryViewHolder(val binding : ItemCategoryBinding) : ViewHolder(binding.root){

    fun render(category: Category){
        binding.titleTextView.text= category.title
    }

}