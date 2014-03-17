package de.mfo.jsurf;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class BufferedImageGenerator implements ImageGenerator
{
    protected Timer timer;
    protected int size;

    public void setSize(int size)
    {
        this.size= size;
    }

    public BufferedImageGenerator(int size)
    {
	this.size= size;
    }

    public BufferedImage createBufferedImageFromRGB(ImgBuffer ib)
    {
	int w= ib.width;
	int h= ib.height;

	DirectColorModel colormodel= new DirectColorModel(24, 0xff0000, 0xff00, 0xff);
	SampleModel sampleModel= colormodel.createCompatibleSampleModel(w, h);
	DataBufferInt data= new DataBufferInt(ib.rgbBuffer, w * h);
	WritableRaster raster= WritableRaster.createWritableRaster(sampleModel, data, new Point(0, 0));
	return new BufferedImage(colormodel, raster, false, null);
    }

    public static void saveToPNG(OutputStream os, BufferedImage bufferedImage) throws java.io.IOException
    {
	AffineTransform tx= AffineTransform.getScaleInstance(1, -1);
	tx.translate(0, -bufferedImage.getHeight(null));
	AffineTransformOp op= new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	bufferedImage= op.filter(bufferedImage, null);
	javax.imageio.ImageIO.write(bufferedImage, "png", os);
    }

    public void draw(ImgBuffer imgBuffer, String fileExportName, int aSize)
    {
	try
	{
	    BufferedImage bufferedImage= createBufferedImageFromRGB(imgBuffer);
	    saveToPNG(new FileOutputStream(new File(fileExportName + ".png")), bufferedImage);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void startTimerPeriodically(final Runnable runnable, int milliseconds)
    {
	timer= new Timer();
	TimerTask timerTask= new TimerTask()
	{
	    public void run()
	    {
		runnable.run();
	    }
	};
	timer.schedule(timerTask, 0, 10);
    }

    public void cancelTimer()
    {
	timer.cancel();
    }
}