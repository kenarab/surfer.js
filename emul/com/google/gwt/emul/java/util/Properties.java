package java.util;

public class Properties
{
    public synchronized boolean containsKey(Object key)
    {
	return false;
    }

    public String getProperty(String key)
    {
	return "";
    }
    
    public synchronized Object setProperty(String key, String value) {
        return null;
    }
}
