package comp5216.sydney.edu.au.reciplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl
            ("https://reciplan-211b0-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText userName = findViewById(R.id.userName);
        final EditText Phone = findViewById(R.id.phone);
        final EditText Password = findViewById(R.id.Password);

        final Button create = findViewById(R.id.create);
        final TextView LoginNowBtn = findViewById(R.id.LoginNowBtn);


        create.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          final String userNameTxt = userName.getText().toString();
                                          final String phoneTxt = Phone.getText().toString();
                                          final String PasswordTxt = Password.getText().toString();


                                          Map<String, Object> user = new HashMap<>();
                                          user.put("userName", userNameTxt);
                                          user.put("Phone", phoneTxt);
                                          user.put("Password", PasswordTxt);

                                          //上传数据
                                          db.collection("user").document(phoneTxt).set(user)
                                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                      @Override
                                                      public void onSuccess(Void avoid) {
                                                          Toast.makeText(Register.this, "Successful", Toast.LENGTH_SHORT).show();
                                                      }

                                                  }).addOnFailureListener(new OnFailureListener() {
                                                      @Override
                                                      public void onFailure(@NonNull Exception e) {
                                                          Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                                                      }
                                                  });
                                          create.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  final String userNameTxt = userName.getText().toString();
                                                  final String phoneTxt = Phone.getText().toString();
                                                  final String PasswordTxt = Password.getText().toString();


                                                  if (userNameTxt.isEmpty() || phoneTxt.isEmpty() || PasswordTxt.isEmpty()) {
                                                      Toast.makeText(Register.this, "Please fill all fields",
                                                              Toast.LENGTH_SHORT).show();

                                                  } else {
                                                      databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                          @Override
                                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                              if (snapshot.hasChild(phoneTxt)) {
                                                                  Toast.makeText(Register.this, "Phone is already registered", Toast.LENGTH_SHORT).show();

                                                              } else {
                                                                  databaseReference.child("user").child(phoneTxt).child("userName").setValue(userNameTxt);
                                                                  databaseReference.child("user").child(phoneTxt).child("Password").setValue(PasswordTxt);
                                                                  Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                                                                  finish();
                                                              }
                                                          }

                                                          @Override
                                                          public void onCancelled(@NonNull DatabaseError error) {

                                                          }
                                                      });
                                                  }
                                              }
                                          });


                LoginNowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                    }
                });
                                      }
        });
    }
}
