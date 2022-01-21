package com.example.app_dota;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.app_dota.interfaces.HeroApi;
import com.example.app_dota.interfaces.UserApi;
import com.example.app_dota.model.Heroes;
import com.example.app_dota.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreMenuActivity extends AppCompatActivity {

    private RecyclerView reciclerViewHeroes;
    private HeroesListAdapter heroesAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_menu);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        //ToobarView();
        reciclerViewHeroes = (RecyclerView)findViewById(R.id.recicler_dota_heroes);
        heroesAdapter = new HeroesListAdapter(this);
        reciclerViewHeroes.setAdapter(heroesAdapter);
        reciclerViewHeroes.setLayoutManager(new LinearLayoutManager(this));
        GetHeroesDota();

    }
    private void ToobarView(){
        Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StoreMenuActivity.this,"aea",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void GetHeroesDota(){
        List<Heroes> listaHeroes;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.opendota.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HeroApi service = retrofit.create(HeroApi.class);

        Call<List<Heroes>> call = service.getHeroes();
        call.enqueue(new Callback<List<Heroes>>() {
            @Override
            public void onResponse(Call<List<Heroes>> call, Response<List<Heroes>> response) {

                if(response.isSuccessful()){
                    List<Heroes> heroesList = response.body();
                   heroesAdapter.adicionarLista(heroesList);

                }
            }

            @Override
            public void onFailure(Call<List<Heroes>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                action(R.string.action_add);
                Intent intent = new Intent(StoreMenuActivity.this, UploadImg.class);
                startActivity(intent);
                finish();
            case R.id.action_settings:
                action(R.string.action_settings);
                return true;
            case R.id.action_help:
                action(R.string.action_help);
                return true;
            case R.id.action_about:
                action(R.string.action_about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void action(int resid) {
        Toast.makeText(this, getText(resid), Toast.LENGTH_SHORT).show();
    }






   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        //menu.findItem(R.id.action_add).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }*/



}