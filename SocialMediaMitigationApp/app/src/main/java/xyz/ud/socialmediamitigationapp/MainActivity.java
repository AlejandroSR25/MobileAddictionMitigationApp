package xyz.ud.socialmediamitigationapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirstFragment firstFragment = new FirstFragment();
    ThirdFragment thirdFragment = new ThirdFragment();

    Gson gson = new Gson();

    public static ArrayList<App> apps = new ArrayList<App>();
    public static ArrayList<AppGuardada> appsagregadas = new ArrayList<AppGuardada>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        try {
            cargarAppsAgregadasGuardadas(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException | JsonSyntaxException exception){
            try {
                cargarAppsAgregadasGuardadas(1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        installedApps();
        actualizarApps();
        loadFragment(firstFragment);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationitemSelectedListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();
//        if (appsagregadas.size()>0){
//            appsagregadas.get(0).getStatistics().getStatistics().put(LocalDate.now().toString(), appsagregadas.get(0).getStatistics().getStatistics().get(LocalDate.now().toString()) + 1.0);
//        }
        guardarAppsAgregadas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        guardarAppsAgregadas();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationitemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.firstFragment:
                    loadFragment(firstFragment);
                    return true;
                case R.id.thirdFragment:
                    loadFragment(thirdFragment);
                    return true;
            }
            return false;
        }
    };

    public void cargarAppsAgregadasGuardadas(int n) throws IOException {

            InputStream is = this.openFileInput("appsguardadas.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonString = new String(buffer, "UTF-8");

            if (!jsonString.equals("")){
                if (n==0){
                    appsagregadas = gson.fromJson(jsonString, new TypeToken<ArrayList<AppGuardada>>(){}.getType());
                }else{
                    appsagregadas.add(gson.fromJson(jsonString, AppGuardada.class));
                }

            }

    }

    public void guardarAppsAgregadas(){
        try {
            File file = new File(this.getFilesDir(),"appsguardadas.json");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if(appsagregadas.size() == 1){
                String json = gson.toJson(appsagregadas.get(0));
                bufferedWriter.write(json);
                bufferedWriter.close();
            }else{
                String json = gson.toJson(appsagregadas);
                bufferedWriter.write(json);
                bufferedWriter.close();
            }

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static int indexFinderApp(String pack){
        for (int i=0; i<apps.size(); i++){
            if (apps.get(i).getApp_package().equals(pack)){
                return i;
            }
        }
        return -1;
    }

    public static int indexFinderAppGuardada(String pack){
        for (int i=0; i<appsagregadas.size(); i++){
            if (appsagregadas.get(i).getApp_package().equals(pack)){
                return i;
            }
        }
        return -1;
    }

    public static void actualizarApps(){
        if (appsagregadas.size()>0){
            for (AppGuardada ag: appsagregadas){
                apps.remove(indexFinderApp(ag.getApp_package()));
            }
        }
    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void installedApps(){
        PackageManager pm = getPackageManager();
        List<PackageInfo> packagelist = pm.getInstalledPackages(0);
        for(int i=0; i<packagelist.size(); i++){
            PackageInfo packageInfo= packagelist.get(i);
            if(pm.getLaunchIntentForPackage(packageInfo.applicationInfo.packageName)!=null){
                String appName= packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                String appPackage= packageInfo.applicationInfo.packageName;
                Drawable appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
                apps.add(new App(appName, appPackage, appIcon));

            }
        }

        for (App a: apps){
            a.setName(a.getName().substring(0, 1).toUpperCase() + a.getName().substring(1));
        }

        sortApps();
    }

    public static void sortApps(){
        sort(apps, 0, apps.size()-1);
    }

    private static ArrayList<App> sort(ArrayList<App> ar, int lo, int hi){
        if (lo < hi){
            int splitPoint = partition(ar, lo, hi);
            sort(ar, lo, splitPoint);
            sort(ar, splitPoint +1, hi);
        }
        return ar;
    }

    private static int partition(ArrayList<App> ar, int lo, int hi){
        String pivot = ar.get(lo).getName();
        lo--;
        hi++;
        while (true){
            lo++;
            while (lo<hi && ar.get(lo).getName().compareTo(pivot) < 0){
                lo++;
            }
            hi--;
            while (hi>lo && ar.get(hi).getName().compareTo(pivot) >= 0){
                hi--;
            }
            if (lo<hi){
                swap(ar, lo, hi);
            }else {
                return hi;
            }
        }
    }

    private static ArrayList<App> swap(ArrayList<App> ar, int a, int b){
        App temp = ar.get(a);
        ar.set(a, ar.get(b));
        ar.set(b, temp);
        return ar;
    }
}