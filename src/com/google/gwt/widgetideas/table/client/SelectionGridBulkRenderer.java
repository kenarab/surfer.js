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

import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.widgetideas.table.client.overrides.Grid;

import java.util.Iterator;

/**
 * Allows bulk rendering of {@link SelectionGrid}s.
 * 
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.client.SelectionGridBulkRenderer}
 */
@Deprecated
public class SelectionGridBulkRenderer extends GridBulkRenderer
{
    /**
     * Construct a new {@link SelectionGridBulkRenderer}.
     * 
     * @param grid {@link Grid} to be be bulk rendered
     */
    public SelectionGridBulkRenderer(Grid grid)
    {
	super(grid);
    }

    @Override
    protected void renderRow(int rowIndex, Iterator<Object> row, StringBuffer buffer, final RenderingOptions options)
    {
	buffer.append("<tr>");

	// Add the input column
	SelectionPolicy selectionPolicy= ((SelectionGrid) getTable()).getSelectionPolicy();
	if (selectionPolicy.hasInputColumn())
	{
	    buffer.append("<td align='CENTER'>");
	    buffer.append(((SelectionGrid) getTable()).getInputHtml(selectionPolicy));
	    buffer.append("</td>");
	}

	for (int cellIndex= 0; row.hasNext(); ++cellIndex)
	{
	    buffer.append(options.getStartCell(cellIndex));
	    Object next= row.next();
	    getCellRenderer().renderCell(rowIndex, cellIndex, next, buffer);
	    buffer.append(options.getEndCell(cellIndex));
	}
	buffer.append("</tr>");
    }

}
