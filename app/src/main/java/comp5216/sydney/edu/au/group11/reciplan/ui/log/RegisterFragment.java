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
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentRegisterBinding;
import comp5216.sydney.edu.au.group11.reciplan.firebase.FireDoc;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;

public class RegisterFragment extends Fragment {
    FireDoc doc;
    private NavHostController controller;
    TextView login;
    EditText name;
    EditText email;
    EditText password;
    EditText confirm;
    Button register;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doc = new FireDoc(getContext());
        FragmentRegisterBinding binding = FragmentRegisterBinding.inflate(inflater, container, false);
        controller = (NavHostController) Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
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
            Toast.makeText(getActivity(), "Please fill all fields.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(passwordTxt.length() < 6) {
            Toast.makeText(getActivity(), "Password required at least 6 characters.", Toast.LENGTH_SHORT).show();
        }
        else if(!passwordTxt.equals(confirmPassword)){
            Toast.makeText(getActivity(), "Password are not matching.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            doc.register(emailAddress, passwordTxt, new CallBack<Object>() {
                @Override
                public void onResponse(Object data) {
                    login(emailAddress,passwordTxt,username);
                }

                @Override
                public void onFail(String msg) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void login(String emailAddress, String passwordTxt, String username) {
        doc.signIn(emailAddress, passwordTxt, username, new CallBack<Object>() {
            @Override
            public void onResponse(Object data) {
                update(doc.getKeys());
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getContext(), "msg", Toast.LENGTH_SHORT).show();
                controller.popBackStack();
            }
        });
    }

    private void update(Map<String, Object> user) {
        doc.addToDatabase(user, new CallBack<Object>() {

            @Override
            public void onResponse(Object data) {
                controller.navigate(R.id.navigation_home);
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginNow(View v) {
        controller.popBackStack();
    }
}
