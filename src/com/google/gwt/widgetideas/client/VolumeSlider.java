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
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.widgetideas.client.Spinner.SpinnerResources;

/**
 * {@link VolumeSlider} widget combines a {@link ProgressBar} with a
 * {@link Spinner}.
 */
public class VolumeSlider extends Composite
{
    /**
     * Default {@link VolumeSlider} images.
     */
    public interface VolumeSliderButtons extends SpinnerResources
    {
	@Resource("arrowLeft.png")
	AbstractImagePrototype arrowDown();

	@Resource("arrowLeftDisabled.png")
	AbstractImagePrototype arrowDownDisabled();

	@Resource("arrowLeftHover.png")
	AbstractImagePrototype arrowDownHover();

	@Resource("arrowLeftPressed.png")
	AbstractImagePrototype arrowDownPressed();

	@Resource("arrowRight.png")
	AbstractImagePrototype arrowUp();

	@Resource("arrowRightDisabled.png")
	AbstractImagePrototype arrowUpDisabled();

	@Resource("arrowRightHover.png")
	AbstractImagePrototype arrowUpHover();

	@Resource("arrowRightPressed.png")
	AbstractImagePrototype arrowUpPressed();
    }

    private Spinner spinner;
    private ProgressBar progressBar;

    private SpinnerListener listener= new SpinnerListener()
    {
	public void onSpinning(long value)
	{
	    progressBar.setProgress(value);
	}
    };

    /**
     * @param value initial value
     * @param min min value
     * @param max max value
     */
    public VolumeSlider(long value, long min, long max)
    {
	this(value, min, max, (VolumeSliderButtons) GWT.create(VolumeSliderButtons.class));
    }

    /**
     * @param value initial value
     * @param min min value
     * @param max max value
     * @param resources the images used for the spinner
     */
    public VolumeSlider(long value, long min, long max, SpinnerResources resources)
    {
	HorizontalPanel horizontalPanel= new HorizontalPanel();
	horizontalPanel.setStyleName("gwt-VolumeSlider");
	horizontalPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	progressBar= new ProgressBar(min, max, value);
	spinner= new Spinner(listener, value, min, max, 1, 5, true, resources);
	SimplePanel leftPanel= new SimplePanel();
	leftPanel.add(spinner.getDecrementArrow());
	leftPanel.setStyleName("decreaseArrow");
	horizontalPanel.add(leftPanel);
	horizontalPanel.add(progressBar);
	progressBar.setWidth("100%");
	horizontalPanel.setCellWidth(progressBar, "100%");
	SimplePanel rightPanel= new SimplePanel();
	rightPanel.add(spinner.getIncrementArrow());
	rightPanel.setStyleName("increaseArrow");
	horizontalPanel.add(rightPanel);
	initWidget(horizontalPanel);
    }

    /**
     * @return the progress bar
     */
    public ProgressBar getProgressBar()
    {
	return progressBar;
    }

    /**
     * @return the spinner
     */
    public Spinner getSpinner()
    {
	return spinner;
    }

    /**
     * @return Gets whether this widget is enabled
     */
    public boolean isEnabled()
    {
	return spinner.isEnabled();
    }

    /**
     * Sets whether this widget is enabled.
     * 
     * @param enabled true to enable the widget, false to disable it
     */
    public void setEnabled(boolean enabled)
    {
	spinner.setEnabled(enabled);
    }
}
