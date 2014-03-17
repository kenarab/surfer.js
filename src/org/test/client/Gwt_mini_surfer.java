package org.test.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.mfo.jsurfer.CanvasImageGenerator;
import de.mfo.jsurfer.GwtSurferExperiment;
import de.mfo.jsurfer.rendering.cpu.CPUAlgebraicSurfaceRenderer;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Gwt_mini_surfer implements EntryPoint
{
    protected GwtSurferExperiment main;
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR= "An error occurred while " + "attempting to contact the server. Please check your network " + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Greeting service.
     */
    private final GreetingServiceAsync greetingService= GWT.create(GreetingService.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
	final ListBox surfaceListbox= new ListBox(false);
	surfaceListbox.addItem("Torus");
	surfaceListbox.addItem("Zitrus");
	surfaceListbox.addItem("KummerCuartic");
	surfaceListbox.addItem("BarthSextic");
	surfaceListbox.addItem("ChmutovOctic");

	final Button sendButton= new Button("Render");
	final TextBox framesField= new TextBox();
	final Button stopButton= new Button("Stop/Report");
	final TextBox sizeField= new TextBox();
	final Button profileButton= new Button("Profile");
	final TextBox casesField= new TextBox();
	sizeField.setText("100");
	framesField.setText("10");
	casesField.setText("5");
	final Label errorLabel= new Label();
	final TextBox equationField= new TextBox();
	equationField.setSize("500px", "30");

	// We can add style names to widgets
	sendButton.addStyleName("sendButton");

	// Add the nameField and sendButton to the RootPanel
	// Use RootPanel.get() to get the entire body element
	RootPanel.get("sizeFieldContainer").add(sizeField);
	RootPanel.get("framesFieldContainer").add(framesField);
	RootPanel.get("casesFieldContainer").add(casesField);
	RootPanel.get("sendButtonContainer").add(sendButton);
	RootPanel.get("profileButtonContainer").add(profileButton);
	RootPanel.get("stopButtonContainer").add(stopButton);
	RootPanel.get("errorLabelContainer").add(errorLabel);
	RootPanel.get("surfacesContainer").add(surfaceListbox);
	RootPanel.get("equationFieldContainer").add(equationField);

	// Focus the cursor on the name field when the app loads
	sizeField.setFocus(true);
	sizeField.selectAll();

	// Create the popup dialog box
	final DialogBox dialogBox= new DialogBox();
	dialogBox.setText("Remote Procedure Call");
	dialogBox.setAnimationEnabled(true);
	final Button closeButton= new Button("Close");
	// We can set the id of a widget by accessing its Element
	closeButton.getElement().setId("closeButton");
	final Label textToServerLabel= new Label();
	final HTML serverResponseLabel= new HTML();
	VerticalPanel dialogVPanel= new VerticalPanel();
	dialogVPanel.addStyleName("dialogVPanel");
	dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
	dialogVPanel.add(textToServerLabel);
	dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
	dialogVPanel.add(serverResponseLabel);
	dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
	dialogVPanel.add(closeButton);
	dialogBox.setWidget(dialogVPanel);

	// Add a handler to close the DialogBox
	closeButton.addClickHandler(new ClickHandler()
	{
	    public void onClick(ClickEvent event)
	    {
		dialogBox.hide();
		sendButton.setEnabled(true);
		sendButton.setFocus(true);
	    }
	});

	// Create a handler for the sendButton and nameField
	class MyHandler implements ClickHandler, KeyUpHandler
	{

	    /**
	     * Fired when the user clicks on the sendButton.
	     */
	    public void onClick(ClickEvent event)
	    {
		sendNameToServer();
	    }

	    /**
	     * Fired when the user types in the nameField.
	     */
	    public void onKeyUp(KeyUpEvent event)
	    {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		{
		    sendNameToServer();
		}
	    }

	    /**
	     * Send the name from the nameField to the server and wait for a response.
	     */
	    private void sendNameToServer()
	    {
		greetingService.getRenderer(equationField.getText(), new AsyncCallback<CPUAlgebraicSurfaceRenderer>()
		{
		    public void onSuccess(CPUAlgebraicSurfaceRenderer result)
		    {
			System.out.println(result.getCamera().getCameraType());
			main= new GwtSurferExperiment(result);
			main.setImageGenerator(new CanvasImageGenerator());
			main.doStandalone(new String[] { "animation", sizeField.getText(), framesField.getText(), surfaceListbox.getItemText(surfaceListbox.getSelectedIndex()) });
		    }
		    
		    public void onFailure(Throwable caught)
		    {
		    }
		});
	    }
	}

	// Add a handler to send the name to the server
	MyHandler handler= new MyHandler();
	sendButton.addClickHandler(handler);
	stopButton.addClickHandler(new ClickHandler()
	{
	    public void onClick(ClickEvent event)
	    {
		main.stop();
	    }
	});

	profileButton.addClickHandler(new ClickHandler()
	{
	    public void onClick(ClickEvent event)
	    {
		main= new GwtSurferExperiment();
		main.setImageGenerator(new CanvasImageGenerator());
		main.doProfile(new String[] { "profile", sizeField.getText(), framesField.getText(), surfaceListbox.getItemText(surfaceListbox.getSelectedIndex()), casesField.getText() });
	    }
	});

	sizeField.addKeyUpHandler(handler);
    }
}
