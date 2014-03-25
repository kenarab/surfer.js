
package de.mfo.jsurf;

public interface ImageGenerator
{
    void draw(ImgBuffer imgBuffer, int aSize);

    void startTimerPeriodically(Runnable runnable, int milliseconds);

    void cancelTimer();

    public abstract void setSize(int size);

//    void draw(ImgBuffer imgBuffer, String fileExportName, int aSize);
}
