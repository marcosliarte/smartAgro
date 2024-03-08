package br.com.smartagro.bioinsumos.helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://api.cnptia.embrapa.br/bioinsumos/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
