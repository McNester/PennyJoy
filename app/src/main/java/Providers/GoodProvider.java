package Providers;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Interfaces.OnGoodsRetrievedListener;
import Interfaces.OnUserRetrievedListener;
import Models.Good;
import Models.User;

public class GoodProvider {
    private FirebaseDatabase db;
    private DatabaseReference goods;

    public GoodProvider(){
        db = FirebaseDatabase.getInstance();
        goods = db.getReference().child("Goods");
    }



    public void addGood(Good good){
        DatabaseReference push= goods.push();
        push.setValue(good);
    }

    public void updateUser(User user){ }

    public void deleteUser(User user){ }


    public void getGoodsFromFirebase(String keyOfUser, OnGoodsRetrievedListener listener){
        ArrayList<Good> goodList=new ArrayList<>();
        Query query = goods.orderByChild("userKey").equalTo(keyOfUser);
        //__________________________________________________
        //**************************************************
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot single : snapshot.getChildren()){
                    Good good = (Good)single.getValue(Good.class);
                    goodList.add(good);
                }
                listener.OnRetrieved(goodList);
                goodList.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
