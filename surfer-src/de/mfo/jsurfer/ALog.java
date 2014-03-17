package de.mfo.jsurfer;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.logging.client.LoggingPopup;
import com.google.gwt.user.client.ui.PopupPanel;

public class ALog
{
    private static final Logger logger= Logger.getLogger("");
    protected static boolean initialized= false;
    static
    {
    }

    public static void lazyInitialization(String platform)
    {
	if (platform.equals("js"))
	{
	    PopupPanel loggingPopup= new LoggingPopup();
	    loggingPopup.setPopupPosition(10, 340);
	    loggingPopup.setWidth("500px");
	    HasWidgetsLogHandler handler= new HasWidgetsLogHandler(loggingPopup);
	    handler.setFormatter(new LogFormatter());
	    handler.setLevel(Level.ALL);
	    logger.addHandler(handler);
	}
	initialized= true;

    }
    public static void log(String msg, String platform, boolean includeEnter)
    {
	if (!initialized)
	    lazyInitialization(platform);
	if (platform.equals("js"))
	    logger.log(Level.INFO, msg);
	if (platform.equals("java"))
	    if (includeEnter)
		System.out.println(msg);
	    else
		System.out.print(msg);
    }

    public static void log(String msg, Throwable e)
    {
	logger.log(Level.INFO, msg, e);
    }
}