package springboot;

import org.springframework.web.bind.annotation.*;

import JavaFX.FrontendProcess;
import javafx.application.Platform;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Map;
/**
 * @author Kiran Jung Shah
 * @version 1.0
 * I am using spring boot with http protocol to send an request to AI21's then get a response.
 * you will new an API key for authetication
 */
@RestController
@RequestMapping("/api")
public class BackendProcess {
            //checking thread this program is running on use static here, because you cannot use thread realated logic outside of an method directly in a class
            //usihng static causes it for this block of code to run when the class i loaded
            static {Thread current = Thread.currentThread();
            System.out.println("Current Thread for backend 1: " + current);}
    

    private static final String AI21_API_KEY = "Your API key here"; // Replace with your AI21 API key
    private static final String AI21_API_URL = "https://api.ai21.com/studio/v1/j2-ultra/chat"; // this uses jurassic 2 model

    

    @PostMapping("/transform") //creating a REST endpoint
    public String transformText(@RequestBody Map<String, String> request) {
        String inputText = request.get("inputText");

        try {
            // Create the JSON payload for AI21 API chat endpoint
            JSONObject message = new JSONObject();
            message.put("text", inputText);
            message.put("role", "user");  // Defines the role of the message

            JSONObject json = new JSONObject();
            json.put("numResults", 1);
            json.put("temperature", 0.7);  // Adjust creativity level
            json.put("messages", new JSONArray().put(message));
            json.put("system", "You are an AI assistant for chatting and paraphrasing, you should be concise");

            // Create the HTTP request to AI21 API
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(AI21_API_URL))
                    .header("Authorization", "Bearer " + AI21_API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(json.toString()))
                    .build();

            // Send the request and receive the response
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Log the raw response for debugging
            System.out.println("API Response: " + response.body());

        //checking thread this program is running on
        Thread current = Thread.currentThread();
        System.out.println("Current Thread for backend 2: " + current);

            // Check if the response status is not 200 (OK)
            if (response.statusCode() != 200) {
                return "Error: AI21 API request failed with status " + response.statusCode();
            }

            // Parse the response based on expected JSON structure for chat
            JSONObject responseBody = new JSONObject(response.body());
            if (responseBody.has("outputs")) {
                JSONArray messagesArray = responseBody.getJSONArray("outputs");
                JSONObject firstMessage = messagesArray.getJSONObject(0);
                String transformedText = firstMessage.getString("text");
                return transformedText.trim();
            } else {
                // Log full response if the expected structure is not found
                System.out.println("Unexpected API Response Structure: " + response.body());
                return "Error: Unexpected response structure received from AI21 API.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


    // Endpoint to trigger staticShow() method in the JavaFX application, this will show the stage from this instance.
    //since we cannot create a new instance. when we run the application
    @PostMapping("/SHOW")
    public String showStage() {
        try {
             // Make sure showStage() is called on the JavaFX Application thread, i did it using platform.runLater() which only
             //runs in javafx thread. if not. we weill get a "not on java fx application thread" but on http-nio-8080-exec-2 where 
             //spring boot runs. java fx has it's own thread for it's UI and is single threaded.
             Platform.runLater(() -> {
            FrontendProcess.showStage(); // Call the static method in the JavaFX application
            });

            return "Stage is now visible.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
