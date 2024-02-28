package br.com.smartagro.noticias.cafepoint

import br.com.smartagro.noticias.RssItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class RssParserCCCMG(private val rssUrl: String) {
    suspend fun fetchRssItems(): List<RssItem> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(rssUrl)
            .build()

        val response = client.newCall(request).execute()
        val xml = response.body?.string() ?: return@withContext emptyList()

        val rssItems = mutableListOf<RssItem>()
        val parserFactory = XmlPullParserFactory.newInstance()
        val parser = parserFactory.newPullParser()
        parser.setInput(StringReader(xml))

        var eventType = parser.eventType
        var currentItem: RssItem? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (parser.name == "item") {
                        currentItem = RssItem()
                    } else if (currentItem != null) {
                        when (parser.name) {
                            "title" -> currentItem.title = parser.nextText()
                            "link" -> currentItem.link = parser.nextText()
                            "description" -> currentItem.description = parser.nextText()
                            "dc:creator" -> currentItem.author = parser.nextText()
                            "pubDate" -> currentItem.pubDate = parser.nextText()
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "item" && currentItem != null) {
                        rssItems.add(currentItem)
                        currentItem = null
                    }
                }
            }
            eventType = parser.next()
        }

        return@withContext rssItems
    }
}
