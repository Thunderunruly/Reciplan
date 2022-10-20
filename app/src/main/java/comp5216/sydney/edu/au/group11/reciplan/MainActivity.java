package comp5216.sydney.edu.au.group11.reciplan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;
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
        NavigationUI.setupWithNavController(binding.barView, navController);
        NavigationView navigationView = (NavigationView) findViewById(R.id.bar_view);
        log = navigationView.getMenu().findItem(R.id.person_log);
        imageButton = binding.appBarMain.titleNav.imageIcon;
        iconNav = navigationView.getHeaderView(0).findViewById(R.id.avatarImage);
        nameTextNav = navigationView.getHeaderView(0).findViewById(R.id.userName);
        emailAddressNav = navigationView.getHeaderView(0).findViewById(R.id.userId);
        search = binding.appBarMain.titleNav.recipeSearch;
        statusTxt = binding.appBarMain.titleNav.status;
        nameTxt = binding.appBarMain.titleNav.name;
        listener(drawerLayout, navController);
        search.setOnClickListener(v -> doSelect());
        setDefault();
        thread.start();
    }

    public class AuthThread extends Thread {
        @Override
        public void run () {
            Message msg = new Message();
            do {
                try {
                    Thread.sleep(1000);
                    msg.what = 0;
                    if(auth.getCurrentUser() != null) {
                        msg.what = 1;
                    }
                    if(msgWhat != msg.what) {
                        handler.sendMessage(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage (Message msg) {
            if(msg.what != msgWhat) {
                msgWhat = msg.what;
            }
            switch (msgWhat) {
                case 0:
                    changeLabel(false);
                    setDefault();
                    break;
                case 1:
                    changeLabel(true);
                    getInformation();
                    break;
            }
        }
    };

    private void setDefault() {
        nameTxt.setText("Please Log In");
        nameTextNav.setText("Please Log In");
        emailAddressNav.setVisibility(View.INVISIBLE);
        iconNav.setBackground(this.getDrawable(R.mipmap.ic_launcher_round));
        imageButton.setBackground(this.getDrawable(R.mipmap.ic_launcher_round));
    }

    private void getInformation() {
        database.collection("reciplan")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        keys = task.getResult().getData();
                        String name = null;
                        String email  = null;
                        if (keys != null) {
                            name = "Hello, " + keys.get("username");
                            email = keys.get("email") + "";
                            nameTxt.setText(name);
                            nameTextNav.setText(name);
                            emailAddressNav.setText(email);
                            emailAddressNav.setVisibility(View.VISIBLE);
                        }
                        setIcon(keys.get("image") + "");
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

    private void setIcon(String image) {
        Handler handler1 = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageButton.setImageBitmap((Bitmap) msg.obj);
                iconNav.setImageBitmap((Bitmap) msg.obj);
            }
        };
        ImageURL.requestImg(handler1,image);
    }

    private void changeLabel(boolean mode) {
        if(mode != login) {
            login = mode;
        }
        if(login) {
            log.setTitle("Log Out");
            log.setIcon(R.drawable.ic_logout);
        }
        else {
            log.setTitle("Log In");
            log.setIcon(R.drawable.ic_login);
        }
        supportInvalidateOptionsMenu();
    }

    private void listener(DrawerLayout drawerLayout, NavController navController) {
        nameTxt.setOnClickListener(v -> {
            drawerLayout.open();
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

    public void showDetail(Bundle bundle) {
        navController.navigate(R.id.recipe_detail,bundle);
    }

    public void doSelect() {
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }
}