/*
 * Copyright 2007 Google Inc.
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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@link PinnedPanel} creates a panel that is, by default, pinned in place.
 * When the pinned state it toggled, the contents of the panel will display only
 * when the users mouse hovers over it, otherwise is will collapse to the left.
 * A {@link ChangeEvent} is fired whenever the {@link PinnedPanel} changes it
 * pinned state.
 * <p>
 * The default style name is gwt-PinnedPanel.
 * <p>
 * Planned enhancements: Allow panel to be collapsed in arbitrary direction.
 */

/**
 * {@link PinnedPanel} creates a panel that is, by default, pinned in place.
 * When the pinned state it toggled, the contents of the panel will display only
 * when the users mouse hovers over it, otherwise is will collapse to the left.
 * A {@link ChangeEvent} is fired whenever the {@link PinnedPanel} changes it
 * pinned state.
 * <p>
 * The default style name is gwt-PinnedPanel.
 * <p>
 * Planned enhancements: Allow panel to be collapsed in arbitrary direction.
 * 
 * @deprecated use {@link CollapsiblePanel} instead.
 */
@Deprecated
public class PinnedPanel extends Composite implements SourcesChangeEvents
{
    /**
     * Hides the {@link PinnedPanel}.
     */
    class HidingTimer extends SlidingTimer
    {
	protected void finish()
	{
	    state.currentState= State.IS_HIDDEN;
	}

	protected boolean processSizeChange(float shouldBe)
	{
	    currentOffshift= (int) (maxOffshift * ((float) 1.0 - shouldBe)) - MIN_SLIDE_STEP;
	    currentOffshift= Math.max(currentOffshift, 0);
	    impl.setPanelPos(currentOffshift);
	    return currentOffshift > 0;
	}
    }

    /**
     * Shows the hovering {@link PinnedPanel}.
     */
    class ShowingTimer extends SlidingTimer
    {
	protected void finish()
	{
	    state.currentState= State.IS_SHOWN;
	}

	protected boolean processSizeChange(float shouldBe)
	{
	    currentOffshift= (int) (maxOffshift * shouldBe) + MIN_SLIDE_STEP;
	    currentOffshift= Math.min(currentOffshift, maxOffshift);
	    impl.setPanelPos(currentOffshift);
	    return currentOffshift < maxOffshift;
	}
    }

    /**
     * Common code for {@link HidingTimer} and {@link ShowingTimer}.
     */
    abstract class SlidingTimer extends Timer
    {

	long started;

	boolean fireChangeListeners= false;

	public void run()
	{
	    float hasTaken= System.currentTimeMillis() - started;
	    float shouldBe= hasTaken / TIME_TO_SLIDE;

	    if (processSizeChange(shouldBe))
	    {
		this.schedule(OVERLAY_SPEED);
	    }
	    else
	    {
		if (fireChangeListeners)
		{
		    changeListeners.fireChange(PinnedPanel.this);
		    fireChangeListeners= false;
		}
		finish();
	    }
	}

	protected abstract void finish();

	protected abstract boolean processSizeChange(float shouldBe);
    }

    /**
     * Delays showing of the {@link PinnedPanel}.
     */
    private class DelayShow extends Timer
    {

	public void activate()
	{
	    state.currentState= State.WILL_SHOW;
	    delayedShow.schedule(DELAY_MILLI);
	}

	public void run()
	{
	    show();
	}
    }

    /**
     * Delays showing of the {@link PinnedPanel}.
     */
    private class DelayHide extends Timer
    {

	public void activate()
	{
	    state.currentState= State.WILL_HIDE;
	    delayedHide.schedule(DELAY_MILLI);
	}

	public void run()
	{
	    hide();
	}
    }
    /**
     * Pinned panel implementation. Factored out of {@link PinnedPanel} to allow
     * eventual use of differed bindings.
     */
    private static class PinnedPanelImpl
    {
	protected PinnedPanel pin;
	protected Element e;
	protected Element mover;

	public void setPinnedPanel(PinnedPanel pinnedPanel)
	{
	    pin= pinnedPanel;
	    e= pinnedPanel.getElement();
	    mover= pinnedPanel.mover.getElement();
	}

	/**
	 * Go into overlay mode.
	 */
	protected void becomeOverlay()
	{
	    int hoverBarWidth= pin.hoverContainer.getOffsetWidth();
	    int aboutHalf= (hoverBarWidth / 2) + 1;
	    int newWidth= pin.width + aboutHalf;
	    pin.maxOffshift= newWidth;
	    pin.currentOffshift= pin.width;

	    // Width is now hoverBarWidth.
	    pin.setWidth(hoverBarWidth + "px");

	    // Looks better to immediately hide (Says the programmer).
	    setPanelPos(0);
	    pin.currentOffshift= 0;

	    // clean up state.
	    pin.hide();
	}

	/**
	 * Go into pinned in place model
	 */
	protected void becomePinned()
	{
	    // The master width needs to be readjusted back to it's original size.
	    if (pin.isAttached())
	    {
		pin.refreshWidth();
	    }
	    DOM.setStyleAttribute(mover, "left", "0px");
	    pin.state.currentState= State.PINNED;
	}

	protected void setPanelPos(int pos)
	{
	    DOM.setStyleAttribute(mover, "left", pos - pin.width + "px");
	}
    }

    /**
     * Current {@link PinnedPanel} state. TODO(ECC) change to enumeration once 1.5
     * is released.
     */
    private static class State
    {
	static int WILL_HIDE= 1;
	static int HIDING= 2;
	static int IS_HIDDEN= 3;
	static int WILL_SHOW= -1;
	static int SHOWING= -2;
	static int IS_SHOWN= -3;
	static int PINNED= -1000;
	int currentState;

