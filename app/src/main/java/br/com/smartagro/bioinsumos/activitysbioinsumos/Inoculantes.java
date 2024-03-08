package br.com.smartagro.bioinsumos.activitysbioinsumos;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import br.com.smartagro.R;
import br.com.smartagro.bioinsumos.activitysbioinsumos.*;


import java.util.ArrayList;
import java.util.List;

import br.com.smartagro.bioinsumos.adapter.AdapterInoculantes;
import br.com.smartagro.bioinsumos.adapter.AdapterItemInoculante;
import br.com.smartagro.bioinsumos.api.ApiService;
import br.com.smartagro.bioinsumos.helper.RetrofitConfig;
import br.com.smartagro.bioinsumos.model.Data2;
import br.com.smartagro.bioinsumos.model.DataRetornoItemInoculante;
import br.com.smartagro.bioinsumos.model.ResultadoInoculantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Inoculantes extends AppCompatActivity {

    public RecyclerView recyclerView;
    public AdapterInoculantes adapterInoculantes;

    public AdapterItemInoculante adapterItemInoculante;
    private Data2 data;
    private DataRetornoItemInoculante dataRetornoItemInoculante;

    private List<Data2> dataList2 = new ArrayList<>();

    private ResultadoInoculantes resultadoInoculantes;

    private Retrofit retrofit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inoculantes);
        retrofit = RetrofitConfig.getRetrofit();
        configuraTollbar();
        recuperaDados("");
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
        setTitle("Inoculantes");
        setSupportActionBar( toolbar );
    }

    private void recuperaDados(String query){
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.recuperaInoculantes("Bearer 5e5f5869-3889-321c-a40b-bd70ec3c26e7").enqueue(new Callback<ResultadoInoculantes>() {
            @Override
            public void onResponse(Call<ResultadoInoculantes> call, Response<ResultadoInoculantes> response) {
                Log.d("resultado", "resultado: " + response.toString() );
                progressBar.setVisibility(View.GONE);
                if( response.isSuccessful() ){
                    resultadoInoculantes = response.body();
                    dataList2 = resultadoInoculantes.data;
                    Log.d("resultado", "resultado: " + dataList2.get(1) );
                    if(adapterItemInoculante == null){
                        configuraRecyclerView();
                    }else{
                        configuraRecyclerViewPesquisa();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultadoInoculantes> call, Throwable t) {

            }
        });
        apiService.recuperaInoculantesPesquisa(query, "Bearer 5e5f5869-3889-321c-a40b-bd70ec3c26e7").enqueue(new Callback<DataRetornoItemInoculante>() {
            @Override
            public void onResponse(Call<DataRetornoItemInoculante> call, Response<DataRetornoItemInoculante> response) {
                Log.d("resultado", "resultado: " + response.toString() );
                progressBar.setVisibility(View.GONE);
                if( response.isSuccessful() ){
                    dataRetornoItemInoculante = response.body();
                    data = dataRetornoItemInoculante.data;
                    Log.d("resultado", "resultado" + data);
                    configuraRecyclerViewPesquisa();
                }
            }
            @Override
            public void onFailure(Call<DataRetornoItemInoculante> call, Throwable t) {

            }
        });

    }

    public void configuraRecyclerView() {

        adapterInoculantes = new AdapterInoculantes(dataList2);
        recyclerView = findViewById(R.id.biologicos_recycler_inoclulantes);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapterInoculantes);
        } else {
            Log.e(TAG, "recyclerView é nulo");
        }
    }

    public void configuraRecyclerViewPesquisa() {

        adapterItemInoculante = new AdapterItemInoculante(data);
        recyclerView = findViewById(R.id.biologicos_recycler_inoclulantes);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapterItemInoculante);
        } else {
            Log.e(TAG, "recyclerView é nulo");
        }
    }
}