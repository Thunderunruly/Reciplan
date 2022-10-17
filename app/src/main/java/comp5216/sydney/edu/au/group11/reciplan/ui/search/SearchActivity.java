package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataBean;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVAdapter;

public class SearchActivity extends AppCompatActivity {

    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RecyclerView searchRecycler = findViewById(R.id.search_recyclerview);
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(this, new ArrayList<>(), R.layout.item_food);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setAdapter(searchRecyclerViewAdapter);
        doSearch();

        searchRecyclerViewAdapter.setOnItemClickListener(new XRVAdapter.OnItemClickListener<DataBean>() {
            @Override
            public void onItemClick(View view, int position, DataBean item) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("data", gson.toJson(item));
                startActivity(intent);
            }
        });
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