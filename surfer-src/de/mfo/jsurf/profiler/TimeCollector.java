package de.mfo.jsurf.profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.TreeMap;

import de.mfo.jsurf.ALog;
import de.mfo.jsurf.MString;

/**
 * 
 * @author petrola/baranek
 */

public class TimeCollector
{
    private Stack<Long> start;
    private Stack<String> keys;
    protected Map<String, List<Long>> elapsedTimes;
    private String platform;
    private int stage;
    private Map<String, Integer> stages;
    private String keyHeader;

    public TimeCollector()
    {
	start= new Stack<Long>();
	keys= new Stack<String>();
	stages= new HashMap<String, Integer>();
	elapsedTimes= new TreeMap<String, List<Long>>();
    }

    public void registerStart(String key)
    {
	long nanoTime= System.nanoTime();
	start.push(nanoTime);
	if (!elapsedTimes.containsKey(key))
	    elapsedTimes.put(key, new ArrayList<Long>());

	keys.push(key);
    }

    public void registerEnd()
    {
	long nanoTime= System.nanoTime();
	String key= (String) keys.pop();
	long elapsedTime= nanoTime - ((Long) start.pop()).longValue();
	elapsedTimes.get(key).add(elapsedTime);
	//	String acaEsta= MString.format("%.4f", new Long(elapsedTime).floatValue());
	//	logger.log(Level.INFO, acaEsta);
	//	acaEsta+="sfsf";
    }

    public void printReport(String prefijo)
    {
	for (Entry<String, List<Long>> entry : elapsedTimes.entrySet())
	{
	    Double total= 0.0;
	    for (Long time : entry.getValue())
	    {
		total+= time;
	    }

	    total= timeUnitNormalization(total);
	    double counter= entry.getValue().size();
	    double average= total / counter;

	    ALog.log(MString.format("%9s -> %50s ==> %.4f ms X %d= %.4fs = %.4fm", prefijo, entry.getKey(), average, (long) counter, total / 1000.0, total / 1000.0 / 60.0), platform, true);
	}
    }

    public void resetStage(String aPlatform)
    {
	stage= 0;
	platform= aPlatform;
    }

    public void printReportAsTable(String prefix)
    {
	String tableFormat= "%9s|%30s|%.5f|%5d|%.5f";
	String tableFormatHeader= "%9s|%30s|%6s|%5s|%6s";
	ALog.log((MString.format(tableFormatHeader, "prefix", keyHeader, "avg", "count", "total")), platform, true);

	for (Entry<String, List<Long>> entry : elapsedTimes.entrySet())
	{
	    Double total= 0.0;
	    for (Long time : entry.getValue())
	    {
		total+= time;
	    }
	    total= timeUnitNormalization(total);

	    double counter= entry.getValue().size();
	    double average= total / counter;
	    ALog.log((MString.format(tableFormat, prefix, entry.getKey(), average, (long) counter, total)), platform, true);
	}
    }

    private Double timeUnitNormalization(double total)
    {
	double ret= total;
	if (platform.equals("java"))
	    //Java method is in nanoseconds and we want milliseconds
	    ret/= 1000000.0;
	if (platform.equals("js"))
	    //JS method is in microseconds and we want milliseconds
	    ret/= 1000.0;
	return ret;
    }

    public void registerStart(int surfaceNumber, String surfaceId, Integer size, String description)
    {
	String actualStageDescription= MString.format("%3d|%10s|%5d|%10s", surfaceNumber, surfaceId, size, description);
	int actualStage;
	if (stages.keySet().contains(actualStageDescription))
	    actualStage= stages.get(actualStageDescription);
	else
	{
	    actualStage= ++stage;
	    stages.put(actualStageDescription, actualStage);
	}
	String keyFormatMask= "%-8s|%3d|%-20s|%5d|%3d|%10s";
	String keyFormatMaskHeader= "%-8s|%3s|%-20s|%5s|%3s|%10s";
	if (keyHeader == null)
	    keyHeader= MString.format(keyFormatMaskHeader, "platform", "#s", "surfId", "size", "stg", "description");
	registerStart(MString.format(keyFormatMask, platform, surfaceNumber, surfaceId, size, actualStage, description));

    }
}