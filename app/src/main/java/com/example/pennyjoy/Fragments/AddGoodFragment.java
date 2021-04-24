package com.example.pennyjoy.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pennyjoy.R;
import com.example.pennyjoy.SignInActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import Models.Category;
import Models.CategoryList;
import Models.Good;
import Models.User;
import Providers.GoodProvider;

public class AddGoodFragment extends Fragment {

    private EditText txtNameOfGood, txtCost,txtPurchaseOfPurpose;
    private ImageButton btnAddGood;
    private Spinner dropDownCategory;
    TextView lblCounterOfTextInput;
    private int counterOfSymbols=0;

    private CategoryList categoryList=CategoryList.getInstance();


    private GoodProvider provider= new GoodProvider();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_good, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        txtNameOfGood=view.findViewById(R.id.txtNameOfGood);
        txtCost=view.findViewById(R.id.txtCostOfGood);
        txtPurchaseOfPurpose=view.findViewById(R.id.txtPurchaseOfPurpose);
        btnAddGood=view.findViewById(R.id.btnAddGoodInFrag);
        dropDownCategory=view.findViewById(R.id.categoryDropDown);

        lblCounterOfTextInput= view.findViewById(R.id.counterOfTextInputAddGood);




        //делаю адаптер для вложенного списка
        ArrayAdapter<Category> categoryAdapter=new ArrayAdapter<Category>(view.getContext(),
                android.R.layout.simple_spinner_item,categoryList.getCategories());

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropDownCategory.setAdapter(categoryAdapter);



        btnAddGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtNameOfGood.getText().toString().isEmpty() && !txtCost.getText().toString().isEmpty()
                && !txtPurchaseOfPurpose.getText().toString().isEmpty()
                && !dropDownCategory.getSelectedItem().toString().equals("Не выбрано")
                && Double.parseDouble(txtCost.getText().toString())>0
                && counterOfSymbols == 120){
                    //получая текущего юзера и устонавливаю кей
                    SignInActivity currentUser=new SignInActivity();
                    User user= currentUser.getCurrentUser();

                    String keyOfUser=user.getKey();
                    String nameOfGood= txtNameOfGood.getText().toString();
                    String purchaseOfPurpose= txtPurchaseOfPurpose.getText().toString();
                    double costOfGood= Double.parseDouble(txtCost.getText().toString());
                    Category category=(Category) dropDownCategory.getSelectedItem();

                    Good good=new Good(category.getId(),nameOfGood,costOfGood,purchaseOfPurpose,keyOfUser);
                    GoodProvider provider=new GoodProvider();
                    provider.addGood(good);

                    // private EditText txtNameOfGood, txtCost,txtPurchaseOfPurpose;
                    //    private Spinner dropDownCategory;
                    txtNameOfGood.getText().clear();
                    txtCost.getText().clear();
                    txtPurchaseOfPurpose.getText().clear();
                    dropDownCategory.setSelection(0);

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
    }



}