package comp5216.sydney.edu.au.group11.reciplan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import comp5216.sydney.edu.au.group11.reciplan.databinding.ActivityMainBinding;
import comp5216.sydney.edu.au.group11.reciplan.ui.search.SearchDialogFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SearchDialogFragment dialogFragment;
    public FirebaseFirestore database;
    public static FirebaseAuth auth;
    ImageButton imageButton;
    ImageView iconNav;
    TextView nameTextNav;
    TextView emailAddressNav;
    ImageButton search;
    TextView statusTxt;
    TextView nameTxt;
    MenuItem log;
    int msgWhat = 0;
    private String status = null;
    NavController navController;
    DrawerLayout drawerLayout;
    public AuthThread thread;
    Map<String,Object> keys = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialogFragment = new SearchDialogFragment();
        thread = new AuthThread();
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
        NavigationUI.setupWithNavController(binding.barView, navController);
        NavigationView navigationView = findViewById(R.id.bar_view);
        log = navigationView.getMenu().findItem(R.id.person_log);
        imageButton = binding.appBarMain.titleNav.imageIcon;
        iconNav = navigationView.getHeaderView(0).findViewById(R.id.avatarImage);
        nameTextNav = navigationView.getHeaderView(0).findViewById(R.id.userName);
        emailAddressNav = navigationView.getHeaderView(0).findViewById(R.id.userId);
        search = binding.appBarMain.titleNav.recipeSearch;
        statusTxt = binding.appBarMain.titleNav.status;
        nameTxt = binding.appBarMain.titleNav.name;
        listener(drawerLayout);
        search.setOnClickListener(v -> doSelect());
        setDefault();
        thread.start();
    }

    public class AuthThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(2000);
                    Message msg = new Message();
                    if(auth.getCurrentUser() != null) {
                        if(handler.obtainMessage().what == 1) {
                            msg.what = 2;
                        }
                        else {
                            msg.what = 1;
                        }
                    }
                    else {
                        msg.what = 0;
                    }
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            if(msg.what != msgWhat) {
                msgWhat = msg.what;
            }
            switch (msgWhat) {
                case 0:
                    changeLabel();
                    setDefault();
                    break;
                case 1:
                case 2:
                    changeLabel();
                    getInformation();
                    break;
            }
        }
    };

    private void setDefault() {
        nameTxt.setText(R.string.user_name);
        nameTextNav.setText(R.string.user_name);
        emailAddressNav.setVisibility(View.INVISIBLE);
        iconNav.setBackground(ResourcesCompat
                .getDrawable(getResources(),R.mipmap.ic_launcher_round,null));
        imageButton.setBackground(ResourcesCompat
                .getDrawable(getResources(),R.mipmap.ic_launcher_round,null));
    }

    private void getInformation() {
        database.collection("reciplan")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        keys = task.getResult().getData();
                        String name;
                        String email;
                        if (keys != null) {
                            name = "Hello, " + keys.get("username");
                            email = keys.get("email") + "";
                            nameTxt.setText(name);
                            nameTextNav.setText(name);
                            emailAddressNav.setText(email);
                            emailAddressNav.setVisibility(View.VISIBLE);
                        }
                        if((keys != null ? keys.get("status") : null) == null) {
                            status = null;
                        }
                        else {
                            status = (String) keys.get("status");
                        }
                        setStatus();
                    }
                    else {
                        Toast.makeText(MainActivity.this,
                                "Fail to connect.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changeLabel() {
        if(auth.getCurrentUser() != null) {
            log.setTitle("Log Out");
            log.setIcon(R.drawable.ic_logout);
        }
        else {
            log.setTitle("Log In");
            log.setIcon(R.drawable.ic_login);
        }
        supportInvalidateOptionsMenu();
    }

    private void listener(DrawerLayout drawerLayout) {
        nameTxt.setOnClickListener(v -> drawerLayout.open());
        statusTxt.setOnClickListener(v -> drawerLayout.open());
        imageButton.setOnClickListener(v -> drawerLayout.open());
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

    public void showDetail(Bundle bundle) {
        navController.navigate(R.id.recipe_detail,bundle);
    }

    public void doSelect() {
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }
}