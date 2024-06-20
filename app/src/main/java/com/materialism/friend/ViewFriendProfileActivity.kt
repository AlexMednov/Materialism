package com.materialism.friend

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materialism.R
import com.materialism.adapter.DatabaseAdapter
import com.materialism.adapter.ItemAdapter
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.utils.DrawerUtils
import com.materialism.utils.ImageRenderer
import com.materialism.database.firebaseDatabase.data.Item as FirebaseItem
import com.materialism.dto.Item as SampleItem

class ViewFriendProfileActivity : AppCompatActivity() {

  private lateinit var databaseAdapter: DatabaseAdapter
  private lateinit var itemAdapter: ItemAdapter
  private lateinit var recyclerView: RecyclerView
  private lateinit var itemsAvailableTitle: TextView
  private lateinit var imageRenderer: ImageRenderer

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_friends_profile)

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    itemsAvailableTitle = findViewById(R.id.items_available_title)

    // Get references to the views
    val profilePicture: ImageView = findViewById(R.id.profile_picture)
    val nameView: TextView = findViewById(R.id.name)
    val locationView: TextView = findViewById(R.id.location)
    val itemsAvailableTitle: TextView = findViewById(R.id.items_available_title)
    recyclerView = findViewById(R.id.item_recycler_view)

    recyclerView.layoutManager = LinearLayoutManager(this)

    // Retrieve user details from the Intent
    val userId = intent.getIntExtra("USER_ID", -1)
    val userName = intent.getStringExtra("USER_NAME")
    val userLocation = intent.getStringExtra("USER_LOCATION")
    val userScore = intent.getIntExtra("USER_SCORE", 0)

    // Populate the user details
    if (userName != null) {
      nameView.text = userName
    }

    if (userLocation != null) {
      locationView.text = userLocation
    }

    databaseAdapter = DatabaseAdapter(DatabaseManager(this))
    imageRenderer =
        ImageRenderer(this.contentResolver) // Ensure ImageRenderer is initialized correctly

    // Initialize the itemAdapter with an empty list and set it to the RecyclerView
    itemAdapter = ItemAdapter(emptyList(), false, ImageRenderer(contentResolver = contentResolver))
    recyclerView.adapter = itemAdapter

    // Fetch items for the user
    fetchItems(userId)
  }

  private fun fetchItems(userId: Int) {
    databaseAdapter.getItemsForSingleUser(userId) { firebaseItems ->
      // Convert FirebaseItem to SampleItem
      val sampleItems =
          firebaseItems.map { firebaseItem -> mapFirebaseItemToSampleItem(firebaseItem) }

      if (sampleItems.isNullOrEmpty()) {
        // Handle empty item list scenario
        itemsAvailableTitle.text = "No items available"
      } else {
        // Update the itemAdapter with the fetched items
        itemAdapter =
            ItemAdapter(
                sampleItems, false, imageRenderer) // Assuming you don't want the edit button
        recyclerView.adapter = itemAdapter
      }
    }
  }

  private fun mapFirebaseItemToSampleItem(firebaseItem: FirebaseItem): SampleItem {
    return SampleItem(
        name = firebaseItem.name,
        description = firebaseItem.description ?: "",
        imageUri = firebaseItem.imageURI,
        category = "", // If required, derive from categoryId
        location = firebaseItem.location ?: "",
        date = firebaseItem.dateAdded // Or another field if needed
        )
  }

  //  private fun populateItems(items: List<Item>) {
  //    val item1Name: TextView = findViewById(R.id.item_name_1)
  //    val item1Description: TextView = findViewById(R.id.item_description_1)
  //    val item1RequestButton: Button = findViewById(R.id.request_button_1)
  //
  //    if (items.isNotEmpty()) {
  //      val firstItem = items[0]
  //      item1Name.text = firstItem.name
  //      item1Description.text =
  // "${firstItem.description}\n${firstItem.category}\n${firstItem.location}\n${firstItem.date}"
  //      item1RequestButton.setOnClickListener {
  //        // Handle request button click
  //      }
  //    }
  //
  //    // If you have more items, create views dynamically or update existing placeholder views
  //    // Repeat similar steps for other items or create a RecyclerView if dynamic listing needed
  //  }
}
