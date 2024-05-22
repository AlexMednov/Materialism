package com.materialism.quests.generation
import com.google.gson.Gson
import com.materialism.quests.generation.dataStructure.Category
import com.materialism.quests.generation.dataStructure.ItemList
import com.materialism.quests.generation.dataStructure.TimeFrame
import java.io.File
import kotlin.random.Random


class QuestGenerator {
    companion object{

        fun readJSONWeights(path:String): TimeFrame? {
            val gson = Gson()
            try {
                val jsonFile = File(path).readText()
                val data: TimeFrame = gson.fromJson(jsonFile, TimeFrame::class.java)
                println(data)
                return data
            }
            catch (e: Exception){
                println(e)
            }
            finally{
                println("lmao")
            }
            return null
        }

        fun readJSONQuestItems(path:String): ItemList? {
            val gson = Gson()
            try {
                val jsonFile = File(path).readText()
                val data: ItemList? = gson.fromJson(jsonFile, ItemList::class.java)
                println(data)
                return data
            }
            catch (e: Exception){
                println(e)
            }
            finally{
                println("lmao")
            }
            return null
        }
        fun calculateWeightsDailyQuests(timeFrame: TimeFrame): Double{
            if (timeFrame == null){
                throw Exception("Time frame is Null, not allowed")
            }
            var weight: Double = 0.0

            for(items: Category in timeFrame.Daily){
                weight += items.Weight
            }
            return weight
        }

        fun calculateWeightsWeeklyQuests(timeFrame: TimeFrame): Double{
            if (timeFrame == null){
                throw Exception("Time frame is Null, not allowed")
            }
            var weight: Double = 0.0

            for(items: Category in timeFrame.Weekly){
                weight += items.Weight
            }
            return weight
        }

        fun calculateWeightsMonthlyQuests(timeFrame: TimeFrame): Double{
            if (timeFrame == null){
                throw IllegalArgumentException("Time frame is Null, not allowed")
            }
            var weight: Double = 0.0

            for(items: Category in timeFrame.Monthly){
                weight += items.Weight
            }
            return weight
        }

        fun getCumulativeWeightsDaily(timeFrame: TimeFrame): MutableList<Double> {
            val cumulative = mutableListOf<Double>()

            var subTotal = 0.0

            for(items: Category in timeFrame.Daily){
                subTotal += items.Weight
                cumulative.add(subTotal)
            }
            return cumulative
        }

        fun getCumulativeWeightsWeekly(timeFrame: TimeFrame): MutableList<Double> {
            val cumulative = mutableListOf<Double>()

            var subTotal = 0.0

            for(items: Category in timeFrame.Weekly){
                subTotal += items.Weight
                cumulative.add(subTotal)
            }
            return cumulative
        }

        fun getCumulativeWeightsMonthly(timeFrame: TimeFrame): MutableList<Double> {
            val cumulative = mutableListOf<Double>()

            var subTotal = 0.0

            for(items: Category in timeFrame.Daily){
                subTotal += items.Weight
                cumulative.add(subTotal)
            }
            return cumulative
        }

        fun generateDailyQuests(totalWeight: Double, cumulative: MutableList<Double>, timeFrame: TimeFrame){
            val randomValue = Random.nextDouble(totalWeight)

            var generatedCategory: String =""

            for(i in cumulative.indices) {
                if (randomValue<cumulative[i]){
                    generatedCategory = timeFrame.Daily[i].Category
                }
            }

            var itemList = readJSONQuestItems("/com/materialism/quests/content/weights.json")

        }

        fun getTotalNumberOfElementsInCategory(category: String, itemList: ItemList):Int{
            val numberOfElements: Int = when(category) {
                "Groceries" -> itemList.Groceries.count()
                "Beverages" -> itemList.Beverages.count()
                "HouseHold_supplies" -> itemList.HouseHold_supplies.count()
                "Personal_care_products" -> itemList.Personal_care_products.count()
                "Health_care" -> itemList.Health_care.count()
                "Office_or_school_supplies" -> itemList.Office_or_school_supplies.count()
                "Entertainment" -> itemList.Entertainment.count()
                "Clothing_accessories" -> itemList.Clothing_accessories.count()
                "Tech" -> itemList.Tech.count()
                "Misc" -> itemList.Misc.count()
                else -> throw IllegalArgumentException("Invalid category")
            }

            return numberOfElements
        }

        fun generateItem(totalNumberOfElements:Int, category:String, itemList:ItemList):String{
            val randomNumber = Random.nextInt()

            val numberOfElements: Int = when(category) {
                "Groceries" -> itemList.Groceries.count()
                "Beverages" -> itemList.Beverages.count()
                "HouseHold_supplies" -> itemList.HouseHold_supplies.count()
                "Personal_care_products" -> itemList.Personal_care_products.count()
                "Health_care" -> itemList.Health_care.count()
                "Office_or_school_supplies" -> itemList.Office_or_school_supplies.count()
                "Entertainment" -> itemList.Entertainment.count()
                "Clothing_accessories" -> itemList.Clothing_accessories.count()
                "Tech" -> itemList.Tech.count()
                "Misc" -> itemList.Misc.count()
                else -> throw IllegalArgumentException("Invalid category")//Finish this later, time to eepy sleepy
                }
        }

        fun generateWeeklyQuests(){

        }

        fun generateMonthlyQuests(){

        }
    }
}