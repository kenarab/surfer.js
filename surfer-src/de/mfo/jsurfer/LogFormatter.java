package de.mfo.jsurfer;

import java.util.logging.LogRecord;

import com.google.gwt.logging.impl.FormatterImpl;

public class LogFormatter extends FormatterImpl
{
    private static final StringBuilder sb= new StringBuilder();

    public String format(LogRecord rec)
    {
	synchronized (sb)
	{
	    sb.setLength(0);
	    //	    sb.append(new Date(rec.getMillis()).toString());
	    //	    sb.append(": ");
	    sb.append(rec.getMessage());
	    sb.append("\n");
	    return sb.toString();
	}
    }
}
