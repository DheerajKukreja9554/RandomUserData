package com.example.volleypractice;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView userImage;
    TextView userName;
    TextView userAddress;
    TextView userContact;
    ImageButton refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userImage=findViewById(R.id.userimage);
        userName=findViewById(R.id.Name);
        userAddress=findViewById(R.id.address);
        userContact=findViewById(R.id.Contact);
        refresh=findViewById(R.id.refresh);
        getUser();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser();

            }
        });

    }

    public void getUser(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "https://randomuser.me/api/",
                null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array= response.getJSONArray("results");
                    JSONObject results=array.getJSONObject(0);
                    JSONObject name=results.getJSONObject("name");
                    JSONObject address=results.getJSONObject("location");
                    //JSONObject contact=results.getJSONObject("phone");
                    String imageURL=results.getJSONObject("picture").getString("large");

                    userName.setText(name.getString("title")+" "+ name.getString("first")+" " +name.getString("last"));
                    userAddress.setText(address.getJSONObject("street").getString("number")+" "+
                            address.getJSONObject("street").getString("name")+"\n"+
                            address.getString("city")+"\n"+address.getString("state")+"\n"+address.getString("country")
                            +"-"+address.getString("postcode"));
                    userContact.setText(results.getString("phone"));
                    Picasso.with(getApplicationContext())
                            .load(imageURL)
                            .into(userImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}