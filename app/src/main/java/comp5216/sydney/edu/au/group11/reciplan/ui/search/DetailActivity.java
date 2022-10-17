package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataBean;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {


    private ImageView detailImage;
    private TextView detailTitle;

    private DataBean data;
    Gson gson = new Gson();
    private TextView detailUsedIngredients;
    private TextView detailMissedIngredients;
    private TextView detailUnusedIngredients;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        data =gson.fromJson(getIntent().getStringExtra("data"),DataBean.class) ;
        detailImage = findViewById(R.id.detail_image);
        detailTitle = findViewById(R.id.detail_title);
        detailUsedIngredients = findViewById(R.id.detailUsedIngredients);
        detailMissedIngredients = findViewById(R.id.detailMissedIngredients);
        detailUnusedIngredients = findViewById(R.id.detailUnusedIngredients);
        if (data.getUsedIngredients().size()>0){
            String tempS = "";
            for (int i = 0; i < data.getUsedIngredients().size(); i++) {
                tempS = tempS +data.getUsedIngredients().get(i).getOriginal() +(i==data.getUsedIngredients().size()-1?"":"\r\n");
            }
            detailUsedIngredients.setText(tempS);
        }else {
            detailUsedIngredients.setText("No UsedIngredients !");
        }

        if (data.getMissedIngredients().size()>0){
            String tempS = "";
            for (int i = 0; i < data.getMissedIngredients().size(); i++) {
                tempS = tempS +data.getMissedIngredients().get(i).getOriginal() +(i==data.getMissedIngredients().size()-1?"":"\r\n");
            }
            detailMissedIngredients.setText(tempS);
        }else {
            detailMissedIngredients.setText("No MissedIngredients !");
        }

        if (data.getUnusedIngredients().size()>0){
            String tempS = "";
            for (int i = 0; i < data.getUnusedIngredients().size(); i++) {
                tempS = tempS +data.getUnusedIngredients().get(i).getOriginal() +(i==data.getUnusedIngredients().size()-1?"":"\r\n");
            }
            detailUnusedIngredients.setText(tempS);
        }else {
            detailUnusedIngredients.setText("No UnusedIngredients !");
        }
        Glide.with(this)
                .load(data.getImage())
                .into(detailImage);
        detailTitle.setText(data.getTitle());
    }

}
