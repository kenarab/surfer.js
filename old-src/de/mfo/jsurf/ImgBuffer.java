package de.mfo.jsurf;

public class ImgBuffer
{
    public int[] rgbBuffer;
    public int width;
    public int height;

    public ImgBuffer(int w, int h)
    {
	rgbBuffer= new int[3 * w * h];
	width= w;
	height= h;
    }
}