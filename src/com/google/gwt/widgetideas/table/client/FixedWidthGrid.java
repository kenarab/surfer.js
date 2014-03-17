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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.widgetideas.table.client.overrides.OverrideDOM;

import java.util.HashMap;
import java.util.Map;

/**
 * A variation of the
 * {@link com.google.gwt.widgetideas.table.client.overrides.Grid} that resizes
 * columns using a fixed table width.
 * 
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.client.FixedWidthGrid}
 */
@Deprecated
public class FixedWidthGrid extends SortableGrid
{
    /**
     * This class contains methods used to format a table's cells.
     */
    public class FixedWidthGridCellFormatter extends SelectionGridCellFormatter
    {
	@Override
	public void setWidth(int row, int column, String width)
	{
	    throw new UnsupportedOperationException("setWidth is not supported.  " + "Use FixedWidthGrid.setColumnWidth(int, int) instead.");
	}

	@Override
	protected Element getRawElement(int row, int column)
	{
	    return super.getRawElement(row + 1, column);
	}
    }

    /**
     * This class contains methods used to format a table's columns.
     */
    public class FixedWidthGridColumnFormatter extends ColumnFormatter
    {
	@Override
	public void setWidth(int column, String width)
	{
	    throw new UnsupportedOperationException("setWidth is not supported.  " + "Use FixedWidthdGrid.setColumnWidth(int, int) instead.");
	}
    }

    /**
     * This class contains methods used to format a table's rows.
     */
    public class FixedWidthGridRowFormatter extends SelectionGridRowFormatter
    {
	@Override
	protected Element getRawElement(int row)
	{
	    return super.getRawElement(row + 1);
	}
    }

    /**
     * An implementation used to accommodate differences in column width
     * implementations between browsers.
     */
    private static class FixedWidthGridImpl
    {
	/**
	 * Create the ghost row.
	 * 
	 * @return the ghost row element
	 */
	public Element createGhostRow()
	{
	    Element ghostRow= DOM.createTR();
	    DOM.setStyleAttribute(ghostRow, "margin", "0px");
	    DOM.setStyleAttribute(ghostRow, "padding", "0px");
	    DOM.setStyleAttribute(ghostRow, "height", "0px");
	    DOM.setStyleAttribute(ghostRow, "overflow", "hidden");
	    return ghostRow;
	}

	/**
	 * Set the width of a column.
	 * 
	 * @param grid the grid to resize
	 * @param column the index of the column
	 * @param width the width in pixels
	 * @throws IndexOutOfBoundsException
	 */
	public void setColumnWidth(FixedWidthGrid grid, int column, int width)
	{
	    DOM.setStyleAttribute(grid.getGhostCellElement(column), "width", width + "px");
	}
    }

    /**
     * Firefox version of the implementation refreshes the table display so the
     * new column size takes effect. Without the display refresh, the column width
     * doesn't update in the browser.
     */
    @SuppressWarnings("unused")
    private static class FixedWidthGridImplFirefox extends FixedWidthGridImpl
    {
	private boolean pendingUpdate= false;

	@Override
	public void setColumnWidth(FixedWidthGrid grid, int column, int width)
	{
	    super.setColumnWidth(grid, column, width);
	    if (!pendingUpdate)
	    {
		pendingUpdate= true;
		final Element tableElem= grid.getElement();
		tableElem.getStyle().setProperty("width", "1px");
		DeferredCommand.addCommand(new Command()
		{
		    public void execute()
		    {
			tableElem.getStyle().setProperty("width", "0px");
			pendingUpdate= false;
		    }
		});
	    }
	}
    }

    /**
     * IE6 version of the implementation. IE6 sets the overall column width
     * instead of the innerWidth, so we need to add the padding and spacing.
     */
    @SuppressWarnings("unused")
    private static class FixedWidthGridImplIE6 extends FixedWidthGridImpl
    {
	@Override
	public Element createGhostRow()
	{
	    Element ghostRow= super.createGhostRow();
	    DOM.setStyleAttribute(ghostRow, "display", "none");
	    return ghostRow;
	}

