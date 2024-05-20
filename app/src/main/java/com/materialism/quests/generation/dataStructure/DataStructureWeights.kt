package com.materialism.quests.generation.dataStructure
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val Groceries: Int,
    val Beverages: Int,
    val HouseHold_supplies: Int,
    val Personal_care_products: Int,
    val Health_care: Int,
    val Office_or_school_supplies: Int,
    val Entertainment: Int,
    val Clothing_accessories: Int,
    val Tech: Int,
    val Misc: Int
)

@Serializable
data class TimeFrames(
    val Daily: Item,
    val Weekly: Item,
    val Monthly: Item
)