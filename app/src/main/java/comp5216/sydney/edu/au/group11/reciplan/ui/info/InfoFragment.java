package comp5216.sydney.edu.au.group11.reciplan.ui.info;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentInformationBinding;

public class InfoFragment extends Fragment {

    private FragmentInformationBinding binding;
    private boolean editMode;
    Button edit;
    TextView name;
    EditText editName;
    EditText editHeight;
    EditText editWeight;
    Spinner dietary;
    Spinner prefer;
    Spinner level;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO get information from firebase
        binding = FragmentInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
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
        // TODO information display
        edit.setOnClickListener(v -> switchMode());
        return root;
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
                        // TODO save information to firebase
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
}
