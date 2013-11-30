package com.denarced.solinorpuzzle

import java.io.InputStream
import scala.io.BufferedSource
import com.denarced.solinorpuzzle.Weekday.Weekday
import org.joda.time.DateTime
import scala.util.matching.Regex
import com.denarced.solinorpuzzle.Weekday.Weekday

class Restaurant(
    val name: String,
    val openingHours: Map[Weekday, List[(DateTime, DateTime)]]) {
}

object Restaurant {
    val CsvRecordPattern: Regex = (
        /* ID   */      """\d+;""" +
        /* Name */      """([^;]+);""" +
        /* Postcode */  """\d+;""" +
        /* City */      """[^;]*;""" +
        /* Hours etc */ """([^;]+).*""").r

    private[solinorpuzzle] def fromCsvRecord(record: String): Restaurant = {
        record match {
            case CsvRecordPattern(name, hours) =>
                new Restaurant(name, OpeningHours.parse(hours))
        }
    }

    def fromCsv(csvSource: BufferedSource): List[Restaurant] = {
        csvSource.getLines.map {line=>
            fromCsvRecord(line)
        }.toList
    }
}
