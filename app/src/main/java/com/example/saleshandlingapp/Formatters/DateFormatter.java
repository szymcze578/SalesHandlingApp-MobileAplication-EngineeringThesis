package com.example.saleshandlingapp.Formatters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static String formatDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm");
        return date.format(formatter);
    }

    public static DateTimeFormatter formatToReport(){
        return DateTimeFormatter.ofPattern("d-M-yyyy");
    }

    public static String dateForHeader(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
