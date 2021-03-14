/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader.domain;

import org.jamel.dbf.DbfReader;
import org.jamel.dbf.structure.DbfField;
import org.jamel.dbf.structure.DbfRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 17/10/13
 * Time: 15:39
 */
public class TableSearcher
        implements Runnable
{

    private final String searchString;

    public TableSearcher(String searchString)
    {

        this.searchString = searchString;
    }

    @Override
    public void run()
    {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (DbfReader reader = new DbfReader(Controller.INSTANCE.getPath())) {

            Controller.INSTANCE.setUIEnabled(false);
            Controller.INSTANCE.setStatusText("Searching...");
            Controller.INSTANCE.setProgress(0);
            Controller.INSTANCE.setSearching(true);
            Controller.INSTANCE.setSearchResults("Search results for \"" + searchString + "\"");
            Controller.INSTANCE.clearTable();
            Controller.INSTANCE.indexChanged();

            int recordCount = reader.getRecordCount();
            int fieldNum = reader.getHeader().getFieldsCount();

            //Variables
            List<String> headerNames = new ArrayList<>();
            Controller.INSTANCE.clearTable();

            //Fields & Header
            List<DbfField> fields = new ArrayList<>();

            for (int i = 0; i < fieldNum; i++) {
                DbfField dbfField = reader.getHeader().getField(i);
                fields.add(dbfField);
                headerNames.add(dbfField.getName());
            }

            Controller.INSTANCE.setTableHeader(headerNames);

            //Records
            int count;
            int hitCount = 0;
            for (count = 0; count < recordCount; count++) {

                Controller.INSTANCE.setStatusText(String.format("Searching... (%d/%d)",
                        count + 1,
                        recordCount));
                reader.seekToRecord(count);
                DbfRow row = reader.nextRow();
                List<String> recordValues = new ArrayList<>();
                boolean hasHit = false;
                for (DbfField field : fields) {
                    String colName = field.getName();
                    if (row.getObject(colName) != null) {
                        String value = row.getString(colName);
                        if (value.toLowerCase().contains(searchString.toLowerCase())) {
                            hasHit = true;
                            hitCount++;
                        }
                    }
                    if (row.getObject(colName) == null) {
                        recordValues.add("");
                    }
                    else {
                        switch (field.getDataType()) {
                            case CHAR:
                                recordValues.add(row.getString(colName));
                                break;

                            case LOGICAL:
                                recordValues.add(row.getBoolean(colName) ? "true" : "false");
                                break;

                            case DATE:
                                recordValues.add(simpleDateFormat.format(row.getDate(colName)));
                                break;

                            case NUMERIC:
                                recordValues.add(row.getBigDecimal(colName).toPlainString());
                                break;

                            case FLOAT:
                                recordValues.add(String.format("%f", row.getFloat(colName)));
                                break;

                            default:
                                recordValues.add(row.getObject(colName).toString());
                                break;
                        }
                    }
                }
                if (hasHit) {

                    Controller.INSTANCE.addRecord(recordValues);
                    Controller.INSTANCE.setSearchResults(String.format("Search results for \"%s\" " +
                            "(%d hit%s)", searchString, hitCount, hitCount == 1 ? "" : "s"));
                }

                Controller.INSTANCE.setProgress((int) Math.floor(((double) count / recordCount) * 100.0));
            }
        }
        finally {
            Controller.INSTANCE.setUIEnabled(true);
            Controller.INSTANCE.setStatusText("Ready");
            Controller.INSTANCE.setProgress(100);
        }
    }
}
