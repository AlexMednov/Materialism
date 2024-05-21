package com.materialism.quests.generation
import com.materialism.quests.generation.dataStructure.TimeFrames
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File


class QuestGenerator {
    companion object{

        fun readJSONFile(filePath:String): String{
            return File(filePath).readText(Charsets.UTF_8)
        }

        fun parseJSON(JSONContent:String):TimeFrames{
            return Json.decodeFromString(JSONContent);
        }
        fun calculateWeightsDailyQuests(): Int{
            val filePath: String = "../content/weights.json"
            val jsonContent: String = readJSONFile(filePath)
            val timeFrames: TimeFrames = parseJSON(jsonContent)
            println(timeFrames)

            return 1
        }

        fun calculateWeightsWeeklyQuests(){

        }

        fun calculateWeightsMonthlyQuests(){

        }


        fun generateDailyQuests(){

        }

        fun generateWeeklyQuests(){

        }

        fun generateMonthlyQuests(){

        }
    }
}