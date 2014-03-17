package org.test.server;

import org.test.client.GreetingService;
import org.test.shared.FieldVerifier;

import clonefactories.TorusFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mfo.jsurfer.rendering.cpu.CPUAlgebraicSurfaceRenderer;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService
{

    public String greetServer(String input) throws IllegalArgumentException
    {
	// Verify that the input is valid. 
	if (!FieldVerifier.isValidName(input))
	{
	    // If the input is not valid, throw an IllegalArgumentException back to
	    // the client.
	    throw new IllegalArgumentException("Name must be at least 4 characters long");
	}

	String serverInfo= getServletContext().getServerInfo();
	String userAgent= getThreadLocalRequest().getHeader("User-Agent");

	// Escape data from the client to avoid cross-site script vulnerabilities.
	input= escapeHtml(input);
	userAgent= escapeHtml(userAgent);

	return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent;
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     * 
     * @param html the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html)
    {
	if (html == null)
	{
	    return null;
	}
	return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    public CPUAlgebraicSurfaceRenderer getRenderer(String equation)
    {
	try
	{
	    if (equation == null || equation.trim().length() == 0)
		equation= "(x^2+y^2+z^2+a^2-b^2)^2-4*b^2*(x^2+y^2)";
	    CPUAlgebraicSurfaceRenderer renderer= (CPUAlgebraicSurfaceRenderer) new TorusFactory().createClone();
	    renderer.setSurfaceFamily(equation);
	    return renderer;
	}
	catch (Exception e)
	{
	    throw new RuntimeException(e);
	}
    }
}
