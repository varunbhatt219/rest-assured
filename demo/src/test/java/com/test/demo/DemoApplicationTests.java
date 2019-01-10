package com.test.demo;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.io.FileOutputStream;
import java.net.MalformedURLException;

import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;

import static io.restassured.RestAssured.given;
import static io.restassured.config.XmlConfig.xmlConfig;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static javafx.scene.input.KeyCode.ACCEPT;
import static javafx.scene.input.KeyCode.L;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.get;
import static io.restassured.module.jsv.JsonSchemaValidator.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {


    ArrayList<HashMap<String,Object>> arrayList;

    LinkedList<HashMap<String,Object>> linkedList;

    @Test
    public void contextLoads() {

    }

    @Before
    public void setup(){
        Swagger swagger = new SwaggerParser().read("/home/ongraph/Downloads/demo/src/test/resources/swagger.json");

        //RestAssured.baseURI = "https://"+swagger.getHost();
        //RestAssured.baseURI ="https://petstore.swagger.io/v2/pet";
       // RestAssured.basePath = swagger.getBasePath();
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", 0);
        hashMap.put("username","string");
        hashMap.put("firstName","string");
        hashMap.put("lastName","string");
        hashMap.put("email","string");
        hashMap.put("password","string");
        hashMap.put("phone","string");
        hashMap.put("userStatus",0);
        arrayList.add(hashMap);
        linkedList.add(hashMap);



    }

    @Test
    public void schema_validation_via_download() {

            try {
                ReadableByteChannel readChannel = Channels.newChannel(new URL("https://petstore.swagger.io/v2/swagger.json").openStream());

                FileOutputStream fileOS = new FileOutputStream("/home/ongraph/Downloads/demo/src/test/resources/swagger.json");
                FileChannel writeChannel = fileOS.getChannel();
                writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory
                .newBuilder()
                .setValidationConfiguration(
                        ValidationConfiguration.newBuilder()
                                .setDefaultVersion(SchemaVersion.DRAFTV4)
                                .freeze()).freeze();
        /*JsonSchemaValidator.settings = settings()
                .with().jsonSchemaFactory(jsonSchemaFactory)
                .and().with().checkedValidation(false);*/
        given().log().all().accept(ContentType.JSON).
                contentType(ContentType.JSON).queryParam("status","available")
                .when()
                .get("https://petstore.swagger.io/v2/pet")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("swagger.json").using(
                        jsonSchemaFactory));
    }

    @Test
    public void schema_validation_url(){


        /*Response response= get("/findByStatus?status=available");
        System.out.println(response.toString());
        try {
            ValidatableResponse jsv = response.then().assertThat().body(matchesJsonSchema(new URL("https://petstore.swagger.io/v2/swagger.json")));

            System.out.println(jsv.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/

        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory
                .newBuilder()
                .setValidationConfiguration(
                        ValidationConfiguration.newBuilder()
                                .setDefaultVersion(SchemaVersion.DRAFTV4)
                                .freeze()).freeze();
        try {
           given().log().all().accept(ContentType.JSON).
                   contentType(ContentType.JSON).queryParam("status","available")
                   .when()
                   .get("https://petstore.swagger.io/v2/pet")
                    .then()
                    .assertThat()
                    .body(matchesJsonSchema(new URL("https://petstore.swagger.io/v2/swagger.json")).using(jsonSchemaFactory)).log().all();
            /*System.out.println(matchesJsonSchema(new URL("http://mysafeinfo.com/api/data?list=englishmonarchs&format=json").toString()));*/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void
    test_pet_post_add() {

        given().log().all().accept(ContentType.JSON).
                contentType(ContentType.JSON).
                body(new HashMap<String, Object>() {{
                    put("id", 1);
                    put("category", new HashMap<String, Object>() {{
                        put("id", 1);
                        put("name", "String");
                    }});
                    put("name", "doggie");
                    put("photoUrls",new ArrayList<String>(){{
                        add("string");
                    }});
                }}).when().
                post("https://petstore.swagger.io/v2/pet").
                then().
                statusCode(200)
                ;
        /*given().log().all().when().post("/1").then().log().all().statusCode(200);*/

    }

    @Test public void
    test_pet_put_update() {

        given().log().all().accept(ContentType.JSON).
                contentType(ContentType.JSON).
        body(new HashMap<String, Object>() {{
                put("id", 1);
                put("category", new HashMap<String, Object>() {{
                put("id", 1);
                put("name", "String");
            }});
                put("name", "doggie");
                put("photoUrls",new ArrayList<String>(){{
                    add("string");
                }});
        }}).
                when().
                put("https://petstore.swagger.io/v2/pet").
                then().
                statusCode(200)
        ;
        /*given().log().all().accept(ContentType.JSON).contentType(ContentType.JSON).put("/1").then()
        .statusCode(200);*/

    }

    @Test public void
    test_pet_get_by_status() {

        given().queryParam("status","available").accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                get("https://petstore.swagger.io/v2/pet/findByStatus").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_pet_get_by_tags() {
        String[] myArray= new String[1];
        myArray[0]="tag1";


        given().log().all().queryParam("tags", myArray).accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                get("https://petstore.swagger.io/v2/pet/findByTags").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_pet_get_by_id() {

        given().log().all().pathParam("petId",1).accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                get("https://petstore.swagger.io/v2/pet/{petId}").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_pet_post_update_form() {

        given().log().all().formParam("name","a").formParam("status","set").pathParam("petId",1).accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                post("https://petstore.swagger.io/v2/pet/{petId}").
                then().
                statusCode(200)
        ;


    }

    @Test public void
    test_pet_delete() {

        given().log().all().accept(ContentType.JSON).
                contentType(ContentType.JSON).pathParam("petId", 1).
                when().
                delete("https://petstore.swagger.io/v2/pet/{petId}").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_pet_post_upload_image() {

        given().log().all().pathParam("petId",1).accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                post("https://petstore.swagger.io/v2/pet/{petId}/uploadImage").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_store_get_inventory() {

        given().accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                get("https://petstore.swagger.io/v2/store/inventory").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_store_post() {

        given().log().all().accept(ContentType.JSON).
                contentType(ContentType.JSON).body(new HashMap<String, Object>() {{
                put("id", 1);
                put("petId",1);
                put("quantity",1);
                put("shipDate","2019-01-10T08:26:47.409Z");
                put("status","placed");
                put("complete", Boolean.FALSE);
        }}).
                when().
                post("https://petstore.swagger.io/v2/store/order").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_store_get_purchase_order() {

        given().log().all().pathParameter("petId",1).accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                get("https://petstore.swagger.io/v2/store/order/{petId}").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_store_delete_purchase_order() {

        given().log().all().pathParameter("orderId",1).accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                delete("https://petstore.swagger.io/v2/store/order/{orderId}").
                then().
                statusCode(200)
        ;

    }



    @Test public void
    test_user_post_create_user() {

        given().log().all().accept(ContentType.JSON).
                    contentType(ContentType.JSON).body(new HashMap<String, Object>() {{
                put("id", 0);
                put("username","string");
                put("firstName","string");
                put("lastName","string");
                put("email","string");
                put("password","string");
                put("phone","string");
                put("userStatus",0);
            }}).
                    when().
                    post("https://petstore.swagger.io/v2/user").
                    then().
                    statusCode(200)
            ;

        }

    @Test public void
    test_user_post_create_with_array() {

        given().log().all().accept(ContentType.JSON).
                contentType(ContentType.JSON).body(arrayList).
                when().
                delete("https://petstore.swagger.io/v2/user/createWithList").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_user_post_create_with_list() {

        given().log().all().accept(ContentType.JSON).
                contentType(ContentType.JSON).body(linkedList).
                when().
                post("https://petstore.swagger.io/v2/user/createWithList").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_user_get_login() {

        given().log().all().queryParam("username","John").queryParam("password","John").accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                get("https://petstore.swagger.io/v2/user/login").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_user_get_logout() {

        given().log().all().accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                get("https://petstore.swagger.io/v2/user/logout").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_user_get_user_by_user_name() {

        given().log().all().pathParam("username","user1").accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                get("https://petstore.swagger.io/v2/user/{username}").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_user_put_updated_user() {

        given().log().all().pathParam("username","user1").accept(ContentType.JSON).
                contentType(ContentType.JSON).body(new HashMap<String, Object>() {{
            put("id", 1);
            put("username","string");
            put("firstName","string");
            put("lastName","string");
            put("email","string");
            put("password","string");
            put("phone","string");
            put("userStatus",0);
        }}).
                when().
                post("https://petstore.swagger.io/v2/user/{username}").
                then().
                statusCode(200)
        ;

    }

    @Test public void
    test_user_delete_user() {

        given().log().all().pathParam("username","user1").accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().
                delete("https://petstore.swagger.io/v2/user/{username}").
                then().
                statusCode(200)
        ;

    }

}