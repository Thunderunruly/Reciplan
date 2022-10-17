package comp5216.sydney.edu.au.group11.reciplan.ui.info;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentInformationBinding;

public class InfoFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private boolean editMode;
    Button edit;
    ImageView imageView;
    TextView name;
    TextView email;
    EditText editName;
    EditText editHeight;
    EditText editWeight;
    Spinner dietary;
    Spinner prefer;
    Spinner level;
    String docId;
    Map<String, Object> keys = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        NavHostController controller = (NavHostController) Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        if(auth.getCurrentUser() == null) {
            Toast.makeText(getActivity(), "Please Log in", Toast.LENGTH_SHORT).show();
            controller.navigate(R.id.person_log);
        }
        comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentInformationBinding binding = FragmentInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        name = binding.basicInfo.userName;
        email = binding.basicInfo.userId;
        imageView = binding.basicInfo.avatarImage;
        edit = binding.editBtn;
        editName = binding.editName;
        editHeight = binding.editHeight;
        editWeight = binding.editWeight;
        dietary = binding.spinnerDietary;
        prefer = binding.spinnerPrefer;
        level = binding.spinnerLevel;
        dietary.setEnabled(false);
        prefer.setEnabled(false);
        level.setEnabled(false);
        getInfo();
        edit.setOnClickListener(v -> switchMode());
        return root;
    }

    private void getInfo() {
        database.collection("reciplan")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot document:task.getResult()) {
                            if(Objects.requireNonNull(auth.getCurrentUser()).getUid().equals(document.get("uid"))) {
                                keys = document.getData();
                                docId = document.getId();
                                Set<String> nameTxt = keys.keySet();
                                if(nameTxt.contains("username")) {
                                    name.setText(Objects.requireNonNull(keys.get("username")).toString());
                                    editName.setText(Objects.requireNonNull(keys.get("username")).toString());
                                }
                                if(nameTxt.contains("email")) {
                                    email.setText(Objects.requireNonNull(keys.get("email")).toString());
                                }
                                if(nameTxt.contains("height")) {
                                    editHeight.setText(Objects.requireNonNull(keys.get("height")).toString());
                                }
                                if(nameTxt.contains("weight")) {
                                    editWeight.setText(Objects.requireNonNull(keys.get("weight")).toString());
                                }
                                String[] a = requireActivity().getResources().getStringArray(R.array.dietary);
                                if(nameTxt.contains("dietary")) {
                                    for(int i = 0; i < a.length; i++){
                                        if(a[i].equals(Objects.requireNonNull(keys.get("dietary")).toString())){
                                            dietary.setSelection(i);
                                        }
                                    }
                                }
                                String[] b = requireActivity().getResources().getStringArray(R.array.preferred);
                                if(nameTxt.contains("preferred")) {
                                    for(int i = 0; i < b.length; i++){
                                        if(b[i].equals(Objects.requireNonNull(keys.get("preferred")).toString())){
                                            prefer.setSelection(i);
                                        }
                                    }
                                }
                                String[] c = requireActivity().getResources().getStringArray(R.array.level);
                                if(nameTxt.contains("level")) {
                                    for(int i = 0; i < c.length; i++){
                                        if(c[i].equals(Objects.requireNonNull(keys.get("level")).toString())){
                                            level.setSelection(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "Please Check Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void switchMode() {
        if(!editMode) {
            edit.setText(R.string.save);
            editName.setEnabled(true);
            editHeight.setEnabled(true);
            editWeight.setEnabled(true);
            dietary.setEnabled(true);
            prefer.setEnabled(true);
            level.setEnabled(true);
            editMode = true;
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            AlertDialog alertDialog = builder.setTitle("Warning: ")
                    .setMessage("Are you sure to save the changes?")
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        updateInfo();
                        edit.setText(R.string.edit);
                        editName.setEnabled(false);
                        editHeight.setEnabled(false);
                        editWeight.setEnabled(false);
                        dietary.setEnabled(false);
                        prefer.setEnabled(false);
                        level.setEnabled(false);
                        editMode = false;
                    })
                    .create();
            alertDialog.show();
        }
    }

    private void updateInfo() {
        keys.put("username",editName.getText().toString());
        keys.put("height",editHeight.getText().toString());
        keys.put("weight",editWeight.getText().toString());
        dietary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String txt = requireActivity().getResources().getStringArray(R.array.dietary)[position];
                keys.put("dietary",txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        prefer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String txt = requireActivity().getResources().getStringArray(R.array.preferred)[position];
                keys.put("preferred",txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String txt = requireActivity().getResources().getStringArray(R.array.level)[position];
                keys.put("level",txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        name.setText(Objects.requireNonNull(keys.get("username")).toString());
        if(docId != null) {
            database.collection("reciplan").document(docId)
                    .set(keys).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Change saved.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Fail to update Information", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
