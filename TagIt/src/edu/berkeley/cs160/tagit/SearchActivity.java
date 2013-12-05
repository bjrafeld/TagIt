package edu.berkeley.cs160.tagit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import edu.berkeley.cs160.adapter.BoxArrayAdapter;
import edu.berkeley.cs160.tagit.util.Box;
import edu.berkeley.cs160.tagit.util.BoxContainer;

import java.util.ArrayList;

/**
 * Created by: Daniel
 * Date: 11/21/13
 */
public class SearchActivity extends Activity {
    private EditText searchField;
    private ImageButton back;
    private ImageButton search;
    private ListView listView;
    private BoxContainer boxContainer;
    private TextView empty;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupActionBar();

        search = (ImageButton)findViewById(R.id.searchButton);
        searchField = (EditText)findViewById(R.id.search_field);
        listView = (ListView)findViewById(R.id.list_view);
        empty = (TextView)findViewById(R.id.empty);

        boxContainer = BoxContainer.getInstance();

        bindSearchAction();
    }

    private void setupActionBar() {
        ActionBar bar = getActionBar();
        bar.setHomeButtonEnabled(false);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setIcon(null);

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBar = inflator.inflate(R.layout.action_bar, null);

        ((TextView)actionBar.findViewById(R.id.title)).setText("Search Boxes");

        back = (ImageButton)actionBar.findViewById(R.id.left_button);
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });

        bar.setCustomView(actionBar);
    }

    private void bindSearchAction() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Box> results = boxContainer.searchBoxes(searchField.getText().toString());
                if (results == null) {
                    String message = "'" + searchField.getText().toString() + "'";
                    message += "\ndid not match any boxes";
                    empty.setText(message);

                    BoxArrayAdapter adapter = (BoxArrayAdapter)listView.getAdapter();
                    adapter.clear();
                } else {
                    BoxArrayAdapter adapter;
                    adapter = new BoxArrayAdapter(SearchActivity.this, R.layout.box_list_item, results);
                    listView.setAdapter(adapter);
                }
            }
        });
        listView.setEmptyView(empty);
    }

}

