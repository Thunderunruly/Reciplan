package comp5216.sydney.edu.au.group11.reciplan.ui.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    DatabaseReference databaseReference;
    TextView login;
    EditText name;
    EditText email;
    EditText password;
    EditText confirm;
    Button register;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://reciplan-211b0-default-rtdb.firebaseio.com/");
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        login = binding.LoginNowBtn;
        name = binding.userName;
        email = binding.email;
        password = binding.password;
        confirm = binding.confirmPassword;
        register = binding.registerNow;
        login.setOnClickListener(this::loginNow);
        register.setOnClickListener(this::registerNow);
        return root;
    }

    private void registerNow(View view) {
        final String username = name.getText().toString();
        final String emailAddress = email.getText().toString();
        final String passwordTxt = password.getText().toString();
        final String confirmPassword = confirm.getText().toString();
        if(username.isEmpty() || emailAddress.isEmpty() || passwordTxt.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
        }
        else if(!passwordTxt.equals(confirmPassword)){
            Toast.makeText(getActivity(), "Password are not matching",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(emailAddress)){
                        Toast.makeText(getActivity(),
                                        "Email is already registered",Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        databaseReference.child("users").child(emailAddress).child("Username").setValue(username);
                        databaseReference.child("users").child(emailAddress).child("Password").setValue(passwordTxt);
                        Toast.makeText(getActivity(),"User registered successfully", Toast.LENGTH_SHORT).show();
                        loginNow(view);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void loginNow(View v) {
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }
}
