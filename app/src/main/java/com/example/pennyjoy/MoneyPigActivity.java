package com.example.pennyjoy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Base64;

import Interfaces.OnGoalRetrievedListener;
import Models.Auth;
import Models.Goal;
import Providers.GoalProvider;
import de.hdodenhof.circleimageview.CircleImageView;

public class MoneyPigActivity extends AppCompatActivity {
    private GoalProvider goalProvider;
    private CircleImageView imageOfGoal;
    private Bitmap image;
    private ArrayList<Goal> goals;
    private Auth auth=Auth.getInstance();
    private TextView lblNameOfGoal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_pig);

        goals=new ArrayList<>();

        goalProvider =new GoalProvider();
        imageOfGoal=findViewById(R.id.imageOfCurrentGoal);
        lblNameOfGoal=findViewById(R.id.lblNameOfGoal);
        goalProvider.getGoalsFromFirebase(auth.getCurrentUser().getKey(), new OnGoalRetrievedListener() {
            @Override
            public void onGoalRetrieved(ArrayList<Goal> goalList) {
                goals.addAll(goalList);

                if(goals != null && !goals.isEmpty()) {

                    Goal currentGoal = goalList.get(0);
                    byte[] imageBytes = new byte[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        imageBytes = Base64.getDecoder().decode(currentGoal.getImage());
                        image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imageOfGoal.setImageBitmap(image);
                    }
                    lblNameOfGoal.setText(currentGoal.getName());

                }

            }
        });

    }

    public void addGoalClicked(View v){
        Intent intent=new Intent(this,AddGoalActivity.class);
        startActivity(intent);
    }
}