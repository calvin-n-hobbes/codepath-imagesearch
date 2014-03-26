package com.codepath.bootcamp.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
    private static final int IMG_FILTER = 1016;
    private static final int RESULT_SIZE = 8; // max 8

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
    ImageResultArrayAdapter imageAdapter;
    ImageFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // set up query EditText
        etQuery = (EditText) findViewById(R.id.etQuery);
        etQuery.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_ENTER == keyCode ) {
                    onImageSearch(v);
                    return true;
                }
                return false;
            }
        });

        // set up grid view
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("kuoj", "onLoadMore, page " + page + ", total count " + totalItemsCount);
                loadImages(etQuery.getText().toString(), page - 1, false);
            }
        });

        // set up search button
        btnSearch = (Button) findViewById(R.id.btnSearch);

        filter = new ImageFilter();

        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);
        gvResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                i.putExtra("result", imageResults.get(position));
                startActivity(i);
            }            
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(View v) {
        // print error message if no Internet connection
        if ( !isNetworkAvailable() ) {
            Toast.makeText(this, R.string.error_network, Toast.LENGTH_LONG).show();
            return;
        }

        String query = etQuery.getText().toString();
        // don't search if the query is empty
        if ( null == query || query.isEmpty() )
            return;

        loadImages(query, 0, true);
    }

    private void loadImages(String query, int startPage, boolean clear) {
        AsyncHttpClient client = new AsyncHttpClient();

        // construct query string
        StringBuilder sb = new StringBuilder("https://ajax.googleapis.com/ajax/services/search/images?rsz=" + RESULT_SIZE);
        sb.append("&start=" + startPage * RESULT_SIZE);
        sb.append("&v=1.0");
        sb.append("&q=" + Uri.encode(query));
        if ( null != filter.getSize() ) sb.append("&imgsz=" + filter.getSize().toString());
        if ( null != filter.getColor() ) sb.append("&imgcolor=" + filter.getColor().toString());
        if ( null != filter.getType() ) sb.append("&imgtype=" + filter.getType().toString());
        if ( null != filter.getSite() ) sb.append("&as_sitesearch=" + Uri.encode(filter.getSite()));
        Log.i("kuoj", "Query URL is " + sb.toString());

        // reset image array if necessary (only when conducting new search)
        if ( clear )
            imageAdapter.clear();

        client.get(sb.toString(),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        JSONArray imageJsonResults = null;
                        try {
                            imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
                            imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                            Log.d("kuoj", imageResults.toString());
                            if ( imageAdapter.isEmpty() )
                                Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });        
    }

    public void onFilterSearch(View v) {
        Intent i = new Intent(this, ImageFilterActivity.class);
        i.putExtra("filter", filter);
        startActivityForResult(i, IMG_FILTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( RESULT_OK == resultCode && IMG_FILTER == requestCode ) {
            filter = (ImageFilter) data.getSerializableExtra("filter");
            Toast.makeText(this, "Filtering images", Toast.LENGTH_SHORT).show();
            imageAdapter.clear();
            // re-run image search
            onImageSearch(null);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        return ( null != netInfo && netInfo.isConnected() );
    }
}
