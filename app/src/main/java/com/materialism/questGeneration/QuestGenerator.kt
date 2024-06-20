package com.materialism.questGeneration

import com.google.firebase.database.FirebaseDatabase
import com.materialism.firebaseDatabase.data.Quest
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.random.Random

class QuestGenerator {
    companion object{
        var fetchedQuests: ArrayList<Quest> = arrayListOf()
        fun fetchAllQuests(){//USE THIS FUNCTION WITH Timer.schedule(1000), AS FB DOES NOT HAVE TIME TO PROCESS IT OTHERWISE I DO NOT KNOW WHY
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Quest")

            reference.get().addOnSuccessListener {
                val arrayRetrieved: ArrayList<Any> = it.value as ArrayList<Any>
                QuestGenerator.initializeQuests()

                for(i in 0..arrayRetrieved.size-1){
                    val arrayElement = arrayRetrieved[i] as Map<String, Any>
                    this@Companion.fetchedQuests.add(QuestGenerator.convertMapToQuest(arrayElement))
                }

            }.addOnFailureListener{
                QuestGenerator.initializeQuests()
            }
        }//CHECK WHETHER THE CHANGE TO DB BREAKS ADAPTER FOR DB, CHANGED WEIGHT TO UPPER case

        private fun convertMapToQuest(map: Map<String, Any>): Quest{
            return Quest(
                id = (map["id"] as Long).toInt(),
                type = map["type"] as String,
                weight = (map["weight"] as Long).toInt(),
                categoryId = (map["categoryId"] as Long).toInt()
            )
        }
        fun initializeQuests(){
            this@Companion.fetchedQuests = arrayListOf()
        }

        fun getTotalWeightDaily(): Int{
            var totalWeight = 0
            for(quest: Quest in this@Companion.fetchedQuests){
                if (quest.type == "Daily"){
                    totalWeight += quest.weight
                }
            }
            return totalWeight
        }

        fun getTotalWeightWeekly(): Int{
            var totalWeight = 0
            for(quest: Quest in this@Companion.fetchedQuests){
                if (quest.type == "Weekly"){
                    totalWeight += quest.weight
                }
            }
            return totalWeight
        }

        fun getTotalWeightMonthly(): Int{
            var totalWeight = 0
            for(quest: Quest in this@Companion.fetchedQuests){
                if (quest.type == "Monthly"){
                    totalWeight += quest.weight
                }
            }
            return totalWeight
        }

        private fun getCumulativeWeightDaily(): ArrayList<Int>{
            var cumulativeArray :ArrayList<Int> = arrayListOf()
            var cumulative : Int = 0

            for(quest: Quest in this@Companion.fetchedQuests){
                if (quest.type == "Daily"){
                    cumulative += quest.weight
                    cumulativeArray.add(cumulative)
                }
            }
            return cumulativeArray
        }

        private fun getCumulativeWeightWeekly(): ArrayList<Int>{
            var cumulativeArray :ArrayList<Int> = arrayListOf()
            var cumulative : Int = 0

            for(quest: Quest in this@Companion.fetchedQuests){
                if (quest.type == "Weekly"){
                    cumulative += quest.weight
                    cumulativeArray.add(cumulative)
                }
            }
            return cumulativeArray
        }

        private fun getCumulativeWeightMonthly(): ArrayList<Int>{
            var cumulativeArray :ArrayList<Int> = arrayListOf()
            var cumulative : Int = 0

            for(quest: Quest in this@Companion.fetchedQuests){
                if (quest.type == "Monthly"){
                    cumulative += quest.weight
                    cumulativeArray.add(cumulative)
                }
            }
            return cumulativeArray
        }

        fun generateDailyQuest(): Int{
            QuestGenerator.fetchAllQuests()
            var totalWeight = 0
            var cumulativeArray: ArrayList<Int>
            var category: Int = Random.nextInt(1, 10)
            Timer().schedule(1000){
                totalWeight = QuestGenerator.getTotalWeightDaily()
                cumulativeArray = QuestGenerator.getCumulativeWeightDaily()
                val randomNumber = (0..totalWeight).random()
                category = QuestGenerator.getCategory(randomNumber, cumulativeArray)
            }
            return category
        }

        fun generateWeeklyQuest(): Int{
            QuestGenerator.fetchAllQuests()
            var totalWeight = 0
            var cumulativeArray: ArrayList<Int>
            var category: Int = Random.nextInt(1, 10)
            Timer().schedule(1000){
                totalWeight = QuestGenerator.getTotalWeightWeekly()
                cumulativeArray = QuestGenerator.getCumulativeWeightWeekly()
                val randomNumber = (0..totalWeight).random()
                category = QuestGenerator.getCategory(randomNumber, cumulativeArray)
            }
            return category
        }

        fun generateMonthlyQuest(): Int{
            QuestGenerator.fetchAllQuests()
            var totalWeight = 0
            var cumulativeArray: ArrayList<Int>
            var category: Int = Random.nextInt(1, 10)
            Timer().schedule(1000){
                totalWeight = QuestGenerator.getTotalWeightMonthly()
                cumulativeArray = QuestGenerator.getCumulativeWeightMonthly()
                val randomNumber = (0..totalWeight).random()
                category = QuestGenerator.getCategory(randomNumber, cumulativeArray)
            }
            return category
        }

        fun getCategory(randomNumber: Int, cumulativeArray: ArrayList<Int>): Int{
            for(i in 0..cumulativeArray.size){
                if (cumulativeArray[i]>randomNumber){
                    return i+1
                }
            }
            return 10
        }
    }
}