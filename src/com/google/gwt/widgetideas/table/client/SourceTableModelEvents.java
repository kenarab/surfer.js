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

/**
 * An interface for {@link TableModel} that trigger events.
 * 
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.event.client.HasRowCountChangeHandlers}
 *             ,
 *             {@link com.google.gwt.gen2.table.event.client.HasRowInsertionHandlers}
 *             ,
 *             {@link com.google.gwt.gen2.table.event.client.HasRowRemovalHandlers}
 *             ,
 *             {@link com.google.gwt.gen2.table.event.client.HasRowValueChangeHandlers}
 */
@Deprecated
public interface SourceTableModelEvents
{
    /**
     * Add a new {@link TableModelListener}.
     * 
     * @param listener the listener
     */
    void addTableModelListener(TableModelListener listener);

    /**
     * Remove a {@link TableModelListener}.
     * 
     * @param listener the listener to remove
     */
    void removeTableModelListener(TableModelListener listener);
}
