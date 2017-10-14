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
import pharaoh.com.bakingapp.R;
import pharaoh.com.bakingapp.data.Models.Ingredient;
import pharaoh.com.bakingapp.data.Models.Step;
import pharaoh.com.bakingapp.ui.Adapters.IngredientsAdapter;
import pharaoh.com.bakingapp.ui.Adapters.StepsAdapter;
import pharaoh.com.bakingapp.ui.FragmentOneListener;
import pharaoh.com.bakingapp.ui.RecyclerViewItemClickListener;


public class StepFragment extends android.app.Fragment {
    FragmentOneListener listener;

    @BindView(R.id.stepsList)
    RecyclerView recycler;
    @BindView(R.id.ingredientList)
    RecyclerView ingredientList;

    ArrayList<Step> steps;
    ArrayList<Ingredient> ingredients;

    int[]trackers;
    int index;

    boolean tablet;
    LinearLayoutManager ingredientsManager,stepsManager;

    int x1,x2;

    public void setFragmentListener(FragmentOneListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.step_fragment, container, false);

        ButterKnife.bind(this, root);

        ingredientsManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        stepsManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        if(savedInstanceState ==null)
        {
            Bundle extra = getArguments();
            ingredients = extra.getParcelableArrayList("ingredients");
            tablet=extra.getBoolean("tablet",false);
            steps = extra.getParcelableArrayList("steps");

            index=0;
        }
        else
        {
            ingredients = savedInstanceState.getParcelableArrayList("ingredients");
            tablet=savedInstanceState.getBoolean("tablet",false);
            steps = savedInstanceState.getParcelableArrayList("steps");
            index=savedInstanceState.getInt("position");

            x1=savedInstanceState.getInt("x1");
            x2=savedInstanceState.getInt("x2");
        }
        trackers=new int[steps.size()];
        if(tablet)
        {
            trackers[index]=1;
        }


        ingredientList.setLayoutManager(ingredientsManager);
        ingredientList.setAdapter(new IngredientsAdapter(getActivity(), ingredients));
        if(x1!=0)
        {
            ingredientList.getLayoutManager().scrollToPosition(x1);
        }


        recycler.setLayoutManager(stepsManager);
        recycler.setAdapter(new StepsAdapter(getActivity(), steps,trackers));
        if(x2!=0)
        {
            recycler.getLayoutManager().scrollToPosition(x2);
        }

        recycler.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), recycler, new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        listener.setStep(position, steps);
                        updateView(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        if(tablet){
            updateView(index);
            listener.setStep(index, steps);
        }

        return root;
    }


    public void updateView(int index)
    {
        this.index=index;
        if(!tablet)
        {
            return;
        }
        trackers=new int[steps.size()];
        try{
            trackers[index]=1;
            ((StepsAdapter)recycler.getAdapter()).trackers=trackers;
            recycler.getAdapter().notifyDataSetChanged();
            recycler.scrollToPosition(index);
        }catch (ArrayIndexOutOfBoundsException E)
        {

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ingredients",ingredients);
        outState.putParcelableArrayList("steps",steps);
        outState.putBoolean("tablet",tablet);

        outState.putInt("position",index);

        outState.putInt("x1",((LinearLayoutManager)ingredientList.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        outState.putInt("x2",((LinearLayoutManager)recycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition());


    }
}


