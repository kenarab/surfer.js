/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mfo.jsurf.algebra;

/**
 *
 * @author Christian Stussak <christian at knorf.de>
 */
public class PolynomialSubtraction implements PolynomialOperation
{
    public PolynomialOperation firstOperand;

    public PolynomialSubtraction()
    {
    }

    public PolynomialOperation getFirstOperand()
    {
	return firstOperand;
    }

    public void setFirstOperand(PolynomialOperation firstOperand)
    {
	this.firstOperand= firstOperand;
    }

    public PolynomialOperation getSecondOperand()
    {
	return secondOperand;
    }

    public void setSecondOperand(PolynomialOperation secondOperand)
    {
	this.secondOperand= secondOperand;
    }

    public PolynomialOperation secondOperand;

    public PolynomialSubtraction(PolynomialOperation firstOperand, PolynomialOperation secondOperand)
    {
	this.firstOperand= firstOperand;
	this.secondOperand= secondOperand;
    }

    public <RETURN_TYPE, PARAM_TYPE> RETURN_TYPE accept(Visitor<RETURN_TYPE, PARAM_TYPE> visitor, PARAM_TYPE arg)
    {
	return visitor.visit(this, arg);
    }
}