package com.swatisingh0960.github.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.swatisingh0960.github.nytimessearch.R;
import com.swatisingh0960.github.nytimessearch.adapters.ArticleAdapter;
import com.swatisingh0960.github.nytimessearch.extras.EndlessRecyclerViewScrollListener;
import com.swatisingh0960.github.nytimessearch.extras.ItemClickSupport;
import com.swatisingh0960.github.nytimessearch.extras.SearchFilters;
import com.swatisingh0960.github.nytimessearch.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

//import com.swatisingh0960.github.nytimessearch.adapters.ArticleArrayAdapter;

public class SearchActivity extends AppCompatActivity {
    //    EditText etQuery;
//    GridView gvResults;
//    Button btnSearch;
    RecyclerView rvResults;
    SearchView searchView;
    SearchFilters filters;

    ArrayList<Article> articles;
    ArticleAdapter adapter;
    private static final int REQUEST_CODE = 50;

//    private final int EDIT_REQUEST_CODE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

    }

    private void setupViews() {
//        etQuery = (EditText) findViewById(etQuery);
//        gvResults = (GridView) findViewById(gvResults);
//        btnSearch = (Button) findViewById(btnSearch);
        filters = new SearchFilters();
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);
        //Setting the Adapter to the GridView
//        gvResults.setAdapter(adapter);
        rvResults.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);

        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });

        //setup onItemClickListener - grid click
        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                        Article article = articles.get(position);
                        i.putExtra("article", Parcels.wrap(article));
                        startActivity(i);
                    }
                }
//
        );
    }

    public void customLoadMoreDataFromApi(int page) {
        final int currentSize = adapter.getItemCount();
        String query = searchView.getQuery().toString();

        //make a network call
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        //specify parameters
        RequestParams params = new RequestParams();
        params.put("api-key", "4eeefd27cc7340938283108d7f8d7cf6");
        params.put("page", page);
        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    List<Article> moreArticles = Article.fromJSONArray(articleJsonResults);
//                    Log.d("DEBUG",articleJsonResults.toString());
                    articles.addAll(moreArticles);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                    articles.addAll(Article.fromJSONArray(articleJsonResults));
//                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
//                    adapter.notifyDataSetChanged(); delete the notify data set changed

//                    Making change directly to the adapter modifies the underlying data and adds it to the array list
//                    but it saves a step and having a  notified  adapter.
//                    Log.d("DEBUG", articles.toString());

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem search_item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(search_item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchQuery(query);

                if (filters.isUpdated()) {
                    searchQueryFilters(filters);
                } else {
                    searchQuery(query);
                }

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void searchQuery(String query) {
        //make a network call
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        //specify parameters
        RequestParams params = new RequestParams();
        params.put("api-key", "4eeefd27cc7340938283108d7f8d7cf6");
        params.put("page", 0);
        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                articles.clear();

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    //direct change to adapter, changes arraylist
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    //Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void onArticleSearch(View view) {
//        String query = etQuery.getText().toString();
//        Toast.makeText(this,"Searching for " +query,Toast.LENGTH_LONG).show();

    public void onSettingsClick(MenuItem item) {
        Intent i = new Intent(SearchActivity.this, FilterActivity.class);
        i.putExtra("filters", Parcels.wrap(filters));
        startActivityForResult(i, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            filters = Parcels.unwrap(data.getParcelableExtra("filters")); //get and update filters
            if (filters.isUpdated()) {
                if (searchView.getQuery() != null) {
                    searchQueryFilters(filters);
                }
            }
        }
    }

    //to call with filters. only if filters.boolean != false
    public void searchQueryFilters(SearchFilters filters) {
        //make a network call
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        String query = searchView.getQuery().toString();
        //specify parameters
        RequestParams params = new RequestParams();
        params.put("api-key", "4eeefd27cc7340938283108d7f8d7cf6");
        params.put("page", 0);
        params.put("q", query);


        //add news desk
        ArrayList<String> news = filters.getNewsDesks();
        if (news.size() > 0) {
            String newsDeskItemsStr = TextUtils.join(" ", news);
            String newsDeskParamValue = String.format("news_desk:(%s)", newsDeskItemsStr);
            params.put("fq", newsDeskParamValue);
        }

        //add sort order
        if (!filters.getSort().equalsIgnoreCase("None")) {
            Log.d("search", filters.getSort());
            params.put("sort", filters.getSort());
        }

        //add begin date
        if (filters.getBeginDate() != 0) {

            params.put("begin_date", filters.getBeginDate());
            Log.d("search", filters.getBeginDate() + "");
        }

        //add end date
        if (filters.getEndDate() != 0) {
            params.put("end_date", filters.getEndDate());
            Log.d("search", filters.getEndDate() + "");
        }

        //go through all the new filters here

        Log.d("url", url);
        Log.d("url", params.toString());
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TESTING", response.toString());
                JSONArray articleJsonResults = null;
                articles.clear();

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    //direct change to adapter, changes arraylist
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("TESTING", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}

