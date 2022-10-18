package com.example.firetest;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOEmployee {
    private DatabaseReference databaseReference;
    public DAOEmployee(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Product.class.getSimpleName());
    }
    public Task<Void> add(Product emp){
        return databaseReference.push().setValue(emp);
    }
}
