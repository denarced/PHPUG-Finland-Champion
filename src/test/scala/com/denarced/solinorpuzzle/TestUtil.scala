package com.denarced.solinorpuzzle

import org.joda.time.DateTime

/**
 * @author denarced
 */
object TestUtil {
    def createTimeRange(startHour: Int, endHour: Int): (DateTime, DateTime) = {
        val base = new DateTime(1970, 1, 1, 0, 0)
        (base.withHourOfDay(startHour), base.withHourOfDay(endHour))
    }
}
