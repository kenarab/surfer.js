/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mfo.jsurf.algebra;

/**
 *
 * @author Christian Stussak <christian at knorf.de>
 */
public class DoubleValue implements DoubleOperation
{
    protected double value1;

    public double getValue1()
    {
	return value1;
    }

    public void setValue1(double value1)
    {
	this.value1= value1;
    }

    public DoubleValue()
    {
    }

    public double getValue()
    {
	return value1;
    }

    public void setValue(double value)
    {
	this.value1= value;
    }

    public DoubleValue(double value)
    {
	this.value1= value;
    }

    public <RETURN_TYPE, PARAM_TYPE> RETURN_TYPE accept(Visitor<RETURN_TYPE, PARAM_TYPE> visitor, PARAM_TYPE arg)
    {
	return visitor.visit(this, arg);
    }
}
