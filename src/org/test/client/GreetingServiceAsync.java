package org.test.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mfo.jsurfer.rendering.cpu.CPUAlgebraicSurfaceRenderer;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync
{
    void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
    void getRenderer(String equation, AsyncCallback<CPUAlgebraicSurfaceRenderer> callback);
}
