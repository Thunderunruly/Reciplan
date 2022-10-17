package comp5216.sydney.edu.au.group11.reciplan.net;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    String HOST = "https://api.spoonacular.com";

    @GET("/recipes/findByIngredients")
    Call<ResponseBody> getFoodData( @Query("ingredients") String ingredients
            ,@Query("number") String number
            ,@Query("apiKey") String apiKey);

    @GET()
    Call<ResponseBody> get(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);

}
