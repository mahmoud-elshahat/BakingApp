package pharaoh.com.bakingapp.ui.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pharaoh.com.bakingapp.R;
import pharaoh.com.bakingapp.ui.Fragments.StepDetailsFragment;

public class StepDetailsActivity extends AppCompatActivity {

    StepDetailsFragment detailsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        if(savedInstanceState == null)
        {
            Bundle bundle = getIntent().getExtras();

            if(bundle.containsKey("name"))
            {
                getSupportActionBar().setTitle(bundle.getString("name")+" Steps");
            }
            bundle.putBoolean("tablet",false);

            detailsFragment = new StepDetailsFragment();
            detailsFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.detailsFragment, detailsFragment)
                    .commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getFragmentManager().putFragment(outState,"fragment",detailsFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        detailsFragment = (StepDetailsFragment) getFragmentManager().getFragment(savedInstanceState,"fragment");
        if(detailsFragment.isAdded())
        {
            return;
        }
        getFragmentManager().beginTransaction()
                .add(R.id.detailsFragment, detailsFragment)
                .commit();
    }

}
