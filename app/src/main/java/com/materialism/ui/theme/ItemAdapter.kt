package com.materialism

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.materialism.databinding.ItemLayoutBinding
import com.materialism.databinding.ItemLayoutMainPageBinding
import com.materialism.sampledata.Item

class ItemAdapter(private var items: List<Item>, private val showEditButton: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (showEditButton) {
      val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      EditViewHolder(binding)
    } else {
      val binding =
          ItemLayoutMainPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      NoEditViewHolder(binding)
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    if (holder is EditViewHolder) {
      holder.binding.itemName.text = item.name
      holder.binding.itemDescription.text = item.description
      holder.binding.itemCategory.text = item.category
      holder.binding.itemLocation.text = item.location
      holder.binding.itemDate.text = item.date
    } else if (holder is NoEditViewHolder) {
      holder.binding.itemName.text = item.name
      holder.binding.itemDescription.text = item.description
      holder.binding.itemCategory.text = item.category
      holder.binding.itemLocation.text = item.location
      holder.binding.itemDate.text = item.date
    }
  }

  override fun getItemCount() = items.size

  class EditViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

  class NoEditViewHolder(val binding: ItemLayoutMainPageBinding) :
      RecyclerView.ViewHolder(binding.root)

  // Methods for sorting
  fun sortByCategory() {
    items = items.sortedBy { it.category }
    notifyDataSetChanged()
  }

  fun sortByNameAsc() {
    items = items.sortedBy { it.name }
    notifyDataSetChanged()
  }

  fun sortByNameDesc() {
    items = items.sortedByDescending { it.name }
    notifyDataSetChanged()
  }
}
