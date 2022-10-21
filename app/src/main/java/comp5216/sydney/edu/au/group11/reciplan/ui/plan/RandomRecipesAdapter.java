package comp5216.sydney.edu.au.group11.reciplan.ui.plan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.materials.Recipe;

public class RandomRecipesAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder> {
    FragmentActivity activity;
    Context context;
    Gson gson = new Gson();
    ArrayList<Recipe> list;
    String[] weekdays = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday", "Sunday"};

    public RandomRecipesAdapter(FragmentActivity activity, Context context, ArrayList<Recipe> list) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        holder.textViewWeekday.setText(weekdays[position]);
        holder.textViewTitle.setText(list.get(position).title);
        holder.textViewTitle.setSelected(true);
        holder.textViewLike.setText(list.get(position).aggregateLikes+"likes");
        System.out.println(list.get(position).aggregateLikes+"likes");
//        holder.textViewServings.setText(Double.toString(list.get(position).pricePerServing));
        holder.textViewServings.setText("    +" +list.get(position).servings+"");
        holder.textViewTime.setText(list.get(position).readyInMinutes+"Mins");
        Picasso.get().load(list.get(position).image).into(holder.imageViewFood);
        holder.randomListContainer.setOnClickListener(v -> clickToShow(position));
    }

    private void clickToShow(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("case","plan");
        bundle.putInt("id",list.get(position).id);
        bundle.putString("title",list.get(position).title);
        bundle.putString("image",list.get(position).image);
        bundle.putString("summary",list.get(position).summary);
        bundle.putString("imageType",list.get(position).imageType);
        bundle.putString("data",gson.toJson(list.get(position)));
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.showDetail(bundle);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


class RandomRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView randomListContainer;
    TextView textViewTitle,textViewServings,textViewLike,textViewTime,textViewWeekday ;
    ImageView imageViewFood;
    public RandomRecipeViewHolder(@NonNull View itemView){
        super(itemView);
        randomListContainer = itemView.findViewById(R.id.random_list_container);
        textViewServings = itemView.findViewById(R.id.textView_servings);
        textViewTitle = itemView.findViewById(R.id.textView_title);
        textViewLike = itemView.findViewById(R.id.textView_likes);
        textViewTime = itemView.findViewById(R.id.textView_time);
        imageViewFood = itemView.findViewById(R.id.imageView_food);
        textViewWeekday = itemView.findViewById(R.id.weekday);
    }
}