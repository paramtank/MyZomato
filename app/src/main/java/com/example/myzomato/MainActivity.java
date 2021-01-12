package com.example.myzomato;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    SearchView sv;
    TextView textView;
    RecyclerView rv;
    restaurantAdapter restaurantAdapter;
    ArrayList<restaurant_item> restaurant_itemArrayList = new ArrayList<>();
    String en_id,en_type,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sv = (SearchView) findViewById(R.id.search_bar);
        rv = (RecyclerView) findViewById(R.id.rv_restaurants);
        textView=(TextView)findViewById(R.id.textinput_error);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    if (en_id == null && en_type == null){
                        textView.setText("Please Set Location First");
                    }
                    else {
                        textView.setText("");
                        search(s);
                    }

                }catch (Exception e){
                    Log.d("tag", String.valueOf(e));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }



    public void search(String query) {
        String q=query;
        RequestQueue queue = Volley.newRequestQueue(this);

        url = "https://developers.zomato.com/api/v2.1/search?entity_id="+en_id+"&entity_type="+en_type+"&q="+q;

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());

                        try {

                            //JSONObject res= response.getJSONObject("restaurants");

                            JSONArray rests = response.getJSONArray("restaurants");
                            int results_count=response.getInt("results_found");
                            for (int i = 0; i < rests.length(); i++) {
                                JSONObject r_index = rests.getJSONObject(i);

                                JSONObject res = r_index.getJSONObject("restaurant");
                                String name = res.getString("name");
                                JSONObject location = res.getJSONObject("location");
                                String locality = location.getString("locality_verbose");
                                String cuisines = res.getString("cuisines");
                                JSONObject user_rating = res.getJSONObject("user_rating");
                                String rating = user_rating.getString("aggregate_rating");


                                restaurant_itemArrayList.add(new restaurant_item(name, locality, rating, cuisines));
                            }

                            restaurantAdapter = new restaurantAdapter(MainActivity.this, restaurant_itemArrayList);
                            rv.setAdapter(restaurantAdapter);
                            Toast.makeText(MainActivity.this, "Results Found: "+results_count, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d("ERROR", "error => " + error.toString());
                                Toast.makeText(MainActivity.this,
                                        error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "631b8a78a647ab127b30c7b9e92d7625");
                params.put("Accept", "application/json");

                return params;
            }
        };
        queue.add(postRequest);
    }






    public void location(String s) {
        String add=s;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://developers.zomato.com/api/v2.1/locations?query=" + add;
        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());

                        try {

                            //JSONObject res= response.getJSONObject("restaurants");

                            JSONArray rests = response.getJSONArray("location_suggestions");
                            JSONObject locationobj=rests.getJSONObject(0);
                            en_id=locationobj.getString("entity_id");
                            en_type=locationobj.getString("entity_type");

                            Toast.makeText(MainActivity.this, locationobj.getString("title")+" Location Set", Toast.LENGTH_LONG).show();//ja_data.opt(0).toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d("ERROR", "error => " + error.toString());
                                Toast.makeText(MainActivity.this,
                                        error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "631b8a78a647ab127b30c7b9e92d7625");
                params.put("Accept", "application/json");

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_location:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Set Location");


                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String n = input.getText().toString();
                        location(n);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}