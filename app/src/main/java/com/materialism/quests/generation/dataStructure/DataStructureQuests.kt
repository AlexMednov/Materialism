package com.materialism.quests.generation.dataStructure

data class Category(
    val Category: String,
    val Weight: Int
)

data class TimeFrame(
    val Daily: List<Category>,
    val Weekly: List<Category>,
    val Monthly: List<Category>
)

data class Item(
    val ID: Int,
    val Item: String
)

data class ItemList(
    val Groceries: List<Item>,
    val Beverages: List<Item>,
    val HouseHold_supplies: List<Item>,
    val Personal_care_products: List<Item>,
    val Health_care: List<Item>,
    val Office_or_school_supplies: List<Item>,
    val Entertainment: List<Item>,
    val Clothing_accessories: List<Item>,
    val Tech: List<Item>,
    val Misc: List<Item>
)