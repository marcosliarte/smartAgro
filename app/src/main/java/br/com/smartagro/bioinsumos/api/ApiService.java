package br.com.smartagro.bioinsumos.api;

import br.com.smartagro.bioinsumos.model.Data;
import br.com.smartagro.bioinsumos.model.DataRetornoItem;
import br.com.smartagro.bioinsumos.model.DataRetornoItemInoculante;
import br.com.smartagro.bioinsumos.model.Resultado;
import br.com.smartagro.bioinsumos.model.ResultadoInoculantes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("produtos-biologicos")
    Call<Resultado> recuperaBiologicos(@Header("Authorization") String authHeader);

    @GET("produtos-biologicos/{registro-produto}")
    Call <DataRetornoItem> recuperaBiologicosPesquisa(
            @Path("registro-produto") String registro,
            @Header("Authorization") String authHeader);

    @GET("inoculantes")
    Call<ResultadoInoculantes> recuperaInoculantes(@Header("Authorization") String authHeader);

    @GET("inoculantes/{registroProduto}")
    Call<DataRetornoItemInoculante> recuperaInoculantesPesquisa(
            @Path("registroProduto") String registro,
            @Header("Authorization") String authHeader);
}

