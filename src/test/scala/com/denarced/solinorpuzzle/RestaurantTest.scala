package com.denarced.solinorpuzzle

import org.junit.Assert
import org.junit.Test
import scala.io.BufferedSource
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.text.{SimpleDateFormat, MessageFormat}
import java.util.Date
import org.joda.time.{Minutes, DateTime}
import com.denarced.solinorpuzzle.Weekday.Weekday

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

    def createCsvRecord(name: String, hours: String): String = {
        val csvArgs: Array[Object] = Array(name, hours)
        RestaurantTest.csvFormat.format(csvArgs)
    }
}

object RestaurantTest {
    val csvFormat = new MessageFormat("0;{0};10000;Joensuu;{1};")
}

