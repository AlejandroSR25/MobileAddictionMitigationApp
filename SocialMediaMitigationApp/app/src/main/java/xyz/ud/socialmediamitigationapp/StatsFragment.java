package xyz.ud.socialmediamitigationapp;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class StatsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private AppGuardada appGuardada;
    private TextView titulo;
    private Button buttonMeses;
    private Button buttonDias;
    private Button buttonEliminar;
    private Switch switchServicio;
    private Fragment barChartFragment;

    public StatsFragment() {
        // Required empty public constructor
    }
    public StatsFragment(AppGuardada appGuardada) {
        this.appGuardada = appGuardada;
    }


    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
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
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titulo = view.findViewById(R.id.tituloTextView);
        titulo.setText(appGuardada.getName());

        buttonMeses = view.findViewById(R.id.buttonMeses);
        buttonDias = view.findViewById(R.id.buttonDias);
        buttonEliminar = view.findViewById(R.id.buttonEliminar);
        switchServicio = view.findViewById(R.id.switchServicio);

        buttonMeses.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {buttonClick("Meses");}
        });

        buttonDias.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {buttonClick("Dias");}
        });
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), appGuardada.getName()+"se eliminó correctamente", Toast.LENGTH_LONG).show();
                try {
                    MainActivity.apps.add(new App(appGuardada.getName(), appGuardada.getApp_package(), iconFinder(appGuardada.getApp_package())));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                MainActivity.sortApps();
                MainActivity.appsagregadas.remove(MainActivity.indexFinderAppGuardada(appGuardada.getApp_package()));
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonClick(String type){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(type.equals("Meses")){
            barChartFragment = new ChartFragment(hashXmeses(), "Meses");//Falta hashmap
        }
        if(type.equals("Dias")){
            barChartFragment = new ChartFragment(appGuardada.getStatistics().getStatistics(), "Dias");//Falta hashmap
        }
        transaction.replace(R.id.frame_container, barChartFragment ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, Double> hashXmeses(){

        HashMap<String, Double> estadisticas = appGuardada.getStatistics().getStatistics();
        List<String> keys = new ArrayList<>(estadisticas.keySet());
        List<Double> values = new ArrayList<>(estadisticas.values());
        HashMap<String, Double> resultado = new HashMap<>();

        String month = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        for (int i=0; i<keys.size(); i++) {
            String s = keys.get(i);
            LocalDate ld = LocalDate.parse(s);
            String key = months[ld.getMonthValue()-1].toUpperCase();
            resultado.put(key, resultado.getOrDefault(key, 0.0) + values.get(i));
        }

        return resultado;
    }

    public Drawable iconFinder(String appPackage) throws PackageManager.NameNotFoundException {
        return getContext().getPackageManager().getApplicationIcon(appPackage);
    }

}