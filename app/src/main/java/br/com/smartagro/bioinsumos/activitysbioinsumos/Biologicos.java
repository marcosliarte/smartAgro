package br.com.smartagro.bioinsumos.activitysbioinsumos;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.com.smartagro.R;
import br.com.smartagro.bioinsumos.adapter.AdapterBiologicos;
import br.com.smartagro.bioinsumos.adapter.AdapterItemBiologico;
import br.com.smartagro.bioinsumos.api.ApiService;
import br.com.smartagro.bioinsumos.helper.RetrofitConfig;
import br.com.smartagro.bioinsumos.model.Data;
import br.com.smartagro.bioinsumos.model.DataRetornoItem;
import br.com.smartagro.bioinsumos.model.Resultado;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Biologicos extends AppCompatActivity {
    public RecyclerView recyclerView;
    public AdapterItemBiologico adapterItemBiologico;

    public AdapterBiologicos adapterBiologicos;

    private Resultado resultado;

    private List<Data> dataList = new ArrayList<>();
    private Data data;
    private DataRetornoItem dataRetornoItem;
    private Retrofit retrofit;
    private ProgressBar progressBar;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biologicos);
        retrofit = RetrofitConfig.getRetrofit();
        recuperaDados("");
        configuraTollbar();
        TrasProgressBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Pesquisar");

        // Configurar o listener para quando o texto da pesquisa for submetido
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Chamar o método que realizará a pesquisa com o termo passado como argumento
                recuperaDados(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recuperaDados(newText);

                return false;
            }
        });

        return true;
    }

    public void TrasProgressBar(){
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void configuraTollbar() {
        //configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Biologicos");
        setSupportActionBar( toolbar );
    }

    private void recuperaDados(String query){
        ApiService apiService = retrofit.create(ApiService.class);


        apiService.recuperaBiologicos("Bearer 5e5f5869-3889-321c-a40b-bd70ec3c26e7").enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    resultado = response.body();
                    dataList = resultado.data;
                    if (adapterItemBiologico == null) {
                        configuraRecyclerView();
                    }else{
                        configuraRecyclerViewPesquisa();
                    }

                } else {
                    if(mContext != null) {
                        Toast.makeText(mContext, "Erro ao recuperar dados", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {
                if(mContext != null) {
                    Toast.makeText(mContext, "Falha na comunicação com o servidor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        apiService.recuperaBiologicosPesquisa(query, "Bearer 5e5f5869-3889-321c-a40b-bd70ec3c26e7").enqueue(new Callback<DataRetornoItem>() {
            @Override
            public void onResponse(Call<DataRetornoItem> call, Response<DataRetornoItem> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    dataRetornoItem = response.body();
                    data = dataRetornoItem.data;
                    Log.d("resultado", "resultado" + data);

                    configuraRecyclerViewPesquisa();

                }
            }
            @Override
            public void onFailure(Call<DataRetornoItem> call, Throwable t) {

            }
        });
    }

    public void configuraRecyclerView() {

        adapterBiologicos = new AdapterBiologicos(dataList);
        recyclerView = findViewById(R.id.biologicos_recycler_view);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapterBiologicos);
        } else {
            Log.e(TAG, "recyclerView é nulo");
        }
    }

    public void configuraRecyclerViewPesquisa() {

        adapterItemBiologico = new AdapterItemBiologico(data);
        recyclerView = findViewById(R.id.biologicos_recycler_view);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapterItemBiologico);
        } else {
            Log.e(TAG, "recyclerView é nulo");
        }
    }
}