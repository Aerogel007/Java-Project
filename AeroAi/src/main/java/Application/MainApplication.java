package Application;

import JavaFX.FrontendProcess; // Your JavaFX application

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.boot.SpringApplication;

import springboot.SpringBoot;

/**
 * @author Kiran Jung Shah
 * @version 1.0
 * 
 * in this app you will have some buttons like AI, polite, flirty. and you have an input text box.
 * once you enter your text here and press the button of you choice. it will so the corresponding response.
 * 
 * and you can start the app by opening the .exe. if you press close windows button (x) i have modified it 
 * to not execute it's default behaviour but just hide the window. and you can control the java fx window 
 * from system tray icon. if you press exist it does system.close(). which closes everything
 * 
 * while the app is running. if you press the .exe again it will not open the app. because a instance is already running.
 * and java fx can run only one instance at a time. but i found a workaaround for it. for this i knew springboot was using
 * 8080 default server. so if it was occupied i created an httprequest, for the server which will show the window of 
 * previously running instance. 
 * 
 * The MainApplication class in Java starts a Spring Boot application in a separate thread and a JavaFX
 * application in the JavaFX Application thread, by default.
 * 
 */
public class MainApplication {
    public static void main(String[] args) {
       

        /*we run this when we click the .exe file. and when we press cross. it hides the stage
         * now, when we press the .exe icon. it does nothing cause you cannot run another instance.
         * so we are checking a condition to see if the thread is already running if it does. just show the stage
         * if it doesn't lauch it. note(you can close the app by pressing the exit pop up menu from system tray)
         */
        boolean isPortAvailable;
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            // Port is available
            isPortAvailable = true;
        } catch (IOException e) {
            // Port is in use
            isPortAvailable = false;
            System.out.println("it detected port use");
        }
        
        if(isPortAvailable){

            System.setProperty("java.awt.headless", "false");//without this the program would run, but system tray would not work.
            //system tray will work if we run frontendprocess.java independtly too
            //when headless is true, it mean that jvm setup where no GUI is available.bY default jvm operates in headless mode
            //System tray feature of awt requires gui environment to render tray icons and handle graphical operations
            //as a result usng abstract window toolkit (awt) in headless mode causes the error "system tray icon is not supported"

            SpringApplication.run(SpringBoot.class, args);
        
            // Launch the JavaFX application
            FrontendProcess.launch(FrontendProcess.class, args);
            
        }
        else{
             try {
                String apiUrl = "http://localhost:8080/api/SHOW"; //this is where the server has the sceneShow() method
                // Send a POST request to show the stage
                sendPostRequest(apiUrl);  
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }


    private static void sendPostRequest(String urlString) throws IOException, InterruptedException {
    

        // Create the HTTP request since we are not passing any commands(body) to the api/SHOW uri.
        //in post i passed nobody request. then we created a HTTP CLIENT  and used it to send the request.
        //then got response in string to check if it worked.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        // Send the request using HttpClient
        HttpClient client = HttpClient.newHttpClient();
        System.out.println("Http Request send");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
         
       

}
