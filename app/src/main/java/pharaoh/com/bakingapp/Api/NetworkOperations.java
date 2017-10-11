package pharaoh.com.bakingapp.Api;

import java.util.List;

import pharaoh.com.bakingapp.data.Models.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by MahmoudAhmed on 9/29/2017.
 */

public class NetworkOperations {
    private static List<Recipe> recipes;
    final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static void getRecipes(final OnRequestFinishedListener listener) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitEndPoint retrofitEndPoint = retrofit.create(RetrofitEndPoint.class);

        Call<List<Recipe>> call = retrofitEndPoint.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                listener.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}

