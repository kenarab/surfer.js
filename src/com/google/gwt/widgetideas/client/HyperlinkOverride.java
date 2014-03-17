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

package com.google.gwt.widgetideas.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.widgetideas.client.impl.HyperlinkOverrideImpl;

/**
 * Version of Hyperlink that tries to do the right thing for modifier keys,
 * allowing users to open in new tabs. This is necessary because the current
 * Hyperlink widget fires on any and all click events, irrespective which button
 * was pressed or what modifiers were held down. This functionality will, if
 * accepted, eventually be collapsed back into GWT's Hyperlink widget.
 */
public class HyperlinkOverride extends Hyperlink
{
    private static HyperlinkOverrideImpl impl= GWT.create(HyperlinkOverrideImpl.class);
    private ClickListenerCollection clickListeners;

    public HyperlinkOverride()
    {
	super();
    }

    public HyperlinkOverride(String text, boolean asHTML, String targetHistoryToken)
    {
	super(text, asHTML, targetHistoryToken);
    }

    public HyperlinkOverride(String text, String targetHistoryToken)
    {
	super(text, targetHistoryToken);
    }

    @Override
    public void addClickListener(ClickListener listener)
    {
	if (clickListeners == null)
	{
	    clickListeners= new ClickListenerCollection();
	}
	clickListeners.add(listener);
    }

    @Override
    public void onBrowserEvent(Event event)
    {
	if (DOM.eventGetType(event) == Event.ONCLICK)
	{
	    if (clickListeners != null)
	    {
		clickListeners.fireClick(this);
	    }
	}

	if (impl.handleAsClick(event))
	{
	    History.newItem(getTargetHistoryToken());
	    DOM.eventPreventDefault(event);
	}
    }

    @Override
    public void removeClickListener(ClickListener listener)
    {
	if (clickListeners != null)
	{
	    clickListeners.remove(listener);
	}
    }
}
