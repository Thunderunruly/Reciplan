package comp5216.sydney.edu.au.myapplication.listeners;

import comp5216.sydney.edu.au.myapplication.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {

    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