	@Override
	public void setColumnWidth(FixedWidthGrid grid, int column, int width)
	{
	    width+= 2 * grid.getCellPadding() + grid.getCellSpacing();
	    super.setColumnWidth(grid, column, width);
	}
    }

    /**
     * Opera version of the implementation refreshes the table display so the new
     * column size takes effect. Without the display refresh, the column width
     * doesn't update in the browser.
     */
    @SuppressWarnings("unused")
    private static class FixedWidthGridImplOpera extends FixedWidthGridImpl
    {
	@Override
	public void setColumnWidth(FixedWidthGrid grid, int column, int width)
	{
	    super.setColumnWidth(grid, column, width);
	    Element tableElem= grid.getElement();
	    Element parentElem= DOM.getParent(tableElem);
	    int scrollLeft= 0;
	    if (parentElem != null)
	    {
		scrollLeft= DOM.getElementPropertyInt(parentElem, "scrollLeft");
	    }
	    DOM.setStyleAttribute(tableElem, "display", "none");
	    DOM.setStyleAttribute(tableElem, "display", "");
	    if (parentElem != null)
	    {
		DOM.setElementPropertyInt(parentElem, "scrollLeft", scrollLeft);
	    }
	}
    }

    /**
     * Safari version of the implementation. Safari sets the overall column width
     * instead of the innerWidth, so we need to add the padding and spacing.
     */
    @SuppressWarnings("unused")
    private static class FixedWidthGridImplSafari extends FixedWidthGridImpl
    {
	@Override
	public void setColumnWidth(FixedWidthGrid grid, int column, int width)
	{
	    width+= 2 * grid.getCellPadding() + grid.getCellSpacing();
	    super.setColumnWidth(grid, column, width);
	}
    }

    /**
     * The default width of a column in pixels.
     */
    public static final int DEFAULT_COLUMN_WIDTH= 80;

    /**
     * The minimum width of any column.
     */
    public static final int MIN_COLUMN_WIDTH= 1;

    /**
     * The implementation class.
     */
    private static FixedWidthGridImpl impl= GWT.create(FixedWidthGridImpl.class);

    /**
     * A mapping of column indexes to their widths in pixels.
     */
    private Map<Integer, Integer> colWidths= new HashMap<Integer, Integer>();

    /**
     * The hidden, ghost row used for sizing the columns.
     */
    private Element ghostRow= null;

