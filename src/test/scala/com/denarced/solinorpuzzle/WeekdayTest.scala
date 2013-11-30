package com.denarced.solinorpuzzle

import org.junit.{Assert, Test}

/**
 * @author denarced
 */
class WeekdayTest {
    @Test
    def testToListOfWithSequentialWeekdays(): Unit = exerciseAndVerify(
      List(Weekday.Ma, Weekday.Ti))

    @Test
    def testToListOfWithAllWeekdays(): Unit = exerciseAndVerify(
        List(
            Weekday.Ma,
            Weekday.Ti,
            Weekday.Ke,
            Weekday.To,
            Weekday.Pe,
            Weekday.La,
            Weekday.Su))

    def exerciseAndVerify(expected: List[Weekday.Weekday]): Unit = {
        // EXERCISE
        val actual: List[Weekday.Weekday] = Weekday.toListOf(
            expected.head.toString,
            expected.last.toString)

        // VERIFY
        Assert.assertEquals(expected, actual)
    }
}
