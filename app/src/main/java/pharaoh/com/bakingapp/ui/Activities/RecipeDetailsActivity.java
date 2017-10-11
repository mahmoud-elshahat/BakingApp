package pharaoh.com.bakingapp.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.ArrayList;

import pharaoh.com.bakingapp.data.Models.Step;
import pharaoh.com.bakingapp.R;
import pharaoh.com.bakingapp.ui.FragmentOneListener;
import pharaoh.com.bakingapp.ui.Fragments.StepDetailsFragment;
import pharaoh.com.bakingapp.ui.Fragments.StepFragment;

public class RecipeDetailsActivity extends AppCompatActivity implements FragmentOneListener {

    FrameLayout detailFragment;
    boolean Tablet;
    private ArrayList<Step> steps;

    String name;

    StepDetailsFragment detailsFragment;

    StepFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        detailFragment= (FrameLayout) findViewById(R.id.fragmentTwo);

        Tablet=true;
        Bundle extras=getIntent().getBundleExtra("bundle");
        name=extras.getString("recipe_name");
        getSupportActionBar().setTitle(name);
        steps=extras.getParcelableArrayList("steps");
        extras.putBoolean("tablet",(detailFragment!=null));

        fragment=new StepFragment();
        fragment.setFragmentListener(this);
        fragment.setArguments(extras);
        getFragmentManager().beginTransaction().add(R.id.fragmentOne, fragment).commit();

        //checking if screen size greater than 600dp
        if(detailFragment == null)
        {
            Tablet=false;
        }
        else {
            this.setStep(0,steps);
        }



    }

    @Override
    public void setStep(int index , ArrayList<Step> steps) {

        if (!Tablet) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("steps", steps);
            intent.putExtra("current",index);
            intent.putExtra("name",name);
            startActivity(intent);
        } else {
            detailsFragment = new StepDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("steps", steps);
            detailsFragment.setFragmentListener(this);
            bundle.putInt("current",index);
            bundle.putBoolean("tablet",true);
            detailsFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragmentTwo, detailsFragment).commit();
        }
    }

    @Override
    public void setCurrent(int index) {
        if(Tablet)
        {
            fragment.updateView(index);
        }
    }

}
