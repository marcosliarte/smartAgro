package br.com.smartagro.noticias.cafepoint

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.smartagro.NewsItemClickListener
import br.com.smartagro.R
import br.com.smartagro.noticias.RssItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsAdapterCafePoint(
    private var newsList: List<RssItem>,
    private val newsItemClickListener: NewsItemClickListener
) :
    RecyclerView.Adapter<NewsAdapterCafePoint.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtAutor: TextView = itemView.findViewById(R.id.txtAutor)
        val txtDia: TextView = itemView.findViewById(R.id.txtDia)
        val txtMes: TextView = itemView.findViewById(R.id.txtMes)
        val txtDescricao: TextView = itemView.findViewById(R.id.txtDescricao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.txtTitulo.text = newsItem.title
        holder.txtAutor.text = "Autor: " + newsItem.author
        holder.txtDia.text = extractDayFromDate(newsItem.pubDate)
        holder.txtMes.text = extractMonthFromDate(newsItem.pubDate)
        holder.txtDescricao.text = newsItem.description

        holder.itemView.setOnClickListener {
            holder.itemView.setOnClickListener {
                val url = "https://www.cafepoint.com.br" + newsItem.link
                newsItemClickListener.onNewsItemClick(url)
            }
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    private fun extractDayFromDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        val outputFormat = SimpleDateFormat("dd", Locale.US)

        val date: Date = inputFormat.parse(dateString) ?: return ""

        return outputFormat.format(date)
    }

    private fun extractMonthFromDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        val outputFormat = SimpleDateFormat("MMMM", Locale("pt", "BR"))

        val date: Date = inputFormat.parse(dateString) ?: return ""

        return outputFormat.format(date)
    }
}
