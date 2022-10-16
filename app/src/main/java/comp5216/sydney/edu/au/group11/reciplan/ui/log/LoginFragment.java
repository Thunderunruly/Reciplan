package comp5216.sydney.edu.au.group11.reciplan.ui.log;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    DatabaseReference databaseReference;
    EditText email;
    EditText password;
    Button login;
    TextView register;
    NavHostController controller;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controller  = (NavHostController) Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main);
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://reciplan-211b0-default-rtdb.firebaseio.com/");
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        email = binding.email;
        password = binding.password;
        login = binding.login;
        register = binding.register;
        login.setOnClickListener(this::loginListener);
        register.setOnClickListener(v -> controller.navigate(R.id.person_register));
        return root;
    }

    private void loginListener(View v) {
        final String emailTxt = email.getText().toString();
        final String passwordTxt = password.getText().toString();
        if(emailTxt.isEmpty() || passwordTxt.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your Email or password", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(emailTxt)){

                        final  String getPassword = snapshot.child(emailTxt).child("Password").getValue(String.class);

                        if(getPassword.equals(passwordTxt)){
                            Toast.makeText(getContext(),"Successfully Logged in",Toast.LENGTH_SHORT).show();
                            controller.navigate(R.id.navigation_home);
                        }
                        else {
                            Toast.makeText(getContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(),"Wrong Email",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }
}
