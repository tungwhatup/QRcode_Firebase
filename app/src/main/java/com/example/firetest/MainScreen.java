package com.example.firetest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    Button btn_scan,btn_scan_loc;
    public String decifer_text = "demo";
    public static boolean checking=false;
    public static boolean checking_loc=false;
    private static List<VariableChangeListener> listeners = new ArrayList<VariableChangeListener>();
    private static List<VariableChangeListener> listeners_loc = new ArrayList<VariableChangeListener>();
    private Button logout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    public static boolean getMyBoolean(){return checking;};

    public static void setMyBoolean(boolean value){
        checking = value;
        for(VariableChangeListener l:listeners){
            l.onVariableChanged();
        }
    }

    public static void setMyBoolean_loc(boolean value){
        checking_loc = value;
        for(VariableChangeListener l:listeners_loc){
            l.onVariableChanged();
        }
    }

    public static void addMyBooleanListener_loc(VariableChangeListener l){
        listeners_loc.add(l);
    }

    public static void addMyBooleanListener(VariableChangeListener l){
        listeners.add(l);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        logout = (Button) findViewById(R.id.btn_logout);
        final TextView tv_code = (TextView) findViewById(R.id.tv_code);
        final TextView tv_name = findViewById(R.id.tv_name);
        final TextView tv_loc = findViewById(R.id.tv_loc);
        final EditText edit_quantity = findViewById(R.id.edit_quantity);
        Button btn_submit = findViewById(R.id.btn_submit);
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan_loc = findViewById(R.id.btn_scan_loc);
        DAOEmployee dao = new DAOEmployee();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Employee");
        userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee employeeProfile = snapshot.getValue(Employee.class);
                if(employeeProfile != null){
                    String fullName = employeeProfile.name;
                    tv_name.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainScreen.this, "Something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });
        btn_submit.setOnClickListener(v->{
            Date currentTime = Calendar.getInstance().getTime();
            Product emp = new Product(tv_name.getText().toString(),tv_code.getText().toString(),currentTime.toString(),
                    edit_quantity.getText().toString(),tv_loc.getText().toString());
            dao.add(emp).addOnSuccessListener(suc->{
                Toast.makeText(this, "Record is sent", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er->{
                Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
        btn_scan_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLoc();
            }
        });
        addMyBooleanListener(new VariableChangeListener() {
            @Override
            public void onVariableChanged() {
                tv_code.setText(decifer_text);
            }
        });
        addMyBooleanListener_loc(new VariableChangeListener() {
            @Override
            public void onVariableChanged() {
                tv_loc.setText(decifer_text);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainScreen.this,MainActivity.class));
            }
        });
    }

    private void scanLoc() {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher_loc.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher_loc = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    decifer_text = result.getContents().toString();
                    setMyBoolean_loc(true);
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    decifer_text = result.getContents().toString();
                    setMyBoolean(true);
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });

}