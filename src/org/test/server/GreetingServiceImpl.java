
package org.test.server;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.vecmath.Matrix4d;

import org.apache.tools.ant.filters.StringInputStream;
import org.test.client.GreetingService;
import org.test.shared.FieldVerifier;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mfo.jsurf.rendering.AlgebraicSurfaceRenderer;
import de.mfo.jsurf.rendering.JsAlgebraicSurfaceRenderer;
import de.mfo.jsurf.rendering.LightSource;
import de.mfo.jsurf.rendering.cpu.JsCPUAlgebraicSurfaceRenderer;
import de.mfo.jsurf.util.BasicIO;
import de.mfo.jsurf.util.FileFormat;

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

    private static String configStringTorus = "#jSurfer surface description\n#Fri Jul 08 16:58:33 CEST 2011\nfront_material_specular_iIntensity=0.5\ncamera_type=ORTHOGRAPHIC_CAMERA\nback_material_specular_iIntensity=0.5\nsurface_parameter_b=0.5\n#0.41559362411499023\nsurface_parameter_a=0.5\n#0.41736117005348206\nlight_position_7=0.0 0.0 0.0\nlight_position_6=0.0 0.0 0.0\nlight_position_5=0.0 0.0 0.0\nlight_position_4=0.0 0.0 0.0\nlight_position_3=0.0 0.0 0.0\nlight_position_2=0.0 -100.0 100.0\ncamera_fov_y=60.0\nlight_position_1=100.0 100.0 100.0\nlight_position_0=-100.0 100.0 100.0\nfront_material_color=0.9411765 0.25882354 0.05882353\nbackground_color=1.0 1.0 1.0\nlight_intensity_7=1.0\nfront_material_ambient_intensity=0.4\nlight_intensity_6=1.0\nscale_factor=0.11388256\nlight_intensity_5=1.0\nlight_intensity_4=1.0\nlight_intensity_3=1.0\nlight_intensity_2=0.3\nlight_intensity_1=0.7\nlight_intensity_0=0.5\nrotation_matrix=-0.9975637 0.06831278 -0.014604302 0.0 -0.050649248 -0.56332713 0.82468677 0.0 0.048110377 0.82340914 0.5654113 0.0 0.0 0.0 0.0 1.0\nlight_color_7=1.0 1.0 1.0\nlight_color_6=1.0 1.0 1.0\nfront_material_diffuse_intensity=0.8\nlight_color_5=1.0 1.0 1.0\nlight_color_4=1.0 1.0 1.0\nlight_color_3=1.0 1.0 1.0\nlight_color_2=1.0 1.0 1.0\nlight_color_1=1.0 1.0 1.0\nlight_color_0=1.0 1.0 1.0\nlight_status_7=OFF\nlight_status_6=OFF\nlight_status_5=OFF\nlight_status_4=OFF\nfront_material_shininess=30.0\nlight_status_3=OFF\nlight_status_2=ON\nlight_status_1=ON\nlight_status_0=ON\ncamera_height=2.0\nsurface_equation=(x^2+y^2+z^2+a^2-b^2)^2-4*b^2*(x^2+y^2)\ncamera_transform=1.0 0.0 0.0 -0.0 0.0 1.0 0.0 -0.0 0.0 0.0 1.0 -1.0 0.0 0.0 0.0 1.0\nback_material_ambient_intensity=0.4\nback_material_color=0.87058824 0.41960785 0.101960786\nback_material_shininess=30.0\nback_material_diffuse_intensity=0.8";

    public JsCPUAlgebraicSurfaceRenderer getRenderer(String equation)
    {
	try
	{
	    Properties properties = new Properties();
	    properties.load(new StringInputStream(configStringTorus));

	    if (equation == null || equation.trim().length() == 0)
		equation = "(x^2+y^2+z^2+a^2-b^2)^2-4*b^2*(x^2+y^2)";

	    JsCPUAlgebraicSurfaceRenderer renderer = new JsCPUAlgebraicSurfaceRenderer();
	    load(properties, renderer);
	    renderer.setSurfaceFamily(equation);
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
