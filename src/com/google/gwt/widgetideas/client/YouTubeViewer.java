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

/**
 * A Widget used to embed a single YouTube video.
 * 
 * @deprecated as of Feb 11th, as it is not a candidate for being a gwt widget
 *             in of itself.
 */
@Deprecated
public class YouTubeViewer extends EmbeddedObject
{
    /**
     * The base URL of all YouTube videos.
     */
    public static final String BASE_URL= "http://www.youtube.com/v/";

    /**
     * Indicates whether or not autoPlay is enabled.
     */
    private boolean autoPlayEnabled= false;

    /**
     * The unique ID of the movie.
     */
    private String movieID;

    /**
     * Indicates whether or not to show related videos in the player.
     */
    private boolean relatedVideosEnabled= false;

    /**
     * Constructor.
     * 
     * @param movieID the ID of the YouTube movie to play
     */
    public YouTubeViewer(String movieID)
    {
	this(movieID, false);
    }

    /**
     * Constructor.
     * 
     * @param movieID the ID of the YouTube movie to play
     * @param autoPlayEnabled true to automatically play movies
     */
    public YouTubeViewer(String movieID, boolean autoPlayEnabled)
    {
	super();
	this.autoPlayEnabled= autoPlayEnabled;
	setInfo(new EmbeddedObjectInfo(TYPE_SHOCKWAVE, null), false);
	setMovieID(movieID);
    }

    /**
     * Get the movie ID.
     * 
     * @return the movie ID
     */
    public String getMovieID()
    {
	return movieID;
    }

    /**
     * Get the full URL of the video.
     * 
     * @return the full URL of the video.
     */
    public String getURL()
    {
	// Base movie URL
	String url= BASE_URL + this.movieID;

	// AutoPlay option
	if (autoPlayEnabled)
	{
	    url+= "&autoplay=1";
	}

	// Related videos option
	if (relatedVideosEnabled)
	{
	    url+= "&rel=1";
	}
	else
	{
	    url+= "&rel=0";
	}

	return url;
    }

    /**
     * Check whether or not autoPlay is enabled.
     * 
     * @return true if autoPlay is enabled
     */
    public boolean isAutoPlayEnabled()
    {
	return autoPlayEnabled;
    }

    /**
     * Check whether or not the player will show related videos.
     * 
     * @return true if related videos will be shown
     */
    public boolean isRelatedVideosEnabled()
    {
	return relatedVideosEnabled;
    }

    /**
     * Enable or disable autoPlay. When enabled, videos will immediately start
     * playing when you set the movieID.
     * 
     * By default, autoPlay is disabled.
     * 
     * @param autoPlayEnabled true to enable autoPlay
     */
    public void setAutoPlayEnabled(boolean autoPlayEnabled)
    {
	this.autoPlayEnabled= autoPlayEnabled;
    }

    /**
     * Set the movie ID.
     * 
     * @param movieID the movie ID
     */
    public void setMovieID(String movieID)
    {
	this.movieID= movieID;

	// Update the info object
	String url= getURL();
	EmbeddedObjectInfo info= getInfo();
	info.setSource(url);
	info.setParam("movie", url);
	render();
    }

    /**
     * Enable or disable related videos in the player. When enabled, the player
     * will display related videos after the current video completed.
     * 
     * By default, related videos are disabled
     * 
     * @param relatedVideosEnabled true to enable related videos
     */
    public void setRelatedVideosEnabled(boolean relatedVideosEnabled)
    {
	this.relatedVideosEnabled= relatedVideosEnabled;
    }
}
