package com.example.pennyjoy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Interfaces.OnCurrencyConvertRetrievedListener;
import Interfaces.OnGoodsRetrievedListener;
import Models.Auth;
import Models.CategoryList;
import Models.CurrenciesList;
import Models.Currency;
import Models.Goal;
import Models.GoalsList;
import Models.Good;
import Models.GoodsList;
import Models.User;
import Providers.CurrencyProvider;
import Providers.GoalProvider;
import Providers.GoodProvider;
import Providers.UserProvider;
import Providers.UsersCurrencyProvider;

public class SetUserActivity extends AppCompatActivity {
    private Button btnSaveSettings, btnDeleteAccount;
    private LinearLayout btnSetPasswd;
    private EditText editTextName, editTextSurname, editTextSalary, editTextLogin;
    private TextView passwdStars, currencySymbol;
    private   Auth auth =Auth.getInstance();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Spinner dropDownCurrency;
    private CurrenciesList currenciesList;
    private double valueOfCurrency=0;

    private String newName ;
    private String newSurname;
    private String newLogin;
    private String newSalary;
    private ProgressBar progressBar;
    private DecimalFormat decimalFormat=new DecimalFormat("#.###");
    private GoalsList goalsList;

    private GoodsList goodsList=GoodsList.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user);
        btnSaveSettings = findViewById(R.id.save_settings);
        goalsList = GoalsList.getInstance();
        btnDeleteAccount = findViewById(R.id.delete_user_btn);
        btnSetPasswd = findViewById(R.id.buttonPasswd);
        editTextName = findViewById(R.id.etext_name);
        editTextSurname = findViewById(R.id.etext_surname);
        editTextSalary = findViewById(R.id.etext_salary);
        editTextLogin = findViewById(R.id.etext_login);
        passwdStars = findViewById(R.id.text_password_for_edit);
        currencySymbol = findViewById(R.id.symbolOfCurrency);
        progressBar=findViewById(R.id.progressBarInSetUser);

        currencySymbol.setText(auth.getCurrentCurrency().getLabel());
        dropDownCurrency=findViewById(R.id.currencyDropDown);
        currenciesList=CurrenciesList.getInstance();
        currenciesList.init();

        //делаю адаптер для вложенного списка
        ArrayAdapter<Currency> currencyAdapter=new ArrayAdapter<Currency>(getApplicationContext(),
                R.layout.currency_spinner_item,currenciesList.getCurrencies());

        //currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyAdapter.setDropDownViewResource(R.layout.drop_down_item_currency);


        dropDownCurrency.setAdapter(currencyAdapter);
        dropDownCurrency.setSelection(auth.getCurrentCurrency().getId());


        //----------------------


        editTextName.setText(auth.getCurrentUser().getName());
        editTextSurname.setText(auth.getCurrentUser().getSurname());
        editTextLogin.setText(auth.getCurrentUser().getLogin());
        editTextSalary.setText(decimalFormat.format(auth.getCurrentUser().getSalary()));

        String passwd = auth.getCurrentUser().getPasswd();
        String passwordStars = "";
        for(int i = 0; i<passwd.length(); i++){
            passwordStars+="*";
        }
        passwdStars.setText(passwordStars);
    }




    OnCurrencyConvertRetrievedListener listener=new OnCurrencyConvertRetrievedListener() {
        @Override
        public void onRetrieved(double currency) {
            valueOfCurrency = currency;

            Currency currencyFromDropDown = (Currency) dropDownCurrency.getSelectedItem();
            currencyFromDropDown.setUserKey(auth.getCurrentUser().getKey());
            currencyFromDropDown.setKey(auth.getCurrentCurrency().getKey());

            auth.setCurrentCurrency(currencyFromDropDown);
            SharedPreferences mySharedPreferences = getSharedPreferences(String.valueOf(R.string.APP_PREFERENCES), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putInt("idOfCurrency", auth.getCurrentCurrency().getId());
            editor.apply();
            UsersCurrencyProvider usersCurrencyProvider = new UsersCurrencyProvider();
            usersCurrencyProvider.updateCurrency(auth.getCurrentCurrency());
            //_____

            //------

            newSalary = auth.getCurrentUser().getSalary() * valueOfCurrency + "";


            //работаю с товарами
            for (Good g : goodsList) {
                double cost = g.getCost() * valueOfCurrency;
                g.setCost(cost);
                GoodProvider provider = new GoodProvider();
                provider.updateGood(g);
            }
            //работаю с юзером
            User updatedUser = auth.getCurrentUser();
            updatedUser.setAccIsActive(auth.getCurrentUser().getAccIsActive());
            updatedUser.setLogin(newLogin);
            updatedUser.setName(newName);
            updatedUser.setSalary(Double.parseDouble(newSalary));

            updatedUser.setSurname(newSurname);

            UserProvider provider = new UserProvider();
            provider.updateUser(updatedUser);

            auth.setCurrentUser(updatedUser);


            sharedPreferences = getSharedPreferences(String.valueOf(R.string.APP_PREFERENCES), Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("loginOfTheAuthorizedUser", updatedUser.getLogin());

            editor.commit();


            GoalProvider goalProvider = new GoalProvider();

            if (goalsList != null && !goalsList.isEmpty()) {

                Goal currentGoal = auth.getCurrentGoal();


                for (Goal g : goalsList) {
                    if (!g.equals(currentGoal)) {
                        g.setCost(g.getCost() * valueOfCurrency);
                        g.setFullness(g.getFullness() * valueOfCurrency);
                        goalProvider.updateGoal(g);
                    }
                }
                currentGoal.setFullness(currentGoal.getFullness() * valueOfCurrency);
                currentGoal.setCost(currentGoal.getCost() * valueOfCurrency);
                goalProvider.updateGoal(currentGoal);

            }
            progressBar.setVisibility(View.GONE);
            currencySymbol.setText(auth.getCurrentCurrency().getLabel());
            editTextSalary.setText(decimalFormat.format(auth.getCurrentUser().getSalary()));
            Toast.makeText(getApplicationContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();
        }
    };


    public void onClick(View v){
        int id = v.getId();
        if(id == btnSaveSettings.getId()){
            newName = editTextName.getText().toString();
            newSurname = editTextSurname.getText().toString();
            newLogin = editTextLogin.getText().toString();
            newSalary = editTextSalary.getText().toString();

            if((!checkNewName(newName) || !checkNewLogin(newLogin) || !checkNewSalary(newSalary) || !checkNewSurname(newSurname))){
                Snackbar.make(v, "Заполните все поля корректно!", Snackbar.LENGTH_LONG).show();
            }
            else{
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("Внимание!");
                ad.setMessage("Вы уверены, что хотите изменить личные данные?");
                ad.setIcon(R.drawable.ic_baseline_warning_24);
                ad.setCancelable(false);
                ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Операция отменена", Toast.LENGTH_SHORT).show();
                    }
                });
                ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CurrencyProvider currencyProvider = new CurrencyProvider();

                        Currency currencyToConvert=(Currency) dropDownCurrency.getSelectedItem();
                        Currency currentCurrency=auth.getCurrentCurrency();

                        if(currencyToConvert.getId() != currentCurrency.getId() ) {
                            progressBar.setVisibility(View.VISIBLE);
                            currencyProvider.setNewCurrency(listener, currentCurrency.getCode(), currencyToConvert.getCode());
                        }else {
                            progressBar.setVisibility(View.VISIBLE);
                            User updatedUser = auth.getCurrentUser();
                            updatedUser.setAccIsActive(auth.getCurrentUser().getAccIsActive());
                            updatedUser.setLogin(newLogin);
                            DateFormat dateFormat = new SimpleDateFormat("MM");
                            Date date = new Date();
                            updatedUser.setUsersCurrentMonth(Integer.parseInt(dateFormat.format(date)));
                            updatedUser.setName(newName);
                            updatedUser.setSalary(Double.parseDouble(newSalary));
                            updatedUser.setSurname(newSurname);

                            UserProvider userProvider = new UserProvider();
                            userProvider.updateUser(updatedUser);
                            auth.setCurrentUser(updatedUser);


                            sharedPreferences = getSharedPreferences(String.valueOf(R.string.APP_PREFERENCES), Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("loginOfTheAuthorizedUser", updatedUser.getLogin());
                            editor.commit();
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ad.show();
            }
        }


        if(v.getId() == btnDeleteAccount.getId()){
            AlertDialog.Builder ad = new AlertDialog.Builder(v.getContext());
            ad.setTitle("Удаление аккаунта");
            ad.setMessage("Вы уверены, что хотите удалить аккаунт?");
            ad.setIcon(R.drawable.ic_baseline_warning_24);
            ad.setCancelable(false);
            ad.setNegativeButton("Нет, отменить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
               Toast.makeText(getApplicationContext(), "Операция отменена", Toast.LENGTH_SHORT).show();
                }
            });
            ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressBar.setVisibility(View.VISIBLE);
                    User user = auth.getCurrentUser();
                    user.setAccIsActive(false);
                    UserProvider up = new UserProvider();
                    up.updateUser(user);
                    CategoryList categoryList=CategoryList.getInstance();
                    categoryList.getCategoryList().clear();
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            ad.show();
        }

        if(id == btnSetPasswd.getId()){
            Intent intent = new Intent(getApplicationContext(), SetPasswdActivity.class);
            startActivity(intent);
        }
    }


    public boolean checkNewName(String newName){
        if(newName.length()>=2){
            return true;
        }
        editTextName.setError("Минимум 2 символа!");
        return false;
    }

    public boolean checkNewSurname(String newSurname){
        if(newSurname.length()>=2){
            return true;
        }
        editTextSurname.setError("Минимум 2 символа!");
        return false;
    }

    public boolean checkNewLogin(String newLogin){
        if(newLogin.length()>=6){
            return true;
        }
        editTextLogin.setError("Логин должен включать в себя минимум 6 символов");
        return false;
    }

    public boolean checkNewSalary(String newSalary){
        if(!newSalary.isEmpty() && newSalary != null){
            return true;
        }
        editTextSalary.setError("Поле обязательно к заполнению");
        return false;
    }
}