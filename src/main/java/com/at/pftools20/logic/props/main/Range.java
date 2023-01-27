package com.at.pftools20.logic.props.main;

/**
 * It is a simple class, that provide Range functional. Range model: [minimum; maximum].
 */
public class Range
{
	private final int max;
	private final int min;

	public static final Range DEFAULT_INT = new Range(Integer.MIN_VALUE, Integer.MAX_VALUE);
	public static final Range UNSIGNED_INT = new Range(0, Integer.MAX_VALUE);
	public static final Range UNSIGNED_INT_ZERO = new Range(1, Integer.MAX_VALUE);

	/**
	 * @param min Min value
	 * @param max Max value
	 */
	public Range(int min, int max)
	{
		this.max = max;
		this.min = min;
	}

	public boolean forward(int num)
	{
		return num >= min && num <= max;
	}

	public int getMax()
	{
		return max;
	}

	public int getMin()
	{
		return min;
	}
}
