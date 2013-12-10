package edu.berkeley.cs160.tagit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import edu.berkeley.cs160.adapter.BoxArrayAdapter;
import edu.berkeley.cs160.tagit.util.Box;
import edu.berkeley.cs160.tagit.util.BoxContainer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private boolean DEMO = true;

    private ImageButton addBox, search;
    private BoxArrayAdapter adapter;
    private ListView listView;
    private ArrayList<Box> boxes;

    private static int PHOTO_SEARCH_CODE = 600;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        setupActionBar();

        addBox.setOnClickListener(actionBarListener);
        search.setOnClickListener(actionBarListener);

        try {
            boxes = (DEMO) ? BoxContainer.prePopulatedBoxes(getApplicationContext()) : BoxContainer.getInstance().getBoxes();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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

            View headerView = View.inflate(MainActivity.this, R.layout.browse_header, null);
            listView.addHeaderView(headerView);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Box b = boxes.get(position - 1);
                    editBox(b.getID(), "Edit Box");
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

    public void takeSearchPicture(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PHOTO_SEARCH_CODE);
    }

    /**
     * For demo purposes only.  Shows the last box added, regardless
     * of the photo taken.
     */
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);

        BoxContainer container = BoxContainer.getInstance();
        Box b = container.lastBox();
        int id = b.getID();

        editBox(id, "Found Box");
    }

    private void editBox(int id, String title) {
        Intent intent = new Intent(MainActivity.this, EditBoxActivity.class);
        intent.putExtra("box_id", id);
        intent.putExtra("title", title);
        startActivity(intent);
    }

}
