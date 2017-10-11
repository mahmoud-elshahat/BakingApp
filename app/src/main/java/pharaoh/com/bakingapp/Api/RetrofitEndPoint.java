package pharaoh.com.bakingapp.Api;

import java.util.List;

import pharaoh.com.bakingapp.data.Models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by MahmoudAhmed on 9/29/2017.
 */

public interface RetrofitEndPoint {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

}
