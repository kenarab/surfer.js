/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mfo.jsurf.rendering.cpu;

import javax.vecmath.Color3f;

import de.mfo.jsurf.algebra.ClosedFormRootFinder;
import de.mfo.jsurf.algebra.DescartesRootFinder;
import de.mfo.jsurf.algebra.PolynomialExpansionCoefficientCalculator;
import de.mfo.jsurf.algebra.TransformedPolynomialRowSubstitutor;
import de.mfo.jsurf.algebra.TransformedPolynomialRowSubstitutorForGradient;
import de.mfo.jsurf.rendering.AlgebraicSurfaceRenderer2;
import de.mfo.jsurf.rendering.LightProducts;
import de.mfo.jsurf.rendering.LightSource;
import de.mfo.jsurf.rendering.cpu.clipping.ClipToSphere;

/**
 *
 * @author Christian Stussak <christian at knorf.de>
 */
public class CPUAlgebraicSurfaceRenderer extends AlgebraicSurfaceRenderer2
{
//    Object drawMutex= new Object();
//
//    public Object getDrawMutex()
//    {
//	return drawMutex;
//    }
//
//    public void setDrawMutex(Object drawMutex)
//    {
//	this.drawMutex= drawMutex;
//    }

//    public Thread getDrawThread()
//    {
//	return drawThread;
//    }
//
//    public void setDrawThread(Thread drawThread)
//    {
//	this.drawThread= drawThread;
//    }

    //    public ThreadGroup getTg()
    //    {
    //        return tg;
    //    }
    //
    //    public void setTg(ThreadGroup tg)
    //    {
    //        this.tg= tg;
    //    }

    public AntiAliasingMode getAaMode()
    {
	return aaMode;
    }

    public void setAaMode(AntiAliasingMode aaMode)
    {
	this.aaMode= aaMode;
    }

    public float getAaThreshold()
    {
	return aaThreshold;
    }

    public void setAaThreshold(float aaThreshold)
    {
	this.aaThreshold= aaThreshold;
    }

    public AntiAliasingPattern getAaPattern()
    {
	return aaPattern;
    }

    public void setAaPattern(AntiAliasingPattern aaPattern)
    {
	this.aaPattern= aaPattern;
    }

//    volatile Thread drawThread;

    //    ThreadGroup tg; // just to know, which threads are active and do rendering

    public synchronized DrawcallStaticData collectDrawCallStaticData(int[] colorBuffer, int width, int height)
    {
	DrawcallStaticData dcsd= new DrawcallStaticData();

	dcsd.colorBuffer= colorBuffer;
	dcsd.width= width;
	dcsd.height= height;

	dcsd.coefficientCalculator= new PolynomialExpansionCoefficientCalculator(getSurfaceExpression());
	if (this.getSurfaceTotalDegree() < 2)
	    dcsd.realRootFinder= new ClosedFormRootFinder();
	else
	    //        dcsd.realRootFinder = new DChainRootFinder();
	    //        dcsd.realRootFinder = new SturmChainRootFinder();
	    dcsd.realRootFinder= new DescartesRootFinder(false);
	//dcsd.realRootFinder = new ClosedFormRootFinder();
	//        dcsd.realRootFinder = new GPUSuitableDescartesRootFinder2( false );
	//dcsd.realRootFinder = new BernsteinDescartesRootFinder( false );

	dcsd.frontAmbientColor= new Color3f(getFrontMaterial().getColor());
	dcsd.frontAmbientColor.scale(getFrontMaterial().getAmbientIntensity());

	dcsd.backAmbientColor= new Color3f(getBackMaterial().getColor());
	dcsd.backAmbientColor.scale(getFrontMaterial().getAmbientIntensity());

	int numOfLightSources= 0;
	for (int i= 0; i < MAX_LIGHTS; i++)
	    if (getLightSource(i) != null && getLightSource(i).getStatus() == LightSource.Status.ON)
		numOfLightSources++;
	dcsd.lightSources= new LightSource[numOfLightSources];
	dcsd.frontLightProducts= new LightProducts[numOfLightSources];
	dcsd.backLightProducts= new LightProducts[numOfLightSources];
	int lightSourceIndex= 0;
	for (int i= 0; i < MAX_LIGHTS; i++)
	{
	    LightSource lightSource= getLightSource(i);
	    if (lightSource != null && lightSource.getStatus() == LightSource.Status.ON)
	    {
		dcsd.lightSources[lightSourceIndex]= lightSource;
		dcsd.frontLightProducts[lightSourceIndex]= new LightProducts(lightSource, getFrontMaterial());
		dcsd.backLightProducts[lightSourceIndex]= new LightProducts(lightSource, getBackMaterial());

		lightSourceIndex++;
	    }
	}

	dcsd.backgroundColor= getBackgroundColor();

	dcsd.antiAliasingThreshold= aaThreshold;

	dcsd.rayCreator= RayCreator.createRayCreator(getTransform(), getSurfaceTransform(), getCamera(), width, height);
	dcsd.rayClipper= new ClipToSphere();
	//dcsd.rayClipper = new ClipToTorus( 0.5, 0.5 );
	//dcsd.rayClipper = new ClipBlowUpSurface( 1.0, 1.0 );
	//dcsd.someA = new PolynomialExpansionRowSubstitutor( getSurfaceExpression(), dcsd.rayCreator.getXForSomeA(), dcsd.rayCreator.getYForSomeA(), dcsd.rayCreator.getZForSomeA() );
	dcsd.surfaceRowSubstitutor= new TransformedPolynomialRowSubstitutor(getSurfaceExpression(), dcsd.rayCreator.getXForSomeA(), dcsd.rayCreator.getYForSomeA(), dcsd.rayCreator.getZForSomeA());
	dcsd.gradientRowSubstitutor= new TransformedPolynomialRowSubstitutorForGradient(getGradientXExpression(), getGradientYExpression(), getGradientZExpression(), dcsd.rayCreator.getXForSomeA(), dcsd.rayCreator.getYForSomeA(), dcsd.rayCreator.getZForSomeA());
	//dcsd.gradientRowSubstitutor = new FastRowSubstitutorForGradient( getGradientXExpression(), getGradientYExpression(), getGradientZExpression(), dcsd.rayCreator );

	//System.out.println( getSurfaceExpression().accept( new ToStringVisitor(), null ) );

	return dcsd;
    }

