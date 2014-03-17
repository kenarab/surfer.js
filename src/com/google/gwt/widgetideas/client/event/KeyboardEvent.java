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

package com.google.gwt.widgetideas.client.event;

import com.google.gwt.user.client.Event;

/**
 * Keyboard event.
 * 
 * @param <FiresType> the ui object which fires the event
 * @param <HandlerType> the type of handler which handles the event
 */
public abstract class KeyboardEvent<FiresType, HandlerType extends EventHandler> extends BrowserEvent<FiresType, HandlerType>
{

    /**
     * Constructor.
     * 
     * @param event native event
     * @param source event source
     */
    public KeyboardEvent(Event event, FiresType source)
    {
	super(event, source);
    }

    /**
     * Gets the current key code.
     * 
     * @return the key code
     */
    public int getKeyCode()
    {
	return getBrowserEvent().getKeyCode();
    }

    /**
     * Is the key code alpha-numeric (i.e. A-z or 0-9).
     * 
     * @return is the key code alpha numeric.
     */
    public boolean isAlphaNumeric()
    {
	int keycode= getKeyCode();
	return (48 <= keycode && keycode <= 57) || (65 <= keycode && keycode <= 90);
    }

    /**
     * Is <code>alt</code> key down.
     * 
     * @return whether the alt key is down
     */
    public boolean isAltKeyDown()
    {
	return getBrowserEvent().getAltKey();
    }

    /**
     * Is <code>control</code> key down.
     * 
     * @return whether the control key is down
     */
    public boolean isControlKeyDown()
    {
	return getBrowserEvent().getCtrlKey();
    }

    /**
     * Is <code>shift</code> key down.
     * 
     * @return whether the shift key is down
     */
    public boolean isShiftKeyDown()
    {
	return getBrowserEvent().getShiftKey();
    }

}
