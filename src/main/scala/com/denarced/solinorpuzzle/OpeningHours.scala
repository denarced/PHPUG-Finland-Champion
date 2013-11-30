package com.denarced.solinorpuzzle

import org.joda.time.DateTime
import org.apache.commons.lang.StringUtils
import scala.collection.mutable
import com.denarced.solinorpuzzle.Weekday.Weekday
import java.text.SimpleDateFormat

/**
 * @author denarced
 */
class OpeningHours() {
    private val hours: mutable.Map[Weekday, List[(DateTime, DateTime)]] =
        Weekday.values.foldLeft(mutable.Map.empty[Weekday, List[(DateTime, DateTime)]]) {(map, each) =>
            map + (each -> List.empty)
        }
    private val timeRangePattern = """(\d\d:\d\d)-(\d\d:\d\d)""".r
    private val andPattern = """ja""".r
    private var timeRangeStack: List[(DateTime, DateTime)] = List.empty

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
            timeRangeStack = createTimeRange(start, end) :: timeRangeStack
        case andPattern() => assert(!timeRangeStack.isEmpty)
        case _ => finish(token)
    }

    private def createTimeRange(start: String, end: String): (DateTime, DateTime) = {
        val alpha = new DateTime(dateFormat.parse(start))
        val omega = new DateTime(dateFormat.parse(end))

        (alpha, omega)
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
            hours(each) = timeRangeStack
        }
        timeRangeStack = List.empty
    }


    /**
     * Return map of all opening hours which have been parsed by passing the
     * necessary tokens to process method.
     *
     * @return the always non null map.
     */
    def openingHours: Map[Weekday, List[(DateTime, DateTime)]] = {
        hours.filter {entry =>
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
     * time periods; the first tuple value defines the inclusive time when the
     * restaurant opens and the second defines the inclusive time when the
     * restaurant closes.
     */
    def parse(hours: String):
        Map[Weekday, List[(DateTime, DateTime)]] = {

        val context = new OpeningHours()
        val trimmedAndNonEmptyTokens = StringUtils.split(hours, ", ")
        trimmedAndNonEmptyTokens.reverse.foreach {each =>
            context.process(each)
        }

        context.openingHours
    }
}
