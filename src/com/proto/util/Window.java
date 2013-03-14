package com.proto.util;


public class Window {

	private int size;

	private double[] window;

	private int count;

	private double total;

	public Window(int size) {
		if (size <= 0)
			throw new IllegalArgumentException("Window size must be > 0 : size=" + size);
		this.size = size;
		window = new double[size];
	}

	/**************************************************************************/
	/**                                                                       */
	/**************************************************************************/

	public void add(double d, int index) {
		total += d;
		count++;
		window[index % size] = d;
	}

	public void add(double d) {
		add(d, count);
	}

	/**************************************************************************/
	/**                                                                       */
	/**************************************************************************/

	public double getAverage() {
		if (count <= size)
			return 0d;
		double a = 0d;
		for (double d : window)
			a += d;
		return a / (double) size;
	}

	public double getSquareAverage() {
		if (count <= size)
			return 0d;
		double a = 0d;
		for (double d : window) {
			double nd = d / (total / count);
			a += nd * nd;
		}
		return Math.sqrt(a / (double) size);
	}

	public void reset() {
		count = 0;
		total = 0d;
		window = new double[size];
	}
}
