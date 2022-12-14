package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentSearchBinding;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;

public class SearchFragment extends Fragment {

    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    EditText input;
    ImageButton search;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentSearchBinding binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle bundle = this.getArguments();
        input = binding.inputSearch;
        search = binding.searchBtn;
        RecyclerView searchRecycler = binding.searchRecyclerview;
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(getActivity(),getContext(), new ArrayList<>(), R.layout.item_food);
        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        searchRecycler.setAdapter(searchRecyclerViewAdapter);
        if(bundle != null) {
            doSearch(bundle);
        }
        search.setOnClickListener(this::normalSearch);
        return root;
    }

    private void normalSearch(View v) {
        String in = input.getText().toString();
        ApiBuilder builder=new ApiBuilder()
                .Url("/recipes/complexSearch")
                .Params("query", in)
                .Params("number",10+"")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().normalGet(builder,new CallBack<DataEntity>(){
            @Override
            public void onResponse(DataEntity data) {
                if (data.getData().size()>0){
                    searchRecyclerViewAdapter.addAll(data.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doSearch(Bundle bundle) {
        ApiBuilder builder=new ApiBuilder()
                .Url("/recipes/findByIngredients")
                .Params("ingredients", bundle.getString("key"))
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
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
            }
        },DataEntity.class,getContext());
    }
}
