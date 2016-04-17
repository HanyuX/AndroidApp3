package com.example.xuehanyu.stressmeter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by xuehanyu on 4/13/16.
 * The fragment for RESULTS page
 */
public class chartFragment extends Fragment {
    private View rootView;
    private List<String[]> DATA_TO_SHOW;                //data of table
    private String CSV_NAME = "stress_timestamp.csv";   //path of CSV file
    private int maxValue = 0;                           //max stress value
    private int numberOfPoints = 0;
    List<PointValue> values = new ArrayList<>();        //data of chart

    @Override
    /*
     * called when the fragment is created
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.chart_layout, container, false);
        DATA_TO_SHOW = new ArrayList<>();

        //read Data
        try {
            InputStream CSVStream = getActivity().openFileInput(CSV_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(CSVStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split(",");
                DATA_TO_SHOW.add(RowData);
                int value = Integer.parseInt(RowData[1]);
                maxValue = value > maxValue ? value : maxValue;
                values.add(new PointValue(numberOfPoints++, value));
            }
        }catch(Exception exc){
            Log.d("charFragment", "Open CSV Fail");
        }

        setChart();
        setTable();
        return rootView;
    }

    /* Add data to chart
     * set axis, title of chart
     */
    private void setChart(){
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        line.setFilled(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        //Axis
        LineChartData data = new LineChartData();
        Axis axisX = new Axis().setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Instance");
        axisY.setName("StressLevel");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setLines(lines);

        //Margin of Chart
        LineChartView chart = (LineChartView)rootView.findViewById(R.id.chart);
        chart.setLineChartData(data);
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = maxValue+1;
        v.left = 0;
        v.right = numberOfPoints == 0 ? 0 : numberOfPoints-1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    /* Add data to table
     * set text size, title of table
     */
    private void setTable(){
        TableView<String[]> tableView = (TableView<String[]>) rootView.findViewById(R.id.stressTable);
        //Adapter of header
        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(getActivity(), "Time", "Stress");
        //Adapter of text
        SimpleTableDataAdapter dataAdapter = new SimpleTableDataAdapter(getActivity(), DATA_TO_SHOW);

        //Set size of text and header
        tableView.setHeaderAdapter(simpleTableHeaderAdapter);
        dataAdapter.setTextSize(13);
        simpleTableHeaderAdapter.setTextSize(18);
        tableView.setDataAdapter(dataAdapter);

        //Set color of background
        int colorEvenRows = getResources().getColor(android.R.color.white);
        int colorOddRows = getResources().getColor(android.R.color.darker_gray);
        tableView.setDataRowColoriser(TableDataRowColorizers.alternatingRows(colorEvenRows, colorOddRows));

        //Set height of the table
        android.widget.TableLayout.LayoutParams params = new  android.widget.TableLayout.LayoutParams();
        params.height = 150+92*numberOfPoints;
        tableView.setLayoutParams(params);
    }
}

