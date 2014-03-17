package de.mfo.jsurfer;


import java.awt.Point;
import java.util.Date;
import java.util.Random;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import de.mfo.jsurfer.algebra.Simplificator;
import de.mfo.jsurfer.profiler.TimeCollector;
import de.mfo.jsurfer.rendering.Camera;
import de.mfo.jsurfer.rendering.cpu.CPUAlgebraicSurfaceRenderer;
import de.mfo.jsurfer.util.RotateSphericalDragger;

/**
 * 
 * @author stussak
 * @author baranek-petrola
 */
public class Main
{
    protected String platform= "js";
    int randomSeed= 0;
    Random randomGenerator;
    protected CPUAlgebraicSurfaceRenderer asr;
    protected Matrix4d rotation;
    protected Matrix4d scale;
    protected TimeCollector timeCollector;
    protected String surfaceId;

    protected int currentFrame= 0;
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;
    protected RotateSphericalDragger rsd;
    protected ImageGenerator imageGenerator;

    public ImageGenerator getImageGenerator()
    {
	return imageGenerator;
    }

    public void setImageGenerator(ImageGenerator imageGenerator)
    {
	this.imageGenerator= imageGenerator;
    }

    public static void main(String[] args)
    {
	Main main= new Main();
	//For set profiling information in java mode 
	main.platform="java";
	
	final Parameters parameters= new Parameters(args);
	main.imageGenerator= new BufferedImageGenerator(parameters.size);
	if (parameters.mode.equals("profile"))
	    main.mainProfile(parameters);
	if (parameters.mode.equals("animation"))
	    main.mainStandalone(parameters);
    }

    public Main()
    {
	timeCollector= new TimeCollector();
	randomGenerator= new Random(randomSeed);
    }

    public void setScale(double scaleFactor)
    {
	scaleFactor= Math.pow(10, scaleFactor);
	scale.setScale(scaleFactor);
    }

    protected static void setOptimalCameraDistance(Camera c)
    {
	float cameraDistance;
	switch (c.getCameraType())
	{
	    case ORTHOGRAPHIC_CAMERA:
		cameraDistance= 1.0f;
		break;
	    case PERSPECTIVE_CAMERA:
		cameraDistance= (float) (1.0 / Math.sin((Math.PI / 180.0) * (c.getFoVY() / 2.0)));
		break;
	    default:
		throw new RuntimeException();
	}
	c.lookAt(new Point3d(0, 0, cameraDistance), new Point3d(0, 0, -1), new Vector3d(0, 1, 0));
    }

    public void doStandalone(String args[])
    {
	final Parameters parameters= new Parameters(args);
	imageGenerator.setSize(parameters.size);
	mainStandalone(parameters);
    }

    public void mainStandalone(final Parameters parameters)
    {
	setSurface4Render(parameters.surface, parameters.size);

	imageGenerator.startTimerPeriodically(new Runnable()
	{
	    public void run()
	    {
		if (!renderCurrentSurface("test", parameters.surface, parameters.frames, parameters.size, true))
		    imageGenerator.cancelTimer();
	    }
	}, 10);
    }
    public void doProfile(final String args[])
    {
	final Parameters parameters= new Parameters(args);
	imageGenerator.setSize(parameters.size);
	mainProfile(parameters);
    }

    public void mainProfile(Parameters parameters)
    {
	int cases= parameters.cases;

	for (int currentSurfaceNumber= 1; currentSurfaceNumber <= 5; currentSurfaceNumber++)
	{
	    // ALog.log("surface " + numeroSuperficie);
	    for (int caseNumber= 0; caseNumber < cases; caseNumber++)
	    {
		// ALog.log("\tcase " + caso);
		int size= parameters.size;
		setSurface4Render(currentSurfaceNumber, size);
		for (int actualSize= 1; actualSize <= 4; actualSize++)
		{
		    // ALog.log("\t\tsize " + size);
		    // for (int i= 0; i < parameters.frames; i++)
		    renderAnimationCurrentSurface("test_" + size + "_" + caseNumber + "_" + currentSurfaceNumber, currentSurfaceNumber, parameters.frames, size, false);
		    currentFrame= 0;
		    size*= 2;
		}
	    }
	}
	printReportAsTable();
    }

