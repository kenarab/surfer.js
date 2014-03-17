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

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.widgetideas.table.client.overrides.Grid;
import com.google.gwt.widgetideas.table.client.overrides.OverrideDOM;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A variation of the {@link Grid} that supports row or cell hovering and row
 * selection.
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <ul class="css">
 * 
 * <li>tr.selected { applied to selected rows }</li>
 * 
 * <li>tr.hovering { applied to row currently being hovered }</li>
 * 
 * <li>td.hovering { applied to cell currently being hovered }</li>
 * 
 * </ul>
 * 
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.client.SelectionGrid}
 */
@Deprecated
public class SelectionGrid extends Grid implements SourceTableSelectionEvents
{
    /**
     * This class contains methods used to format a table's cells.
     */
    public class SelectionGridCellFormatter extends CellFormatter
    {
	@Override
	protected Element getRawElement(int row, int column)
	{
	    if (selectionPolicy.hasInputColumn())
	    {
		column+= 1;
	    }
	    return super.getRawElement(row, column);
	}
    }

    /**
     * This class contains methods used to format a table's rows.
     */
    public class SelectionGridRowFormatter extends RowFormatter
    {
	@Override
	protected Element getRawElement(int row)
	{
	    return super.getRawElement(row);
	}
    }

    /**
     * Selection policies.
     * 
     * <ul>
     * <li>ONE_ROW - one row can be selected at a time</li>
     * <li>MULTI_ROW - multiple rows can be selected at a time</li>
     * <li>CHECKBOX - multiple rows can be selected using checkboxes</li>
     * <li>RADIO - one row can be selected using radio buttons</li>
     * </ul>
     */
    public static enum SelectionPolicy
    {
	ONE_ROW(null), MULTI_ROW(null), CHECKBOX("<input type='checkbox'/>"), RADIO("<input name='%NAME%' type='radio'/>");

	private String inputHtml;

	private SelectionPolicy(String inputHtml)
	{
	    this.inputHtml= inputHtml;
	}

	/**
	 * @return true if the policy requires a selection column
	 */
	public boolean hasInputColumn()
	{
	    return inputHtml != null;
	}

	/**
	 * @return the HTML string used for this policy
	 */
	private String getInputHtml()
	{
	    return inputHtml;
	}
    }

    /**
     * Unique IDs assigned to the grids.
     */
    private static int uniqueID= 0;

    /**
     * The cell element currently being hovered.
     */
    private Element hoveringCellElem= null;

    /**
     * The index of the cell currently being hovered.
     */
    private int hoveringCellIndex= -1;

    /**
     * The row element currently being hovered.
     */
    private Element hoveringRowElem= null;

    /**
     * The index of the row currently being hovered.
     */
    private int hoveringRowIndex= -1;

    /**
     * Unique ID of this grid.
     */
    private int id;

    /**
     * The index of the row that the user selected last.
     */
    private int lastSelectedRowIndex= -1;

    /**
     * The rows that are currently selected.
     */
    private Map<Integer, Element> selectedRows= new HashMap<Integer, Element>();

    /**
     * A boolean indicating if selection is enabled disabled.
     */
    private boolean selectionEnabled= true;

    /**
     * The selection policy determines if the user can select zero, one, or many
     * rows.
     */
    private SelectionPolicy selectionPolicy= SelectionPolicy.MULTI_ROW;

    /**
     * The {@link TableSelectionListener}s attached to this grid.
     */
    private TableSelectionListenerCollection tableSelectionListeners= null;

