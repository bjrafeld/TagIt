package edu.berkeley.cs160.adapter.TabAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import edu.berkeley.cs160.tagit.AddBoxFragment;
import edu.berkeley.cs160.tagit.BoxesFragment;
import edu.berkeley.cs160.tagit.SearchFragment;

/**
 * Created by: Daniel Roth
 * Date: 11/21/13
 * 
 * Last Edited by: Brendan Rafeld
 * Last Edited: 11/22/13
 */
public class TabAdapter extends FragmentStatePagerAdapter {
	
	public static final int BOX_FRAGMENT = 0;
	public static final int ADD_BOX_FRAGMENT = 1;
	public static final int SEARCH_FRAGMENT = 2;

    public TabAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case BOX_FRAGMENT:
                return new BoxesFragment();
            case ADD_BOX_FRAGMENT:
                return new AddBoxFragment();
            case SEARCH_FRAGMENT:
                return new SearchFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
