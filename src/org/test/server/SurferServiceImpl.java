
package org.test.server;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;

import org.apache.tools.ant.filters.StringInputStream;
import org.test.client.SurferService;
import org.test.shared.FieldVerifier;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mfo.jsurf.rendering.AlgebraicSurfaceRenderer;
import de.mfo.jsurf.rendering.JsAlgebraicSurfaceRenderer;
import de.mfo.jsurf.rendering.LightSource;
import de.mfo.jsurf.rendering.cpu.JsCPUAlgebraicSurfaceRenderer;
import de.mfo.jsurf.util.BasicIO;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SurferServiceImpl extends RemoteServiceServlet implements SurferService
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

	String serverInfo = getServletContext().getServerInfo();
	String userAgent = getThreadLocalRequest().getHeader("User-Agent");

	// Escape data from the client to avoid cross-site script vulnerabilities.
	input = escapeHtml(input);
	userAgent = escapeHtml(userAgent);

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

    public JsCPUAlgebraicSurfaceRenderer getRenderer(String surfaceDescription, Map<String, String> overrideProperties)
    {
	try
	{
	    Properties properties = new Properties();
	    properties.load(new StringInputStream(surfaceDescription));

	    JsCPUAlgebraicSurfaceRenderer renderer = new JsCPUAlgebraicSurfaceRenderer();
	    for (Entry<String, String> entry : overrideProperties.entrySet())
		if (entry.getValue() != null && !entry.getValue().trim().isEmpty())
		    properties.put(entry.getKey(), entry.getValue());

	    load(properties, renderer);

	    return renderer;
	}
	catch (Exception e)
	{
	    throw new RuntimeException(e);
	}
    }

    public static void load(Properties jsurf, JsAlgebraicSurfaceRenderer asr) throws Exception
    {
	asr.setSurfaceFamily(jsurf.getProperty("surface_equation"));

	Set<Map.Entry<Object, Object>> entries = jsurf.entrySet();
	String parameter_key_prefix = "surface_parameter_";
	for (Map.Entry<Object, Object> entry : entries)
	{
	    String name = (String) entry.getKey();
	    if (name.startsWith(parameter_key_prefix))
	    {
		String parameterName = name.substring(parameter_key_prefix.length());
		asr.setParameterValue(parameterName, Float.parseFloat((String) entry.getValue()));
	    }
	}

	asr.getCamera().loadProperties(jsurf, "camera_", "");
	asr.getFrontMaterial().loadProperties(jsurf, "front_material_", "");
	asr.getBackMaterial().loadProperties(jsurf, "back_material_", "");
	for (int i = 0; i < AlgebraicSurfaceRenderer.MAX_LIGHTS; i++)
	{
	    asr.getLightSource(i).setStatus(LightSource.Status.OFF);
	    asr.getLightSource(i).loadProperties(jsurf, "light_", "_" + i);
	}
	asr.setBackgroundColor(BasicIO.fromColor3fString(jsurf.getProperty("background_color")));

	Matrix4d transform;
	try
	{
	    transform = BasicIO.fromMatrix4dString(jsurf.getProperty("transform_matrix"));
	}
	catch (Exception e)
	{
	    // if the property isn't there initialize with identity matrix
	    transform = new Matrix4d();
	    transform.setIdentity();
	}
	try
	{
	    Matrix4d rotation_matrix = BasicIO.fromMatrix4dString(jsurf.getProperty("rotation_matrix"));
	    transform.mul(rotation_matrix);
	}
	catch (Exception e)
	{
	}
	asr.setTransform(transform);

	Matrix4d surface_transform;
	try
	{
	    surface_transform = BasicIO.fromMatrix4dString(jsurf.getProperty("surface_transform_matrix"));
	}
	catch (Exception e)
	{
	    // if the property isn't there initialize with identity matrix
	    surface_transform = new Matrix4d();
	    surface_transform.setIdentity();
	}
	try
	{
	    Matrix4d scale_matrix = new Matrix4d();
	    scale_matrix.setIdentity();
	    scale_matrix.setScale(Math.pow(10, Double.parseDouble(jsurf.getProperty("scale_factor"))));
	    surface_transform.mul(scale_matrix);
	}
	catch (Exception e)
	{
	}

	asr.setSurfaceTransform(surface_transform);
    }
}
