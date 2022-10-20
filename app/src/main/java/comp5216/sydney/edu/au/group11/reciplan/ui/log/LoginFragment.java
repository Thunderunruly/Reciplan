package comp5216.sydney.edu.au.group11.reciplan.ui.log;

import android.app.AlertDialog;
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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    EditText email;
    EditText password;
    Button login;
    ProgressDialog dialog;
    TextView register;
    NavHostController controller;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controller  = (NavHostController) Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        if (MainActivity.auth.getCurrentUser() != null) {
            askLogout();
        }
        else {
            FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater,
                    container,
                    false);
            View root = binding.getRoot();
            dialog = new ProgressDialog(getContext());
            dialog.setTitle("Progress");
            dialog.setMessage("Login...");
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
                    MainActivity.auth.signOut();
                    popMessage("See you next Time.");
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
            popMessage("Please enter your Email and password");
        }
        else {
            dialog.show();
            MainActivity.auth.signInWithEmailAndPassword(emailTxt,passwordTxt)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            dialog.dismiss();
                            popMessage("Enjoy your Life!");
                            controller.popBackStack();
                        }
                        else {
                            dialog.dismiss();
                            popMessage("Fail to login, please check your account and password.");
                        }
                    });
        }
    }

    private void popMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