    /**
     * Construct a new {@link SelectionGrid}.
     */
    public SelectionGrid()
    {
	super();
	id= uniqueID++;
	setCellFormatter(new SelectionGridCellFormatter());
	setRowFormatter(new SelectionGridRowFormatter());

	// Sink hover and selection events
	sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEDOWN | Event.ONCLICK);
    }

    /**
     * Constructs a {@link SelectionGrid} with the requested size.
     * 
     * @param rows the number of rows
     * @param columns the number of columns
     * @throws IndexOutOfBoundsException
     */
    public SelectionGrid(int rows, int columns)
    {
	this();
	resize(rows, columns);
    }

    /**
     * Add a {@link TableSelectionListener}.
     * 
     * @param listener the listener
     */
    public void addTableSelectionListener(TableSelectionListener listener)
    {
	if (tableSelectionListeners == null)
	{
	    tableSelectionListeners= new TableSelectionListenerCollection();
	}
	tableSelectionListeners.add(listener);
    }

    /**
     * Deselect all selected rows in the data table.
     */
    public void deselectAllRows()
    {
	// Deselect all rows
	boolean hasInputColumn= selectionPolicy.hasInputColumn();
	for (Map.Entry<Integer, Element> entry : selectedRows.entrySet())
	{
	    Element rowElem= entry.getValue();
	    setStyleName(rowElem, "selected", false);
	    if (hasInputColumn)
	    {
		setInputSelected(getSelectionPolicy(), (Element) rowElem.getFirstChildElement(), false);
	    }
	}

	// Clear out the rows
	selectedRows.clear();

	// Fire grid listeners
	if (tableSelectionListeners != null)
	{
	    tableSelectionListeners.fireAllRowsDeselected(this);
	}
    }

    /**
     * Deselect a row in the grid. This method is safe to call even if the row is
     * not selected, or doesn't exist (out of bounds).
     * 
     * @param row the row index
     */
    public void deselectRow(int row)
    {
	Element rowElem= selectedRows.remove(new Integer(row));
	if (rowElem != null)
	{
	    setStyleName(rowElem, "selected", false);
	    if (selectionPolicy.hasInputColumn())
	    {
		setInputSelected(getSelectionPolicy(), (Element) rowElem.getFirstChildElement(), false);
	    }
	    if (tableSelectionListeners != null)
	    {
		tableSelectionListeners.fireRowDeselected(this, row);
	    }
	}
    }

    /**
     * @return the set of selected row indexes
     */
    public Set<Integer> getSelectedRows()
    {
	return selectedRows.keySet();
    }

    /**
     * Explicitly gets the {@link SelectionGridCellFormatter}. The results of
     * {@link com.google.gwt.widgetideas.table.client.overrides.HTMLTable#getCellFormatter()}
     * may also be downcast to a {@link SelectionGridCellFormatter}.
     * 
     * @return the FlexTable's cell formatter
     */
    public SelectionGridCellFormatter getSelectionGridCellFormatter()
    {
	return (SelectionGridCellFormatter) getCellFormatter();
    }

    /**
     * Explicitly gets the {@link SelectionGridRowFormatter}. The results of
     * {@link com.google.gwt.widgetideas.table.client.overrides.HTMLTable#getRowFormatter()}
     * may also be downcast to a {@link SelectionGridRowFormatter}.
     * 
     * @return the FlexTable's cell formatter
     */
    public SelectionGridRowFormatter getSelectionGridRowFormatter()
    {
	return (SelectionGridRowFormatter) getRowFormatter();
    }

    /**
     * @return the selection policy
     */
    public SelectionPolicy getSelectionPolicy()
    {
	return selectionPolicy;
    }

    @Override
    public int insertRow(int beforeRow)
    {
	deselectAllRows();
	return super.insertRow(beforeRow);
    }

    /**
     * @param row the row index
     * @return true if the row is selected, false if not
     */
    public boolean isRowSelected(int row)
    {
	return selectedRows.containsKey(new Integer(row));
    }

    /**
     * @return true if selection is enabled
     */
    public boolean isSelectionEnabled()
    {
	return selectionEnabled;
    }

    @Override
    public void onBrowserEvent(Event event)
    {
	super.onBrowserEvent(event);
	Element targetRow= null;
	Element targetCell= null;

	switch (DOM.eventGetType(event))
	{
	// Hover the cell on mouse over
	    case Event.ONMOUSEOVER:
		Element cellElem= getEventTargetCell(event);
		if (cellElem != null)
		{
		    hoverCell(cellElem);
		}
		break;

	    // Unhover on mouse out
	    case Event.ONMOUSEOUT:
		Element toElem= DOM.eventGetToElement(event);
		if (hoveringRowElem != null && (toElem == null || !DOM.isOrHasChild(hoveringRowElem, toElem)))
		{
		    // Check that the coordinates are not directly over the cell
		    int clientX= DOM.eventGetClientX(event);
		    int clientY= DOM.eventGetClientY(event);
		    int rowLeft= DOM.getAbsoluteLeft(hoveringRowElem);
		    int rowTop= DOM.getAbsoluteTop(hoveringRowElem);
		    int rowWidth= DOM.getElementPropertyInt(hoveringRowElem, "offsetWidth");
		    int rowHeight= DOM.getElementPropertyInt(hoveringRowElem, "offsetHeight");
		    int rowBottom= rowTop + rowHeight;
		    int rowRight= rowLeft + rowWidth;
		    if (clientX > rowLeft && clientX < rowRight && clientY > rowTop && clientY < rowBottom)
		    {
			return;
		    }

		    // Unhover the current cell
		    hoverCell(null);
		}
		break;

	    // Select a row on click
	    case Event.ONMOUSEDOWN:
	    {
		// Ignore if selection is disabled
		if (!selectionEnabled)
		{
		    return;
		}

		// Get the target row
		targetCell= getEventTargetCell(event);
		if (targetCell == null)
		{
		    return;
		}
		targetRow= DOM.getParent(targetCell);
		int targetRowIndex= getRowIndex(targetRow);

		// Select the row
		if (selectionPolicy == SelectionPolicy.MULTI_ROW)
		{
		    boolean shiftKey= DOM.eventGetShiftKey(event);
		    boolean ctrlKey= DOM.eventGetCtrlKey(event) || DOM.eventGetMetaKey(event);

		    // Prevent default text selection
		    if (ctrlKey || shiftKey)
		    {
			event.preventDefault();
		    }

		    // Select the rows
		    selectRow(targetRowIndex, ctrlKey, shiftKey);
		}
		else if (selectionPolicy == SelectionPolicy.ONE_ROW || (selectionPolicy == SelectionPolicy.RADIO && targetCell == targetRow.getFirstChild()))
		{
		    selectRow(-1, targetRow, true, true);
		    lastSelectedRowIndex= targetRowIndex;
		}
	    }
		break;

	    // Prevent native inputs from being checked
	    case Event.ONCLICK:
	    {
		// Ignore if selection is disabled
		if (!selectionEnabled)
		{
		    return;
		}

		// Get the target row
		targetCell= getEventTargetCell(event);
		if (targetCell == null)
		{
		    return;
		}
		targetRow= DOM.getParent(targetCell);
		int targetRowIndex= getRowIndex(targetRow);

		// Select the row
		if (selectionPolicy == SelectionPolicy.CHECKBOX && targetCell == targetRow.getFirstChild())
		{
		    selectRow(targetRowIndex, true, DOM.eventGetShiftKey(event));
		}
	    }
	}
    }

    @Override
    public void removeRow(int row)
    {
	deselectAllRows();
	super.removeRow(row);
    }

    /**
     * Remove a {@link TableSelectionListener}.
     * 
     * @param listener the listener to remove
     */
    public void removeTableSelectionListener(TableSelectionListener listener)
    {
	if (tableSelectionListeners != null)
	{
	    tableSelectionListeners.remove(listener);
	}
    }

    /**
     * Select all rows in the table.
     */
    public void selectAllRows()
    {
	// Select all rows
	RowFormatter rowFormatter= getRowFormatter();
	int rowCount= getRowCount();
	for (int i= 0; i < rowCount; i++)
	{
	    if (!selectedRows.containsKey(i))
	    {
		selectRow(i, rowFormatter.getElement(i), false, true);
	    }
	}
    }

    /**
     * Select a row in the data table.
     * 
     * @param row the row index
     * @param unselectAll unselect all other rows first
     * @throws IndexOutOfBoundsException
     */
    public void selectRow(int row, boolean unselectAll)
    {
	selectRow(row, getRowFormatter().getElement(row), unselectAll, true);
    }

    /**
     * Select a row in the data table. Simulate the effect of a shift click and/or
     * control click. This method ignores the selection policy, which only applies
     * to user selection via mouse events.
     * 
     * @param row the row index
     * @param ctrlKey true to simulate a control click
     * @param shiftKey true to simulate a shift selection
     * @throws IndexOutOfBoundsException
     */
    public void selectRow(int row, boolean ctrlKey, boolean shiftKey)
    {
	// Check the row bounds
	checkRowBounds(row);

	// Deselect all rows
	if (!ctrlKey)
	{
	    deselectAllRows();
	}

	boolean isSelected= selectedRows.containsKey(new Integer(row));
	if (shiftKey && (lastSelectedRowIndex > -1))
	{
	    // Shift+select rows
	    SelectionGridRowFormatter formatter= getSelectionGridRowFormatter();
	    int firstRow= Math.min(row, lastSelectedRowIndex);
	    int lastRow= Math.max(row, lastSelectedRowIndex);
	    lastRow= Math.min(lastRow, getRowCount() - 1);
	    for (int curRow= firstRow; curRow <= lastRow; curRow++)
	    {
		if (isSelected)
		{
		    deselectRow(curRow);
		}
		else
		{
		    selectRow(curRow, formatter.getRawElement(curRow), false, false);
		}
	    }

	    // Fire grid listeners
	    lastSelectedRowIndex= row;
	    if (tableSelectionListeners != null)
	    {
		tableSelectionListeners.fireRowsSelected(this, firstRow, lastRow - firstRow + 1);
	    }
	}
	else if (isSelected)
	{
	    // Ctrl+unselect a selected row
	    deselectRow(row);
	    lastSelectedRowIndex= row;
	}
	else
	{
	    // Select the row
	    SelectionGridRowFormatter formatter= getSelectionGridRowFormatter();
	    selectRow(row, formatter.getRawElement(row), false, true);
	    lastSelectedRowIndex= row;
	}
    }

    /**
     * Enable or disable row selection.
     * 
     * @param enabled true to enable, false to disable
     */
    public void setSelectionEnabled(boolean enabled)
    {
	selectionEnabled= enabled;

	// Update the input elements
	if (selectionPolicy.hasInputColumn())
	{
	    SelectionGridCellFormatter formatter= getSelectionGridCellFormatter();
	    int rowCount= getRowCount();
	    for (int i= 0; i < rowCount; i++)
	    {
		Element td= formatter.getRawElement(i, -1);
		setInputEnabled(selectionPolicy, td, enabled);
	    }
	}
    }

    /**
     * Set the selection policy, which determines if the user can select zero,
     * one, or multiple rows.
     * 
     * @param selectionPolicy the selection policy
     */
    public void setSelectionPolicy(SelectionPolicy selectionPolicy)
    {
	if (this.selectionPolicy == selectionPolicy)
	{
	    return;
	}
	deselectAllRows();

	// Update the input column
	if (selectionPolicy.hasInputColumn())
	{
	    if (this.selectionPolicy.hasInputColumn())
	    {
		// Update the existing input column
		String inputHtml= getInputHtml(selectionPolicy);
		for (int i= 0; i < numRows; i++)
		{
		    Element tr= getRowFormatter().getElement(i);
		    tr.getFirstChildElement().setInnerHTML(inputHtml);
		}
	    }
	    else
	    {
		// Add an input column to every row
		String inputHtml= getInputHtml(selectionPolicy);
		Element td= createCell();
		td.setInnerHTML(inputHtml);
		for (int i= 0; i < numRows; i++)
		{
		    Element tr= getRowFormatter().getElement(i);
		    tr.insertBefore(td.cloneNode(true), tr.getFirstChildElement());
		}
	    }
	}
	else if (this.selectionPolicy.hasInputColumn())
	{
	    // Remove the input column from every row
	    for (int i= 0; i < numRows; i++)
	    {
		Element tr= getRowFormatter().getElement(i);
		tr.removeChild(tr.getFirstChildElement());
	    }
	}
	this.selectionPolicy= selectionPolicy;

	// Update the enabled state
	setSelectionEnabled(selectionEnabled);
    }

    @Override
    protected Element createRow()
    {
	Element tr= super.createRow();
	if (selectionPolicy.hasInputColumn())
	{
	    Element td= createCell();
	    td.setPropertyString("align", "center");
	    td.setInnerHTML(getInputHtml(selectionPolicy));
	    DOM.insertChild(tr, td, 0);
	    if (!selectionEnabled)
	    {
		setInputEnabled(selectionPolicy, td, false);
	    }
	}
	return tr;
    }

    @Override
    protected int getCellIndex(Element rowElem, Element cellElem)
    {
	int index= super.getCellIndex(rowElem, cellElem);
	if (selectionPolicy.hasInputColumn())
	{
	    index--;
	}
	return index;
    }

    @Override
    protected int getDOMCellCount(int row)
    {
	int count= super.getDOMCellCount(row);
	if (getSelectionPolicy().hasInputColumn())
	{
	    count--;
	}
	return count;
    }

    /**
     * Get the html used to create the native input selection element.
     * 
     * @param selectionPolicy the associated {@link SelectionPolicy}
     * @return the html representation of the input element
     */
    protected String getInputHtml(SelectionPolicy selectionPolicy)
    {
	String inputHtml= selectionPolicy.getInputHtml();
	if (inputHtml != null)
	{
	    inputHtml= inputHtml.replace("%NAME%", "__gwtSelectionGrid" + id);
	}
	return inputHtml;
    }

    /**
     * @return a map or selected row indexes to their elements
     */
    protected Map<Integer, Element> getSelectedRowsMap()
    {
	return selectedRows;
    }

    /**
     * Set the current hovering cell.
     * 
     * @param cellElem the cell element
     */
    protected void hoverCell(Element cellElem)
    {
	// Ignore if the cell is already being hovered
	if (cellElem == hoveringCellElem)
	{
	    return;
	}

	// Get the row element
	Element rowElem= null;
	if (cellElem != null)
	{
	    rowElem= DOM.getParent(cellElem);
	}

	// Unhover the current cell
	if (hoveringCellElem != null)
	{
	    setStyleName(hoveringCellElem, "hovering", false);
	    if (tableSelectionListeners != null)
	    {
		tableSelectionListeners.fireCellUnhover(this, hoveringRowIndex, hoveringCellIndex);
	    }
	    hoveringCellElem= null;
	    hoveringCellIndex= -1;

	    // Unhover the current row if it changed
	    if (rowElem != hoveringRowElem)
	    {
		setStyleName(hoveringRowElem, "hovering", false);
		if (tableSelectionListeners != null)
		{
		    tableSelectionListeners.fireRowUnhover(this, hoveringRowIndex);
		}
		hoveringRowElem= null;
		hoveringRowIndex= -1;
	    }
	}

	// Hover the cell
	if (cellElem != null)
	{
	    setStyleName(cellElem, "hovering", true);
	    hoveringCellElem= cellElem;
	    hoveringCellIndex= OverrideDOM.getCellIndex(cellElem);

	    // Hover the row if it changed
	    if (hoveringRowElem == null)
	    {
		setStyleName(rowElem, "hovering", true);
		hoveringRowElem= rowElem;
		hoveringRowIndex= getRowIndex(hoveringRowElem);
		if (tableSelectionListeners != null)
		{
		    tableSelectionListeners.fireRowHover(this, hoveringRowIndex);
		}
	    }

	    // Fire listeners
	    if (tableSelectionListeners != null)
	    {
		tableSelectionListeners.fireCellHover(this, hoveringRowIndex, hoveringCellIndex);
	    }
	}
    }

    /**
     * Select a row in the data table.
     * 
     * @param row the row index, or -1 if unknown
     * @param rowElem the row element
     * @param unselectAll true to unselect all currently selected rows
     * @param fireEvent true to fire the select event to listeners
     */
    protected void selectRow(int row, Element rowElem, boolean unselectAll, boolean fireEvent)
    {
	// Get the row index if needed
	if (row < 0)
	{
	    row= getRowIndex(rowElem);
	}

	// Ignore request if row already selected
	Integer rowI= new Integer(row);
	if (selectedRows.containsKey(rowI))
	{
	    return;
	}

	// Deselect current rows
	if (unselectAll)
	{
	    deselectAllRows();
	}

	// Select the new row
	selectedRows.put(rowI, rowElem);
	setStyleName(rowElem, "selected", true);
	if (selectionPolicy.hasInputColumn())
	{
	    setInputSelected(getSelectionPolicy(), (Element) rowElem.getFirstChildElement(), true);
	}

	// Fire grid listeners
	if (fireEvent && tableSelectionListeners != null)
	{
	    tableSelectionListeners.fireRowsSelected(this, row, 1);
	}
    }

    @Override
    protected void setBodyElement(Element element)
    {
	super.setBodyElement(element);
	if (!selectionEnabled)
	{
	    setSelectionEnabled(selectionEnabled);
	}
    }

    /**
     * Enabled or disabled the native input element in the given cell. This method
     * should correspond with the HTML returned from {@link #getInputHtml}.
     * 
     * @param selectionPolicy the associated {@link SelectionPolicy}
     * @param td the cell containing the element
     * @param enabled true to enable, false to disable
     */
    protected void setInputEnabled(SelectionPolicy selectionPolicy, Element td, boolean enabled)
    {
	((InputElement) td.getFirstChild()).setDisabled(!enabled);
    }

    /**
     * Select the native input element in the given cell. This method should
     * correspond with the HTML returned from {@link #getInputHtml}.
     * 
     * @param selectionPolicy the associated {@link SelectionPolicy}
     * @param td the cell containing the element
     * @param selected true to select, false to deselect
     */
    protected void setInputSelected(SelectionPolicy selectionPolicy, Element td, boolean selected)
    {
	((InputElement) td.getFirstChild()).setChecked(selected);
    }
}
