/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader.domain;

import com.wgzhao.dbfreader.DBFReader;
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

    public TableSearcher(String searchString )
    {

        this.searchString = searchString;
    }

    @Override
    public void run()
    {

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
                    String fieldValue = Utils.getRecord(field, row);
                    if (fieldValue.toLowerCase().contains(searchString.toLowerCase())) {
                        hasHit = true;
                        hitCount++;
                    }
                    recordValues.add(fieldValue);
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
