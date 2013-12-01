package com.denarced.solinorpuzzle

import org.junit.Assert
import org.junit.Test
import scala.io.BufferedSource
import java.io.ByteArrayInputStream
import java.text.MessageFormat
import org.joda.time.Interval

class RestaurantTest {
    @Test
    def testRestaurantNameParsing(): Unit = {
        // SETUP SUT
        val name = "ÄHTÄRIN RAVINTOLA"
        val csv = createCsvRecord(name, "Ma-Pe 09:30-16:00")

        // EXERCISE
        val source: BufferedSource = new BufferedSource(
            new ByteArrayInputStream(csv.getBytes("UTF-8")))
        val restaurantList: List[Restaurant] = Restaurant.fromCsv(source)

        // VERIFY
        Assert.assertEquals(name, restaurantList.head.name)
    }

    @Test
    def testOpenDuringWeek(): Unit = {
        // SETUP SUT
        val startHour = 9
        val endHour = 17
        val restaurant = new Restaurant(
            "godlike",
            Map(Weekday.Ma ->
                List(TestUtil.createInterval(startHour, endHour))))

        // EXERCISE
        val minuteCount = restaurant.openDuringWeek

        // VERIFY
        Assert.assertEquals((endHour - startHour) * 60, minuteCount)
    }

    @Test
    def testOpenDuringWeekWithSplitOpeningHours(): Unit = {
        // SETUP SUT
        val hourList: List[(Int, Int)] = List((9, 11), (12, 18))
        val hourMap = hourList.foldLeft(
            Map.empty[Weekday.Weekday, List[Interval]]) {(map, each) =>

            val newIntervalList: List[Interval] =
                List(TestUtil.createInterval(each._1, each._2))
            val list: List[Interval] =
                newIntervalList ++ map.getOrElse(Weekday.Ma, List.empty)

            map + (Weekday.Ma -> list)
        }
        val restaurant = new Restaurant("something", hourMap)

        // EXERCISE
        val minuteCount = restaurant.openDuringWeek

        // VERIFY
        val hourCount = hourList.foldLeft(0) {(total, each) =>
            total + (each._2 - each._1)
        }
        Assert.assertEquals(hourCount * 60, minuteCount)
    }

    def createCsvRecord(name: String, hours: String): String = {
        val csvArgs: Array[Object] = Array(name, hours)
        RestaurantTest.csvFormat.format(csvArgs)
    }
}

object RestaurantTest {
    val csvFormat = new MessageFormat("0;{0};10000;Joensuu;{1};")
}

