package com.moviepocket.util

import android.content.Context
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by diegosantos on 12/16/17.
 */
object DateUtil {

    val brLocale = Locale("pt", "BR")

    val DATE_FORMATS = arrayOf("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd", "yyyy-MM", "dd/MM/yyyy")

    val currentTimeStamp: Long
        get() {

            val date = Date()

            if (isObjectNotNull(date)) {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = date
                calendar.set(Calendar.MILLISECOND, 0)

                return calendar.timeInMillis
            } else {
                return 0
            }
        }

    val previousMonth: String
        get() {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cal.add(Calendar.MONTH, -1)

            return SimpleDateFormat("MMMM yyyy").format(cal.time)
        }

    val previousDay: String
        get() {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cal.add(Calendar.DAY_OF_MONTH, -1)

            return SimpleDateFormat("yyyy-MMM/dd").format(cal.time)
        }

    internal val DATEFORMAT = "yyyy-MM-dd HH:mm:ss"

    val currentTimeStampString: String
        get() = currentTimeStamp.toString()

    fun timestampToDate(stamp: String): Date {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = java.lang.Long.parseLong(stamp)
        return cal.time
    }

    fun getDateTimeStamp(date: Date?): Long {
        if (isObjectNotNull(date)) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = date
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.timeInMillis
        } else {
            return 0
        }
    }

    fun getDateTimeStampLocal(date: Date): Long {
        if (isObjectNotNull(date)) {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.timeInMillis
        } else {
            return 0
        }
    }

    fun getDayOfMonth(d: Date): String {

        val format = SimpleDateFormat("dd", brLocale)

        return format.format(d)
    }

    fun getDayMonthYear(d: Date): String {

        val format = SimpleDateFormat("dd/MM/yyyy", brLocale)

        return format.format(d)
    }

    fun getYearMonthDay(d: Date): String {

        val format = SimpleDateFormat("yyyy-MMM/dd", brLocale)

        return format.format(d)
    }

    fun getStringDateWithTime(d: Date): String {

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", brLocale)

        format.timeZone = TimeZone.getTimeZone("UTC")

        return format.format(d)
    }

    fun getStringDate(d: Date): String {

        val format = SimpleDateFormat("yyyy-MM-dd", brLocale)

        return format.format(d)
    }

    fun getMonthYearNumber(d: Date): String {

        val format = SimpleDateFormat("MM/yyyy", brLocale)

        return format.format(d)
    }

    fun getMonthYearString(date: String): String {
        return date.substring(5, 7) + "/" + date.substring(0, 4)
    }

    fun getDayMonthYearTime(d: Date): String {

        val format = SimpleDateFormat("dd/MM/yyyy - HH:mm", brLocale)

        return format.format(d)
    }

    fun getMonth(d: Date): String {

        val format = SimpleDateFormat("MMMM", brLocale)

        return format.format(d)
    }

    fun getMonthNumber(d: Date): Int {

        val format = SimpleDateFormat("MM", brLocale)

        val month = format.format(d)
        return if (isStringValidNumber(month)) {
            Integer.parseInt(month)
        } else 0
    }

    fun getDayNumber(d: Date): Int {

        val format = SimpleDateFormat("dd", brLocale)

        val day = format.format(d)
        return if (isStringValidNumber(day)) {
            Integer.parseInt(day)
        } else 0
    }

    fun lastSaturdayStamp(): Long {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.add(Calendar.DAY_OF_WEEK, -cal.get(Calendar.DAY_OF_WEEK))

        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        return cal.timeInMillis
    }

    fun sundayAWeekAgoStamp(): Long {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.add(Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK) + 6))

        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        return cal.timeInMillis
    }

    fun getHourNumber(d: Date): Int {

        val format = SimpleDateFormat("hh", brLocale)

        val day = format.format(d)
        return if (isStringValidNumber(day)) {
            Integer.parseInt(day)
        } else 0
    }

    fun fromMilisecondsToTime(seconds: Double?): String {
        var bd = BigDecimal(seconds!!)
        bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
        //        bd = bd.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_DOWN);

        var intSeconds = bd.toInt()
        var minutes = 0
        var hours = 0

        if (intSeconds > 60) {
            minutes = intSeconds / 60

            if (minutes > 60) {
                hours = minutes / 60
                minutes = minutes % 60
            }
            intSeconds = intSeconds % 60

        } else {
            minutes = 0
        }

        return (if (hours > 0) (if (hours >= 10) "" else "0") + hours + ":" else "") + (if (minutes >= 10) "" else "0") + minutes + ":" + (if (intSeconds >= 10) "" else "0") + intSeconds
    }

    fun getMinuteNumber(d: Date): Int {

        val format = SimpleDateFormat("mm", brLocale)

        val day = format.format(d)
        return if (isStringValidNumber(day)) {
            Integer.parseInt(day)
        } else 0
    }

    fun getYear(d: Date): String {

        val format = SimpleDateFormat("yyyy", brLocale)

        return format.format(d)
    }

    fun GetUTCdatetimeAsString(): String {
        val sdf = SimpleDateFormat(DATEFORMAT)
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        return sdf.format(Date())
    }

    fun getTimePassed(date: Date?, context: Context): String {
        var lastUpdate = "-"
        if (date != null) {
            val lastUpdateTimeStamp = DateUtil.getDateTimeStamp(date) / 1000

            val current = stringToDate(GetUTCdatetimeAsString())

            val currentTimeStamp = getDateTimeStamp(current) / 1000

            var diffTimeStamp = currentTimeStamp - lastUpdateTimeStamp

            if (diffTimeStamp < 0) {
                diffTimeStamp = 0
            }

            if (diffTimeStamp < 60) {
                if (diffTimeStamp >= 10) {
                    lastUpdate = diffTimeStamp.toString() + "s"
                } else {
                    lastUpdate = "0" + diffTimeStamp + "s"
                }
            } else if (diffTimeStamp >= 60) {
                var bd = BigDecimal(diffTimeStamp)
                bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                bd = bd.divide(BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP)
                bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                var minutes = bd.toInt()

                if (minutes < 60) {
                    if (minutes >= 10) {
                        lastUpdate = minutes.toString() + "m"
                    } else {
                        lastUpdate = "0" + minutes + "m"
                    }
                } else {
                    bd = BigDecimal(minutes)
                    bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                    bd = bd.divide(BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP)
                    bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                    val hours = bd.toInt()

                    if (hours < 24) {

                        if (hours >= 10) {
                            lastUpdate = hours.toString() + "h"
                        } else {
                            lastUpdate = "0" + hours + "h"
                        }

                        minutes = minutes - hours * 60
                        if (minutes >= 10) {
                            lastUpdate = lastUpdate + minutes + "m"
                        } else {
                            lastUpdate = lastUpdate + "0" + minutes + "m"
                        }
                    } else {
                        bd = BigDecimal(hours)
                        bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                        bd = bd.divide(BigDecimal(24), 2, BigDecimal.ROUND_HALF_UP)
                        bd = bd.setScale(0, BigDecimal.ROUND_DOWN)

                        val days = bd.toInt()

                        if (days < 30) {
                            if (days >= 10) {
                                lastUpdate = days.toString() + "d"
                            } else {
                                lastUpdate = "0" + days + "d"
                            }
                        } else {
                            bd = BigDecimal(days)
                            bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                            bd = bd.divide(BigDecimal(30), 2, BigDecimal.ROUND_HALF_UP)
                            bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                            val months = bd.toInt()

                            if (months < 12) {
                                if (months >= 10) {
                                    lastUpdate = months.toString() + "months"
                                } else {
                                    if (months == 1) {
                                        lastUpdate = "0$months month"
                                    } else {
                                        lastUpdate = "0$months months"
                                    }
                                }
                            } else {
                                bd = BigDecimal(months)
                                bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                                bd = bd.divide(BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP)
                                bd = bd.setScale(0, BigDecimal.ROUND_DOWN)
                                val years = bd.toInt()

                                if (years >= 10) {
                                    lastUpdate = years.toString() + "y"
                                } else {
                                    lastUpdate = "0" + years + "y"
                                }
                            }
                        }
                    }
                }
            }
        }

        return lastUpdate
    }

    fun getYearNumber(d: Date): Int {

        val format = SimpleDateFormat("yyyy", brLocale)

        val year = format.format(d)
        return if (isStringValidNumber(year)) {
            Integer.parseInt(year)
        } else 0
    }

    fun stringToDate(dateStr: String): Date? {
        var dateStr = dateStr
        for (dateFmt in DATE_FORMATS) {
            try {
                val format = SimpleDateFormat(dateFmt,
                        brLocale)

                //                format.setTimeZone(TimeZone.getTimeZone("UTC"));

                dateStr = format.format(format.parse(dateStr))

                return format.parse(dateStr)
            } catch (e: Exception) {
            }

        }
        return null
    }

    fun stringToDateLocalTimezone(dateStr: String): Date? {
        var dateStr = dateStr
        for (dateFmt in DATE_FORMATS) {
            try {
                val format = SimpleDateFormat(dateFmt,
                        brLocale)

                dateStr = format.format(format.parse(dateStr))

                return format.parse(dateStr)
            } catch (e: Exception) {
            }

        }
        return null
    }

    fun fromMonthToNumber(monthName: String): Int {
        var date: Date? = null
        try {
            date = SimpleDateFormat("MMMM").parse(monthName)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")
        )
        cal.time = date
        return cal.get(Calendar.MONTH) + 1
    }
}
