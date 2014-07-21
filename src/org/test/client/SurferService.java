package org.test.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mfo.jsurf.rendering.cpu.JsCPUAlgebraicSurfaceRenderer;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("surfer-service")
public interface SurferService extends RemoteService
{
    String greetServer(String name) throws IllegalArgumentException;
    JsCPUAlgebraicSurfaceRenderer getRenderer(String surfaceDescription, Map<String, String> properties);
}
