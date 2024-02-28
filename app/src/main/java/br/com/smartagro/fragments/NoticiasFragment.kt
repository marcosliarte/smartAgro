package br.com.smartagro.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.smartagro.NewsItemClickListener
import br.com.smartagro.R
import br.com.smartagro.databinding.FragmentNoticiasBinding
import br.com.smartagro.noticias.cafepoint.NewsAdapterCCCMG
import br.com.smartagro.noticias.cafepoint.NewsAdapterCafePoint
import br.com.smartagro.noticias.cafepoint.RssParserCCCMG
import br.com.smartagro.noticias.cafepoint.RssParserCafePoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NoticiasFragment : Fragment() {

    private lateinit var binding: FragmentNoticiasBinding
    private lateinit var newsAdapterCafePoint: NewsAdapterCafePoint
    private lateinit var newsAdapterCCCMG: NewsAdapterCCCMG
    private lateinit var rssParserCafePoint: RssParserCafePoint
    private lateinit var rssParserCCCMG: RssParserCCCMG
    private var noticiasCarregadas = false

    private fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        if (!::binding.isInitialized) {
            binding = FragmentNoticiasBinding.inflate(inflater, container, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRssFeeds()
        setupChipGroupRss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initializeBinding(inflater, container)
        return binding.root
    }

    private fun setupRssFeeds() {
        initializeBinding(layoutInflater, null)
        rssParserCafePoint = RssParserCafePoint("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml")
        rssParserCCCMG = RssParserCCCMG("https://www.cepea.esalq.usp.br/rss.php")

        showLoadingIndicator(true)

        MainScope().launch(Dispatchers.Main) {
            try {
                val rssItemsCCCMG = rssParserCCCMG.fetchRssItems()
                val rssItemsCafePoint = rssParserCafePoint.fetchRssItems()

                newsAdapterCafePoint = NewsAdapterCafePoint(rssItemsCafePoint, requireActivity() as NewsItemClickListener)
                newsAdapterCCCMG = NewsAdapterCCCMG(rssItemsCCCMG, requireActivity() as NewsItemClickListener)

                binding.recyclerView.adapter = newsAdapterCCCMG
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao carregar os feeds RSS", Toast.LENGTH_SHORT).show()
            } finally {
                showLoadingIndicator(false)
                noticiasCarregadas = true
                binding.chipGroupRss.check(R.id.chipCafePoint)
            }
        }
    }

    private fun setupChipGroupRss() {
        initializeBinding(layoutInflater, null)
        binding.chipGroupRss.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipCafePoint -> {
                    if (noticiasCarregadas) {
                        binding.recyclerView.adapter = newsAdapterCafePoint
                        newsAdapterCafePoint.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "Carregando notícias...", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.chipCCCMG -> {
                    if (noticiasCarregadas) {
                        binding.recyclerView.adapter = newsAdapterCCCMG
                        newsAdapterCCCMG.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "Carregando notícias...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun showLoadingIndicator(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}