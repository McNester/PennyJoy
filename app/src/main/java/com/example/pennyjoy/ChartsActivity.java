package com.example.pennyjoy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class ChartsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);





        PieChart pieChart = findViewById(R.id.pieChartOfSpendings);

        ArrayList<PieEntry> students = new ArrayList<PieEntry>();
        students.add(new PieEntry(0.13f));
        students.add(new PieEntry(0.26f));

//

        students.add(new PieEntry(0.39f));

        students.add(new PieEntry(0.42f));
        students.add(new PieEntry(0.55f));
        students.add(new PieEntry(0.68f));
        students.add(new PieEntry(0.81f));
        students.add(new PieEntry(0.94f));
        students.add(new PieEntry(1.07f));
        students.add(new PieEntry(1.20f));
        students.add(new PieEntry(1.33f));
        students.add(new PieEntry(1.46f));
        students.add(new PieEntry(1.59f));


        for (PieEntry pieEntry:students) {

            if(pieEntry.getValue() <= 0.42f){
                pieEntry.setIcon(null);
            }

        }

        PieDataSet pieDataSet = new PieDataSet(students,"Students");



        int[] colors=new int[]{R.color.food,R.color.travelings, R.color.transport,R.color.car
        ,R.color.cloth, R.color.debts, R.color.investments, R.color.goals,
                R.color.lodging,R.color.entertainment, R.color.health,R.color.goods
                ,R.color.smth};

        pieDataSet.setColors(colors,getApplicationContext());



        PieData pieData = new PieData(pieDataSet);

        pieDataSet.setDrawValues(false);



        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);

        pieChart.setCenterText("Total Questions 5" );
        pieChart.setCenterTextSize(14f);
        pieChart.setCenterTextColor(Color.WHITE);


        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(60f);
        pieChart.setHoleColor(Color.BLACK);

        pieChart.animateY(500, Easing.EaseInOutCubic);
        pieChart.animate();

    }
}