package pharaoh.com.bakingapp.ui.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pharaoh.com.bakingapp.ui.Adapters.IngredientsAdapter;
import pharaoh.com.bakingapp.ui.Adapters.StepsAdapter;
import pharaoh.com.bakingapp.data.Models.Ingredient;
import pharaoh.com.bakingapp.data.Models.Step;
import pharaoh.com.bakingapp.R;
import pharaoh.com.bakingapp.ui.RecyclerViewItemClickListener;
import pharaoh.com.bakingapp.ui.FragmentOneListener;


public class StepFragment extends android.app.Fragment {
    FragmentOneListener listener;

    @BindView(R.id.stepsList)
    RecyclerView recycler;
    @BindView(R.id.ingredientList)
    RecyclerView ingredientList;

    ArrayList<Step> steps;
    ArrayList<Ingredient> ingredients;

    int[]trackers;

    boolean tablet;


    public void setFragmentListener(FragmentOneListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.step_fragment, container, false);

        ButterKnife.bind(this, root);
        Bundle extra = getArguments();

        ingredients = extra.getParcelableArrayList("ingredients");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ingredientList.setLayoutManager(layoutManager);
        ingredientList.setAdapter(new IngredientsAdapter(getActivity(), ingredients));

        tablet=extra.getBoolean("tablet",false);


        steps = extra.getParcelableArrayList("steps");
        trackers=new int[steps.size()];
        if(tablet)
        {
            trackers[0]=1;
        }

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new StepsAdapter(getActivity(), steps,trackers));
        recycler.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), recycler, new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        listener.setStep(position, steps);
                        trackers=new int[steps.size()];
                        if(tablet)
                        {
                            trackers[position]=1;
                        }
                        ((StepsAdapter)recycler.getAdapter()).trackers=trackers;
                        recycler.getAdapter().notifyDataSetChanged();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


        return root;
    }


    public void updateView(int index)
    {
        if(!tablet)
        {
            return;
        }
        trackers=new int[steps.size()];
        trackers[index]=1;
        ((StepsAdapter)recycler.getAdapter()).trackers=trackers;
        recycler.getAdapter().notifyDataSetChanged();
        recycler.scrollToPosition(index);
    }
}
