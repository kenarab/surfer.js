package de.mfo.jsurf;

public class MString
{
    public static String format(String format, Object... args)
    {
	return new PrintfFormat(format).sprintf(args);
    };
}
