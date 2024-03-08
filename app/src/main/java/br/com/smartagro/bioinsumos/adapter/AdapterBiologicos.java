package br.com.smartagro.bioinsumos.adapter;

import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import br.com.smartagro.R;
import br.com.smartagro.bioinsumos.model.Data;
import br.com.smartagro.bioinsumos.model.Pragas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AdapterBiologicos extends RecyclerView.Adapter<AdapterBiologicos.DataViewHolder> {

    private List<Data> dataList;
    public AdapterBiologicos(List<Data> dataList) {
        this.dataList = dataList;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_biologicos, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.registroProdutoTextView.setText(data.registroProduto);
        holder.marcaComercialTextView.setText(data.marcaComercial);
        holder.titularRegistroTextView.setText(data.titularRegistro);
        holder.ingredienteAtivo.setText(data.ingredienteAtivo);

        String[] array = data.classes;
        String textoSemColchetes = String.join(", ", array);


        holder.classesTextView.setText(textoSemColchetes);
        boolean aprovado;
        aprovado = Boolean.parseBoolean(data.aprovadoParaAgriculturaOrganica);

        if (aprovado == true){
            data.aprovadoParaAgriculturaOrganica = "sim";
        }else {
            data.aprovadoParaAgriculturaOrganica = "não";
        }
        holder.aprovadoParaAgriculturaOrganicaTextView.setText(data.aprovadoParaAgriculturaOrganica);
        holder.classificacaoToxicologicaTextView.setText(data.classificacaoToxicologica);
        holder.classificacaoAmbientalTextView.setText(data.classificacaoAmbiental);

        String link = data.url;
        String text = "Visite o site";
        String htmlLink = "<a href='" + link + "'>" + text + "</a>";
        holder.urlTextView.setText(Html.fromHtml(htmlLink));
        holder.urlTextView.setMovementMethod(LinkMovementMethod.getInstance());

        StringBuilder pragasText = new StringBuilder();
        HashSet<String> culturasUnicas = new HashSet<>();

        for (Pragas p : data.pragas) {
            if (culturasUnicas.contains(p.cultura)) {
                continue;
            }

            culturasUnicas.add(p.cultura);
            pragasText.append("Cultura: ").append(p.cultura).append("\n");
            pragasText.append("Nome Científico: ").append(p.nomeCientifico).append("\n");

            String[] nomeComumArray = p.nomeComum;
            StringBuilder nomeComumText = new StringBuilder();
            for (int i = 0; i < nomeComumArray.length; i++) {
                nomeComumText.append(nomeComumArray[i]);
                if (i < nomeComumArray.length - 1) {
                    nomeComumText.append(", ");
                }
            }
            pragasText.append("Nomes Comuns: ").append(nomeComumText.toString()).append("\n\n");
        }

        holder.pragasTextView.setText("");
        SpannableString spannableString = new SpannableString(pragasText.toString());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), pragasText.indexOf("Nome Científico"), pragasText.indexOf("Nome Científico") + 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), pragasText.indexOf("Nomes Comuns"), pragasText.indexOf("Nomes Comuns") + 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.pragasTextView.setText(spannableString);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView registroProdutoTextView;
        public TextView marcaComercialTextView;
        public TextView titularRegistroTextView;
        public TextView ingredienteAtivo;
        public TextView classesTextView;
        public TextView aprovadoParaAgriculturaOrganicaTextView;
        public TextView classificacaoToxicologicaTextView;
        public TextView classificacaoAmbientalTextView;
        public TextView urlTextView;
        public TextView pragasTextView;

        public DataViewHolder(View itemView) {
            super(itemView);
            registroProdutoTextView = itemView.findViewById(R.id.registro_produto_textview);
            marcaComercialTextView = itemView.findViewById(R.id.marca_comercial_textview);
            titularRegistroTextView = itemView.findViewById(R.id.titular_registro_textview);
            ingredienteAtivo = itemView.findViewById(R.id.ingrediente_ativo_textview);
            classesTextView = itemView.findViewById(R.id.classes_textview);
            aprovadoParaAgriculturaOrganicaTextView = itemView.findViewById(R.id.aprovado_agricultura_organica_textview);
            classificacaoToxicologicaTextView = itemView.findViewById(R.id.classificacao_toxicologica_textview);
            classificacaoAmbientalTextView = itemView.findViewById(R.id.classificacao_ambiental_textview);
            urlTextView = itemView.findViewById(R.id.url_textview);
            pragasTextView = itemView.findViewById(R.id.pragas_text);
        }
    }
}