package br.com.smartagro.bioinsumos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import br.com.smartagro.R;
import br.com.smartagro.bioinsumos.model.Culturas;
import br.com.smartagro.bioinsumos.model.Data2;
import java.util.ArrayList;
import java.util.List;

public class AdapterInoculantes extends RecyclerView.Adapter<AdapterInoculantes.DataViewHolder> {

    private List<Data2> dataList2 = new ArrayList<>();

    public AdapterInoculantes(List<Data2> dataList2) {
        this.dataList2 = dataList2;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_inoculantes, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        Data2 data = dataList2.get(position);
        holder.registroProduto.setText(data.registroProduto);
        holder.cnpj.setText(data.cnpj);
        holder.razaoSocial.setText(data.razaoSocial);
        holder.uf.setText(data.uf);
        holder.atividade.setText(data.atividade);
        holder.tipoFertilizante.setText(data.tipoFertilizante);
        holder.especie.setText(data.especie);
        holder.dataRegistro.setText(data.dataRegistro);
        holder.garantia.setText(data.garantia);
        holder.naturezaFisica.setText(data.naturezaFisica);

        List<Culturas> culturasList = data.culturas;

        for (Culturas culturas : culturasList) {
            holder.cultura.setText(culturas.cultura);
            holder.nomeCientifico.setText(culturas.nomeCientifico);
        }

    }

    @Override
    public int getItemCount() {
        return dataList2.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView registroProduto;
        public TextView cnpj;
        public TextView razaoSocial;
        public TextView uf;
        public TextView atividade;

        public TextView tipoFertilizante;
        public TextView especie;
        public TextView dataRegistro;

        public TextView garantia;

        public TextView naturezaFisica;

        public TextView cultura;

        public TextView nomeCientifico;

        public DataViewHolder(View itemView) {
            super(itemView);
            registroProduto = itemView.findViewById(R.id.registro_produto);
            cnpj = itemView.findViewById(R.id.cnpj);
            razaoSocial = itemView.findViewById(R.id.razaoSocial);
            uf = itemView.findViewById(R.id.uf);
            atividade = itemView.findViewById(R.id.atividade);
            tipoFertilizante = itemView.findViewById(R.id.tipoFertilizante);
            especie = itemView.findViewById(R.id.especie);
            dataRegistro = itemView.findViewById(R.id.dataRegistro);
            garantia = itemView.findViewById(R.id.garantia);
            naturezaFisica = itemView.findViewById(R.id.naturezaFisica);

            cultura = itemView.findViewById(R.id.cultura);
            nomeCientifico = itemView.findViewById(R.id.nomeCientifico);


        }
    }
}