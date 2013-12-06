package edu.berkeley.cs160.tagit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import edu.berkeley.cs160.adapter.BoxArrayAdapter;
import edu.berkeley.cs160.tagit.util.Box;
import edu.berkeley.cs160.tagit.util.BoxContainer;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ImageButton addBox, search;
    private BoxArrayAdapter adapter;
    private ListView listView;
    private ArrayList<Box> boxes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        setupActionBar();

        addBox.setOnClickListener(actionBarListener);
        search.setOnClickListener(actionBarListener);

        boxes = BoxContainer.getInstance().getBoxes();

        adapter = new BoxArrayAdapter(this, R.layout.box_list_item, boxes);
        listView.setAdapter(adapter);
	}

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void setupListView() {
        if (listView == null) {
            listView = (ListView)findViewById(R.id.list_view);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Box b = boxes.get(position);
                    Intent intent = new Intent(MainActivity.this, EditBoxActivity.class);
                    intent.putExtra("box_id", b.getID());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        setupListView();
        View empty = findViewById(R.id.empty);
        listView.setEmptyView(empty);
    }


    private View.OnClickListener actionBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            if (view == addBox) {
                intent = new Intent(MainActivity.this, EditBoxActivity.class);
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

        addBox = (ImageButton)v.findViewById(R.id.right_button);
        addBox.setImageResource(R.drawable.new_box);

        search = (ImageButton)v.findViewById(R.id.left_button);
        search.setImageResource(R.drawable.search);

        bar.setCustomView(v);
    }

    public void updateList() {
        adapter.notifyDataSetChanged();
    }

}
