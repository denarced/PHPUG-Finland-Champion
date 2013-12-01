package com.denarced.solinorpuzzle

import java.io.InputStream
import scala.io.BufferedSource
import org.apache.commons.io.input.BOMInputStream
import scala.collection.SortedMap
import java.text.MessageFormat

/**
 * @author denarced
 */
object Main {
    def sortByOpeningHours(restaurantList: List[Restaurant]): SortedMap[Int, Restaurant] = {
        restaurantList.foldLeft(SortedMap.empty[Int, Restaurant]) {(map, each) =>
            map + (each.openDuringWeek -> each)
        }
    }

    def printNameAndHoursOpen(minutesAndRestaurant: (Int, Restaurant)): Unit = {
        val format = new MessageFormat(
            """
              |Name: {0}
              |Open: {1} hours per week
            """.stripMargin)
        val args = Array[Any](
            minutesAndRestaurant._2.name,
            minutesAndRestaurant._1.toDouble / 60)
        println(format.format(args).trim)
    }

    def main(args: Array[String]): Unit = {
        val stream: InputStream = getClass.getResourceAsStream("/ravintolat.csv")
        val restaurantList: List[Restaurant] =
            Restaurant.fromCsv(new BufferedSource(new BOMInputStream(stream)))
        val sorted: SortedMap[Int, Restaurant] = sortByOpeningHours(restaurantList)

        println("Open the least")
        printNameAndHoursOpen(sorted.head)

        println("\nOpen the most")
        printNameAndHoursOpen(sorted.last)
    }
}
