package edu.berkeley.cs160.adapter.TabAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import edu.berkeley.cs160.tagit.AddBoxFragment;
import edu.berkeley.cs160.tagit.BoxesFragment;
import edu.berkeley.cs160.tagit.SearchFragment;

/**
 * Created by: Daniel Roth
 * Date: 11/21/13
 */
public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new BoxesFragment();
            case 1:
                return new AddBoxFragment();
            case 2:
                return new SearchFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
