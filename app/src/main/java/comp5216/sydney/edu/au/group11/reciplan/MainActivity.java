package comp5216.sydney.edu.au.group11.reciplan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.databinding.ActivityMainBinding;
import comp5216.sydney.edu.au.group11.reciplan.ui.search.SearchDialogFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SearchDialogFragment dialogFragment;
    public FirebaseFirestore database;
    public static FirebaseAuth auth;
    ImageButton imageButton;
    ImageButton search;
    TextView statusTxt;
    TextView nameTxt;
    private String status = null;
    NavController navController;
    DrawerLayout drawerLayout;
    boolean login = false;
    Map<String,Object> keys = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialogFragment = new SearchDialogFragment();
        AuthThread thread = new AuthThread();
        database = FirebaseFirestore.getInstance();
        setSupportActionBar(binding.appBarMain.toolbar);
        drawerLayout = binding.drawerLayout;
        new AppBarConfiguration.Builder(
                R.id.navigation_like, R.id.info_status, R.id.info_information, R.id.person_log)
                .setOpenableLayout(drawerLayout)
                .build();
        new AppBarConfiguration.Builder(R.id.app_bar_main).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.appBarMain.navView, navController);
        imageButton = binding.appBarMain.titleNav.imageIcon;
        search = binding.appBarMain.titleNav.recipeSearch;
        statusTxt = binding.appBarMain.titleNav.status;
        nameTxt = binding.appBarMain.titleNav.name;
        listener(drawerLayout, navController);
        search.setOnClickListener(v -> doSelect());
        thread.start();
    }

    public class AuthThread extends Thread {
        @Override
        public void run () {
            Message msg = new Message();
            msg.what = 0;
            while(auth.getCurrentUser() != null) {
                msg.what = 1;
                handler.sendMessage(msg);
            }
            handler.sendMessage(msg);
        }
    }
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                changeLabel(0);
                getInformation();
            } else {
                changeLabel(1);
                setDefault();
            }
        }
    };

    private void setDefault() {
        nameTxt.setText("Please Log In");
    }

    private void getInformation() {
        database.collection("reciplan").document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        keys = task.getResult().getData();
                        String name = null;
                        if (keys != null) {
                            name = "Hello, " + keys.get("username");
                        }
                        nameTxt.setText(name);
                        if(keys.get("status") == null) {
                            status = null;
                        }
                        else {
                            status = (String) keys.get("status");
                        }
                        setStatus();
                    }
                    else {
                        Toast.makeText(MainActivity.this,
                                "Fail to access database, please check your network connection.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changeLabel(int mode) {
        login = mode == 0;
        supportInvalidateOptionsMenu();
    }

    private void listener(DrawerLayout drawerLayout, NavController navController) {
        nameTxt.setOnClickListener(v -> {
            drawerLayout.open();
            NavigationUI.setupWithNavController(binding.barView, navController);
        });
        statusTxt.setOnClickListener(v -> {
            drawerLayout.open();
            NavigationUI.setupWithNavController(binding.barView, navController);
        });
        imageButton.setOnClickListener(v -> {
            drawerLayout.open();
            NavigationUI.setupWithNavController(binding.barView, navController);
        });
    }

    public void setStatus() {
        if(status != null) {
            statusTxt.setText(status);
            statusTxt.setTextColor(this.getColor(R.color.default_color));
            statusTxt.setHovered(true);
        }
        else {
            statusTxt.setText(R.string.user_status);
            statusTxt.setTextColor(this.getColor(R.color.gray));
            statusTxt.setHovered(false);
        }
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.info_menu,menu);
        if(login) {
            menu.findItem(R.id.person_log).setTitle("Log Out").setIcon(R.drawable.ic_logout);
        }
        else {
            menu.findItem(R.id.person_log).setTitle("Log In").setIcon(R.drawable.ic_login);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    public void showDetail(Bundle bundle) {
        navController.navigate(R.id.recipe_detail,bundle);
    }

    public void doSelect() {
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }
}