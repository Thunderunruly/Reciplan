package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;

public class SearchActivity extends AppCompatActivity {

    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RecyclerView searchRecycler = findViewById(R.id.search_recyclerview);
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(this, new ArrayList<>(), R.layout.item_food);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setAdapter(searchRecyclerViewAdapter);
        doSearch();
    }

    private void doSearch() {
        ApiBuilder builder=new ApiBuilder()
                .Url("/recipes/findByIngredients")
                .Params("ingredients",getIntent().getStringExtra("key"))
                .Params("number",10+"")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().doGet(builder,new CallBack<DataEntity>(){
            @Override
            public void onResponse(DataEntity data) {
                if (data.getData().size()>0){
                    searchRecyclerViewAdapter.addAll(data.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(SearchActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        }, DataEntity.class, this);
    }
}