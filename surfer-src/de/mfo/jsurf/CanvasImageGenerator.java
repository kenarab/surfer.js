package de.mfo.jsurf;

import java.awt.Color;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class CanvasImageGenerator implements ImageGenerator
{
    protected Context2d context;
    protected Canvas canvas;
    protected ImageData imageData;
    protected int size;
    protected Timer timer;

    public CanvasImageGenerator(int aSize)
    {
	this.size= aSize;
	initCanvas();
    }

    public CanvasImageGenerator()
    {
    }

    private void initCanvas()
    {
	if (canvas == null)
	{
	    canvas= Canvas.createIfSupported();
	    canvas.setCoordinateSpaceHeight(size);
	    canvas.setCoordinateSpaceWidth(size);
	    RootPanel.get().add(canvas);
	    context= canvas.getContext2d();
	    context.getCanvas().setAttribute("style", "position: absolute; top: 0px; left: 540px; width: 750px; height: 750px;");
	    imageData= context.createImageData(size, size);
	}
    }

    public void draw(ImgBuffer imgBuffer, int aSize)
    {
	for (int x= 0; x < aSize; x++)
	{
	    for (int y= 0; y < aSize; y++)
	    {
		int rgb= imgBuffer.rgbBuffer[y * aSize + x];
		Color color= new Color(rgb);

		imageData.setAlphaAt(color.getAlpha(), x, y);
		imageData.setRedAt(color.getRed(), x, y);
		imageData.setBlueAt(color.getBlue(), x, y);
		imageData.setGreenAt(color.getGreen(), x, y);
	    }
	}

	context.putImageData(imageData, 0, 0);
    }

    public void startTimerPeriodically(final Runnable runnable, int milliseconds)
    {
	timer= new Timer()
	{
	    public void run()
	    {
		runnable.run();
	    }
	};
	timer.scheduleRepeating(10);
    }

    public void cancelTimer()
    {
	timer.cancel();
    }

    public void setSize(int size)
    {
	this.size= size;
	initCanvas();
    }
}