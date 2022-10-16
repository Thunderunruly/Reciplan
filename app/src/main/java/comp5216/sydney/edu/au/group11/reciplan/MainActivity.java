package comp5216.sydney.edu.au.group11.reciplan;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.database.FirebaseDatabase;

import comp5216.sydney.edu.au.group11.reciplan.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ImageButton imageButton;
    ImageButton search;
    TextView statusTxt;
    TextView nameTxt;
    private String status = null;
    FirebaseDatabase database;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        status = "Working";
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawerLayout = binding.drawerLayout;
        new AppBarConfiguration.Builder(
                R.id.navigation_like, R.id.info_status, R.id.info_information, R.id.person_log)
                .setOpenableLayout(drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        new AppBarConfiguration.Builder(R.id.app_bar_main).build();
        NavigationUI.setupWithNavController(binding.appBarMain.navView, navController);
        imageButton = binding.appBarMain.titleNav.imageIcon;
        search = binding.appBarMain.titleNav.recipeSearch;
        statusTxt = binding.appBarMain.titleNav.status;
        nameTxt = binding.appBarMain.titleNav.name;
        setStatus();
        listener(drawerLayout, navController);
        search.setOnClickListener(v -> doSelect());
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

    public void updateStatus() {
        // TODO
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    public void showDetail(Bundle bundle) {
        navController.navigate(R.id.recipe_detail,bundle);
    }

    public void doSelect() {
        // TODO
    }
}