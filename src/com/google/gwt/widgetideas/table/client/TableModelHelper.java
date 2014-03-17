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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.widgetideas.table.client.TableModel.ColumnSortList;
import com.google.gwt.widgetideas.table.client.TableModel.Response;

/**
 * A helper class that provides all of the inner classes used by
 * {@link TableModel}. These classes cannot be included in the
 * {@link TableModel} class itself because of a JDT error that prevents the GWT
 * RPC generator from creating implementations of remote services:
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=243820
 * 
 * This class should be removed once this bug is fixed.
 * 
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.client.TableModelHelper}
 */
@Deprecated
public abstract class TableModelHelper implements SourceTableModelEvents
{
    /**
     * A {@link TableModelHelper} request.
     */
    public static class Request implements IsSerializable
    {
	/**
	 * The number of rows to request.
	 */
	private int numRows;

	/**
	 * An ordered list of ColumnSortInfo.
	 */
	private ColumnSortList columnSortList;

	/**
	 * The first row of table data to request.
	 */
	private int startRow;

	/**
	 * Default constructor.
	 */
	public Request()
	{
	    this(0, 0, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param startRow the first row to request
	 * @param numRows the number of rows to request
	 */
	public Request(int startRow, int numRows)
	{
	    this(startRow, numRows, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param startRow the first row to request
	 * @param numRows the number of rows to request
	 * @param columnSortList column sort orders
	 */
	public Request(int startRow, int numRows, ColumnSortList columnSortList)
	{
	    this.startRow= startRow;
	    this.numRows= numRows;
	    this.columnSortList= columnSortList;
	}

	/**
	 * Get the ColumnSortInfo, which includes the sort indexes and ascending or
	 * descending order info.
	 * 
	 * @return the sort info
	 */
	public ColumnSortList getColumnSortList()
	{
	    return columnSortList;
	}

	/**
	 * Get the number of rows to request.
	 * 
	 * @return the number of requested rows
	 */
	public int getNumRows()
	{
	    return numRows;
	}

	/**
	 * Get the first row to request.
	 * 
	 * @return the first requested row
	 */
	public int getStartRow()
	{
	    return startRow;
	}
    }

    /**
     * A response from the {@link TableModelHelper} that is serializable, and can
     * by used over RPC.
     * 
     * @param <R> the serializable data type of the row values
     */
    public static class SerializableResponse<R extends Serializable> extends Response<R> implements IsSerializable
    {
	/**
	 * The values associated with each row.
	 */
	private List<R> rowValues;

	/**
	 * The 2D {@link Collection} of serializable cell data.
	 */
	private Collection<Collection<Serializable>> rows;

	/**
	 * Default constructor.
	 */
	public SerializableResponse()
	{
	    this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param rows the row data
	 */
	public SerializableResponse(Collection<Collection<Serializable>> rows)
	{
	    this(rows, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param rows the row data
	 * @param rowValues the values to associate with each row
	 */
	public SerializableResponse(Collection<Collection<Serializable>> rows, List<R> rowValues)
	{
	    super();
	    this.rowValues= rowValues;
	    this.rows= rows;
	}

	@Override
	public Iterator<Iterator<Object>> getIterator()
	{
	    if (rows == null)
	    {
		return null;
	    }
	    return new SerializableResponseIterator(rows);
	}

	@Override
	public List<R> getRowValues()
	{
	    return rowValues;
	}
    }

    /**
     * Converts an iterator of some type into an iterator of objects.
     * 
     * @param <E> the type of iterator to wraps
     */
    private static class ObjectIterator<E> implements Iterator<Object>
    {
	private Iterator<E> iterator;

	public ObjectIterator(Iterator<E> iterator)
	{
	    this.iterator= iterator;
	}

	public boolean hasNext()
	{
	    return iterator.hasNext();
	}

	public Object next()
	{
	    return iterator.next();
	}

	public void remove()
	{
	    throw (new UnsupportedOperationException());
	}
    }

    /**
     * An {@link Iterator} over a 2D {@link Collection} of column data.
     */
    private static class SerializableResponseIterator implements Iterator<Iterator<Object>>
    {
	/**
	 * The {@link Iterator} of row data. Each row is a {@link Collection} of
	 * column data.
	 */
	private Iterator<Collection<Serializable>> rows;

	/**
	 * Default constructor.
	 */
	public SerializableResponseIterator()
	{
	    this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param rows the row data
	 */
	public SerializableResponseIterator(Collection<Collection<Serializable>> rows)
	{
	    if (rows != null)
	    {
		this.rows= rows.iterator();
	    }
	}

	public boolean hasNext()
	{
	    if (rows == null)
	    {
		return false;
	    }
	    return rows.hasNext();
	}

	public Iterator<Object> next()
	{
	    if (!hasNext())
	    {
		throw (new NoSuchElementException());
	    }
	    Collection<Serializable> next= rows.next();
	    if (next == null)
	    {
		return null;
	    }
	    else
	    {
		return new ObjectIterator<Serializable>(next.iterator());
	    }
	}

	public void remove()
	{
	    throw new UnsupportedOperationException();
	}
    }
}
