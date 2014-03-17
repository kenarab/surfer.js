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
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An embedded object, such as a flash application.
 */
public class EmbeddedObject extends Widget
{
    /**
     * Information about this generic object.
     */
    public class EmbeddedObjectInfo
    {
	/**
	 * The parameters attached to the object element.
	 */
	private Map/* <String, String> */params= new HashMap/* <String, String> */();

	/**
	 * The plugins page if the user doesn't have a valid plugin.
	 */
	private String embedPluginsPage= null;

	/**
	 * The source of the embedded object.
	 */
	private String embedSrc;

	/**
	 * The type of embedded element.
	 */
	private int embedType;

	/**
	 * The string representation of the type.
	 */
	private String embedTypeString;

	/**
	 * The class ID of the object.
	 */
	private String objectClassID= null;

	/**
	 * The code base of the object.
	 */
	private String objectCodeBase= null;

	/**
	 * Create a new EmbeddedObjectInfo.
	 * 
	 * The type should be a type of embedded object as described by the static
	 * TYPE_ variables in the EmbeddedObject class. If the type is not valid, an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param type the type of embedded element
	 * @param src the source of the content
	 * @throws IllegalArgumentException
	 */
	public EmbeddedObjectInfo(int type, String src) throws IllegalArgumentException
	{
	    this.embedSrc= src;
	    setType(type);
	}

	/**
	 * Get the class ID used to identify the ActiveX control.
	 * 
	 * @return the class ID
	 */
	public String getClassID()
	{
	    return objectClassID;
	}

	/**
	 * Get the code base that identifies the location of the
	 * ActiveX control.
	 * 
	 * @return the code base
	 */
	public String getCodeBase()
	{
	    return objectCodeBase;
	}

	/**
	 * Get a parameter value from the object.
	 * 
	 * @param name the parameter name
	 * @return the parameter value
	 */
	public String getParam(String name)
	{
	    Object value= params.get(name);
	    if (value == null)
	    {
		return null;
	    }
	    else
	    {
		return (String) value;
	    }
	}

	/**
	 * Get all of the parameters.
	 * 
	 * @return a map of all parameters
	 */
	public Map getParams()
	{
	    return params;
	}

	/**
	 * Get the plugins page that the user can visit to download the required
	 * pluging.
	 * 
	 * @return the plugins download page
	 */
	public String getPluginsPage()
	{
	    return embedPluginsPage;
	}

	/**
	 * Get the source.
	 * 
	 * @return the source
	 */
	public String getSource()
	{
	    return embedSrc;
	}

	/**
	 * Get the type.
	 * 
	 * @return the type
	 */
	public int getType()
	{
	    return embedType;
	}

	/**
	 * Get the string representation of the type.
	 * 
	 * @return the type represented as a string
	 */
	public String getTypeString()
	{
	    return embedTypeString;
	}

	/**
	 * Set the class ID used to identify the ActiveX control.
	 * 
	 * @param classID the class ID
	 */
	public void setClassID(String classID)
	{
	    this.objectClassID= classID;
	}

	/**
	 * Set the code base that identifies the location of the
	 * ActiveX control.
	 * 
	 * @param codeBase the code base
	 */
	public void setCodeBase(String codeBase)
	{
	    this.objectCodeBase= codeBase;
	}

	/**
	 * Set a parameter value in the object.
	 * 
	 * @param name the parameter name
	 * @param value the parameter value
	 */
	public void setParam(String name, String value)
	{
	    params.put(name, value);
	}

	/**
	 * Set the page where the user can download the necessary plugin to use the
	 * embedded application.
	 * 
	 * Set the plugings page to null to use the default pluging page.
	 * 
	 * @param pluginsPage the plugins download page
	 */
	public void setPluginsPage(String pluginsPage)
	{
	    this.embedPluginsPage= pluginsPage;
	}

