package com.example.app.Domain;
import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.app.model.OrderInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

        public class OrderRepository {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference(  "Orders");



        public String sendOrder(OrderInfo orderInfo){
            Log.d(TAG, String.valueOf(orderInfo.getTotalPrice()));
         DatabaseReference orderRef = database.child(orderInfo.getId());
         orderRef.setValue(orderInfo);
         return orderInfo.getId();



        }
    }
