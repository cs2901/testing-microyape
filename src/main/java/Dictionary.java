import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dictionary {
    private static final Logger logger = Logger.getLogger(Dictionary.class.getName());

    private String encodedText;
    private String initialLanguage;
    private URL url;
    static private HashMap<String, String> languages = new HashMap<String, String>(){{
        put("Afrikaans", "af");
        put("Albanian", "sq");
        put("Amharic", "am");
        put("Arabic", "ar");
        put("Armenian", "hy");
        put("Azerbaijani", "az");
        put("Basque", "eu");
        put("Belarusian", "be");
        put("Bengali", "bn");
        put("Bosnian", "bs");
        put("Bulgarian", "bg");
        put("Catalan", "ca");
        put("Cebuano", "ceb");
        put("Chinese-(Simplified)", "zh-CN");
        put("Chinese-(Traditional)", "zh-TW");
        put("Corsican", "co");
        put("Croatian", "hr");
        put("Czech", "cs");
        put("Danish", "da");
        put("Dutch", "nl");
        put("English", "en");
        put("Esperanto", "eo");
        put("Estonian", "et");
        put("Finnish", "fi");
        put("French", "fr");
        put("Frisian", "fy");
        put("Galician", "gl");
        put("Georgian", "ka");
        put("German", "de");
        put("Greek", "el");
        put("Gujarati", "gu");
        put("Haitian-Creole", "ht");
        put("Hausa", "ha");
        put("Hawaiian", "haw");
        put("Hebrew", "he");
        put("Hindi", "hi");
        put("Hmong", "hmn");
        put("Hungarian", "hu");
        put("Icelandic", "is");
        put("Igbo", "ig");
        put("Indonesian", "id");
        put("Irish", "ga");
        put("Italian", "it");
        put("Japanese", "ja");
        put("Javanese", "jw");
        put("Kannada", "kn");
        put("Kazakh", "kk");
        put("Khmer", "km");
        put("Korean", "ko");
        put("Kurdish", "ku");
        put("Kyrgyz", "ky");
        put("Lao", "lo");
        put("Latin", "la");
        put("Latvian", "lv");
        put("Lithuanian", "lt");
        put("Luxembourgish", "lb");
        put("Macedonian", "mk");
        put("Malagasy", "mg");
        put("Malay", "ms");
        put("Malayalam", "ml");
        put("Maltese", "mt");
        put("Maori", "mi");
        put("Marathi", "mr");
        put("Mongolian", "mn");
        put("Myanmar-(Burmese)", "my");
        put("Nepali", "ne");
        put("Norwegian", "no");
        put("Nyanja-(Chichewa)", "ny");
        put("Pashto", "ps");
        put("Persian", "fa");
        put("Polish", "pl");
        put("Portuguese-(Portugal,-Brazil)", "pt");
        put("Punjabi", "pa");
        put("Romanian", "ro");
        put("Russian", "ru");
        put("Samoan", "sm");
        put("Scots-Gaelic", "gd");
        put("Serbian", "sr");
        put("Sesotho", "st");
        put("Shona", "sn");
        put("Sindhi", "sd");
        put("Sinhala-(Sinhalese)", "si");
        put("Slovak", "sk");
        put("Slovenian", "sl");
        put("Somali", "so");
        put("Spanish", "es");
        put("Sundanese", "su");
        put("Swahili", "sw");
        put("Swedish", "sv");
        put("Tagalog-(Filipino)", "tl");
        put("Tajik", "tg");
        put("Tamil", "ta");
        put("Telugu", "te");
        put("Thai", "th");
        put("Turkish", "tr");
        put("Ukrainian", "uk");
        put("Urdu", "ur");
        put("Uzbek", "uz");
        put("Vietnamese", "vi");
        put("Welsh", "cy");
        put("Xhosa", "xh");
        put("Yiddish", "yi");
        put("Yoruba", "yo");
        put("Zulu", "zu");
    }};

    public Dictionary(String text){
        encodedText = encodeText(text);
    }

    public Dictionary(){}


    private void createURLS(String initLang, String targetLang) {
        try{
        url = new URL (
                "https://translate.googleapis.com/translate_a/single?client=gtx" +
                        "&sl=" + languages.get(initLang) +
                        "&tl=" + languages.get(targetLang) +
                        "&dt=t&q=" + encodedText
                );
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Could not create URL. " + e.toString(), e);
        }
    }

    private String makeRequest(URL url){
        HttpURLConnection con;
        int status;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent", "Chrome/74.0.3729.169");
            status = con.getResponseCode();

        } catch(Exception e){
            logger.log(Level.SEVERE, "Could not make request. " + e.toString(), e);
            return "";
        }

        if (status!=200){
            logger.log(Level.SEVERE, "Get request returned status code {0}", status);
            return "";
        }

        String response;
        try{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null)  content.append(inputLine);
            in.close();
            response = content.toString();
        }catch (Exception e){
            logger.log(Level.SEVERE, "Could not read request. " + e.toString(), e);
            return "";
        }
        return response;
    }

    private String parseResponse(String response){
        JSONArray parsedResponse = new JSONArray(response);
        for (int i=0; i<2; ++i) parsedResponse = (JSONArray) parsedResponse.get(0);

        return (String) parsedResponse.get(0);
    }


    public String translate(String initLang, String targetLang){
        createURLS(initLang, targetLang);

        String response;
        try {
            response = makeRequest(url);
        }catch(Exception e){
            logger.log(Level.SEVERE, "Could not create URL. " + e.toString(), e);
            return "";
        }

        return parseResponse(response);
    }

    public String encodeText(String text){
        try{
            encodedText =  URLEncoder.encode(text, "UTF-8");
        } catch (Exception e){
            logger.log(Level.SEVERE, "Could not encode text. " + e.toString(), e);
            return "";
        }
        return encodedText;
    }


    public void setText(String text){ encodedText = encodeText(text); }
    static public List<String> getLanguages(){ return new ArrayList<String>(languages.keySet()); }

}