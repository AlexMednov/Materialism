package com.materialism.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.materialism.databinding.ItemLayoutBinding
import com.materialism.databinding.ItemLayoutMainPageBinding
import com.materialism.dto.Item
import com.materialism.utils.ImageRenderer

class ItemAdapter(
  private var items: List<Item>,
  private val showEditButton: Boolean,
  val imageRenderer: ImageRenderer
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var onClickListener: OnClickListener? = null
  private var onEditButtonClickListener: OnClickListener? = null

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
    val imageUri = item.imageUri.toUri()
    var image: Bitmap? = null
    try {
      image = imageRenderer.getThumbnail(imageUri, 240)
    } catch (e: Exception) {
      Log.e("ImageRenderer", e.toString())
    }

    if (holder is EditViewHolder) {
      holder.binding.itemImage.setImageBitmap(image)
      holder.binding.itemName.text = item.name
      holder.binding.itemDescription.text = item.description
      holder.binding.itemCategory.text = item.category
      holder.binding.itemLocation.text = item.location
      holder.binding.itemDate.text = item.date
      holder.binding.editButton.setOnClickListener {
        onEditButtonClickListener?.onClick(position, item)
      }
      holder.itemView.setOnClickListener { onClickListener?.onClick(position, item) }
    } else if (holder is NoEditViewHolder) {
      holder.binding.itemImage.setImageBitmap(image)
      holder.binding.itemName.text = item.name
      holder.binding.itemDescription.text = item.description
      holder.binding.itemCategory.text = item.category
      holder.binding.itemLocation.text = item.location
      holder.binding.itemDate.text = item.date
      holder.itemView.setOnClickListener { onClickListener?.onClick(position, item) }
    }
  }

  override fun getItemCount() = items.size

  // Set the click listener for the adapter
  fun setOnClickListener(listener: OnClickListener?) {
    this.onClickListener = listener
  }

  fun setOnEditButtonClickListener(listener: OnClickListener?) {
    this.onEditButtonClickListener = listener
  }

  fun getItems(): List<Item> = items

  fun setItems(newItems: List<Item>) {
    items = newItems
    notifyDataSetChanged()
  }

  // Interface for the click listener
  interface OnClickListener {
    fun onClick(position: Int, itemModel: Item)
  }

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

  fun sortByLocationAsc() {
    items = items.sortedBy { it.location }
    notifyDataSetChanged()
  }

  fun sortByLocationDesc() {
    items = items.sortedByDescending { it.location }
    notifyDataSetChanged()
  }

  fun sortByDateAsc() {
    items = items.sortedBy { it.date }
    notifyDataSetChanged()
  }

  fun sortByDateDesc() {
    items = items.sortedByDescending { it.date }
    notifyDataSetChanged()
  }
}