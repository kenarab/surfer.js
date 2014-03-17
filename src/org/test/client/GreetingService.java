package org.test.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mfo.jsurfer.rendering.cpu.CPUAlgebraicSurfaceRenderer;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService
{
    String greetServer(String name) throws IllegalArgumentException;
    CPUAlgebraicSurfaceRenderer getRenderer(String equation);
}
