/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader.domain;

import org.jamel.dbf.processor.DbfProcessor;

import java.io.File;
import java.nio.charset.Charset;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 16/10/13
 * Time: 14:05
 */
public class SQLExporter
        implements Runnable
{

    private final File target;

    public SQLExporter(File target)
    {

        this.target = target;
    }

    @Override
    public void run()
    {

        try {

            Controller.INSTANCE.setUIEnabled(false);

            //Set variables
            Controller.INSTANCE.setProgress(0);
            Controller.INSTANCE.setStatusText("Exporting %s to csv file...");
            DbfProcessor.writeToTxtFile(
                    Controller.INSTANCE.getPath(),
                    this.target,
                    Charset.defaultCharset()
            );

            Controller.INSTANCE.showMessage("Completed", "Export to SQL completed!");
        }
        catch (Exception e) {

            e.printStackTrace();
            Controller.INSTANCE.errorOccurred(e);
        }
        finally {

            Controller.INSTANCE.setUIEnabled(true);
            Controller.INSTANCE.setStatusText("Ready");
            Controller.INSTANCE.setProgress(100);
        }
    }
}
