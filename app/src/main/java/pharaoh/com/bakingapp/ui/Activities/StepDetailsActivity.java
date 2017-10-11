package pharaoh.com.bakingapp.ui.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import pharaoh.com.bakingapp.data.Models.Step;
import pharaoh.com.bakingapp.R;
import pharaoh.com.bakingapp.ui.Fragments.StepDetailsFragment;

public class StepDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Bundle bundle = getIntent().getExtras();

        if(bundle.containsKey("steps"))
        {
            ArrayList<Step> steps = bundle.getParcelableArrayList("steps");
            int index=bundle.getInt("index");
        }

        if(bundle.containsKey("name"))
        {
            getSupportActionBar().setTitle(bundle.getString("name")+" Steps");
        }
        bundle.putBoolean("tablet",false);

        StepDetailsFragment detailsFragment = new StepDetailsFragment();
        detailsFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.detailsFragment, detailsFragment)
                .commit();
    }
}
