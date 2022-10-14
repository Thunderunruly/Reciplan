package comp5216.sydney.edu.au.group11.reciplan;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import comp5216.sydney.edu.au.group11.reciplan.databinding.ActivityMainBinding;
import comp5216.sydney.edu.au.group11.reciplan.ui.detail.DetailFragment;
import comp5216.sydney.edu.au.group11.reciplan.ui.search.MyDialogFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private MyDialogFragment dialogFragment;
    private ActivityMainBinding binding;
    ImageButton imageButton;
    ImageButton search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO firebase
        dialogFragment = new MyDialogFragment();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawerLayout = binding.drawerLayout;
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_like,R.id.info_status,R.id.info_information,R.id.person_log)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.app_bar_main).build();
        NavigationUI.setupWithNavController(binding.appBarMain.navView, navController);
        imageButton = (ImageButton) binding.appBarMain.titleNav.imageIcon;
        search = (ImageButton) binding.appBarMain.titleNav.recipeSearch;
        imageButton.setOnClickListener(v -> {
            drawerLayout.open();
            NavigationUI.setupWithNavController(binding.barView, navController);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    public void showDetail(Fragment frag, int id) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        fragment.setArguments(bundle);
        FragmentManager manager = frag.getParentFragmentManager();
        manager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }
    public void doSelect(View view) {
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }
}