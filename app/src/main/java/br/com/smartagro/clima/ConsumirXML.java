package br.com.smartagro.clima;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ConsumirXML {
    public static List<Previsao> getPrevisao(String dados) {
        try {
            Log.i("LOG", "getPrevisao: " + dados);
            boolean dadosNaTag = false;
            String tagAtual = "";
            Previsao previsao = null;
            List<Previsao> previsoesList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(dados));

            int eventType = xmlPullParser.getEventType();

            while (eventType != xmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        tagAtual = xmlPullParser.getName();
                        if (tagAtual.endsWith("previsao")) {
                            dadosNaTag = true;
                            previsao = new Previsao();
                            previsoesList.add(previsao);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlPullParser.getName().equals("previsao")) {
                            dadosNaTag = false;
                        }
                        tagAtual = "";
                        break;
                    case XmlPullParser.TEXT:
                        if (dadosNaTag && previsao != null) {
                            switch (tagAtual) {
                                case "dia":
                                    previsao.setDia(xmlPullParser.getText());
                                    break;
                                case "tempo":
                                    previsao.setTempo(xmlPullParser.getText());
                                    break;
                                case "maxima":
                                    previsao.setMaxima(xmlPullParser.getText());
                                    break;
                                case "minima":
                                    previsao.setMinima(xmlPullParser.getText());
                                    break;
                                case "iuv":
                                    previsao.setIuv(xmlPullParser.getText());
                                    break;
                            }
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return previsoesList;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Cidade> getCidade(String dados) {
        try {
            Log.i("LOG", "getCidade: " + dados);
            boolean dadosNaTag = false;
            String tagAtual = "";
            Cidade cidade = null;
            List<Cidade> cidadeList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(dados));

            int eventType = xmlPullParser.getEventType();

            while (eventType != xmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        tagAtual = xmlPullParser.getName();
                        if (tagAtual.endsWith("cidade")) {
                            dadosNaTag = true;
                            cidade = new Cidade();
                            cidadeList.add(cidade);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlPullParser.getName().equals("cidade")) {
                            dadosNaTag = false;
                        }
                        tagAtual = "";
                        break;
                    case XmlPullParser.TEXT:
                        if (dadosNaTag && cidade != null) {
                            switch (tagAtual) {
                                case "nome":
                                    cidade.setNome(xmlPullParser.getText());
                                    break;
                                case "uf":
                                    cidade.setUf(xmlPullParser.getText());
                                    break;
                                case "id":
                                    cidade.setId(xmlPullParser.getText());
                                    break;
                            }
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return cidadeList;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
