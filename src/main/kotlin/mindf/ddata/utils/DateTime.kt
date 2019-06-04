package mindf.utils

import java.text.SimpleDateFormat
import java.util.*


class DateTime {

    companion object {
        val MONDAY = "monday"
        val TUESDAY = "tuesday"
        val WEDNESSDAY = "wednessday"
        val THURSDAY = "thursday"
        val FRIDAY = "friday"
        val SATURDAY = "saturday"
        val SUNDAY = "sunday"
        val LUNDI = "lundi"
        val MARDI = "mardi"
        val MERCREDI = "mercredi"
        val JEUDI = "jeudi"
        val VENDREDI = "vendredi"
        val SAMEDI = "samedi"
        val DIMANCHE = "dimanche"

        val ISO8601_DATE_FORMAT = "yyyy-MM-dd"
        val YEAR_FORMAT = "yyyy"
        val MONTH_FORMAT = "MM"
        val DAY_FORMAT = "dd"
        val ISO8601_DATE_SEPARATOR = '-'
        val ISO8601_TIME_FORMAT = "HH:mm:ss"
        val HOUR_FORMAT = "HH"
        val MINUTE_FORMAT = "mm"
        val SECONDS_FORMAT = "ss"
        val ISO8601_TIME_SEPARATOR = ':'
        val WEEK_DAY_FORMAT = "EEEE"
        val YEAR_MONTH_FORMAT = "MMM"

        val secondMilliseconds = 1000.0
        val minuteMilliseconds = 60000.0
        val hourMilliseconds = 3.6e+6
        val dayMilliseconds = 8.64e+7
        val weekMilliseconds = 6.048e+8
        val monthMilliseconds = 2.628e+9
        val yearMilliseconds = 3.154e+10

        private val currentYear = getCurrentYear()
        private val currentMonth = getCurrentMonth()
        private val currentDay = getCurrentDay()

        var locale: Locale? = Locale.ENGLISH

        //format == iso8601
        fun DateTime(locale: Locale) {
            Companion.locale = locale
        }

        fun getCurrentDateTime(): String {
            return getCurrentDate().toString() + " " + getCurrentTime()
        }

        fun getSimpleDateYearsFormat(): SimpleDateFormat {
            return SimpleDateFormat(YEAR_FORMAT, locale!!)
        }

        fun getSimpleDateMonthsFormat(): SimpleDateFormat {
            return SimpleDateFormat(MONTH_FORMAT, locale!!)
        }

        fun getSimpleDateDaysFormat(): SimpleDateFormat {
            return SimpleDateFormat(DAY_FORMAT, locale!!)
        }

        fun getSimpleDateTimeFormat(): SimpleDateFormat {
            return SimpleDateFormat(ISO8601_TIME_FORMAT, locale!!)
        }

        fun getSimpleDateHoursFormat(): SimpleDateFormat {
            return SimpleDateFormat(HOUR_FORMAT, locale!!)
        }

        fun getSimpleDateMinutesFormat(): SimpleDateFormat {
            return SimpleDateFormat(MINUTE_FORMAT, locale!!)
        }

        fun getSimpleDateSecondsFormat(): SimpleDateFormat {
            return SimpleDateFormat(SECONDS_FORMAT, locale!!)
        }

        fun getCurrentMilliSecondsValue(): Long {
            return Date().time
        }

        fun getADateMilliSeconds(dateString: String): String {
            return SimpleDateFormat(ISO8601_DATE_FORMAT, locale!!).parse(dateString).time.toString()
        }

        fun getStringDateMilliSecondsLong(dateString: String): Long {
            return SimpleDateFormat(ISO8601_DATE_FORMAT, locale!!).parse(dateString).time
        }

        fun getStringDateMilliSeconds(dateString: String): Double {
            return SimpleDateFormat(ISO8601_DATE_FORMAT, locale!!).parse(dateString).time.toDouble()
        }

        fun changeDateFormatToIso8601(stringDate: String, OLD_FORMAT: String): String {
            val simpleDateFormat = SimpleDateFormat(OLD_FORMAT, locale!!)
            simpleDateFormat.applyPattern(ISO8601_DATE_FORMAT)
            return simpleDateFormat.format(defineDate(simpleDateFormat, stringDate))
        }

        fun extractYear(dateString: String): String {
            return dateString.substring(0, 4)
        }

        fun extractMonth(dateString: String): String {
            return dateString.substring(5, 7)
        }

        fun extractDay(dateString: String): String {
            return dateString.substring(8, 10)
        }

        fun isDateBetween(inputDate: String, startingDate: String, endingDate: String): Boolean {
            val milliSecondsInputDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    inputDate
                )
            )
            val milliSecondsStartingDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    startingDate
                )
            )
            val milliSecondsEndingDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    endingDate
                )
            )
            return milliSecondsInputDate in milliSecondsStartingDate..milliSecondsEndingDate
        }

        fun isDateSmallerOrEquals(inputDate: String, comparedDate: String): Boolean {
            val milliSecondsInputDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    inputDate
                )
            )
            val milliSecondsComparedDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    comparedDate
                )
            )
            return milliSecondsInputDate <= milliSecondsComparedDate
        }

        fun isDateAfter(inputDate: String, comparedDate: String): Boolean {
            val milliSecondsInputDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    inputDate
                )
            )
            val milliSecondsComparedDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    comparedDate
                )
            )
            return milliSecondsInputDate > milliSecondsComparedDate
        }

        fun isDateHigherOrEquals(inputDate: String, comparedDate: String): Boolean {
            val milliSecondsInputDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    inputDate
                )
            )
            val milliSecondsComparedDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    comparedDate
                )
            )
            return milliSecondsInputDate >= milliSecondsComparedDate
        }

        fun isDateBefore(inputDate: String, comparedDate: String): Boolean {
            val milliSecondsInputDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    inputDate
                )
            )
            val milliSecondsComparedDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    comparedDate
                )
            )
            return milliSecondsInputDate < milliSecondsComparedDate
        }

        fun isDateEquals(inputDate: String, comparedDates: String): Boolean {
            val milliSecondsInputDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    inputDate
                )
            )
            val milliSecondsComparedDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    comparedDates
                )
            )
            return milliSecondsInputDate == milliSecondsComparedDate
        }

        fun isDateValid(inputDate: String): Boolean {
            val dateFormat = SimpleDateFormat(ISO8601_DATE_FORMAT, Locale.ENGLISH)
            dateFormat.isLenient = false
            dateFormat.parse(inputDate)
            return true
        }

        fun isAfterCurrentDate(stringDate: String): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            val milliSecondsStringDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    stringDate
                )
            )
            return milliSecondsStringDate > milliSecondsCurrentDate
        }

        fun isAfterCurrentDate(millisecondsDate: Long): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            return millisecondsDate > milliSecondsCurrentDate
        }

        fun isBeforeCurrentDate(stringDate: String): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            val milliSecondsStringDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    stringDate
                )
            )
            return milliSecondsStringDate < milliSecondsCurrentDate
        }

        fun isBeforeCurrentDate(millisecondsDate: Long): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            return millisecondsDate < milliSecondsCurrentDate
        }

        fun isEqualsCurrentDate(stringDate: String): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            val milliSecondsStringDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    stringDate
                )
            )
            return milliSecondsStringDate == milliSecondsCurrentDate
        }

        fun isEqualsCurrentDate(millisecondsDate: Long): Boolean {
            return millisecondsDate == getCurrentMilliSecondsValue()
        }

        fun isAfterOrEqualsCurrentDate(stringDate: String): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            val milliSecondsStringDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    stringDate
                )
            )
            return milliSecondsStringDate >= milliSecondsCurrentDate
        }

        fun isAfterOrEqualsCurrentDate(millisecondsDate: Long): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            return millisecondsDate >= milliSecondsCurrentDate
        }

        fun isBeforeOrEqualsCurrentDate(stringDate: String): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            val milliSecondsStringDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    stringDate
                )
            )
            return milliSecondsStringDate <= milliSecondsCurrentDate
        }

        fun isBeforeOrEqualsCurrentDate(millisecondsDate: Long): Boolean {
            val milliSecondsCurrentDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    getCurrentStringDate()
                )
            )
            return millisecondsDate <= milliSecondsCurrentDate
        }

        fun isMinor(age: String): Boolean {
            return getAge(age) <= 18
        }

        fun isMinor(age: Int): Boolean {
            return age <= 18
        }

        fun isYearValid(stringYear: String): Boolean {
            val yearBetweenCurentDate = getYearsBetweenTwoDates(
                getCurrentYear(),
                stringYear
            )
            return yearBetweenCurentDate > 150
        }

        fun getMilliSecondsBetweenTwoDates(inputDate: String, comparedDate: String): Double {
            val milliSecondsInputDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    inputDate
                )
            )
            val milliSecondsComparedDate = java.lang.Double.parseDouble(
                getADateMilliSeconds(
                    comparedDate
                )
            )
            return milliSecondsInputDate - milliSecondsComparedDate
        }

        fun getDoubleDayBetweenTwoDates(inputDate: String, comparedDate: String): Double {
            val differenceInMilliSeconds =
                getMilliSecondsBetweenTwoDates(inputDate, comparedDate)
            return differenceInMilliSeconds / dayMilliseconds
        }

        fun getDayBetweenTwoDates(inputDate: String, comparedDate: String): Int {
            val differenceInMilliSeconds =
                getMilliSecondsBetweenTwoDates(inputDate, comparedDate)
            return (differenceInMilliSeconds / dayMilliseconds).toInt()
        }

        fun getMonthBetweenTwoDates(inputDate: String, comparedDate: String): Double {
            val differenceInMilliSeconds =
                getMilliSecondsBetweenTwoDates(inputDate, comparedDate)
            return Math.round(differenceInMilliSeconds / monthMilliseconds).toDouble()
        }

        fun getYearsBetweenTwoDates(inputDate: String, comparedDate: String): Double {
            val differenceInMilliSeconds =
                getMilliSecondsBetweenTwoDates(inputDate, comparedDate)
            return Math.round(differenceInMilliSeconds / yearMilliseconds).toDouble()
        }

        fun getHoursBetweenTwoDates(inputDate: String, comparedDate: String): Double {
            val differenceInMilliSeconds =
                getMilliSecondsBetweenTwoDates(inputDate, comparedDate)
            return differenceInMilliSeconds / hourMilliseconds
        }

        fun getMinutesBetweenTwoDates(inputDate: String, comparedDate: String): Double {
            val deferenceInMilliSeconds =
                getMilliSecondsBetweenTwoDates(inputDate, comparedDate)
            return deferenceInMilliSeconds / minuteMilliseconds
        }

        fun getSecondsBetweenTwoDates(inputDate: String, comparedDate: String): Double {
            val differenceInMilliSeconds =
                getMilliSecondsBetweenTwoDates(inputDate, comparedDate)
            return differenceInMilliSeconds / secondMilliseconds
        }

        fun getWeakDayName(): String {
            val date = getSimpleDateFormat()
                .parse(getCurrentStringDate())
            return SimpleDateFormat(WEEK_DAY_FORMAT, Locale.ENGLISH).format(date)
        }

        fun getYearMonthName(): String {
            val date = getSimpleDateFormat()
                .parse(getCurrentStringDate())
            return SimpleDateFormat(YEAR_MONTH_FORMAT, Locale.ENGLISH).format(date)
        }

        fun getAge(birthDate: String): Int { //must not be born in future
            val birthYear = extractYear(birthDate)
            val birthMonth = extractMonth(birthDate)
            val birthDay = extractDay(birthDate)
            var age = Integer.parseInt(currentYear) - Integer.parseInt(birthYear)
            if (currentMonth == birthMonth && Integer.parseInt(birthDay) > Integer.parseInt(
                    currentDay
                ) || Integer.parseInt(
                    birthMonth
                ) > Integer.parseInt(currentMonth)
            ) {
                --age
            }
            return age
        }

        fun isBirthDateValid(birthDate: String): Boolean {
            return isDateValid(birthDate) && isBeforeCurrentDate(
                birthDate
            )
        }

        fun getSimpleDateFormat(): SimpleDateFormat {
            return SimpleDateFormat(ISO8601_DATE_FORMAT, locale!!)
        }

        fun getYesterday(): String {
            return milliSecondsToStringDate(getCurrentMilliSecondsValue() - dayMilliseconds)
        }

        fun getTomorrow(): String {
            return milliSecondsToStringDate(getCurrentMilliSecondsValue() + dayMilliseconds)
        }

        private fun updateValue(stringDate: String, value: Int, milliseconds: Double): String {
            return milliSecondsToStringDate(
                getStringDateMilliSeconds(
                    stringDate
                ) + value * milliseconds
            )
        }

        fun updateSecond(stringDate: String, value: Int): String {
            return updateValue(
                stringDate,
                value,
                secondMilliseconds
            )
        }

        fun updateMinute(stringDate: String, value: Int): String {
            return updateValue(
                stringDate,
                value,
                minuteMilliseconds
            )
        }

        fun updateHour(stringDate: String, value: Int): String {
            return updateValue(
                stringDate,
                value,
                hourMilliseconds
            )
        }

        fun updateDay(stringDate: String, value: Int): String {
            return updateValue(
                stringDate,
                value,
                dayMilliseconds
            )
        }

        fun updateMonth(stringDate: String, value: Int): String {
            return updateValue(
                stringDate,
                value,
                monthMilliseconds
            )
        }

        fun updateYear(stringDate: String, value: Int): String {
            return updateValue(
                stringDate,
                value,
                yearMilliseconds
            )
        }

        /** Deprecated  */
        fun milliSecondsToStringDate(milliseconds: Long): String {
            return getSimpleDateFormat().format(Date(milliseconds))
        }

        fun milliSecondsToStringDate(milliseconds: Double): String {
            return getSimpleDateFormat().format(Date(milliseconds.toLong()))
        }

        fun dateToStringDate(date: Date): String {
            val dateValue = date.time
            return milliSecondsToStringDate(dateValue.toDouble())
        }

        fun getCurrentMilliSecondsDoubleValue(): Double {
            return Date().time.toDouble()
        }

        fun getCurrentDate(): Date {
            return stringDateToDate(getCurrentStringDate())
        }

        fun stringDateToDate(stringDate: String): Date {
            return getSimpleDateFormat().parse(stringDate)
        }

        private fun defineDate(simpleDateFormat: SimpleDateFormat, stringDate: String): Date {
            return simpleDateFormat.parse(stringDate)
        }

        fun getCurrentStringDate(): String {
            return getSimpleDateFormat().format(Date())
        }

        fun getCurrentYear(): String {
            if (locale == null) locale = Locale.ENGLISH
            return getSimpleDateYearsFormat().format(Date())
        }

        fun getCurrentMonth(): String {
            if (locale == null) locale = Locale.ENGLISH
            return getSimpleDateMonthsFormat().format(Date())
        }

        fun getCurrentDay(): String {
            if (locale == null) locale = Locale.ENGLISH
            return getSimpleDateDaysFormat().format(Date())
        }

        fun getCurrentTime(): String {
            return getSimpleDateTimeFormat().format(Date())
        }

        fun getCurrentHours(): String {
            return getSimpleDateHoursFormat().format(Date())
        }

        fun getCurrentMinute(): String {
            return getSimpleDateMinutesFormat().format(Date())
        }

        fun getCurrentSeconds(): String {
            return getSimpleDateSecondsFormat().format(Date())
        }

        fun getCurrentMilliSeconds(): String {
            return Date().time.toString()
        }
    }
}