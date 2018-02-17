package application;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder ;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;


public class GoogleImageSearch {
    public static void search(String image){
        try {
            HttpClient client = HttpClientBuilder.create().build();
            String url="https://www.google.com/searchbyimage/upload";
            String imageFile=image;
            HttpPost post = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addPart("encoded_image", new FileBody(new File(imageFile)));
            builder.addPart("image_url",new StringBody("", ContentType.DEFAULT_TEXT));
            builder.addPart("image_content",new StringBody("", ContentType.DEFAULT_TEXT));
            builder.addPart("filename",new StringBody("", ContentType.DEFAULT_TEXT));
            builder.addPart("h1",new StringBody("en", ContentType.DEFAULT_TEXT));
            builder.addPart("bih",new StringBody("179", ContentType.DEFAULT_TEXT));
            builder.addPart("biw",new StringBody("1600", ContentType.DEFAULT_TEXT));

            HttpEntity entity = builder.build();

            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("HREF")>0){
                    Desktop.getDesktop().browse(new URI(line.substring(9).replace("\">here</A>", "")));
                }
            }

        }catch (ClientProtocolException cpx){
            cpx.printStackTrace();
        }catch (IOException ioex){
            ioex.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}