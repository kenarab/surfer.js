/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mfo.jsurf.algebra;

/**
 *
 * @author Christian Stussak <christian at knorf.de>
 */
public class DoubleBinaryOperation implements DoubleOperation
{
    public enum Op
    {
	add, sub, mult, div, pow, atan2;
    }

    public Op operator;

    public DoubleBinaryOperation()
    {
    }

    public Op getOperator()
    {
	return operator;
    }

    public void setOperator(Op operator)
    {
	this.operator= operator;
    }

    public DoubleOperation getFirstOperand()
    {
	return firstOperand;
    }

    public void setFirstOperand(DoubleOperation firstOperand)
    {
	this.firstOperand= firstOperand;
    }

    public DoubleOperation getSecondOperand()
    {
	return secondOperand;
    }

    public void setSecondOperand(DoubleOperation secondOperand)
    {
	this.secondOperand= secondOperand;
    }

    public DoubleOperation firstOperand;
    public DoubleOperation secondOperand;

    public DoubleBinaryOperation(Op operator, DoubleOperation firstOperand, DoubleOperation secondOperand)
    {
	this.operator= operator;
	this.firstOperand= firstOperand;
	this.secondOperand= secondOperand;
    }

    public <RETURN_TYPE, PARAM_TYPE> RETURN_TYPE accept(Visitor<RETURN_TYPE, PARAM_TYPE> visitor, PARAM_TYPE arg)
    {
	return visitor.visit(this, arg);
    }
}
