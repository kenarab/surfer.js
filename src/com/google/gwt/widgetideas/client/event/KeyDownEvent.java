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
 * Fires {@link KeyDownEvent}.
 */
public class KeyDownEvent extends KeyboardEvent<FiresKeyDownEvents, KeyDownHandler>
{

    /**
     * Constructor.
     * 
     * @param source source
     */
    public KeyDownEvent(Event event, FiresKeyDownEvents source)
    {
	super(event, source);
    }

    @Override
    public void fire(KeyDownHandler e)
    {
	e.onKeyDown(this);
    }

}
