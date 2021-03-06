/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.widgetideas.table.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable;

/**
 * A {@link PreloadedTable} allow users to configure a table with HTML cell data
 * before rendering the table. This can lead to faster performance for large
 * tables.
 * 
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.client.PreloadedTable}
 */
@Deprecated
public class PreloadedTable extends FlexTable
{
    List<List<Object>> rows= new ArrayList<List<Object>>();
    boolean isFilledIn= false;

    /*
     * As often users manipulate a single row at a time, caching the current row
     * and index for speed;
     */
    List<Object> curRow;

    int curIndex= -1;

    @Override
    public void setHTML(int row, int column, String html)
    {
	ensureLoaded();
	super.setHTML(row, column, html);
    }

    /**
     * Sets cells HTML that will be included in the table upon initialization.
     * 
     * @param row row
     * @param column column
     * @param html html to set
     */
    public void setPendingHTML(int row, int column, String html)
    {
	if (isFilledIn)
	{
	    throw new UnsupportedOperationException("Cannot call setPendingHTML after either setWidget/setText/setHTML has been called or the table has been attached to the DOM");
	}
	List<Object> l= ensureRow(row);
	ensureCell(l, column, html);
    }

    @Override
    public void setText(int row, int column, String text)
    {
	ensureLoaded();
	super.setText(row, column, text);
    }

    @Override
    public void setWidget(int row, int column, Widget widget)
    {
	ensureLoaded();
	super.setWidget(row, column, widget);
    }

    @Override
    protected void onLoad()
    {
	ensureLoaded();
    }

    private void ensureCell(List<Object> row, int column, String value)
    {
	if (column == row.size())
	{
	    row.add(value);
	}
	else
	{
	    for (int i= row.size(); i < column + 1; i++)
	    {
		row.add("");
	    }
	    row.set(column, value);
	}
    }

    private void ensureLoaded()
    {
	if (!isFilledIn)
	{
	    isFilledIn= true;
	    new FlexTableBulkRenderer(this).renderRows(rows, null);
	}
    }

    private List<Object> ensureRow(int row)
    {
	if (row == curIndex)
	{
	    return curRow;
	}
	for (int i= rows.size(); i < row + 1; i++)
	{
	    curRow= new ArrayList<Object>();
	    curRow.add("");
	    rows.add(curRow);
	}
	curRow= rows.get(row);
	curIndex= row;
	return rows.get(row);
    }
}
