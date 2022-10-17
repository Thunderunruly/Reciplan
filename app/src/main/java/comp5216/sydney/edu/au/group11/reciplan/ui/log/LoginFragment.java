package comp5216.sydney.edu.au.group11.reciplan.ui.log;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    EditText email;
    EditText password;
    Button login;
    TextView register;
    NavHostController controller;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        auth =FirebaseAuth.getInstance();
        controller  = (NavHostController) Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_activity_main);
        if (auth.getCurrentUser() != null) {
            askLogout();
        }
        else {
            comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            email = binding.email;
            password = binding.password;
            login = binding.login;
            register = binding.register;
            login.setOnClickListener(this::loginListener);
            register.setOnClickListener(v -> controller.navigate(R.id.person_register));
            return root;
        }
        return null;
    }

    private void askLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder
                .setTitle("Log out")
                .setMessage("Do you want to log out? ")
                .setIcon(AppCompatResources.getDrawable(requireContext(),R.mipmap.ic_launcher_round))
                .setPositiveButton("YES", (dialog1, which) -> {
                    auth.signOut();
                    Toast.makeText(getContext(), "See you next Time.", Toast.LENGTH_SHORT).show();
                    controller.popBackStack();
                })
                .setNegativeButton("No", (dialog12, which) -> controller.popBackStack())
                .create();
        dialog.show();
    }

    private void loginListener(View v) {
        final String emailTxt = email.getText().toString();
        final String passwordTxt = password.getText().toString();
        if(emailTxt.isEmpty() || passwordTxt.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your Email or password", Toast.LENGTH_SHORT).show();
        }
        else {
            auth.signInWithEmailAndPassword(emailTxt,passwordTxt)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Enjoy your Life!", Toast.LENGTH_SHORT).show();
                            controller.navigate(R.id.navigation_home);
                        }
                        else {
                            Toast.makeText(getActivity(), "Fail to login, please check your account and password.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
