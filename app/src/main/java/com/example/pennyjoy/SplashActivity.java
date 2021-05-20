package com.example.pennyjoy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

import Interfaces.OnGoalRetrievedListener;
import Interfaces.OnGoodsRetrievedListener;
import Interfaces.OnUserRetrievedListener;
import Interfaces.OnUsersCurrencyRetrievedListener;
import Models.Auth;
import Models.CategoryList;
import Models.CurrenciesList;
import Models.Currency;
import Models.Goal;
import Models.GoalsList;
import Models.Good;
import Models.GoodsList;
import Models.User;
import Providers.GoalProvider;
import Providers.GoodProvider;
import Providers.UserProvider;
import Providers.UsersCurrencyProvider;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Auth auth=Auth.getInstance();
    private GoalsList goalsList = GoalsList.getInstance();
    private GoodsList goodsList=GoodsList.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isUserExist();
    }







    //функция для проверки наличия юзера
    public void isUserExist(){

        sharedPreferences=getSharedPreferences(String.valueOf(R.string.APP_PREFERENCES),Context.MODE_PRIVATE);


        String login=sharedPreferences.getString("loginOfTheAuthorizedUser",null);

        if(login!=null){
            UserProvider provider=new UserProvider();
            GoalProvider goalProvider = new GoalProvider();
            OnUserRetrievedListener listener = new OnUserRetrievedListener() {
                @Override
                public void OnRetrieved(User user) {
                    if(login.equals(user.getLogin())&& user.getAccIsActive()==true){

                        //здесь вызываю маин при совпадении логина в сп с логином из бд
                        //и настраиваю флаги


                        auth.setCurrentUser(user);

                        OnGoalRetrievedListener listener1 = new OnGoalRetrievedListener() {
                            @Override
                            public void onGoalRetrieved(ArrayList<Goal> goalList) {
                                if(goalList !=null && !goalList.isEmpty()) {
                                    //сохраняю текущую цель в auth, чтобы она быстрее отображалась
                                    auth.setCurrentGoal(goalList.get(0));
                                    goalsList.addAll(goalList);

                                }

                            }
                        };
                        goalProvider.getGoalsFromFirebase(user.getKey(),listener1);

                        //заполняем лист с товарами
                        GoodProvider goodProvider=new GoodProvider();

                        goodProvider.getGoodsFromFirebase(user.getKey(), new OnGoodsRetrievedListener() {
                            @Override
                            public void OnRetrieved(ArrayList<Good> goods) {
                                if(goods != null && !goods.isEmpty()){
                                    goodsList.addAll(goods);
                                }
                            }
                        });


                        CurrenciesList currenciesList=CurrenciesList.getInstance();
                        currenciesList.init();
                        SharedPreferences mySharedPreferences = getSharedPreferences(String.valueOf(R.string.APP_PREFERENCES), Context.MODE_PRIVATE);
                        int idOfCurrency=mySharedPreferences.getInt("idOfCurrency",-1);
                        if(idOfCurrency==-1) {
                            auth.setCurrentCurrency(currenciesList.getCurrencies().get(0));
                        }

                        else{
                            UsersCurrencyProvider usersCurrencyProvider=new UsersCurrencyProvider();
                            usersCurrencyProvider.getUsersCurrencyFromFirebase(auth.getCurrentUser().getKey(), new OnUsersCurrencyRetrievedListener() {
                                @Override
                                public void OnRetrieved(Currency currency) {
                                    auth.setCurrentCurrency(currency);
                                    CategoryList categoryList=CategoryList.getInstance();
                                    categoryList.init();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }


                    }
                    else{
                        sharedPreferences.edit().remove("loginOfTheAuthorizedUser").commit();
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
            };
            provider.getUserFromFirebaseByLogin(login,listener);
        }

        else{
            sharedPreferences.edit().remove("loginOfTheAuthorizedUser").commit();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            return;
        }
    }


}