/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader.domain;

import org.jamel.dbf.DbfReader;
import org.jamel.dbf.structure.DbfField;
import org.jamel.dbf.structure.DbfRow;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 14:25
 */
public class TableLoader
        implements Runnable
{

    private final File path;

    public TableLoader(File path)
    {

        this.path = path;
    }

    @Override
    public void run()
    {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (DbfReader reader = new DbfReader(this.path)) {

            int recordCount = reader.getRecordCount();
            int fieldNum = reader.getHeader().getFieldsCount();
            if (Controller.INSTANCE.getOffset() > recordCount) {

                Controller.INSTANCE.setOffset(Controller.INSTANCE.getOffset() - Controller.NUMBER_OF_RECORDS);
                return;
            }

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

            //Table info
            TableInfo tableInfo = new TableInfo(
                    this.path.getName(),
                    new Date(this.path.lastModified()),
                    fields.size(),
                    recordCount,
                    null);

            Controller.INSTANCE.setTableInfo(tableInfo);

            Controller.INSTANCE.setTableHeader(headerNames);

            //Records

            int to = Controller.INSTANCE.getOffset() + Controller.NUMBER_OF_RECORDS;
            if (to >= recordCount) {

                to = recordCount;
                Controller.INSTANCE.setNext(false);
            }
            else {

                Controller.INSTANCE.setNext(true);
            }

            //Set from & to
            Controller.INSTANCE.setShowingFrom(Controller.INSTANCE.getOffset());
            Controller.INSTANCE.setShowingTo(to);
            Controller.INSTANCE.indexChanged();

            int count = 0;
            int total = to - Controller.INSTANCE.getOffset();

            for (int j = Controller.INSTANCE.getOffset(); j < to; j++) {

                Controller.INSTANCE.setStatusText(String.format("Loading dBASE %s (%d/%d)",
                        path.getName(),
                        (j + 1),
                        to));

                reader.seekToRecord(j);
                DbfRow row = reader.nextRow();
                List<String> recordValues = new ArrayList<>();
                for (DbfField field : fields) {
                    recordValues.add(Utils.getRecord(field, row));
                }

                Controller.INSTANCE.addRecord(recordValues);

                Controller.INSTANCE.setProgress((int) Math.floor(((double) count++ / (double) total) * 100.0));
            }
        }
        finally {
            //Done
            Controller.INSTANCE.setStatusText("Ready");
            Controller.INSTANCE.setProgress(100);
            Controller.INSTANCE.setUIEnabled(true);
        }
    }
}
