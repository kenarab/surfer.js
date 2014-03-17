package de.mfo.jsurfer.rendering.cpu;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SamplingPointIterator implements Iterator<SamplingPoint>
{
    SamplingPoint[] points;

    public SamplingPoint[] getPoints()
    {
	return points;
    }

    public void setPoints(SamplingPoint[] points)
    {
	this.points= points;
    }

    public int getPosition()
    {
	return position;
    }

    public void setPosition(int position)
    {
	this.position= position;
    }

    int position;

    public SamplingPointIterator(SamplingPoint[] points)
    {
	this.points= points;
	position= 0;
    }

    public boolean hasNext()
    {
	return this.position < this.points.length;
    }

    public SamplingPoint next() throws NoSuchElementException
    {
	try
	{
	    return points[position++];
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    throw new NoSuchElementException("No more elements");
	}
    }

    public void remove() throws UnsupportedOperationException
    {
	throw new UnsupportedOperationException("Operation is not supported");
    }
}