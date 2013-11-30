package com.denarced.solinorpuzzle

import org.junit.{Assert, Test}
import org.joda.time.DateTime
import com.denarced.solinorpuzzle.Weekday.Weekday

/**
 * @author denarced
 */
class OpeningHoursTest {
    @Test
    def testSplitHours(): Unit = {
        // SETUP SUT
        val hours = "Ma-Ti 09:00-11:00 ja 12:00-18:00"
        val hoursList: List[(DateTime, DateTime)] = List(
            TestUtil.createTimeRange(9, 11),
            TestUtil.createTimeRange(12, 18))
        val hoursMap: Map[Weekday, List[(DateTime, DateTime)]] = Map(
            Weekday.Ma -> hoursList,
            Weekday.Ti -> hoursList)

        exerciseAndVerify(hoursMap, hours)
    }

    @Test
    def testBasicHours(): Unit = {
        // SETUP SUT
        val hours = "Ma-Ke 09:00-19:00"
        val hoursList: List[(DateTime, DateTime)] = List(TestUtil.createTimeRange(9, 19))
        val hoursMap: Map[Weekday, List[(DateTime, DateTime)]] = Map(
            Weekday.Ma -> hoursList,
            Weekday.Ti -> hoursList,
            Weekday.Ke -> hoursList)

        exerciseAndVerify(hoursMap, hours)
    }

    @Test
    def testSeparateWeekdayHours(): Unit = {
        // SETUP SUT
        val hours = "Ma-Ti 10:00-19:00, Pe 10:00-16:00"
        val monTueHours: List[(DateTime, DateTime)] =
            List(TestUtil.createTimeRange(10, 19))
        val fridayHours: List[(DateTime, DateTime)] =
            List(TestUtil.createTimeRange(10, 16))
        val hoursMap: Map[Weekday, List[(DateTime, DateTime)]] = Map(
            Weekday.Ma -> monTueHours,
            Weekday.Ti -> monTueHours,
            Weekday.Pe -> fridayHours)

        exerciseAndVerify(hoursMap, hours)
    }

    def exerciseAndVerify(
        expected: Map[Weekday, List[(DateTime, DateTime)]],
        hours: String): Unit = {

        // EXERCISE
        val actual: Map[Weekday, List[(DateTime, DateTime)]] =
            OpeningHours.parse(hours)

        // VERIFY
        Assert.assertEquals(expected, actual)
    }
}
