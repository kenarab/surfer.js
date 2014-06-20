package de.mfo.jsurf;

import de.mfo.jsurf.util.MathService;

public final class JsMathService implements MathService
{
    public double nextPowerOfTwo(double d) // computes the next power of two with respect to outward rounding
    {
	return Math.pow(2, Math.ceil(Math.log(d) / Math.log(2)));
    }
}