package java.lang;


public class Thread implements Runnable
{
    static protected Thread J2S_THREAD;
    
    public static boolean interrupted()
    {
	return false;
    }
    public static Thread currentThread()
    {
	if (J2S_THREAD == null)
	{
	    J2S_THREAD= new Thread();
	}
	return J2S_THREAD;
    }
    
    public void run()
    {
	
    }
}
