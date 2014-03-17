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

import java.util.Iterator;

import com.google.gwt.widgetideas.table.client.overrides.Grid;

/**
 * Allows bulk rendering of {@link Grid}s.
 * <p>
 * Must use the {@link Grid} in the overrides package.
 * </p>
 * 
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.client.GridBulkRenderer}
 */
@Deprecated
public class GridBulkRenderer extends TableBulkRenderer
{

    /**
     * Grids need repair code after the table is constructed. The
     * {@link ChainingCallback} allows the bulk renderer to insert repair code
     * before the user's callback is called.
     */
    protected class ChainingCallback implements RendererCallback
    {
	private RendererCallback userCallback;

	ChainingCallback(RendererCallback userCallback)
	{
	    this.userCallback= userCallback;
	}

	public void onRendered()
	{
	    localOnRendered();
	    if (this.userCallback != null)
	    {
		this.userCallback.onRendered();
	    }
	}

	protected void localOnRendered()
	{
	    Grid table= (Grid) getTable();
	    setGridDimensions(table);
	}
    }

    /**
     * Constructor.
     * 
     * @param grid Grid to be be bulk rendered
     */
    public GridBulkRenderer(Grid grid)
    {
	super(grid);
	if (grid instanceof FixedWidthGrid && (!(this instanceof FixedWidthGridBulkRenderer)))
	{
	    throw new UnsupportedOperationException("Must use a FixedWidthGridBulkLoader to bulk load a fixed grid");
	}
	if (grid instanceof SelectionGrid && (!(this instanceof SelectionGridBulkRenderer)))
	{
	    throw new UnsupportedOperationException("Must use a SelectionGridBulkLoader to bulk load a selection grid");
	}
    }

    /**
     * Adds the chaining callback to the given options. Pulled out to allow
     * subclasses to override.
     * 
     * @param options options to add the chaining callback to
     */
    protected void addChainingCallback(final RenderingOptions options)
    {
	// Set the callback.
	options.callback= new ChainingCallback(options.callback);
    }

    @Override
    protected void renderRows(Iterator<Iterator<Object>> iterator, final RenderingOptions options)
    {
	addChainingCallback(options);

	// Render the rows.
	super.renderRows(iterator, options);
    }

    /**
     * Short term hack to set protected row and columns.
     */
    @SuppressWarnings("deprecation")
    native void setGridDimensions(Grid table) /*-{
					      var numRows =  table.@com.google.gwt.widgetideas.table.client.overrides.HTMLTable::getDOMRowCount()();
					      table.@com.google.gwt.widgetideas.table.client.overrides.Grid::numRows = numRows;
					      table.@com.google.gwt.widgetideas.table.client.overrides.Grid::numColumns =
					      table.@com.google.gwt.widgetideas.table.client.overrides.HTMLTable::getDOMCellCount(I)(numRows - 1);
					      }-*/;

}
