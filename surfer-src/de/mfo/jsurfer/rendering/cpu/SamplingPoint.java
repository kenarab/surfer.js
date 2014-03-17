package de.mfo.jsurfer.rendering.cpu;

public class SamplingPoint
{
    private float u, v, weight;

    public void setU(float u)
    {
	this.u= u;
    }

    public void setV(float v)
    {
	this.v= v;
    }

    public void setWeight(float weight)
    {
	this.weight= weight;
    }

    SamplingPoint(float u, float v, float weight)
    {
	this.u= u;
	this.v= v;
	this.weight= weight;
    }

    public float getU()
    {
	return u;
    }
    public float getV()
    {
	return v;
    }
    public float getWeight()
    {
	return weight;
    }
}