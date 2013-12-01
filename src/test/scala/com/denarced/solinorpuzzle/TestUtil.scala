package com.denarced.solinorpuzzle

import org.joda.time.{Interval, DateTime}

/**
 * @author denarced
 */
object TestUtil {
    def createInterval(startHour: Int, endHour: Int): Interval = {
        val base = new DateTime(1970, 1, 1, 0, 0)
        new Interval(base.withHourOfDay(startHour), base.withHourOfDay(endHour))
    }
}