	/**
	 * Set the source of the embedded object.
	 * 
	 * @param src the new source
	 */
	public void setSource(String src)
	{
	    this.embedSrc= src;
	}

	/**
	 * Set the type of embedded.
	 * 
	 * @param type the new type.
	 * @throws IllegalArgumentException
	 */
	public void setType(int type) throws IllegalArgumentException
	{
	    // Convert the type to a string
	    switch (type)
	    {
		case TYPE_SHOCKWAVE:
		    this.embedTypeString= "application/x-shockwave-flash";
		    break;
		default:
		    throw new IllegalArgumentException("type invalid: Unrecognized type");
	    }

	    // Save the new type
	    this.embedType= type;
	}
    }

    /**
     * A shockwave video type.
     */
    public static final int TYPE_SHOCKWAVE= 0;

    /**
     * The info object used to render this embedded object.
     */
    private EmbeddedObjectInfo info= null;

    /**
     * Create an empty embedded object. The object will not be rendered until the
     * {@link #setInfo(EmbeddedObjectInfo)} method is called, but the container
     * Element will be rendered to the page for display purposes.
     */
    public EmbeddedObject()
    {
	setElement(DOM.createDiv());
    }

    /**
     * Create a new embedded object based on the info.
     * 
     * @param info the information used to create the embedded object
     */
    public EmbeddedObject(EmbeddedObjectInfo info)
    {
	// <div>
	// <object width="100%" height="100%">
	// <param name="$name1" value="$value1">
	// <param name="$name2" value="$value2">
	// <param name="$name3" value="$value3">
	// <embed src="$src" type="$type" width="100%" height="100%"></embed>
	// </object>
	// </div>

	this();
	render();
    }

    /**
     * Get the EmbeddedObjectInfo used to render this object.
     * 
     * @return the embedded info
     */
    public EmbeddedObjectInfo getInfo()
    {
	return info;
    }

    /**
     * Update the information in the EmbeddedObject and re-render it.
     * 
     * @param info the new embedded info
     */
    public void setInfo(EmbeddedObjectInfo info)
    {
	setInfo(info, true);
    }

    /**
     * Render the object into the container div. Dynamically manipulating embedded
     * elements can have weird results and throw errors, so we just replace the
     * entire contents of the container with a new object.
     */
    protected void render()
    {
	// Do not render if no information is available
	if (info == null)
	{
	    return;
	}

	// Open the object
	String innerHTML= "<object width=\"100%\" height=\"100%\"";
	if (info.getClassID() != null)
	{
	    innerHTML+= " classid=\"" + info.getClassID() + "\"";
	}
	if (info.getCodeBase() != null)
	{
	    innerHTML+= " codebase=\"" + info.getCodeBase() + "\"";
	}
	innerHTML+= ">";

	// Add object parameters
	Iterator/* <Map.Entry<String, String>> */it= info.getParams().entrySet().iterator();
	while (it.hasNext())
	{
	    Map.Entry entry= (Map.Entry) it.next();
	    String name= (String) entry.getKey();
	    String value= (String) entry.getValue();
	    innerHTML+= "<param name=\"" + name + "\" value=\"" + value + "\"/>";
	}

	// Add the embed tag
	innerHTML+= "<embed height=\"100%\" width=\"100%\" " + "type=\"" + info.getTypeString() + "\"" + "src=\"" + info.getSource() + "\"";
	if (info.getPluginsPage() != null)
	{
	    innerHTML+= " pluginspage=\"" + info.getPluginsPage() + "\"";
	}
	innerHTML+= "></embed>";

	// Close the object
	innerHTML+= "</object>";
	DOM.setInnerHTML(getElement(), innerHTML);
    }

    /**
     * Set the info element and optionally re-render.
     * 
     * @param info the new embedded info
     * @param render true to rerender the object
     */
    protected void setInfo(EmbeddedObjectInfo info, boolean render)
    {
	this.info= info;
	if (render)
	{
	    render();
	}
    }
}
