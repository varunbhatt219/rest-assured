package com.test.demo;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainClass {

    public static void main(String[] args) {

        try {
           // URL url= new URL("https://petstore.swagger.io/v2/swagger.json");
            //url.
            //InputStream inputStream= url.openStream();
            //System.out.println(inputStream.read());
            //System.out.println("path =="+url.getPath()+" -"+url.getHost());
            Swagger swagger = new SwaggerParser().read("/home/ongraph/Downloads/demo/src/test/resources/swagger.json");
            System.out.println("host =="+swagger.getHost()+"   base path" +swagger.getBasePath());
            Set s = swagger.getPaths().entrySet();
            Iterator itr = s.iterator();
            while(itr.hasNext()){
                Map.Entry entry= (Map.Entry) itr.next();
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
