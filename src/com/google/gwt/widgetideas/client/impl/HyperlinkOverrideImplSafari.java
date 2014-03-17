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

package com.google.gwt.widgetideas.client.impl;

import com.google.gwt.user.client.Event;

/**
 * Safari version of HyperlinkOverrideImpl. Safari has special behavior for
 * all the modifier keys except shift, which behaves like a regular click. 
 */
public class HyperlinkOverrideImplSafari extends HyperlinkOverrideImpl
{
    @Override
    public boolean handleAsClick(Event event)
    {
	int mouseButtons= event.getButton();
	boolean alt= event.getAltKey();
	boolean ctrl= event.getCtrlKey();
	boolean meta= event.getMetaKey();
	boolean modifiers= alt || ctrl || meta;
	boolean middle= mouseButtons == Event.BUTTON_MIDDLE;
	boolean right= mouseButtons == Event.BUTTON_RIGHT;

	return !modifiers && !middle && !right;
    }
}
