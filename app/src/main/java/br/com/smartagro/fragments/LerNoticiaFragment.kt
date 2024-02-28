package br.com.smartagro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import br.com.smartagro.R
class LerNoticiaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ler_noticia, container, false)

        val webView = rootView.findViewById<WebView>(R.id.verNoticiaWebView)

        val url = arguments?.getString("url")

        webView.loadUrl(url!!)

        return rootView
    }
}