    public CPUAlgebraicSurfaceRenderer()
    {
	super();

	this.setAntiAliasingMode(AntiAliasingMode.ADAPTIVE_SUPERSAMPLING);
	//	this.tg= new ThreadGroup("Group of rendering threads of " + this);
    }

    //    ExecutorService createExecutorService()
    //    {
    //	class PriorityThreadFactory implements ThreadFactory
    //	{
    //	    public Thread newThread(Runnable r)
    //	    {
    //		Thread t= new Thread(CPUAlgebraicSurfaceRenderer.this.tg, r);
    //		t.setPriority(Thread.MIN_PRIORITY);
    //		return t;
    //	    }
    //	}
    //	return Executors.newFixedThreadPool(4 * Runtime.getRuntime().availableProcessors(), new PriorityThreadFactory());
    //	//return Executors.newSingleThreadExecutor( new PriorityThreadFactory() );
    //    }

    public enum AntiAliasingMode
    {
	SUPERSAMPLING, ADAPTIVE_SUPERSAMPLING;
    }

    private AntiAliasingMode aaMode;
    private float aaThreshold;
    private AntiAliasingPattern aaPattern;

    public void setAntiAliasingMode(AntiAliasingMode mode)
    {
	this.aaMode= mode;
	if (mode == AntiAliasingMode.SUPERSAMPLING)
	    this.aaThreshold= 0.0f;
	else
	    this.aaThreshold= 0.3f;
    }

    public AntiAliasingMode getAntiAliasingMode()
    {
	return this.aaMode;
    }

    public void draw(int[] colorBuffer, int width, int height)
    {
	DrawcallStaticData dcsd= collectDrawCallStaticData(colorBuffer, width, height);

	//	ExecutorService threadPoolExecutor= createExecutorService();

//	synchronized (drawMutex)
	{
//	    drawThread= Thread.currentThread();
	    //	    LinkedList<Future<?>> currentRenderingTasks= new LinkedList<Future<?>>();
	    int xStep= 1222; //width / Math.min(width, Math.max(2, Runtime.getRuntime().availableProcessors()));
	    int yStep= 1222; //height / Math.min(height, Math.max(2, Runtime.getRuntime().availableProcessors()));

	    for (int x= 0; x < width; x+= xStep)
		for (int y= 0; y < height && !Thread.interrupted(); y+= yStep)
		    new RenderingTask(dcsd, x, y, Math.min(x + xStep, width - 1), Math.min(y + yStep, height - 1)).call();
	    //            currentRenderingTasks.add( threadPoolExecutor.submit( new RenderingTask( dcsd, x, y, Math.min( x + xStep, width - 1 ), Math.min( y + yStep, height - 1 ) ) ) );

	    //	    try
	    //	    {
	    //		for (Future<?> f : currentRenderingTasks)
	    //		{
	    //		    try
	    //		    {
	    //			f.get();
	    //		    }
	    //		    catch (InterruptedException ie)
	    //		    {
	    //			// either this thread is interrupted while waiting
	    //			threadPoolExecutor.shutdownNow();
	    //			throw new RenderingInterruptedException("Rendering interrupted");
	    //		    }
	    //		    catch (ExecutionException ee)
	    //		    {
	    //			threadPoolExecutor.shutdownNow();
	    //			throw new RenderingInterruptedException("Rendering interrupted");
	    //		    }
	    //		    catch (Throwable t)
	    //		    {
	    //			t.printStackTrace(); // we did not except this exception
	    //			threadPoolExecutor.shutdownNow();
	    //			throw new RenderingInterruptedException("Rendering interrupted by unexpected cause", t);
	    //		    }
	    //		    if (Thread.interrupted()) // or while it was not waiting
	    //		    {
	    //			threadPoolExecutor.shutdownNow();
	    //			throw new RenderingInterruptedException("Rendering interrupted");
	    //		    }
	    //		}
	    //	    }
	    //	    finally
	    //	    {
	    //		drawThread= null;
	    //		threadPoolExecutor.shutdownNow();
	    //	    }
	}
    }

    public void stopDrawing()
    {
	//	Thread tmp_thread= drawThread;
	//	if (tmp_thread != null)
	//	    tmp_thread.interrupt();
    }
}
