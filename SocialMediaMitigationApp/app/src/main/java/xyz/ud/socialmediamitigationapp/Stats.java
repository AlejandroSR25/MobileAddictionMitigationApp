package xyz.ud.socialmediamitigationapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Stats {
    //Fecha, Horas
    private HashMap<String, Double> statistics;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Stats() {
        this.statistics = new HashMap<String, Double>();
        this.initHashmap();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initHashmap(){
        for (LocalDate ld: getDatesBetweenUsingJava8(LocalDate.of(2022,1,1), LocalDate.of(2023,1,1))){
            this.statistics.put(ld.toString(), 0.0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void incrementarHoras(String fecha, double d ){
        this.statistics.put(fecha, this.statistics.getOrDefault(fecha, (double) 0) + d);
    }

    public double getHoras(String fecha){
        return this.statistics.get(fecha);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<LocalDate> getDatesBetweenUsingJava8(LocalDate startDate, LocalDate endDate) {

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
    }

    public HashMap<String, Double> getStatistics() {
        return statistics;
    }

    public void setStatistics(HashMap<String, Double> statistics) {
        this.statistics = statistics;
    }
}
