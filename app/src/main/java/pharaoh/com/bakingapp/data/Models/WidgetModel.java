package pharaoh.com.bakingapp.data.Models;

import java.util.ArrayList;

/**
 * Created by MahmoudAhmed on 10/4/2017.
 */

public class WidgetModel {
    public String recipeTitle;
    public ArrayList<Ingredient>ingredients;

    public WidgetModel(String recipeTitle, ArrayList<Ingredient> ingredients) {
        this.recipeTitle = recipeTitle;
        this.ingredients = ingredients;
    }

}
