package org.test.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mfo.jsurf.rendering.cpu.JsCPUAlgebraicSurfaceRenderer;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SurferServiceAsync
{
    void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
    void getRenderer(String surfaceDescription, Map<String, String> properties, AsyncCallback<JsCPUAlgebraicSurfaceRenderer> callback);
}
