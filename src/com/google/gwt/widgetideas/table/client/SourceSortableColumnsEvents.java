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
 * An interface for tables that can do paging.
 * 
 * @deprecated Replaced by
 *             {@link com.google.gwt.gen2.table.event.client.HasColumnSortHandlers}
 */
@Deprecated
public interface SourceSortableColumnsEvents
{
    /**
     * Add a {@link SortableColumnsListener} listener.
     * 
     * @param listener the listener
     */
    void addSortableColumnsListener(SortableColumnsListener listener);

    /**
     * Remove a {@link SortableColumnsListener}bleColumnsListener listener.
     * 
     * @param listener the listener to remove
     */
    void removeSortableColumnsListener(SortableColumnsListener listener);
}
