package pharaoh.com.bakingapp.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import pharaoh.com.bakingapp.data.Models.Ingredient;
import pharaoh.com.bakingapp.data.Models.WidgetModel;

public class Database extends SQLiteOpenHelper {
    Context context;

    public Database(Context context) {
        super(context, Contract.DATABASE_NAME, null, 1);
        this.context = context;
    }

    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE recipe(id INTEGER PRIMARY KEY AUTOINCREMENT ,name TEXT , widget_id INTEGER )");
        db.execSQL("CREATE TABLE ingredient(id INTEGER PRIMARY KEY AUTOINCREMENT ,content TEXT , measure TEXT , quantity REAL , recipe_id INTEGER )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void insertItem(WidgetModel model, int widgetId) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME, model.recipeTitle);
        values.put(Contract.COLUMN_WIDGET_ID, Integer.valueOf(widgetId));
        long id = database.insert(Contract.RECIPE_TABLE_NAME, null, values);
        ContentValues values2 = new ContentValues();
        for (int i = 0; i < model.ingredients.size(); i++) {
            values2.put(Contract.COLUMN_INGREDIENT, ((Ingredient) model.ingredients.get(i)).getIngredient());
            values2.put(Contract.COLUMN_MEASURE, ((Ingredient) model.ingredients.get(i)).getMeasure());
            values2.put(Contract.COLUMN_QUANTITY, Float.valueOf(((Ingredient) model.ingredients.get(i)).getQuantity()));
            values2.put(Contract.COLUMN_RECIPE_ID, Long.valueOf(id));
            database.insert(Contract.INGREDIENT_TABLE_NAME, null, values2);
        }
    }

    public String getRecipeTitle(int widgetId) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM recipe WHERE widget_id=" + widgetId, null);
        String title = null;
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_NAME));
            } while (cursor.moveToNext());
        }
        return title;
    }

    public ArrayList<Ingredient> getIngredients(int widgetId) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT content,measure,quantity FROM ingredient join recipe on ingredient.recipe_id=recipe.id WHERE widget_id=" + widgetId, null);
        ArrayList<Ingredient> list = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setMeasure(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_MEASURE)));
                ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_INGREDIENT)));
                ingredient.setQuantity(cursor.getFloat(cursor.getColumnIndex(Contract.COLUMN_QUANTITY)));
                list.add(ingredient);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
