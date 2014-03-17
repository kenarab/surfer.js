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

/**
 * A class to retrieve row data to be used in a table.
 * 
 * @param <R> the data type of the row values
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.client.ListTableModel}
 */
@Deprecated
public class ListTableModel<R> extends ClientTableModel<R>
{
    /**
     * The row data.
     */
    private List<List<Object>> rows;

    /**
     * Constructor.
     * 
     * @param rows the data that this model feeds from
     */
    public ListTableModel(List<List<Object>> rows)
    {
	this.rows= rows;
	setRowCount(rows.size());
    }

    /**
     * @see ClientTableModel
     */
    @Override
    public Object getCell(int rowNum, int cellNum)
    {
	// Get the row List
	List<Object> row= getList(rowNum);
	if (row == null)
	{
	    return null;
	}

	// Get the
	if (cellNum >= row.size())
	{
	    return null;
	}
	return row.get(cellNum);
    }

    @Override
    protected boolean onRowInserted(int beforeRow)
    {
	if (beforeRow < rows.size())
	{
	    rows.add(beforeRow, null);
	}
	return true;
    }

    @Override
    protected boolean onRowRemoved(int row)
    {
	if (row < rows.size())
	{
	    rows.remove(row);
	}
	return true;
    }

    @Override
    protected boolean onSetData(int row, int cell, Object data)
    {
	// Get the row list
	List<Object> rowList= getList(row);
	if (rowList == null)
	{
	    // Expand to fit row
	    for (int i= rows.size(); i <= row; i++)
	    {
		rows.add(null);
	    }

	    // Create the rowList
	    rowList= new ArrayList<Object>();
	    rows.set(row, rowList);
	}

	// Enlarge the row as needed
	for (int i= rowList.size(); i <= cell; i++)
	{
	    rowList.add(null);
	}
	rowList.set(cell, data);

	return true;
    }

    /**
     * Get the list for a given row.
     * 
     * @param rowIndex the row index
     */
    private List<Object> getList(int rowIndex)
    {
	if (rowIndex >= rows.size())
	{
	    return null;
	}
	return rows.get(rowIndex);
    }
}
