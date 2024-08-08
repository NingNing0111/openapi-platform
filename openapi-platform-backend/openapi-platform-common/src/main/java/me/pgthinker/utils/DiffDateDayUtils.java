package me.pgthinker.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @Project: me.pgthinker.utils
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/8/8 23:18
 * @Description:
 */
public class DiffDateDayUtils {

    public static long daysBetween(Date start) {
        return daysBetween(start,new Date());
    }

    public static long daysBetween(Date start, Date end){
        LocalDate startTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endTime = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(startTime,endTime);
    }
}