    public void setSurface4Render(int aSurfaceNumber, Integer aSize)
    {
	currentFrame= 0;
	timeCollector.resetStage(platform);
	int surfaceNumber= aSurfaceNumber;
	// initCanvas(aSize);
	asr= new CPUAlgebraicSurfaceRenderer();
	timeCollector.registerStart(surfaceNumber, surfaceId, 0, "init surface");

	Simplificator simplificator= new Simplificator();
	simplificator.setParameterValue("a", 0.5d);
	simplificator.setParameterValue("b", 0.5d);
	asr.setParameterSubstitutor(simplificator);

	scale= new Matrix4d();
	scale.setIdentity();
	setScale(0.0);

	if (rotation == null)
	{
	    rotation= new Matrix4d();
	    rotation.setIdentity();
	}

	timeCollector.registerEnd();
	//	timeCollector.registerStart(surfaceNumber, surfaceId, aSize, "configFromString");
	// setOptimalCameraDistance(asr.getCamera());
	//	timeCollector.registerEnd();

    }

    public boolean renderAnimationCurrentSurface(String fileExportNamePrefix, int surfaceNumber, int frames, int aSize, boolean showing)
    {
	if (currentFrame < frames)
	{
	    if (currentFrame == 0)
	    {
		rsd= new RotateSphericalDragger();
		Matrix4d rotationCopy= new Matrix4d(rotation);
		rotationCopy.invert();
		rsd.setRotation(rotationCopy);
		startX= randomGenerator.nextInt(aSize - 1) + 1;
		startY= randomGenerator.nextInt(aSize - 1) + 1;
		float dragMoveResolution= 5;
		endX= saturate((int) (randomGenerator.nextInt((int) (2 * aSize / dragMoveResolution)) - aSize / dragMoveResolution+ startX), 1, aSize);
		endY= saturate((int) (randomGenerator.nextInt((int) (2 * aSize / dragMoveResolution)) - aSize / dragMoveResolution+ startY), 1, aSize);
	    }

	    int digits= (int) Math.ceil(Math.log10(frames));

	    int maxValue= showing ? 1 : frames;
	    for (int i= 0; i < maxValue; i++)
	    {
		renderCurrentSurface(fileExportNamePrefix + "F" + /*
								  					 * String.format("%0"
								  					 * + digits + "d",
								  					 * i)
								  					 */+currentFrame, surfaceNumber, 1, aSize, showing);
		rsd.startDrag(new Point(startX, startY));
		rsd.dragTo(new Point(endX, endY));
		rotation.invert(rsd.getRotation());
		currentFrame++;
	    }
	    return true;
	}
	else
	{
	    currentFrame= 0;
	    return false;
	}
    }

    protected int saturate(int value, int min, int max)
    {
	return Math.max(Math.min(value, max), min);
    }

    public boolean renderCurrentSurface(String fileExportName, int surfaceNumber, int frames, int aSize, boolean showing)
    {
	if (frames > 1)
	    return renderAnimationCurrentSurface(fileExportName, surfaceNumber, frames, aSize, showing);
	try
	{
	    timeCollector.registerStart(surfaceNumber, surfaceId, aSize, "configScene");
	    ImgBuffer imgBuffer= new ImgBuffer(aSize, aSize);

	    // do rendering

	    asr.setTransform(rotation);
	    asr.setSurfaceTransform(scale);
	    setOptimalCameraDistance(asr.getCamera());

	    timeCollector.registerEnd();
	    try
	    {
		timeCollector.registerStart(surfaceNumber, surfaceId, aSize, "render");
		asr.draw(imgBuffer.rgbBuffer, imgBuffer.width, imgBuffer.height);
		imageGenerator.draw(imgBuffer, fileExportName, aSize);
		timeCollector.registerEnd();
	    }
	    catch (Throwable t)
	    {
		t.printStackTrace();
	    }
	    timeCollector.registerStart(surfaceNumber, surfaceId, aSize, "drawCanvas");
	    timeCollector.registerEnd();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return true;
    }

    protected void printReportAsTable()
    {
	Date date= new Date();
	timeCollector.printReportAsTable(""/*
					   				 * MString.format("%ty%tm%td%tH%tM%tS"
					   				 * , date, date, date, date, date,
					   				 * date)
					   				 */);
    }

    public void onModuleLoad()
    {
    }

    public void stop()
    {
	imageGenerator.cancelTimer();
	printReportAsTable();
    }

}
