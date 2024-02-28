package br.com.smartagro.clima;

import java.util.HashMap;
import java.util.Map;

public abstract class SiglaDescricao {

    private static final Map<String, String> siglaDescricaoMap = new HashMap<>();

    static {
        siglaDescricaoMap.put("ec", "Encoberto com Chuvas Isoladas");
        siglaDescricaoMap.put("ci", "Chuvas Isoladas");
        siglaDescricaoMap.put("c", "Chuva");
        siglaDescricaoMap.put("in", "Instável");
        siglaDescricaoMap.put("pp", "Poss. de Pancadas de Chuva");
        siglaDescricaoMap.put("cm", "Chuva pela Manhã");
        siglaDescricaoMap.put("cn", "Chuva a Noite");
        siglaDescricaoMap.put("pt", "Pancadas de Chuva a Tarde");
        siglaDescricaoMap.put("pm", "Pancadas de Chuva pela Manhã");
        siglaDescricaoMap.put("np", "Nublado e Pancadas de Chuva");
        siglaDescricaoMap.put("pc", "Pancadas de Chuva");
        siglaDescricaoMap.put("pn", "Parcialmente Nublado");
        siglaDescricaoMap.put("cv", "Chuvisco");
        siglaDescricaoMap.put("ch", "Chuvoso");
        siglaDescricaoMap.put("t", "Tempestade");
        siglaDescricaoMap.put("ps", "Predomínio de Sol");
        siglaDescricaoMap.put("e", "Encoberto");
        siglaDescricaoMap.put("n", "Nublado");
        siglaDescricaoMap.put("cl", "Céu Claro");
        siglaDescricaoMap.put("nv", "Nevoeiro");
        siglaDescricaoMap.put("g", "Geada");
        siglaDescricaoMap.put("ne", "Neve");
        siglaDescricaoMap.put("nd", "Não Definido");
        siglaDescricaoMap.put("pnt", "Pancadas de Chuva a Noite");
        siglaDescricaoMap.put("psc", "Possibilidade de Chuva");
        siglaDescricaoMap.put("pcm", "Possibilidade de Chuva pela Manhã");
        siglaDescricaoMap.put("pct", "Possibilidade de Chuva a Tarde");
        siglaDescricaoMap.put("pcn", "Possibilidade de Chuva a Noite");
        siglaDescricaoMap.put("npt", "Nublado com Pancadas a Tarde");
        siglaDescricaoMap.put("npn", "Nublado com Pancadas a Noite");
        siglaDescricaoMap.put("ncn", "Nublado com Poss. de Chuva a Noite");
        siglaDescricaoMap.put("nct", "Nublado com Poss. de Chuva a Tarde");
        siglaDescricaoMap.put("ncm", "Nubl. c/ Poss. de Chuva pela Manhã");
        siglaDescricaoMap.put("npm", "Nublado com Pancadas pela Manhã");
        siglaDescricaoMap.put("npp", "Nublado com Possibilidade de Chuva");
        siglaDescricaoMap.put("vn", "Variação de Nebulosidade");
        siglaDescricaoMap.put("ct", "Chuva a Tarde");
        siglaDescricaoMap.put("ppn", "Poss. de Panc. de Chuva a Noite");
        siglaDescricaoMap.put("ppt", "Poss. de Panc. de Chuva a Tarde");
        siglaDescricaoMap.put("ppm", "Poss. de Panc. de Chuva pela Manhã");
    }

    public static String converterSiglaParaDescricao(String sigla) {
        return siglaDescricaoMap.get(sigla);
    }
}
