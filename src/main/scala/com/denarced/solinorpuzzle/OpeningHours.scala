package com.denarced.solinorpuzzle

import org.joda.time.{Interval, DateTime}
import org.apache.commons.lang.StringUtils
import scala.collection.mutable
import com.denarced.solinorpuzzle.Weekday.Weekday
import java.text.SimpleDateFormat

/**
 * @author denarced
 */
class OpeningHours() {
    private val hours: mutable.Map[Weekday, List[Interval]] =
        Weekday.values.foldLeft(mutable.Map.empty[Weekday, List[Interval]]) {(map, each) =>
            map + (each -> List.empty)
        }
    private val timeRangePattern = """(\d\d:\d\d)-(\d\d:\d\d)""".r
    private val andPattern = """ja""".r
    private var intervalStack: List[Interval] = List.empty

    private val dateFormat = new SimpleDateFormat("HH:mm")

    /**
     * Process the next string token in csv record's opening hours.
     *
     * @param token Must be either (1) definition of time range, (2) literal
     * and, or (3) definition of weekday(s). Respective examples of all three:
     * (1) "09:00-18:00", (2) "ja", (3) "Ma-Pe".
     */
    def process(token: String): Unit = token match {
        case timeRangePattern(start: String, end: String) =>
            intervalStack = createInterval(start, end) :: intervalStack
        case andPattern() => assert(!intervalStack.isEmpty)
        case _ => finish(token)
    }

    private def createInterval(start: String, end: String): Interval = {
        val alpha = new DateTime(dateFormat.parse(start))
        val omega = new DateTime(dateFormat.parse(end))

        new Interval(alpha, omega)
    }

    /**
     * Finish parsing.
     * @param weekdayToken Either one weekday or a range of weekdays.
     * Respective examples: "Ma", "Ti-Pe".
     */
    private[solinorpuzzle] def finish(weekdayToken: String): Unit = {
        val days: List[Weekday.Weekday] = if (weekdayToken.contains("-")) {
            val pieces: Array[String] = StringUtils.split(weekdayToken, "[- ]")
            assert(pieces.size == 2)
            Weekday.toListOf(pieces(0), pieces(1))
        } else {
            List(Weekday.withName(weekdayToken))
        }
        days.foreach {each =>
            hours(each) = intervalStack
        }
        intervalStack = List.empty
    }


    /**
     * Return map of all opening hours which have been parsed by passing the
     * necessary tokens to process method.
     *
     * @return the always non null map.
     */
    def openingHours: Map[Weekday, List[Interval]] = {
        hours.filter {entry: (Weekday.Weekday, List[Interval]) =>
            !entry._2.isEmpty
        }.toMap
    }
}

object OpeningHours {
    /**
     * Parse opening hours portion of restaurant csv's record.
     *
     * @param hours A string which contains a definition for some
     * restaurant's opening hours. Must start with weekday definition and end
     * with time range.
     * @return map of opening hours where keys are the weekdays and values are
     * time periods.
     */
    def parse(hours: String): Map[Weekday, List[Interval]] = {
        val context = new OpeningHours()
        val trimmedAndNonEmptyTokens = StringUtils.split(hours, ", ")
        trimmedAndNonEmptyTokens.reverse.foreach {each =>
            context.process(each)
        }

        context.openingHours
    }
}
