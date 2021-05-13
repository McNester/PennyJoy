package com.example.pennyjoy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pennyjoy.HistoryActivity;
import com.example.pennyjoy.MainActivity;
import com.example.pennyjoy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Models.Auth;
import Models.Category;
import Models.CategoryList;
import Models.Good;
import Models.User;
import Notifycations.ReminderForTimer;
import Providers.GoodProvider;

public class AddGoodFragment extends Fragment {

    private EditText txtNameOfGood, txtCost,txtPurchaseOfPurpose;
    private ImageButton btnAddGood;
    private FloatingActionButton floatingActionButton;
    private Spinner dropDownCategory;
    private TextView lblCounterOfTextInput,currencyOfCostInAddGood;
    private int counterOfSymbols=0;
    private ProgressBar progressBar;

    private CategoryList categoryList=CategoryList.getInstance();





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_good, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        floatingActionButton = view.findViewById(R.id.historyBtn);
        txtNameOfGood=view.findViewById(R.id.txtNameOfGood);
        txtCost=view.findViewById(R.id.txtCostOfGood);
        txtPurchaseOfPurpose=view.findViewById(R.id.txtPurchaseOfPurpose);
        btnAddGood=view.findViewById(R.id.btnAddGoodInFrag);
        dropDownCategory=view.findViewById(R.id.categoryDropDown);

        progressBar=view.findViewById(R.id.progressBarInAddGood);


        lblCounterOfTextInput= view.findViewById(R.id.counterOfTextInputAddGood);










        //делаю адаптер для вложенного списка
        ArrayAdapter<Category> categoryAdapter=new ArrayAdapter<Category>(view.getContext(),
                android.R.layout.simple_spinner_item,categoryList.getCategoryList());

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropDownCategory.setAdapter(categoryAdapter);
        //----------------------



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });



        btnAddGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtNameOfGood.getText().toString().isEmpty() && !txtCost.getText().toString().isEmpty()
                && !txtPurchaseOfPurpose.getText().toString().trim().isEmpty()
                && Double.parseDouble(txtCost.getText().toString())>0
                && counterOfSymbols >= 90
                        && !txtPurchaseOfPurpose.getText().toString().trim().isEmpty()){
                    //получаю текущего юзера и устанавливаю кей

                    User user=new User();
                    Auth auth=Auth.getInstance();

                    progressBar.setVisibility(View.VISIBLE);
                    String keyOfUser= auth.getCurrentUser().getKey();
                    String nameOfGood= txtNameOfGood.getText().toString();
                    String purchaseOfPurpose= txtPurchaseOfPurpose.getText().toString();
                    double costOfGood= Double.parseDouble(txtCost.getText().toString());
                    Category category=(Category) dropDownCategory.getSelectedItem();

                    Good good=new Good(category.getId(),nameOfGood,costOfGood,purchaseOfPurpose,keyOfUser);
                    GoodProvider provider=new GoodProvider();
                    provider.addGood(good);


                    txtNameOfGood.getText().clear();
                    txtCost.getText().clear();
                    txtPurchaseOfPurpose.getText().clear();
                    dropDownCategory.setSelection(0);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(view.getContext(),"Товар добавлен!",Toast.LENGTH_LONG).show();


                }else{
                    Toast.makeText(view.getContext(),"Заполните все поля!",Toast.LENGTH_LONG).show();
                }

            }
        });

        txtPurchaseOfPurpose.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable s) {
                lblCounterOfTextInput.setText(String.valueOf(s.toString().length()) + "/120" );
                counterOfSymbols=s.toString().length();

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        currencyOfCostInAddGood=view.findViewById(R.id.currencyOfCostInAddGood);
        Auth auth=Auth.getInstance();
        currencyOfCostInAddGood.setText(auth.getCurrentCurrency().getLabel());
    }




}
