package de.mfo.jsurf;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import de.mfo.jsurf.profiler.TimeCollector;
import de.mfo.jsurf.rendering.Camera;
import de.mfo.jsurf.rendering.LightSource;
import de.mfo.jsurf.rendering.cpu.JsCPUAlgebraicSurfaceRenderer;
import de.mfo.jsurf.util.BasicIO;
// input/output

/**
 * 
 * @author stussak
 */
public class MainJava {
	String platform="java";
	static JsCPUAlgebraicSurfaceRenderer asr;
	static Matrix4d rotation;
	static Matrix4d scale;
	TimeCollector timeCollector;

	// default case 1
	private static String configStringTorus = "#jSurfer surface description\n#Fri Jul 08 16:58:33 CEST 2011\nfront_material_specular_iIntensity=0.5\ncamera_type=ORTHOGRAPHIC_CAMERA\nback_material_specular_iIntensity=0.5\nsurface_parameter_b=0.5\n#0.41559362411499023\nsurface_parameter_a=0.5\n#0.41736117005348206\nlight_position_7=0.0 0.0 0.0\nlight_position_6=0.0 0.0 0.0\nlight_position_5=0.0 0.0 0.0\nlight_position_4=0.0 0.0 0.0\nlight_position_3=0.0 0.0 0.0\nlight_position_2=0.0 -100.0 100.0\ncamera_fov_y=60.0\nlight_position_1=100.0 100.0 100.0\nlight_position_0=-100.0 100.0 100.0\nfront_material_color=0.9411765 0.25882354 0.05882353\nbackground_color=1.0 1.0 1.0\nlight_intensity_7=1.0\nfront_material_ambient_intensity=0.4\nlight_intensity_6=1.0\nscale_factor=0.11388256\nlight_intensity_5=1.0\nlight_intensity_4=1.0\nlight_intensity_3=1.0\nlight_intensity_2=0.3\nlight_intensity_1=0.7\nlight_intensity_0=0.5\nrotation_matrix=-0.9975637 0.06831278 -0.014604302 0.0 -0.050649248 -0.56332713 0.82468677 0.0 0.048110377 0.82340914 0.5654113 0.0 0.0 0.0 0.0 1.0\nlight_color_7=1.0 1.0 1.0\nlight_color_6=1.0 1.0 1.0\nfront_material_diffuse_intensity=0.8\nlight_color_5=1.0 1.0 1.0\nlight_color_4=1.0 1.0 1.0\nlight_color_3=1.0 1.0 1.0\nlight_color_2=1.0 1.0 1.0\nlight_color_1=1.0 1.0 1.0\nlight_color_0=1.0 1.0 1.0\nlight_status_7=OFF\nlight_status_6=OFF\nlight_status_5=OFF\nlight_status_4=OFF\nfront_material_shininess=30.0\nlight_status_3=OFF\nlight_status_2=ON\nlight_status_1=ON\nlight_status_0=ON\ncamera_height=2.0\nsurface_equation=(x^2+y^2+z^2+a^2-b^2)^2-4*b^2*(x^2+y^2)\ncamera_transform=1.0 0.0 0.0 -0.0 0.0 1.0 0.0 -0.0 0.0 0.0 1.0 -1.0 0.0 0.0 0.0 1.0\nback_material_ambient_intensity=0.4\nback_material_color=0.87058824 0.41960785 0.101960786\nback_material_shininess=30.0\nback_material_diffuse_intensity=0.8";
	// case 2
	private static String configStringZitrus = "#jSurfer surface description\n#Fri Jul 08 16:58:33 CEST 2011\nfront_material_specular_iIntensity=0.5\ncamera_type=ORTHOGRAPHIC_CAMERA\nback_material_specular_iIntensity=0.5\nsurface_parameter_b=0.5\n#0.41559362411499023\nsurface_parameter_a=0.5\n#0.41736117005348206\nlight_position_7=0.0 0.0 0.0\nlight_position_6=0.0 0.0 0.0\nlight_position_5=0.0 0.0 0.0\nlight_position_4=0.0 0.0 0.0\nlight_position_3=0.0 0.0 0.0\nlight_position_2=0.0 -100.0 100.0\ncamera_fov_y=60.0\nlight_position_1=100.0 100.0 100.0\nlight_position_0=-100.0 100.0 100.0\nfront_material_color=0.9411765 0.25882354 0.05882353\nbackground_color=1.0 1.0 1.0\nlight_intensity_7=1.0\nfront_material_ambient_intensity=0.4\nlight_intensity_6=1.0\nscale_factor=0.05388256\nlight_intensity_5=1.0\nlight_intensity_4=1.0\nlight_intensity_3=1.0\nlight_intensity_2=0.3\nlight_intensity_1=0.7\nlight_intensity_0=0.5\nrotation_matrix=-0.9975637 0.06831278 -0.014604302 0.0 -0.050649248 -0.56332713 0.82468677 0.0 0.048110377 0.82340914 0.5654113 0.0 0.0 0.0 0.0 1.0\nlight_color_7=1.0 1.0 1.0\nlight_color_6=1.0 1.0 1.0\nfront_material_diffuse_intensity=0.8\nlight_color_5=1.0 1.0 1.0\nlight_color_4=1.0 1.0 1.0\nlight_color_3=1.0 1.0 1.0\nlight_color_2=1.0 1.0 1.0\nlight_color_1=1.0 1.0 1.0\nlight_color_0=1.0 1.0 1.0\nlight_status_7=OFF\nlight_status_6=OFF\nlight_status_5=OFF\nlight_status_4=OFF\nfront_material_shininess=30.0\nlight_status_3=OFF\nlight_status_2=ON\nlight_status_1=ON\nlight_status_0=ON\ncamera_height=2.0\nsurface_equation="
			+ "1.2*x^2+1.2*z^2-5*(y+0.5)^3*(0.5-y)^3"
			+ "\ncamera_transform=1.0 0.0 0.0 -0.0 0.0 1.0 0.0 -0.0 0.0 0.0 1.0 -1.0 0.0 0.0 0.0 1.0\nback_material_ambient_intensity=0.4\nback_material_color=0.87058824 0.41960785 0.101960786\nback_material_shininess=30.0\nback_material_diffuse_intensity=0.8";;
	// ********** aca
	// case 3
	private static String configStringKummerCuartic = "#jSurfer surface description\n#Fri Jul 08 16:58:33 CEST 2011\nfront_material_specular_iIntensity=0.5\ncamera_type=ORTHOGRAPHIC_CAMERA\nback_material_specular_iIntensity=0.5\nsurface_parameter_b=0.5\n#0.41559362411499023\nsurface_parameter_a=0.5\n#0.41736117005348206\nlight_position_7=0.0 0.0 0.0\nlight_position_6=0.0 0.0 0.0\nlight_position_5=0.0 0.0 0.0\nlight_position_4=0.0 0.0 0.0\nlight_position_3=0.0 0.0 0.0\nlight_position_2=0.0 -100.0 100.0\ncamera_fov_y=60.0\nlight_position_1=100.0 100.0 100.0\nlight_position_0=-100.0 100.0 100.0\nfront_material_color=0.9411765 0.25882354 0.05882353\nbackground_color=1.0 1.0 1.0\nlight_intensity_7=1.0\nfront_material_ambient_intensity=0.4\nlight_intensity_6=1.0\nscale_factor=0.5\nlight_intensity_5=1.0\nlight_intensity_4=1.0\nlight_intensity_3=1.0\nlight_intensity_2=0.3\nlight_intensity_1=0.7\nlight_intensity_0=0.5\nrotation_matrix=-0.9975637 0.06831278 -0.014604302 0.0 -0.050649248 -0.56332713 0.82468677 0.0 0.048110377 0.82340914 0.5654113 0.0 0.0 0.0 0.0 1.0\nlight_color_7=1.0 1.0 1.0\nlight_color_6=1.0 1.0 1.0\nfront_material_diffuse_intensity=0.8\nlight_color_5=1.0 1.0 1.0\nlight_color_4=1.0 1.0 1.0\nlight_color_3=1.0 1.0 1.0\nlight_color_2=1.0 1.0 1.0\nlight_color_1=1.0 1.0 1.0\nlight_color_0=1.0 1.0 1.0\nlight_status_7=OFF\nlight_status_6=OFF\nlight_status_5=OFF\nlight_status_4=OFF\nfront_material_shininess=30.0\nlight_status_3=OFF\nlight_status_2=ON\nlight_status_1=ON\nlight_status_0=ON\ncamera_height=2.0\nsurface_equation="
			+ "(x^2+y^2+z^2-(0.5+2*a)^2)^2-(3.0*((0.5+2*a)^2)-1.0)/(3.0-((0.5+2*a)^2))*(1-z-sqrt(2)*x)*(1-z+sqrt(2)*x)*(1+z+sqrt(2)*y)*(1+z-sqrt(2)*y)"
			+ "\ncamera_transform=1.0 0.0 0.0 -0.0 0.0 1.0 0.0 -0.0 0.0 0.0 1.0 -1.0 0.0 0.0 0.0 1.0\nback_material_ambient_intensity=0.4\nback_material_color=0.87058824 0.41960785 0.101960786\nback_material_shininess=30.0\nback_material_diffuse_intensity=0.8";
	// case 4 a=1
	private static String configStringBarthSextic = "#jSurfer surface description\n#Fri Jul 08 16:58:33 CEST 2011\nfront_material_specular_iIntensity=0.5\ncamera_type=ORTHOGRAPHIC_CAMERA\nback_material_specular_iIntensity=0.5\nsurface_parameter_b=0.5\n#0.41559362411499023\nsurface_parameter_a=1\n#0.41736117005348206\nlight_position_7=0.0 0.0 0.0\nlight_position_6=0.0 0.0 0.0\nlight_position_5=0.0 0.0 0.0\nlight_position_4=0.0 0.0 0.0\nlight_position_3=0.0 0.0 0.0\nlight_position_2=0.0 -100.0 100.0\ncamera_fov_y=60.0\nlight_position_1=100.0 100.0 100.0\nlight_position_0=-100.0 100.0 100.0\nfront_material_color=0.9411765 0.25882354 0.05882353\nbackground_color=1.0 1.0 1.0\nlight_intensity_7=1.0\nfront_material_ambient_intensity=0.4\nlight_intensity_6=1.0\nscale_factor=0.5\nlight_intensity_5=1.0\nlight_intensity_4=1.0\nlight_intensity_3=1.0\nlight_intensity_2=0.3\nlight_intensity_1=0.7\nlight_intensity_0=0.5\nrotation_matrix=-0.9975637 0.06831278 -0.014604302 0.0 -0.050649248 -0.56332713 0.82468677 0.0 0.048110377 0.82340914 0.5654113 0.0 0.0 0.0 0.0 1.0\nlight_color_7=1.0 1.0 1.0\nlight_color_6=1.0 1.0 1.0\nfront_material_diffuse_intensity=0.8\nlight_color_5=1.0 1.0 1.0\nlight_color_4=1.0 1.0 1.0\nlight_color_3=1.0 1.0 1.0\nlight_color_2=1.0 1.0 1.0\nlight_color_1=1.0 1.0 1.0\nlight_color_0=1.0 1.0 1.0\nlight_status_7=OFF\nlight_status_6=OFF\nlight_status_5=OFF\nlight_status_4=OFF\nfront_material_shininess=30.0\nlight_status_3=OFF\nlight_status_2=ON\nlight_status_1=ON\nlight_status_0=ON\ncamera_height=2.0\nsurface_equation="
			+ "4*((a*(1+sqrt(5))/2)^2*x^2-y^2)*((a*(1+sqrt(5))/2)^2*y^2-z^2)*((a*(1+sqrt(5))/2)^2*z^2-x^2)-(1+2*(a*(1+sqrt(5))/2))*(x^2+y^2+z^2-1)^2"
			+ "\ncamera_transform=1.0 0.0 0.0 -0.0 0.0 1.0 0.0 -0.0 0.0 0.0 1.0 -1.0 0.0 0.0 0.0 1.0\nback_material_ambient_intensity=0.4\nback_material_color=0.87058824 0.41960785 0.101960786\nback_material_shininess=30.0\nback_material_diffuse_intensity=0.8";
	// case 5 a=0
	private static String configStringChmutovOctic = "#jSurfer surface description\n#Fri Jul 08 16:58:33 CEST 2011\nfront_material_specular_iIntensity=0.5\ncamera_type=ORTHOGRAPHIC_CAMERA\nback_material_specular_iIntensity=0.5\nsurface_parameter_b=0.5\n#0.41559362411499023\nsurface_parameter_a=0\n#0.41736117005348206\nlight_position_7=0.0 0.0 0.0\nlight_position_6=0.0 0.0 0.0\nlight_position_5=0.0 0.0 0.0\nlight_position_4=0.0 0.0 0.0\nlight_position_3=0.0 0.0 0.0\nlight_position_2=0.0 -100.0 100.0\ncamera_fov_y=60.0\nlight_position_1=100.0 100.0 100.0\nlight_position_0=-100.0 100.0 100.0\nfront_material_color=0.9411765 0.25882354 0.05882353\nbackground_color=1.0 1.0 1.0\nlight_intensity_7=1.0\nfront_material_ambient_intensity=0.4\nlight_intensity_6=1.0\nscale_factor=0.3\nlight_intensity_5=1.0\nlight_intensity_4=1.0\nlight_intensity_3=1.0\nlight_intensity_2=0.3\nlight_intensity_1=0.7\nlight_intensity_0=0.5\nrotation_matrix=-0.9975637 0.06831278 -0.014604302 0.0 -0.050649248 -0.56332713 0.82468677 0.0 0.048110377 0.82340914 0.5654113 0.0 0.0 0.0 0.0 1.0\nlight_color_7=1.0 1.0 1.0\nlight_color_6=1.0 1.0 1.0\nfront_material_diffuse_intensity=0.8\nlight_color_5=1.0 1.0 1.0\nlight_color_4=1.0 1.0 1.0\nlight_color_3=1.0 1.0 1.0\nlight_color_2=1.0 1.0 1.0\nlight_color_1=1.0 1.0 1.0\nlight_color_0=1.0 1.0 1.0\nlight_status_7=OFF\nlight_status_6=OFF\nlight_status_5=OFF\nlight_status_4=OFF\nfront_material_shininess=30.0\nlight_status_3=OFF\nlight_status_2=ON\nlight_status_1=ON\nlight_status_0=ON\ncamera_height=2.0\nsurface_equation="
			+ "(a*(-2))/125+x^8+y^8+z^8-2*x^6-2*y^6-2*z^6+1.25*x^4+1.25*y^4+1.25*z^4-0.25*x^2-0.25*y^2-0.25*z^2+0.03125"
			+ "\ncamera_transform=1.0 0.0 0.0 -0.0 0.0 1.0 0.0 -0.0 0.0 0.0 1.0 -1.0 0.0 0.0 0.0 1.0\nback_material_ambient_intensity=0.4\nback_material_color=0.87058824 0.41960785 0.101960786\nback_material_shininess=30.0\nback_material_diffuse_intensity=0.8";;
	private static int maxSurfaces = 5;

