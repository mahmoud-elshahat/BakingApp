package pharaoh.com.bakingapp.ui;

import java.util.ArrayList;

import pharaoh.com.bakingapp.data.Models.Step;

/**
 * Created by MahmoudAhmed on 9/29/2017.
 */

public interface FragmentOneListener {

    void setStep(int index , ArrayList<Step> steps);


    void setCurrent(int index);

}
