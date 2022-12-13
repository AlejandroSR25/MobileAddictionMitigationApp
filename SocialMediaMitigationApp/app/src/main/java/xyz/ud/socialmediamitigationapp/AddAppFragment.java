package xyz.ud.socialmediamitigationapp;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAppFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<ListElement> elements;
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAppFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAppFragment newInstance(String param1, String param2) {
        AddAppFragment fragment = new AddAppFragment();
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
        return inflater.inflate(R.layout.fragment_add_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            initApps();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ListAdapter listAdapterApps = new ListAdapter(elements, getContext(), new ListAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(ListElement item) {
                itemClick(item);
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView_appActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapterApps);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void itemClick(ListElement item){
        AppGuardada ag = new AppGuardada(item.getAppElement().getName(), item.getAppElement().getApp_package(), new Stats());
        MainActivity.appsagregadas.add(ag);
        MainActivity.apps.remove(MainActivity.indexFinderApp(item.appElement.getApp_package()));
        Toast.makeText(getContext(), item.getAppElement().getName()+" se agregó correctamente", Toast.LENGTH_SHORT).show();
        getFragmentManager().popBackStackImmediate();
    }

    public void initApps() throws PackageManager.NameNotFoundException {
        elements = new ArrayList<>();
        for (App a: MainActivity.apps){
            elements.add(new ListElement(a));
        }
    }
}