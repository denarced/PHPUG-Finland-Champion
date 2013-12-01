package com.denarced.solinorpuzzle

import org.junit.{Assert, Test}
import org.joda.time.Interval
import com.denarced.solinorpuzzle.Weekday.Weekday

/**
 * @author denarced
 */
class OpeningHoursTest {
    @Test
    def testSplitHours(): Unit = {
        // SETUP SUT
        val hours = "Ma-Ti 09:00-11:00 ja 12:00-18:00"
        val intervalList: List[Interval] = List(
            TestUtil.createInterval(9, 11),
            TestUtil.createInterval(12, 18))
        val hoursMap: Map[Weekday, List[Interval]] = Map(
            Weekday.Ma -> intervalList,
            Weekday.Ti -> intervalList)

        exerciseAndVerify(hoursMap, hours)
    }

    @Test
    def testBasicHours(): Unit = {
        // SETUP SUT
        val hours = "Ma-Ke 09:00-19:00"
        val intervalList: List[Interval] = List(TestUtil.createInterval(9, 19))
        val hoursMap: Map[Weekday, List[Interval]] = Map(
            Weekday.Ma -> intervalList,
            Weekday.Ti -> intervalList,
            Weekday.Ke -> intervalList)

        exerciseAndVerify(hoursMap, hours)
    }

    @Test
    def testSeparateWeekdayHours(): Unit = {
        // SETUP SUT
        val hours = "Ma-Ti 10:00-19:00, Pe 10:00-16:00"
        val monTueIntervalList: List[Interval] =
            List(TestUtil.createInterval(10, 19))
        val fridayIntervalList: List[Interval] =
            List(TestUtil.createInterval(10, 16))
        val hoursMap: Map[Weekday, List[Interval]] = Map(
            Weekday.Ma -> monTueIntervalList,
            Weekday.Ti -> monTueIntervalList,
            Weekday.Pe -> fridayIntervalList)

        exerciseAndVerify(hoursMap, hours)
    }

    def exerciseAndVerify(
        expected: Map[Weekday, List[Interval]],
        hours: String): Unit = {

        // EXERCISE
        val actual: Map[Weekday, List[Interval]] =
            OpeningHours.parse(hours)

        // VERIFY
        Assert.assertEquals(expected, actual)
    }
}
