package xyz.ud.socialmediamitigationapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HashMap<String, Double> datos;
    Cartesian lineChart;
    String type;

    public ChartFragment() {
    }

    public ChartFragment(HashMap<String, Double> datos, String type) {
        this.datos = datos;
        this.type = type;
    }

    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.lineChart);
        lineChart = AnyChart.line();

        ArrayList<DataEntry> entradas = new ArrayList<>();
        List<String> keys = new ArrayList<>(datos.keySet());


        if (this.type.equals("Meses")){
            Collections.sort(keys,  new Comparator<String>() {
                public int compare(String o1, String o2) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
                        return sdf.parse(o1).compareTo(sdf.parse(o2));
                    } catch (ParseException ex) {
                        return o1.compareTo(o2);
                    }
                }
            });
            for (String key: keys){
                entradas.add(new ValueDataEntry(key, datos.get(key)));
            }
            lineChart.data(entradas);
            lineChart.yAxis(0).title("Horas");
            lineChart.xAxis(0).title("Meses");
            lineChart.xScroller(true);


        }
        if (this.type.equals("Dias")){
            Collections.sort(keys);
            for (String key: keys){
                entradas.add(new ValueDataEntry(key, datos.get(key)));
            }
            lineChart.data(entradas);
            lineChart.yAxis(0).title("Horas");
            lineChart.xAxis(0).title("Dias");
            lineChart.xScroller(true);
        }

        anyChartView.setChart(lineChart);

    }


}