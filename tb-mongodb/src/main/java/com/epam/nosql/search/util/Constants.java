package com.epam.nosql.search.util;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final String DATE_FORMAT_REGEXP = "^\\d{2}-\\d{2}-\\d{4}$";

}
