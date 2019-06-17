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
    private String initialLanguage = "en";
    private List<URL> urls = new ArrayList<URL>();
    private List<String> languagesCodes = Arrays.asList("es", "de", "fr");
    private List<String> languages = Arrays.asList("spanish", "german", "french");

    public Dictionary(String text){
        try{
            encodedText =  URLEncoder.encode(text, "UTF-8");
        } catch (Exception e){
            logger.log(Level.SEVERE, "Could not encode text. " + e.toString(), e);
        }
        createURLS();
    }

    private void createURLS() {
        for (String targetLanguage : languagesCodes){
            try{
                urls.add (
                        new URL (
                        "https://translate.googleapis.com/translate_a/single?client=gtx" +
                                "&sl=" + initialLanguage +
                                "&tl=" + targetLanguage +
                                "&dt=t&q=" + encodedText
                        )

                );
            }catch(Exception e){
                logger.log(Level.SEVERE, "Could not create URL. " + e.toString(), e);
                return;
            }
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


    public HashMap<String, String> translate(){
        HashMap<String, String> translations = new HashMap<String, String>();

        for (int i=0; i<urls.size(); ++i){
            if (makeRequest(urls.get(i)).length()==0) {
                return translations;
            }

            String response;
            // Make request
            try {
                response = makeRequest(urls.get(i));
            }catch(Exception e){
                logger.log(Level.SEVERE, "Could not create URL. " + e.toString(), e);
                return translations;
            }

            translations.put(languages.get(i), parseResponse(response));
        }
        return translations;
    }

}