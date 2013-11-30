package com.denarced.solinorpuzzle

import sun.security.provider.Sun

/**
 * @author denarced
 */
object Weekday extends Enumeration {
    type Weekday = Value
    val Ma, Ti, Ke, To, Pe, La, Su = Value
    val all = List(Ma, Ti, Ke, To, Pe, La, Su)

    /**
     * Create list of weekdays between alpha and omega.
     *
     * @param alpha Inclusive lower bound for the created list. The matching
     * Weekday object will be the first item in the returned list. Must be
     * earlier in the week than omega.
     * @param omega Inclusive higher bound to the created list. The matching
     * Weekday object will be the last item in the returned list. Must be
     * later in the week than alpha.
     * @return always non null list.
     */
    def toListOf(alpha: String, omega: String): List[Weekday.Weekday] = {
        val start = Weekday.withName(alpha)
        val end = Weekday.withName(omega)
        assert(start < end)

        var ongoing = false
        all.foldRight(List.empty[Weekday.Weekday]) {(each, list) =>
            if (!ongoing && (each == end)) {
                ongoing = true
                each :: list
            } else if (ongoing && each != start) {
                each :: list
            } else if (ongoing && each == start) {
                ongoing = false
                each :: list
            } else {
                list
            }
        }
    }
}