	public MainJava() {
		timeCollector = new TimeCollector();
	}

	public static void setScale(double scaleFactor) {
		scaleFactor = Math.pow(10, scaleFactor);
		scale.setScale(scaleFactor);
	}

	static BufferedImage createBufferedImageFromRGB(ImgBuffer ib) {
		int w = ib.width;
		int h = ib.height;

		DirectColorModel colormodel = new DirectColorModel(24, 0xff0000,
				0xff00, 0xff);
		SampleModel sampleModel = colormodel.createCompatibleSampleModel(w, h);
		DataBufferInt data = new DataBufferInt(ib.rgbBuffer, w * h);
		WritableRaster raster = WritableRaster.createWritableRaster(
				sampleModel, data, new Point(0, 0));
		return new BufferedImage(colormodel, raster, false, null);
	}

	public static void saveToPNG(OutputStream os, BufferedImage bufferedImage)
			throws java.io.IOException {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -bufferedImage.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);
		javax.imageio.ImageIO.write(bufferedImage, "png", os);
	}

	protected static void setOptimalCameraDistance(Camera c) {
		float cameraDistance;
		switch (c.getCameraType()) {
		case ORTHOGRAPHIC_CAMERA:
			cameraDistance = 1.0f;
			break;
		case PERSPECTIVE_CAMERA:
			cameraDistance = (float) (1.0 / Math.sin((Math.PI / 180.0)
					* (c.getFoVY() / 2.0)));
			break;
		default:
			throw new RuntimeException();
		}
		c.lookAt(new Point3d(0, 0, cameraDistance), new Point3d(0, 0, -1),
				new Vector3d(0, 1, 0));
	}

	public static void loadFromString(String s) throws Exception {
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(s.getBytes()));
		loadFromProperties(props);
	}

	/*
	 * public static void loadFromFile( URL url ) throws IOException, Exception
	 * { Properties props = new Properties(); props.load( url.openStream() );
	 * loadFromProperties( props ); }
	 */
	public static void loadFromProperties(Properties props) throws Exception {
		asr.setSurfaceFamily(props.getProperty("surface_equation"));

		Set<Map.Entry<Object, Object>> entries = props.entrySet();
		String parameter_prefix = "surface_parameter_";
		for (Map.Entry<Object, Object> entry : entries) {
			String name = (String) entry.getKey();
			if (name.startsWith(parameter_prefix)) {
				String parameterName = name
						.substring(parameter_prefix.length());
				asr.setParameterValue(parameterName,
						Float.parseFloat((String) entry.getValue()));
			}
		}

		asr.getCamera().loadProperties(props, "camera_", "");
		asr.getFrontMaterial().loadProperties(props, "front_material_", "");
		asr.getBackMaterial().loadProperties(props, "back_material_", "");
		for (int i = 0; i < asr.MAX_LIGHTS; i++) {
			asr.getLightSource(i).setStatus(LightSource.Status.OFF);
			asr.getLightSource(i).loadProperties(props, "light_", "_" + i);
		}
		asr.setBackgroundColor(BasicIO.fromColor3fString(props
				.getProperty("background_color")));
		setScale(Float.parseFloat(props.getProperty("scale_factor")));
		rotation = BasicIO.fromMatrix4dString(props
				.getProperty("rotation_matrix"));
	}

	public static void main(String args[]) throws Exception {
		// mainStandalone(args);
		mainProfile(args);
	}

	public static void mainStandalone(String args[]) throws Exception {

		int surface = 0;
		try {
			surface = new Integer(args[1]);
		} catch (Exception unE) {
			surface = 0;
		}
		Integer size;
		try {
			size = new Integer(args[0]);
		} catch (Exception unE) {
			size = 100;
		}

		if (!(surface > 0 && surface <= maxSurfaces)) {
			if (args[1].equals("Torus"))
				surface = 1;
			if (args[1].equals("Zitrus"))
				surface = 2;
			if (args[1].equals("KummerCuartic"))
				surface = 3;
			if (args[1].equals("BarthSextic"))
				surface = 4;
			if (args[1].equals("ChmutovOctic"))
				surface = 5;
		}
		if (surface == 0) {
			System.out.println(" [WARNING] using Torus as default");
			System.out
					.println(" correct call: JSurferMinimal [size] [surface]");
			System.out
					.println("\t\twhere surface in {Torus, Zitrus, KummerCuartic, BarthSextic, ChmutovOctic,1,2,3,4,5}");
			surface = 1;
		}

		MainJava aSurfaceRenderer = new MainJava();
		aSurfaceRenderer.renderSurface(surface, size, false);
	}

	public static void mainProfile(String args[]) throws Exception {
		MainJava aSurfaceRenderer = new MainJava();
		for (int i = 1; i <= 5; i++) {
			System.out.println("surface " + i);
			for (int k = 0; k < 5; k++) {
				System.out.println("\tcase " + k);
				int size = 100;
				for (int j = 1; j <= 4; j++) {
					System.out.println("\t\tsize " + size);
					aSurfaceRenderer.renderSurface(i, size, false);
					size *= 2;
				}
			}
		}
		aSurfaceRenderer.printReportAsTable();

	}

	public void renderSurface(int surfaceNumber, Integer size,
			boolean printReport) {
		timeCollector.resetStage(platform);
		
		String surfaceId = "";
		String configString;
		switch (surfaceNumber) {
		case 2:
			configString = configStringZitrus;
			surfaceId = "Zitrus";
			break;
		case 3:
			configString = configStringKummerCuartic;
			surfaceId = "KummerCuartic";
			break;
		case 4:
			configString = configStringBarthSextic;
			surfaceId = "BarthSextic";
			break;
		case 5:
			configString = configStringChmutovOctic;
			surfaceId = "ChmutovOctic";
			break;
		default:
			configString = configStringTorus;
			surfaceId = "Torus";
			break;
		}
		timeCollector.registerStart(surfaceNumber, surfaceId, size, "total");
		timeCollector.registerStart(surfaceNumber, surfaceId, size, "init");

		asr = new JsCPUAlgebraicSurfaceRenderer();

		scale = new Matrix4d();
		scale.setIdentity();
		setScale(0.0);

		rotation = new Matrix4d();
		rotation.setIdentity();

		timeCollector.registerEnd();
		timeCollector.registerStart(surfaceNumber, surfaceId, size, "config");

		try {
			loadFromString(configString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setOptimalCameraDistance(asr.getCamera());
		try {
			// create color buffer
			ImgBuffer ib = new ImgBuffer(size, size);

			// do rendering

			asr.setTransform(rotation);
			asr.setSurfaceTransform(scale);
			setOptimalCameraDistance(asr.getCamera());

			timeCollector.registerEnd();
			try {
				timeCollector.registerStart(surfaceNumber, surfaceId, size,
						"draw");
				asr.draw(ib.rgbBuffer, ib.width, ib.height);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			timeCollector.registerEnd();
			timeCollector.registerStart(surfaceNumber, surfaceId, size,
					"drawCanvas");
			BufferedImage bi = createBufferedImageFromRGB(ib);
			saveToPNG(new FileOutputStream(new File("test.png")), bi);
			timeCollector.registerEnd();
			timeCollector.registerEnd(); // Total
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (printReport)
			printReportAsTable();

	}

	private void printReportAsTable() {
		// new SimpleDateFormat("yymmddhhNNss").format(new Date())
		// TODO gwt no se banca simpledateformat segun error:
		// new SimpleDateFormat("yymmddhhNNss").format(new Date())
		timeCollector.printReportAsTable("1304251821");
	}
}

