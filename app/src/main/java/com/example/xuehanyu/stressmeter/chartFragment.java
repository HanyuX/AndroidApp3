package com.example.xuehanyu.stressmeter;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import android.graphics.Color;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;
/**
 * Created by xuehanyu on 4/13/16.
 */
public class chartFragment extends Fragment {

    private static final String[][] DATA_TO_SHOW = { { "This", "is"},
            {"second", "test" },{"second", "test" },
            {"second", "test" },{"second", "test" },
            {"second", "test" },{"second", "test" },
            {"second", "test" },{"second", "test" } };

    @Override
    /** called when the fragment is created */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.chart_layout, container, false);

        List<PointValue> values = new ArrayList<>();
        values.add(new PointValue(0, 2));
        values.add(new PointValue(1, 4));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(3, 4));
        int numberOfPoints = 4;

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        line.setFilled(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData();
        Axis axisX = new Axis().setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Instance");
        axisY.setName("StressLevel");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setLines(lines);

        LineChartView chart = (LineChartView) rootView.findViewById(R.id.chart);
        chart.setLineChartData(data);
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 5;
        v.left = 0;
        v.right = numberOfPoints-1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);

        //table
        TableView<String[]> tableView = (TableView<String[]>) rootView.findViewById(R.id.stressTable);
        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(getActivity(), "Time", "Stress");
        SimpleTableDataAdapter dataAdapter = new SimpleTableDataAdapter(getActivity(), DATA_TO_SHOW);
        tableView.setHeaderAdapter(simpleTableHeaderAdapter);
        dataAdapter.setTextSize(10);
        tableView.setDataAdapter(dataAdapter);
        int colorEvenRows = getResources().getColor(android.R.color.white);
        int colorOddRows = getResources().getColor(android.R.color.darker_gray);
        tableView.setDataRowColoriser(TableDataRowColorizers.alternatingRows(colorEvenRows, colorOddRows));
        return rootView;
    }
}

