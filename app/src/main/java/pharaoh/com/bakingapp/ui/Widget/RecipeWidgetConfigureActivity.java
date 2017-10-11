package pharaoh.com.bakingapp.ui.Widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pharaoh.com.bakingapp.Api.NetworkOperations;
import pharaoh.com.bakingapp.Api.OnRequestFinishedListener;
import pharaoh.com.bakingapp.R;
import pharaoh.com.bakingapp.data.Models.Ingredient;
import pharaoh.com.bakingapp.data.Models.Recipe;
import pharaoh.com.bakingapp.data.Models.WidgetModel;
import pharaoh.com.bakingapp.data.database.Database;
import retrofit2.Response;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends Activity implements OnRequestFinishedListener {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    ProgressDialog dialog;
    private ArrayList<Recipe> recipes;
    private Spinner spinner;


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = RecipeWidgetConfigureActivity.this;

            int position=spinner.getSelectedItemPosition();
            WidgetModel model=new WidgetModel(recipes.get(position).getName(),
                    (ArrayList<Ingredient>) recipes.get(position).getIngredients());

            Database db=new Database(RecipeWidgetConfigureActivity.this);
            db.insertItem(model,mAppWidgetId);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RecipeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public RecipeWidgetConfigureActivity() {
        super();
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);

        spinner= (Spinner) findViewById(R.id.spinner);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        NetworkOperations.getRecipes(this);


    }

    @Override
    public void onFailure(String message) {
        dialog.dismiss();
        Toast.makeText(this, "There is a problem try again later ! or make sure you are connected to internet"
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Response<List<Recipe>> response) {
        dialog.dismiss();
        recipes = (ArrayList<Recipe>) response.body();

        String[]values= new  String [recipes.size()];
        for(int i=0; i < recipes.size();i++)
        {
            values[i]=recipes.get(i).getName();
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }
}

