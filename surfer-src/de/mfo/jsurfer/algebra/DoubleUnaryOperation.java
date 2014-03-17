/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mfo.jsurfer.algebra;

/**
 *
 * @author Christian Stussak <christian at knorf.de>
 */
public class DoubleUnaryOperation implements DoubleOperation
{
    public enum Op
    {
	neg, sin, cos, tan, asin, acos, atan, exp, log, sqrt, ceil, floor, abs, sign;
    }

    public Op operator;

    public DoubleUnaryOperation()
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

    public DoubleOperation getOperand()
    {
	return operand;
    }

    public void setOperand(DoubleOperation operand)
    {
	this.operand= operand;
    }

    public DoubleOperation operand;

    public DoubleUnaryOperation(Op operator, DoubleOperation operand)
    {
	this.operator= operator;
	this.operand= operand;
    }

    public <RETURN_TYPE, PARAM_TYPE> RETURN_TYPE accept(Visitor<RETURN_TYPE, PARAM_TYPE> visitor, PARAM_TYPE arg)
    {
	return visitor.visit(this, arg);
    }
}
