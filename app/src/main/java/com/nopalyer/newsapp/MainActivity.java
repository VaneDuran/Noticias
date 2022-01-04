package com.nopalyer.newsapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.service.autofill.DateTransformation;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nopalyer.newsapp.Clases.Articulos;
import com.nopalyer.newsapp.Clases.Noticia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etQuery;
    Button btnSearch, btd, btt;
    String Desde = "", Hasta ="";
    final String API_KEY = "1d0feb9e489643fcb64d891b52040ae2";
    Adaptador adapter;
    List<Articulos> articles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btd = findViewById(R.id.btd);
        btt = findViewById(R.id.btt);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);

        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final String country = getCountry();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("",country,API_KEY);
            }
        });
        retrieveJson("",country,API_KEY);

        btd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        btd.setText(i + "/" + (String.valueOf(i1).length() <= 1 ? "0" + (i1 + 1) : "" + (i1 + 1)) + "/" + (String.valueOf(i2).length() <= 1 ? "0" + (i2) : "" + (i2)));
                        Desde = i + "-";
                        Desde += (String.valueOf(i1).length() <= 1 ? "0" + (i1 + 1) : "" + (i1 + 1)) + "-";
                        Desde += (String.valueOf(i2).length() <= 1 ? "0" + (i2) : "" + (i2));
                        Toast.makeText(MainActivity.this, Desde, Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        btt.setText(i + "/" + (String.valueOf(i1).length() <= 1 ? "0" + (i1 + 1) : "" + (i1 + 1)) + "/" + (String.valueOf(i2).length() <= 1 ? "0" + (i2) : "" + (i2)));
                        Hasta = i + "-";
                        Hasta += (String.valueOf(i1).length() <= 1 ? "0" + (i1 + 1) : "" + (i1 + 1)) + "-";
                        Hasta += (String.valueOf(i2).length() <= 1 ? "0" + (i2) : "" + (i2));
                        Toast.makeText(MainActivity.this, Hasta, Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
                dpd.show();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etQuery.getText().toString().equals("")){
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson(etQuery.getText().toString(),country,API_KEY);
                        }
                    });
                    retrieveJson(etQuery.getText().toString(),country,API_KEY);
                }else{
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("",country,API_KEY);
                        }
                    });
                    retrieveJson("",country,API_KEY);
                }
            }
        });

    }

    public void retrieveJson(String q ,String country, String apiKey){
        swipeRefreshLayout.setRefreshing(true);
        Call<Noticia> call;
        if (!etQuery.getText().toString().equals("")){
            call= Api.getInstance().getApi().getSpecificData(q, Desde, Hasta,apiKey);
        }else{
            call= Api.getInstance().getApi().getHeadlines(country,apiKey);
        }

        call.enqueue(new Callback<Noticia>() {
            @Override
            public void onResponse(Call<Noticia> call, Response<Noticia> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adaptador(MainActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Noticia> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }



    }




