package edu.berkeley.cs160.tagit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
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
    private ImageButton searchButton;
    private ListView listView;
    private BoxContainer boxContainer;
    private TextView empty;

    private ArrayList<Box> results;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupActionBar();

        searchButton = (ImageButton)findViewById(R.id.searchButton);
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
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchField.getText().toString();
                if (query == null || query.trim().equals("")) {
                    empty.setText("Search string cannot be empty.");
                    return;
                }

                results = boxContainer.searchBoxes(query);
                if (results == null) {
                    String message = "'" + query + "'";
                    message += "\ndid not match any boxes";
                    empty.setText(message);

                    BoxArrayAdapter adapter = (BoxArrayAdapter) listView.getAdapter();
                    if (adapter != null) {
                        adapter.clear();
                    }
                } else {
                    BoxArrayAdapter adapter;
                    adapter = new BoxArrayAdapter(SearchActivity.this, R.layout.box_list_item, results);
                    listView.setAdapter(adapter);
                }
            }
        });
        listView.setEmptyView(empty);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Box b = results.get(position);
                Intent intent = new Intent(SearchActivity.this, EditBoxActivity.class);
                intent.putExtra("box_id", b.getID());
                intent.putExtra("title", "Found Box");
                startActivityForResult(intent, 900);
            }
        });
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    searchButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);

        ((BoxArrayAdapter)listView.getAdapter()).clear();
    }

}

