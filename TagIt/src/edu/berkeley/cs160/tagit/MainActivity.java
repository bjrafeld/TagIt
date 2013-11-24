package edu.berkeley.cs160.tagit;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends FragmentActivity {
    private ImageButton addBox, search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        setupActionBar();

        addBox.setOnClickListener(actionBarListener);
        search.setOnClickListener(actionBarListener);
	}

    private View.OnClickListener actionBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            if (view == addBox) {
                intent = new Intent(MainActivity.this, AddBoxActivity.class);
            } else if (view == search) {
                intent = new Intent(MainActivity.this, SearchActivity.class);
            } else {
                return;
            }

            startActivity(intent);
        }
    };

    private void setupActionBar() {
        ActionBar bar = getActionBar();
        bar.setHomeButtonEnabled(false);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setIcon(null);

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar, null);

        addBox = (ImageButton)v.findViewById(R.id.add_box);
        search = (ImageButton)v.findViewById(R.id.search);

        bar.setCustomView(v);
    }

}
