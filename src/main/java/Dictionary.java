
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class Dictionary {
    String text;
    String encodedText;
    String initialLanguage = "en";
    List<String> urls = new ArrayList();
    List<String> languages = Arrays.asList("es", "de", "fr");

    public Dictionary(String text){
        this.text = text;

        try{
            encodedText =  URLEncoder.encode(text, "UTF-8");
        } catch (Exception e){
            System.out.println("Could not encode text. Error: " + e);
        }
        createURLS();
    }

    private void createURLS() {

        for (String targetLanguage : languages){
            urls.add (
                "https://translate.googleapis.com/translate_a/single?client=gtx" +
                        "&sl=" + initialLanguage +
                        "&tl=" + targetLanguage +
                        "&dt=t&q=" + encodedText
            );

        }
    }


    public void translate(){
        for (int i=0; i<urls.size(); ++i){
            URL url;
            try{
                url = new URL(urls.get(i));
                System.out.println(url);
            }catch(Exception e){
                System.out.println("Could not create URL. Error: " + e);
                return;
            }

            HttpURLConnection con;
            int status;
            try {
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                status = con.getResponseCode();

            } catch(Exception e){
                System.out.println("Could not make request. Error: " + e);
                return;
            }

            if (status!=200){
                System.out.println("Get request returned status code " + status);
                return;
            }

            String response;
            try{
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                response = content.toString();
            }catch (Exception e){
                System.out.println("Could not read request. Error: " + e);
                return;
            }

            System.out.println("El response es: " + response);



        }
    }

}