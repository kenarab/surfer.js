package org.test.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mfo.jsurf.rendering.cpu.JsCPUAlgebraicSurfaceRenderer;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService
{
    String greetServer(String name) throws IllegalArgumentException;
    JsCPUAlgebraicSurfaceRenderer getRenderer(String equation);
}
