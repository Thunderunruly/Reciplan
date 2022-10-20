package comp5216.sydney.edu.au.group11.reciplan.ui.plan;

import comp5216.sydney.edu.au.group11.reciplan.ui.plan.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {

    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
