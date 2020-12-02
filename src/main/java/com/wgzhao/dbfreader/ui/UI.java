/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader.ui;

import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 13:44
 */
public interface UI
{

    void setUIEnabled(boolean enabled);

    void setStatusText(String status);

    void setProgress(int progress);

    void errorOccurred(Exception e);

    void showMessage(String title, String message);

    void clearTable();

    void setTableHeader(List<String> names);

    void addRecord(List<String> values);

    void indexChanged();

    void setSearching(boolean isSearching);

    void setSearchResults(String results);
}
