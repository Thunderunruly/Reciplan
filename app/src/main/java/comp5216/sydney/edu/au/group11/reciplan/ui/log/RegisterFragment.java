package comp5216.sydney.edu.au.group11.reciplan.ui.log;

import android.app.ProgressDialog;
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
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    FirebaseFirestore database;
    ProgressDialog dialog;
    private NavHostController controller;
    private final String url = "https://spoonacular.com/recipeimages/716429-312x231.jpg";
    TextView login;
    EditText name;
    EditText email;
    EditText password;
    EditText confirm;
    Button register;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Progress");
        FragmentRegisterBinding binding = FragmentRegisterBinding.inflate(inflater,
                container,
                false);
        controller = (NavHostController) Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
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
        String REGEX = "^(?![\\d]+$)(?![A-Za-z]+$)(?![_*@!#%?$]+$)[\\dA-Za-z_*@!#%?$]{6,16}$";
        if(username.isEmpty() || emailAddress.isEmpty() || passwordTxt.isEmpty()) {
            popMessage("Please fill all fields.");
        }
        else if(passwordTxt.length() < 6) {
            popMessage("Password required at least 6 characters.");
        }
        else if(passwordTxt.length() > 16) {
            popMessage("Password required no more than 16 characters.");
        }
        else if(!isMatched(passwordTxt, REGEX)) {
            popMessage("Password need to contains at least 2 type:\nchar, numbers or special(#, @).");
        }
        else if(!passwordTxt.equals(confirmPassword)){
            popMessage("Password are not matching.");
        }
        else {
            dialog.setMessage("Register...");
            dialog.show();
            MainActivity.auth.createUserWithEmailAndPassword(emailAddress,passwordTxt)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    login(emailAddress,passwordTxt,username);
                                }
                                else{
                                    dialog.dismiss();
                                    popMessage("Fail to register. the Email address might be used.");
                                }
                            });
        }
    }

    public boolean isMatched(String value, String regex) {
        return value != null && value.matches(regex);
    }

    private void login(String emailAddress, String passwordTxt, String username) {
        dialog.setMessage("Login...");
        Map<String,Object> keys = new HashMap<>();
        MainActivity.auth.signInWithEmailAndPassword(emailAddress,passwordTxt)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                keys.put("username",username);
                                keys.put("email",emailAddress);
                                keys.put("image",url);
                                update(keys);
                            }
                            else{
                                dialog.dismiss();
                                popMessage("Fail to Login");
                                controller.popBackStack();
                            }
                        });
    }

    private void update(Map<String, Object> user) {
        database.collection("reciplan")
                .document(Objects.requireNonNull(MainActivity.auth.getCurrentUser()).getUid())
                .set(user)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        dialog.dismiss();
                        controller.navigate(R.id.navigation_home);
                    }
                    else{
                        dialog.dismiss();
                        popMessage("Fail to set database");
                    }
                });
    }

    private void popMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void loginNow(View v) {
        controller.popBackStack();
    }
}
