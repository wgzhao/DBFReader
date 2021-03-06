/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader.domain;

import java.util.ResourceBundle;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 18/10/13
 * Time: 11:24
 */
public class Config
{

    public static String getApplicationVersion()
    {

        return getConfig().getString("dbfreader.version");
    }

    public static String getVersion()
    {

        return getApplicationVersion();
    }

    private static ResourceBundle getConfig()
    {

        return ResourceBundle.getBundle("dbfreader");
    }
}
