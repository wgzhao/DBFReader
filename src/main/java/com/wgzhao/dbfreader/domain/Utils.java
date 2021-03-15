/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader.domain;

import org.jamel.dbf.structure.DbfField;
import org.jamel.dbf.structure.DbfRow;

import javax.swing.JFileChooser;

import java.io.File;
import java.nio.charset.CharacterCodingException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 16/10/13
 * Time: 11:20
 */
public class Utils
{

    public static final String NEW_LINE = System.getProperty("line.separator");
    private static String lastDir = null;

    public static String toHumanReadableByteCount(long size)
    {

        int unit = 1024;

        if (size < unit) {
            return size + " B";
        }

        int exp = (int) (Math.log(size) / Math.log(unit));

        String pre = "KMGTPE".charAt(exp - 1) + "i";

        return String.format("%.1f %sB", size / Math.pow(unit, exp), pre);
    }

    public static String cleanFileName(String name)
    {

        return name.substring(0, name.lastIndexOf(".")).replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String cleanString(byte[] rawData)
    {

        //Make sure it's UTF-8 valid

        String value;
        try {

            value = UTF8Util.utf8(rawData);
        }
        catch (CharacterCodingException e) {

            //Should not get here...
            value = "";
        }

        return cleanString(value);
    }

    public static String cleanString(String string)
    {

        return string.replace("'", "\\'");
    }

    public static JFileChooser getFileChooser()
    {
        if (lastDir != null) {
            return new JFileChooser(lastDir);
        }
        else {
            return new JFileChooser();
        }
    }

    public static void setLastDir(File file)
    {
        lastDir = file.getParent();
    }

    public static String getRecord(DbfField field, DbfRow row)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String colName = field.getName();
        if (row.getObject(colName) == null) {
            return "";
        }
        else {
            switch (field.getDataType()) {
                case CHAR:
                    return row.getString(colName);

                case LOGICAL:
                    return row.getBoolean(colName) ? "true" : "false";

                case DATE:
                    return simpleDateFormat.format(row.getDate(colName));

                case NUMERIC:
                    int scale = field.getDecimalCount();
                    int precision = field.getFieldLength();
                    if (scale == 0) {
                        return String.valueOf(row.getLong(colName));
                    }
                    else {
                        String pattern = "%" + precision + "." + scale + "f";
                        return String.format(pattern, row.getBigDecimal(colName));
                    }

                default:
                    return row.getObject(colName).toString();
            }
        }
    }
}
