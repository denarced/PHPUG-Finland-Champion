package com.denarced.solinorpuzzle

import scala.io.BufferedSource
import org.joda.time.{Interval, Minutes}
import scala.util.matching.Regex
import com.denarced.solinorpuzzle.Weekday.Weekday
import org.apache.commons.lang3.builder.ToStringBuilder

class Restaurant(
    val name: String,
    val openingHours: Map[Weekday, List[Interval]]) {

    /**
     * Calculate the number of minutes that the restaurant is open during a week.
     */
    def openDuringWeek: Int = {
        openingHours.values.foldLeft(0) {(minutes, each) =>
            each.foldLeft(0) {(weekdayMinutes, interval) =>
                val mins: Minutes = interval.toDuration.toStandardMinutes
                mins.getMinutes + weekdayMinutes
            } + minutes
        }
    }

    override def toString: String = {
        new ToStringBuilder(this)
            .append("name", name)
            .append("openingHours", openingHours)
            .toString
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