    /**
     * Constructor.
     */
    public FixedWidthGrid()
    {
	super();
	setClearText("&nbsp;");

	// Setup the table element
	Element tableElem= getElement();
	DOM.setStyleAttribute(tableElem, "tableLayout", "fixed");
	DOM.setStyleAttribute(tableElem, "width", "0px");

	// Replace the formatters
	setRowFormatter(new FixedWidthGridRowFormatter());
	setCellFormatter(new FixedWidthGridCellFormatter());
	setColumnFormatter(new FixedWidthGridColumnFormatter());

	// Create the ghost row for sizing
	ghostRow= impl.createGhostRow();
	DOM.insertChild(getBodyElement(), ghostRow, 0);

	// Sink hover and selection events
	sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEDOWN | Event.ONCLICK);
    }

    /**
     * Constructs a {@link FixedWidthGrid} with the requested size.
     * 
     * @param rows the number of rows
     * @param columns the number of columns
     * @throws IndexOutOfBoundsException
     */
    public FixedWidthGrid(int rows, int columns)
    {
	this();
	resize(rows, columns);
    }

    /**
     * Return the column width for a given column index. If a width has not been
     * assigned, the default width is returned.
     * 
     * @param column the column index
     * @return the column width in pixels
     */
    public int getColumnWidth(int column)
    {
	Integer colWidth= colWidths.get(new Integer(column));
	if (colWidth == null)
	{
	    return DEFAULT_COLUMN_WIDTH;
	}
	else
	{
	    return colWidth.intValue();
	}
    }

    @Override
    public void resizeColumns(int columns)
    {
	super.resizeColumns(columns);
	updateGhostRow();
    }

    @Override
    public void setCellPadding(int padding)
    {
	super.setCellPadding(padding);

	// Reset the width of all columns
	for (Map.Entry<Integer, Integer> entry : colWidths.entrySet())
	{
	    setColumnWidth(entry.getKey(), entry.getValue());
	}
	if (getSelectionPolicy().hasInputColumn())
	{
	    impl.setColumnWidth(this, -1, getInputColumnWidth());
	}
    }

    @Override
    public void setCellSpacing(int spacing)
    {
	super.setCellSpacing(spacing);

	// Reset the width of all columns
	for (Map.Entry<Integer, Integer> entry : colWidths.entrySet())
	{
	    setColumnWidth(entry.getKey(), entry.getValue());
	}
	if (getSelectionPolicy().hasInputColumn())
	{
	    impl.setColumnWidth(this, -1, getInputColumnWidth());
	}
    }

    /**
     * Set the width of a column.
     * 
     * @param column the index of the column
     * @param width the width in pixels
     * @throws IndexOutOfBoundsException
     */
    public void setColumnWidth(int column, int width)
    {
	// Ensure that the indices are not negative.
	if (column < 0)
	{
	    throw new IndexOutOfBoundsException("Cannot access a column with a negative index: " + column);
	}

	// Add the width to the map
	width= Math.max(MIN_COLUMN_WIDTH, width);
	colWidths.put(new Integer(column), new Integer(width));

	// Update the cell width if possible
	if (column >= numColumns)
	{
	    return;
	}

	// Set the actual column width
	impl.setColumnWidth(this, column, width);
    }

    @Override
    public void setSelectionPolicy(SelectionPolicy selectionPolicy)
    {
	// Update the input column in the ghost row
	if (selectionPolicy.hasInputColumn() && !getSelectionPolicy().hasInputColumn())
	{
	    // Add ghost input column
	    Element tr= getGhostRow();
	    Element td= createGhostCell();
	    tr.insertBefore(td, tr.getFirstChildElement());
	    super.setSelectionPolicy(selectionPolicy);
	    impl.setColumnWidth(this, -1, getInputColumnWidth());
	}
	else if (!selectionPolicy.hasInputColumn() && getSelectionPolicy().hasInputColumn())
	{
	    // Remove ghost input column
	    Element tr= getGhostRow();
	    tr.removeChild(tr.getFirstChildElement());
	    super.setSelectionPolicy(selectionPolicy);
	}
	else
	{
	    super.setSelectionPolicy(selectionPolicy);
	}
    }

    @Override
    protected int getDOMCellCount(int row)
    {
	return super.getDOMCellCount(row + 1);
    }

    @Override
    protected int getDOMRowCount()
    {
	return super.getDOMRowCount() - 1;
    }

    /**
     * Explicitly gets the {@link FixedWidthGridCellFormatter}. The results of
     * {@link com.google.gwt.user.client.ui.HTMLTable#getCellFormatter()} may also
     * be downcast to a {@link FixedWidthGridCellFormatter}.
     * 
     * @return the {@link FixedWidthGrid}'s cell formatter
     */
    protected FixedWidthGridCellFormatter getFixedWidthGridCellFormatter()
    {
	return (FixedWidthGridCellFormatter) getCellFormatter();
    }

    /**
     * Explicitly gets the {@link FixedWidthGridRowFormatter}. The results of
     * {@link com.google.gwt.user.client.ui.HTMLTable#getCellFormatter()} may also
     * be downcast to a {@link FixedWidthGridRowFormatter}.
     * 
     * @return the {@link FixedWidthGrid}'s cell formatter
     */
    protected FixedWidthGridRowFormatter getFixedWidthGridRowFormatter()
    {
	return (FixedWidthGridRowFormatter) getRowFormatter();
    }

    /**
     * @return the number of columns in the ghost row
     */
    protected int getGhostColumnCount()
    {
	return super.getDOMCellCount(0);
    }

    /**
     * @return the ghost row element
     */
    protected Element getGhostRow()
    {
	return ghostRow;
    }

    /**
     * Get the width of the input column used in the current
     * {@link SelectionGrid.SelectionPolicy}.
     * 
     * @return the width of the input element
     */
    protected int getInputColumnWidth()
    {
	return 30;
    }

    @Override
    protected int getRowIndex(Element rowElem)
    {
	int rowIndex= super.getRowIndex(rowElem);
	if (rowIndex < 0)
	{
	    return rowIndex;
	}
	return rowIndex - 1;
    }

    @Override
    protected void prepareCell(int row, int column)
    {
	prepareColumn(column);
	super.prepareCell(row, column);
    }

    @Override
    protected void prepareColumn(int column)
    {
	// Ensure that the indices are not negative.
	if (column < 0)
	{
	    throw new IndexOutOfBoundsException("Cannot access a column with a negative index: " + column);
	}

	// FixedWidthGrid lazily creates cells
	if (column >= numColumns)
	{
	    resizeColumns(column + 1);
	}
    }

    @Override
    protected void prepareRow(int row)
    {
	// Ensure that the indices are not negative.
	if (row < 0)
	{
	    throw new IndexOutOfBoundsException("Cannot access a row with a negative index: " + row);
	}

	// Lazily creates cells
	if (row >= numRows)
	{
	    resizeRows(row + 1);
	}
    }

    /**
     * Sets the ghost row variable. This does not change the underlying structure
     * of the table.
     * 
     * @param ghostRow the new ghost row
     */
    protected void setGhostRow(Element ghostRow)
    {
	this.ghostRow= ghostRow;
    }

    /**
     * Add or remove ghost cells when the table size changes.
     */
    protected void updateGhostRow()
    {
	int numGhosts= getGhostColumnCount();
	if (numColumns > numGhosts)
	{
	    // Add ghosts as needed
	    for (int i= numGhosts; i < numColumns; i++)
	    {
		Element td= createGhostCell();
		DOM.appendChild(ghostRow, td);
		setColumnWidth(i, getColumnWidth(i));
	    }
	}
	else if (numColumns < numGhosts)
	{
	    int cellsToRemove= numGhosts - numColumns;
	    for (int i= 0; i < cellsToRemove; i++)
	    {
		Element td= getGhostCellElement(numColumns);
		DOM.removeChild(ghostRow, td);
	    }
	}
    }

    @Override
    void applySort(Element[] trElems)
    {
	// Move the rows to their new positions
	Element bodyElem= getBodyElement();
	for (int i= trElems.length - 1; i >= 0; i--)
	{
	    if (trElems[i] != null)
	    {
		DOM.removeChild(bodyElem, trElems[i]);
		// Need to insert below the ghost row
		DOM.insertChild(bodyElem, trElems[i], 1);
	    }
	}
    }

    /**
     * @return a cell to use in the ghost row
     */
    private Element createGhostCell()
    {
	Element td= OverrideDOM.createTD();
	td.getStyle().setPropertyPx("height", 0);
	td.getStyle().setProperty("overflow", "hidden");
	td.getStyle().setPropertyPx("paddingTop", 0);
	td.getStyle().setPropertyPx("paddingBottom", 0);
	td.getStyle().setPropertyPx("borderTop", 0);
	td.getStyle().setPropertyPx("borderBottom", 0);
	td.getStyle().setPropertyPx("margin", 0);
	return td;
    }

    /**
     * Returns a cell in the ghost row.
     * 
     * @param column the cell's column
     * @return the ghost cell
     */
    private Element getGhostCellElement(int column)
    {
	if (getSelectionPolicy().hasInputColumn())
	{
	    column++;
	}
	return DOM.getChild(ghostRow, column);
    }
}
