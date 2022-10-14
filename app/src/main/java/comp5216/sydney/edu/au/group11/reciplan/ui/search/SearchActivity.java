package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.SearchRecyclerViewAdapter;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchRecycler;
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchRecycler = findViewById(R.id.search_recyclerview);
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(this, new ArrayList<>(), R.layout.item_food);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setAdapter(searchRecyclerViewAdapter);
        doSearch();
    }

    private void doSearch() {
        ApiBuilder builder=new ApiBuilder()
                .Url("/recipes/findByIngredients")//这里填根Url后的部分
                .Params("ingredients",getIntent().getStringExtra("key"))//请求参数 键,值
                .Params("number",10+"")//参数可以有多个
                .Params("apiKey",getResources().getString(R.string.apikey));//参数可以有多个
        ApiClient.getInstance().doGet(builder,new CallBack<DataEntity>(){
            //BeanClass为解析接口数据得到的实体类
            @Override
            public void onResponse(DataEntity data) {
                if (data.getData().size()>0){ //获取接口数据成功
                    //请求体处理
                    searchRecyclerViewAdapter.addAll(data.getData());
                }else {
                    //请求失败处理 Activity.this为当前活动.this
                }
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(SearchActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        }, DataEntity.class, this);
    }
}