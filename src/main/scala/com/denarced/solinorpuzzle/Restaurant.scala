package com.denarced.solinorpuzzle

import scala.io.BufferedSource
import org.joda.time.{Minutes, DateTime}
import scala.util.matching.Regex
import com.denarced.solinorpuzzle.Weekday.Weekday

class Restaurant(
    val name: String,
    val openingHours: Map[Weekday, List[(DateTime, DateTime)]]) {

    /**
     * Calculate the number of minutes that the restaurant is open during a week.
     */
    def openDuringWeek: Int = {
        openingHours.values.foldLeft(0) {(minutes, each) =>
            each.foldLeft(0) {(weekdayMinutes, timeRange) =>
                val mins: Minutes =
                    Minutes.minutesBetween(timeRange._1, timeRange._2)
                mins.getMinutes + weekdayMinutes
            } + minutes
        }
    }
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
