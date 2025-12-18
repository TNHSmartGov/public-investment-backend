package com.tnh.baseware.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DateTimeUtils {

    // Múi giờ Việt Nam (UTC+7)
    public static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public static LocalDateTime toLocalDateTime(String dateTimeString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    // Chuyển đổi string thành LocalDateTime với múi giờ +7
    public static LocalDateTime toLocalDateTimeWithVietnamZone(String dateTimeString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (pattern.contains("H") || pattern.contains("h")) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, formatter.withZone(VIETNAM_ZONE));
            return zonedDateTime.toLocalDateTime();
        } else {
            LocalDate localDate = LocalDate.parse(dateTimeString, formatter);
            return localDate.atStartOfDay(VIETNAM_ZONE).toLocalDateTime();
        }
    }

    // Chuyển đổi string date thành LocalDateTime với giờ 00:00:00 và múi giờ +7
    public static LocalDateTime dateStringToLocalDateTime(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atStartOfDay();
    }

    // Lấy thời gian hiện tại theo múi giờ Việt Nam
    public static LocalDateTime nowInVietnam() {
        return ZonedDateTime.now(VIETNAM_ZONE).toLocalDateTime();
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    // Format LocalDateTime với múi giờ Việt Nam
    public static String formatWithVietnamZone(LocalDateTime dateTime, String pattern) {
        ZonedDateTime zonedDateTime = dateTime.atZone(VIETNAM_ZONE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDate toLocalDate(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateString, formatter);
    }

    public static void main(String[] args) {
        // Ví dụ chuyển đổi string date thành LocalDateTime
        String dateStr = "2025-12-21";
        LocalDateTime dateTime1 = dateStringToLocalDateTime(dateStr, "yyyy-MM-dd");
        System.out.println("Date string to LocalDateTime: " + dateTime1);

        // Ví dụ với string datetime
        String dateTimeStr = "2025-12-21 14:30:00";
        LocalDateTime dateTime2 = toLocalDateTime(dateTimeStr, "yyyy-MM-dd HH:mm:ss");
        System.out.println("DateTime string to LocalDateTime: " + dateTime2);

        // Thời gian hiện tại theo múi giờ Việt Nam
        LocalDateTime vietnamNow = nowInVietnam();
        System.out.println("Vietnam time now: " + vietnamNow);

        // Format với múi giờ Việt Nam
        String formattedTime = formatWithVietnamZone(vietnamNow, "yyyy-MM-dd HH:mm:ss");
        System.out.println("Formatted Vietnam time: " + formattedTime);
    }
}
