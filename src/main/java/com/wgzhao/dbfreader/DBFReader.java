/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader;

import com.wgzhao.dbfreader.domain.Controller;
import com.wgzhao.dbfreader.ui.graphical.GUIPanelContainer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 13:43
 */
public class DBFReader
{

    public DBFReader()
    {

        SwingUtilities.invokeLater(new Runnable()
        {

            public void run()
            {

                try {

                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception e) {

                    e.printStackTrace();
                }

                GUIPanelContainer guiPanelContainer = new GUIPanelContainer();
                Controller.INSTANCE.addObserver(guiPanelContainer);
                guiPanelContainer.postConstruct();
            }
        });
    }

    public static void main(String[] args)
    {

        new DBFReader();
    }
}
