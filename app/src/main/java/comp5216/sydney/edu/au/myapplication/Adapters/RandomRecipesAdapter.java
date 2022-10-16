package comp5216.sydney.edu.au.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comp5216.sydney.edu.au.myapplication.Models.Recipe;
import comp5216.sydney.edu.au.myapplication.R;

public class RandomRecipesAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder> {
    Context context;
    List<Recipe> list;

    public RandomRecipesAdapter(Context context, List<Recipe> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        holder.textViewTitle.setText(list.get(position).title);
        holder.textViewTitle.setSelected(true);
        holder.textViewLike.setText(list.get(position).aggregateLikes+"likes");
        holder.textViewServings.setText(list.get(position).servings+"Servings");
        holder.textViewTime.setText(list.get(position).readyInMinutes+"Mins");
        Picasso.get().load(list.get(position).image).into(holder.imageViewFood);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}



class RandomRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView randomListContainer;
    TextView textViewTitle,textViewServings,textViewLike,textViewTime ;
    ImageView imageViewFood;
    public RandomRecipeViewHolder(@NonNull View itemView){
        super(itemView);
        randomListContainer = itemView.findViewById(R.id.random_list_container);
        textViewServings = itemView.findViewById(R.id.textView_servings);
        textViewTitle = itemView.findViewById(R.id.textView_title);
        textViewLike = itemView.findViewById(R.id.textView_likes);
        textViewTime = itemView.findViewById(R.id.textView_time);
        imageViewFood = itemView.findViewById(R.id.imageView_food);


    }
}