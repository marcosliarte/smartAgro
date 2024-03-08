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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.smartagro.R;
import br.com.smartagro.bioinsumos.model.Data;
import br.com.smartagro.bioinsumos.model.Pragas;

import java.util.HashSet;

public class AdapterItemBiologico extends RecyclerView.Adapter<AdapterItemBiologico.ViewHolder> {
    private Data data;

    public AdapterItemBiologico(Data data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_biologicos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.registroProduto.setText(data.registroProduto);
        holder.marcaComercial.setText(data.marcaComercial);
        holder.titularRegistro.setText(data.titularRegistro);
        holder.ingredienteAtivo.setText(data.ingredienteAtivo);
        holder.aprovadoParaAgriculturaOrganica.setText(data.aprovadoParaAgriculturaOrganica);
        holder.classificacaoToxicologica.setText(data.classificacaoToxicologica);
        holder.classificacaoAmbiental.setText(data.classificacaoAmbiental);

        String[] array = data.classes;
        String textoSemColchetes = String.join(", ", array);

        holder.classesTextView.setText(textoSemColchetes);

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
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView registroProduto;
        public TextView marcaComercial;
        public TextView titularRegistro;
        public TextView ingredienteAtivo;
        public TextView pragasTextView;
        public TextView urlTextView;
        public TextView classesTextView;
        public TextView aprovadoParaAgriculturaOrganica;
        public TextView classificacaoToxicologica;
        public TextView classificacaoAmbiental;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            registroProduto = itemView.findViewById(R.id.registro_produto_textview);
            marcaComercial = itemView.findViewById(R.id.marca_comercial_textview);
            titularRegistro = itemView.findViewById(R.id.titular_registro_textview);
            ingredienteAtivo = itemView.findViewById(R.id.ingrediente_ativo_textview);
            aprovadoParaAgriculturaOrganica = itemView.findViewById(R.id.aprovado_agricultura_organica_textview);
            classificacaoToxicologica = itemView.findViewById(R.id.classificacao_toxicologica_textview);
            classificacaoAmbiental = itemView.findViewById(R.id.classificacao_ambiental_textview);
            classesTextView = itemView.findViewById(R.id.classes_textview);
            urlTextView = itemView.findViewById(R.id.url_textview);
            pragasTextView = itemView.findViewById(R.id.pragas_text);
        }
    }
}
