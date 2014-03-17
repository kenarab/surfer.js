package de.mfo.jsurfer;

import java.util.List;
import java.util.Vector;

public class Parameters
{
    static List<String> allowedModes;
 
    static {
	allowedModes= new Vector<String>();
	allowedModes.add("animation");
	allowedModes.add("profile");
    }
    public String mode;
    public int surface= 0;
    private String surfaceName;
    public int size;
    public int frames;
    public int cases;
    private int maxSurfaces= 5;

    public Parameters(String[] args)
    {


	boolean error= false;
	try
	{
	    mode= args[0].toLowerCase();
	    if (!allowedModes.contains(mode))
	    {
		error= true;
		throw new JSurferException();
	    }
	}
	catch (Exception unE)
	{
	    mode= "animation";
	}
	try
	{
	    surface= new Integer(args[3]);
	}
	catch (Exception unE)
	{
	    surface= 0;
	}
	try
	{
	    surfaceName= args[3];
	}
	catch (Exception unE)
	{
	    surfaceName= "";
	    error= true;
	}
	try
	{
	    size= new Integer(args[1]);
	}
	catch (Exception unE)
	{
	    size= 100;
	    error= true;
	}
	try
	{
	    frames= new Integer(args[2]);
	}
	catch (Exception unE)
	{
	    frames= 10;
	    error= true;
	}
	try
	{
	    cases= new Integer(args[3]);
	}
	catch (Exception unE)
	{
	    cases= 1;
	    if ("profile".equals(mode))
		error= true;
	}
	if (!(surface > 0 && surface <= maxSurfaces))
	{
	    if (surfaceName.equals("Torus"))
		surface= 1;
	    if (surfaceName.equals("Zitrus"))
		surface= 2;
	    if (surfaceName.equals("KummerCuartic"))
		surface= 3;
	    if (surfaceName.equals("BarthSextic"))
		surface= 4;
	    if (surfaceName.equals("ChmutovOctic"))
		surface= 5;
	}
	if (surface == 0)
	{
	    surface= 1;
	    System.out.println(" [WARNING] using Torus as default");
	}
	if (error)
	{
	    System.out.println(" correct call:");
	    System.out.println("for standalone mode:");
	    //			System.out
	    //					.println("\tJSurferMinimal [size] [surface] [frames]");
	    System.out.println("\tGwtSurferExperiment animation [size] [frames] [surface]");
	    System.out.println("for profile mode:");
	    //			System.out
	    //					.println("\tJSurferMinimal [size] profile [frames] [cases]");
	    System.out.println("\tGwtSurferExperiment profile [size]  [frames] [cases]");
	    System.out.println("");
	    System.out.println("where \tsurface in {Torus, Zitrus, KummerCuartic, BarthSextic, ChmutovOctic,1,2,3,4,5}");
	    System.out.println("\t size is the render size");
	    System.out.println("\t cases is the number of running cases");
	}

    }
}
