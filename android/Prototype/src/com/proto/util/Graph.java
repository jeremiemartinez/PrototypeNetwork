package com.proto.util;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class Graph extends FrameLayout {

	/**************************************************************************/
	/**                                                                       */
	/**************************************************************************/

	public Graph(Context context) {
		super(context);
	}

	public Graph(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Graph(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**************************************************************************/
	/**                                                                       */
	/**************************************************************************/

	private Context context;

	private String title;

	private double lowerBound;

	private ArrayList<GraphViewData> plots = new ArrayList<GraphViewData>();

	private int count;

	private double max;

	/**************************************************************************/
	/**                                                                       */
	/**************************************************************************/

	public void init(Context context, String title, double lowerBound) {
		this.context = context;
		this.title = title;
		this.lowerBound = lowerBound;
		max = 0;
		plots.clear();
		count = 0;
		GraphView graph = new LineGraphView(context, title);
		graph.setManualYAxisBounds(lowerBound, 0d);
		setGraph(graph);
	}

	/**************************************************************************/
	/**                                                                       */
	/**************************************************************************/

	private void setGraph(GraphView graph) {
		removeAllViews();
		addView(graph);
	}

	public void addPlot(double y) {
		if (y >= max)
			max = y;
		GraphView graph = new LineGraphView(context, title);
		graph.setManualYAxisBounds(max, 0d);
		plots.add(new GraphViewData(count++, y));

		GraphViewData[] aplots = new GraphViewData[plots.size()];
		int i = 0;
		for (GraphViewData g : plots)
			aplots[i++] = g;

		graph.addSeries(new GraphViewSeries(aplots));
		setGraph(graph);
	}

	public void reset(double initLow) {
		this.lowerBound = initLow;
		max = 0;
		plots.clear();
		count = 0;
	}

}
