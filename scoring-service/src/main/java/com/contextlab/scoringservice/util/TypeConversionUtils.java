package com.contextlab.scoringservice.util;

import java.sql.Clob;
import java.sql.SQLException;

public class TypeConversionUtils {

    public static double convertClobToDouble(Clob clob) throws SQLException {
        return Double.parseDouble(convertClobToString(clob));
    }

    public static int convertClobToInt(Clob clob) throws SQLException {
        return Integer.parseInt(convertClobToString(clob));
    }

    public static String convertClobToString(Clob clob) throws SQLException {
        return clob.getSubString(1, (int) clob.length());
    }
}