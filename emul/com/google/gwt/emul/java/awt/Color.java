package java.awt;

public class Color
{
    protected int value;

    public Color(int rgb)
    {
	value= 0xff000000 | rgb;
    }
    
    public Color(float r, float g, float b) 
    { 
        this( (int) (r*255+0.5), (int) (g*255+0.5), (int) (b*255+0.5));
    }

    public Color(int r, int g, int b)
    {
	this(r, g, b, 255);
    }

    public Color(int r, int g, int b, int a)
    {
	value= ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
    }

    public int getRGB() 
    {
        return value;
    }
    public int getRed()
    {
	return (getRGB() >> 16) & 0xFF;
    }

    public int getGreen()
    {
	return (getRGB() >> 8) & 0xFF;
    }

    public int getBlue()
    {
	return (getRGB() >> 0) & 0xFF;
    }

    public int getAlpha()
    {
	return (getRGB() >> 24) & 0xff;
    }

}