	public boolean shouldHide()
	{
	    return (currentState < 0);
	}

	public boolean shouldShow()
	{
	    return currentState > 0;
	}
    }

    /**
     * Pause between each interval.
     */
    private static int OVERLAY_SPEED= 2;

    /**
     * Number of intervals used to display panel.
     */
    private static float TIME_TO_SLIDE= 200;

    /**
     * Minimum increment change per slide.
     */
    private static int MIN_SLIDE_STEP= 20;

    /**
     * How many milliseconds to delay a hover event before executing it.
     */
    private static final int DELAY_MILLI= 400;

    /**
     * Default style name.
     */
    private static final String DEFAULT_STYLENAME= "gwt-PinnedPanel";
    private ShowingTimer overlayTimer= new ShowingTimer();
    private HidingTimer hidingTimer= new HidingTimer();
    private DelayShow delayedShow= new DelayShow();
    private DelayHide delayedHide= new DelayHide();
    private State state= new State();
    private PinnedPanelImpl impl= new PinnedPanelImpl();
    private int width;
    private int maxOffshift;
    private int currentOffshift;
    private Panel mover;
    private SimplePanel hoverContainer;
    private ToggleButton pinnedToggle;
    private AbsolutePanel master;
    private ChangeListenerCollection changeListeners= new ChangeListenerCollection();

    /**
     * Constructor.
     * 
     * @param width width of panel
     * @param pinnedToggle toggle for pinned state
     * @param contents contents to be displayed
     */
    public PinnedPanel(int width, final ToggleButton pinnedToggle, Widget contents)
    {
	this(width, pinnedToggle, contents, new HTML());
    }

    /**
     * Constructor.
     * 
     * @param width width of panel
     * @param pinnedToggle toggle for pinned state
     * @param contents contents to be displayed
     * @param hoverBar hover bar
     */
    public PinnedPanel(int width, final ToggleButton pinnedToggle, Widget contents, Widget hoverBar)
    {

	// Create the composite widget.
	master= new AbsolutePanel()
	{
	    {
		sinkEvents(Event.ONMOUSEOUT | Event.ONMOUSEOVER);
	    }

	    public void onBrowserEvent(Event event)
	    {
		if (!PinnedPanel.this.pinnedToggle.isDown())
		{
		    switch (DOM.eventGetType(event))
		    {
			case Event.ONMOUSEOUT:
			    Element to= DOM.eventGetToElement(event);
			    if (state.currentState == State.WILL_SHOW)
			    {
				state.currentState= State.HIDING;
				delayedShow.cancel();
				break;
			    }

			    if (to == null || (!DOM.isOrHasChild(this.getElement(), to)))
			    {
				delayedHide.activate();
				break;
			    }
			    break;
			case Event.ONMOUSEOVER:
			    if ((state.shouldShow()))
			    {
				if (state.currentState == State.WILL_HIDE)
				{
				    delayedHide.cancel();
				    state.currentState= State.SHOWING;
				}
				else
				{

				    delayedShow.activate();
				}
			    }
			    break;
		    }
		}

		super.onBrowserEvent(event);
	    }
	};

	DOM.setStyleAttribute(master.getElement(), "overflow", "visible");
	initWidget(master);
	setStyleName(DEFAULT_STYLENAME);
	master.setWidth(width + "px");

	// Create hovering container.
	hoverContainer= new SimplePanel();
	hoverContainer.setWidget(hoverBar);
	hoverContainer.setStyleName("hover-bar");
	master.add(hoverContainer, 0, 0);

	// Create the contents container.
	mover= new SimplePanel();
	mover.add(contents);
	contents.addStyleName("content");
	mover.setStyleName("mover");

	mover.setWidth(width + "px");
	master.add(mover, 0, 0);

	// Process the toggle.
	this.pinnedToggle= pinnedToggle;
	pinnedToggle.addStyleName("toggle");
	pinnedToggle.setDown(true);
	pinnedToggle.addClickListener(new ClickListener()
	{

	    public void onClick(Widget sender)
	    {
		setPinned(PinnedPanel.this.pinnedToggle.isDown());
	    }
	});

	impl.setPinnedPanel(this);
	state.currentState= State.PINNED;
    }

    public void addChangeListener(ChangeListener listener)
    {
	changeListeners.add(listener);
    }

    public ToggleButton getSwitchButton()
    {
	return pinnedToggle;
    }

    /**
     * Is the panel pinned?
     */
    public boolean isPinned()
    {
	return state.currentState == State.PINNED;
    }

    public void removeChangeListener(ChangeListener listener)
    {
	changeListeners.remove(listener);
    }

    protected void hide()
    {
	state.currentState= State.HIDING;
	overlayTimer.cancel();
	hidingTimer.cancel();
	hidingTimer.started= System.currentTimeMillis();
	hidingTimer.run();
    }

    /**
     * This method is called immediately after a widget becomes attached to the
     * browser's document.
     */
    protected void onLoad()
    {
	refreshWidth();
    }

    protected void show()
    {
	state.currentState= State.SHOWING;

	overlayTimer.cancel();
	hidingTimer.cancel();
	overlayTimer.started= System.currentTimeMillis();
	overlayTimer.run();
    }

    private void refreshWidth()
    {
	// Now include borders into master.
	width= mover.getOffsetWidth();
	master.setWidth(width + "px");
    }

    private void setPinned(boolean pinned)
    {
	if (isPinned() == pinned)
	{
	    return;
	}
	pinnedToggle.setDown(pinned);
	if (pinned)
	{
	    impl.becomePinned();
	}
	else
	{
	    impl.becomeOverlay();
	}
	changeListeners.fireChange(this);
    }
}
