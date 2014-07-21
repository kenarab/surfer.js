
package org.test.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mfo.jsurf.CanvasImageGenerator;
import de.mfo.jsurf.GwtSurfer;
import de.mfo.jsurf.rendering.cpu.JsCPUAlgebraicSurfaceRenderer;

public class ApiExporter
{
    private static final SurferServiceAsync surferService = GWT.create(SurferService.class);
    public static String surfaceDescription = "#jSurfer surface description\n#Fri Jul 08 16:58:33 CEST 2011\nfront_material_specular_iIntensity=0.5\ncamera_type=ORTHOGRAPHIC_CAMERA\nback_material_specular_iIntensity=0.5\nsurface_parameter_b=0.5\n#0.41559362411499023\nsurface_parameter_a=0.5\n#0.41736117005348206\nlight_position_7=0.0 0.0 0.0\nlight_position_6=0.0 0.0 0.0\nlight_position_5=0.0 0.0 0.0\nlight_position_4=0.0 0.0 0.0\nlight_position_3=0.0 0.0 0.0\nlight_position_2=0.0 -100.0 100.0\ncamera_fov_y=60.0\nlight_position_1=100.0 100.0 100.0\nlight_position_0=-100.0 100.0 100.0\nfront_material_color=0.9411765 0.25882354 0.05882353\nbackground_color=1.0 1.0 1.0\nlight_intensity_7=1.0\nfront_material_ambient_intensity=0.4\nlight_intensity_6=1.0\nscale_factor=0.11388256\nlight_intensity_5=1.0\nlight_intensity_4=1.0\nlight_intensity_3=1.0\nlight_intensity_2=0.3\nlight_intensity_1=0.7\nlight_intensity_0=0.5\nrotation_matrix=-0.9975637 0.06831278 -0.014604302 0.0 -0.050649248 -0.56332713 0.82468677 0.0 0.048110377 0.82340914 0.5654113 0.0 0.0 0.0 0.0 1.0\nlight_color_7=1.0 1.0 1.0\nlight_color_6=1.0 1.0 1.0\nfront_material_diffuse_intensity=0.8\nlight_color_5=1.0 1.0 1.0\nlight_color_4=1.0 1.0 1.0\nlight_color_3=1.0 1.0 1.0\nlight_color_2=1.0 1.0 1.0\nlight_color_1=1.0 1.0 1.0\nlight_color_0=1.0 1.0 1.0\nlight_status_7=OFF\nlight_status_6=OFF\nlight_status_5=OFF\nlight_status_4=OFF\nfront_material_shininess=30.0\nlight_status_3=OFF\nlight_status_2=ON\nlight_status_1=ON\nlight_status_0=ON\ncamera_height=2.0\nsurface_equation=(x^2+y^2+z^2+a^2-b^2)^2-4*b^2*(x^2+y^2)\ncamera_transform=1.0 0.0 0.0 -0.0 0.0 1.0 0.0 -0.0 0.0 0.0 1.0 -1.0 0.0 0.0 0.0 1.0\nback_material_ambient_intensity=0.4\nback_material_color=0.87058824 0.41960785 0.101960786\nback_material_shininess=30.0\nback_material_diffuse_intensity=0.8";
    protected static Map<String, String> properties = new HashMap<String, String>();

    public static void setSurfaceDescription(String surfaceDescription)
    {
	ApiExporter.surfaceDescription = surfaceDescription;
    }

    public static String getSurfaceDescription()
    {
	return surfaceDescription;
    }

    public static void setSurfaceProperty(String aKey, String aValue)
    {
	properties.put(aKey, aValue);
    }
    
    public static String getSurfaceProperty(String aKey)
    {
	return properties.get(aKey);
    }


    public static int renderSurface(final int size, final int startX, final int startY, final int endX, final int endY, final JavaScriptObject callback)
    {
	surferService.getRenderer(surfaceDescription, properties, new AsyncCallback<JsCPUAlgebraicSurfaceRenderer>()
	{
	    public void onSuccess(JsCPUAlgebraicSurfaceRenderer renderer)
	    {
		GwtSurfer main = new GwtSurfer(renderer, startX, startY, endX, endY);
		main.setImageGenerator(new CanvasImageGenerator());
		main.doStandalone(new String[] { "animation", size + "", "1", "surface-x" });
		if (callback != null)
		    executeCallback(callback);
	    }

	    public void onFailure(Throwable caught)
	    {
	    }
	});

	//	JsCPUAlgebraicSurfaceRenderer renderer = surferService.getRenderer(surfaceDescription, properties);

	return 0;
    }

    protected native static void executeCallback(JavaScriptObject callback)/*-{
		callback();
    }-*/;

    public static native void exportStaticMethod() /*-{
		$wnd.renderSurface= $entry(@org.test.client.ApiExporter::renderSurface(IIIIILcom/google/gwt/core/client/JavaScriptObject;));
		$wnd.setSurfaceProperty = $entry(@org.test.client.ApiExporter::setSurfaceProperty(Ljava/lang/String;Ljava/lang/String;));
		$wnd.getSurfaceProperty = $entry(@org.test.client.ApiExporter::getSurfaceProperty(Ljava/lang/String;));
		$wnd.setSurfaceDescription= $entry(@org.test.client.ApiExporter::setSurfaceDescription(Ljava/lang/String;));
		$wnd.getSurfaceDescription= $entry(@org.test.client.ApiExporter::getSurfaceDescription());
    }-*/;
}
