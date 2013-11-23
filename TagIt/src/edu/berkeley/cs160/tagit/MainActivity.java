package edu.berkeley.cs160.tagit;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.berkeley.cs160.adapter.TabAdapter.TabAdapter;

import static android.app.ActionBar.Tab;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private ActionBar actionBar;
    private TabData[] tabData = {new TabData("Browse", R.drawable.boxes),
                                 new TabData("Add", R.drawable.box),
                                 new TabData("Search", R.drawable.search)};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        actionBar = getActionBar();

        viewPager.setAdapter(tabAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(false);

        // Generate the tabs
        for (TabData t : tabData) {
            Tab tab = actionBar.newTab();
            tab.setTabListener(this);

            View tabView = getLayoutInflater().inflate(R.layout.tab, null);
            tab.setCustomView(tabView);

            ImageView icon = (ImageView)tabView.findViewById(R.id.tab_icon);
            TextView title = (TextView)tabView.findViewById(R.id.tab_label);

            icon.setImageResource(t.icon);
            title.setText(t.title);

            actionBar.addTab(tab);
        }

        // Listen for page swipes and update the tabs
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int pos) {
                actionBar.setSelectedNavigationItem(pos);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {}

    private class TabData {
        String title;
        int icon;

        public TabData(String t, int i) {
            title = t;
            icon = i;
        }
    }
}
