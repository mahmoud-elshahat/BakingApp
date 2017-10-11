package pharaoh.com.bakingapp.Api;

import java.util.List;

import pharaoh.com.bakingapp.data.Models.Recipe;
import retrofit2.Response;

/**
 * Created by MahmoudAhmed on 10/2/2017.
 */

public interface OnRequestFinishedListener {
    void onFailure(String message);

    void onResponse(Response<List<Recipe>> response);
}
