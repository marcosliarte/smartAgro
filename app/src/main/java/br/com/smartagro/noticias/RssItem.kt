package br.com.smartagro.noticias

data class RssItem(
    var title: String = "",
    var link: String = "",
    var description: String = "",
    var author: String = "",
    var pubDate: String = ""
)
