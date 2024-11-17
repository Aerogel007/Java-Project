package JavaFX;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;//to run a different process concurrently using Task<>
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox; // HBox for horizontal button layout
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.awt.*;//for system tray functionality
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;//to make requests with apis.
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;


/**
 * @author Kiran Jung Shah
 * @version 1.0
 * I have created a simple JavaFX UI. that reads your prompts and then appends it with extra string base on the button you press.
 * it will run as utility program in system tray.
 * it also runs an concurrent task to send that promopt to the backend. 
 */
public class FrontendProcess extends Application {
   
    public static Stage staticStage;  // Store reference to the Stage. so it can be used by other classes
    public static TextArea output;
    public static Label staticLabel;

     @Override
    public void start(Stage primaryStage) {
        
        // Create the labels
        Label inputLabel = new Label("Input Script:");
        Label outputLabel = new Label("Output Script:");
        Label copiedLabel = new Label("copied!");
        outputLabel.getStyleClass().add("outputlabel");
        copiedLabel.getStyleClass().add("copiedlabel");
        staticLabel= copiedLabel;
        copiedLabel.setVisible(false);



        staticStage = primaryStage;

        // Create the text areas
        TextArea inputText = new TextArea();
        inputText.setWrapText(true);
        inputText.setPromptText("Enter your text here...");
        inputText.setPrefSize(400, 100); // Set preferred size,
        inputText.setMaxSize(460, 100);//hardcoding max size

        TextArea outputText = new TextArea();
        outputText.setWrapText(true);
        outputText.setPromptText("Transformed text will appear here...");
        outputText.setEditable(false); // Prevent user from editing output

        inputText.getStyleClass().add("my-textarea");
        outputText.getStyleClass().add("my-textarea");



        output= outputText;

        // Create buttons
        Button AIButton = new Button("  AI  ");
        Button politeButton = new Button("Polite");
        Button flirtyButton = new Button("Flirty");
        Button wittyButton = new Button("Witty");
        Button copy1 = new Button("1");
        Button copy2 = new Button("2");
        Button copy3 = new Button("3");
        copy1.getStyleClass().add("copy-button");  // Assigning class for the copy button so it can be in a different set in CSS
        copy2.getStyleClass().add("copy-button");
        copy3.getStyleClass().add("copy-button");



        // Set button actions (for simplicity, just add text to output based on the
        // button clicked)
        AIButton.setOnAction(event ->{
            transformText(inputText.getText(), "AI", outputText);
            ChangingButtonColor.changeButtonColorTemporarilyForButton(AIButton);
        });
        politeButton.setOnAction(event ->{
             transformText(inputText.getText(), "polite", outputText);
        ChangingButtonColor.changeButtonColorTemporarilyForButton(politeButton);
        });
        flirtyButton.setOnAction(event ->{
             transformText(inputText.getText(), "flirty", outputText);
        ChangingButtonColor.changeButtonColorTemporarilyForButton(flirtyButton);
        });
        wittyButton.setOnAction(event ->{
             transformText(inputText.getText(), "witty", outputText);
        ChangingButtonColor.changeButtonColorTemporarilyForButton(wittyButton);
        });
        copy1.setOnAction(event ->{
             copyText(outputText, 1);
        ChangingButtonColor.changeButtonColorTemporarilyForCopy(copy1);
        });
        copy2.setOnAction(event ->{
             copyText(outputText,2);
        ChangingButtonColor.changeButtonColorTemporarilyForCopy(copy2);
        });
        copy3.setOnAction(event ->{
            copyText(outputText,3);
        ChangingButtonColor.changeButtonColorTemporarilyForCopy(copy3);
        });
        HBox textBox =new HBox(10);
        textBox.getChildren().addAll(outputLabel, copiedLabel);

        //layout for copy buttons
        VBox copyButton = new VBox(10);
        copyButton.setAlignment(Pos.CENTER);
        copyButton.setStyle("-fx-padding: 5px;");
        copyButton.getChildren().addAll(copy1, copy2, copy3);
            
        HBox outputField= new HBox(10);
        outputField.setAlignment(Pos.CENTER);
        outputField.setStyle("-fx-padding: 10px;");
        outputField.getChildren().addAll(outputText, copyButton);

        // Layout for the buttons in a horizontal line (HBox)
        HBox buttonLayout = new HBox(20); // 10px spacing between buttons
        buttonLayout.setAlignment(Pos.CENTER); // Align buttons in the center
        buttonLayout.getChildren().addAll(AIButton, politeButton, flirtyButton, wittyButton);

        // Main layout setup (VBox for the text areas and labels)
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        //setting margins to the right form them to move left
        VBox.setMargin(inputLabel, new javafx.geometry.Insets(0, 350, 0, 0 ));
        VBox.setMargin(textBox, new javafx.geometry.Insets(0, 0, 0, 25 ));
        VBox.setMargin(buttonLayout, new javafx.geometry.Insets(0, 50, 0, 0 ));



        layout.setStyle("-fx-padding: 10 10 20 10;");//top,right,bottom,left
        layout.getChildren().addAll(inputLabel, inputText, textBox, outputField, buttonLayout);


        // Prevent children from affecting VBox layout. this will lock the positions of child and prevent layout shuffle when size of child changes.
        //here outputFiled will not grow or change when the layout of vbox changes. 
        VBox.setVgrow(outputField, null);
        VBox.setVgrow(buttonLayout, null);
        HBox.setHgrow(copyButton, null);
        VBox.setVgrow(inputLabel, null);
        VBox.setVgrow(copy1, null);
        HBox.setHgrow(copy1, null);
        //the above code didnot work. i think it's better to fix the postion of objects using max and min sizes instead.

        

        // Scene setup
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setTitle("Aero AI");
        primaryStage.setScene(scene);
        scene.getStylesheets().add("style.css");

        // Set the stage to not show up in taskbar
         primaryStage.initStyle(javafx.stage.StageStyle.UTILITY); // Make it a utility window, that doesn't show up in the taskbar. aka it runs in the background

         Platform.setImplicitExit(false); // Keep JavaFX running in the background when stage is hidden. if we don't then java fx terminates
         //when no stage is visbile. aka implicilty. this method makes sure that only explicit termination is allowed aka System.exit

        /**change the original behavior of close button */
        //we need to do this because when we close the javafx ui by pressing x . the java fx app will terminate
        //and there is no logic to being the window back. some processees maybe from tast will still run
         primaryStage.setOnCloseRequest(event -> {
            System.out.println("Close button clicked");
            event.consume(); // Prevent default close action
            Platform.runLater(() -> {
                // Hide the window when the close button is clicked
                if (primaryStage.isShowing()) {
                    primaryStage.hide();
                }
            });
        });
        
        
        primaryStage.show();
        primaryStage.toFront();
        // Show the tray icon and start system tray setup
   

        //checking thread this program is running on
        Thread current = Thread.currentThread();
        System.out.println("Current Thread for Java UI: " + current);
        Platform.runLater( () -> setupSystemTray(primaryStage));

     }
   /*creating a static method which can be called by Main application for stage to show */
    public static void showStage(){
        staticStage.show();
        staticStage.toFront();
    }
    public static String getOutputText(){
       return(output.getText());
    }

    private void setupSystemTray(Stage stage) {

        // Create a system tray icon with a right-click menu
        if (!SystemTray.isSupported()) {
            System.out.println("System Tray is not supported on this platform.");
            return;
        }

        SystemTray systemTray = SystemTray.getSystemTray();
        TrayIcon trayIcon = createTrayIcon(stage);

        // Add the tray icon to the system tray
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    private TrayIcon createTrayIcon(Stage stage) {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/botIcon.jpeg"));
        if (image == null) {
            System.out.println("Icon image not found.");
        } else {
            System.out.println("Icon image loaded successfully.");
        }
        TrayIcon trayIcon = new TrayIcon(image, "Jurassic2 AI Assist", createPopupMenu(stage));
        trayIcon.setImageAutoSize(true);
         // Add a click listener to the tray icon to open the stage
        ActionListener clickListener = e -> Platform.runLater(() -> {
        stage.show();
        stage.toFront(); // Bring to the front
         });
         trayIcon.addActionListener(clickListener);

                //checking thread this program is running on
                Thread current = Thread.currentThread();
                System.out.println("Current Thread for tray icon: " + current);

        return trayIcon;
    }



private PopupMenu createPopupMenu(Stage stage) {
    PopupMenu popupMenu = new PopupMenu();


    // Create a custom font (size 16, bold)
    Font menuItemFont = new Font("Arial", Font.BOLD, 16);

    popupMenu.addSeparator(); //adds a horizontal line 

    
    // Add "Open" option to popup menu
    MenuItem openItem = new MenuItem("Open");
    openItem.setFont(menuItemFont); // Set font size and style 
    openItem.addActionListener(e -> {
    
        Platform.runLater(() -> {
           // Show the window from the tray icon but without showing it in the taskbar
           stage.show();
           stage.toFront(); // Bring it to the front
        });
    });
    popupMenu.add(openItem);
    
    popupMenu.addSeparator(); 

    // Add "Exit" option to popup menu
    MenuItem exitItem = new MenuItem("Exit");
    exitItem.setFont(menuItemFont); // Set font size and style 
    exitItem.addActionListener(e -> {
        Platform.runLater(() ->System.exit(0) // Exit the application when clicked
        );
    });
    popupMenu.add(exitItem);

    popupMenu.addSeparator(); 

    return popupMenu;
}

    //Method to copy text based on copied button, there are three buttons. for 3 different output iterations
    private void copyText(TextArea output, int button){
        String text = output.getText();
        // Split the text into lines

        String[] lines = text.split("\n");
        System.out.println("the text on the text field :"+ text);//--to Debug--

        switch(button) {
            case 1:
               
        
                // Check if there's at least one line
                if (lines.length > 1) {
                     // regex to Only remove the bullet if the first line starts with a numerical bullet
                    String buffer= lines[0].replaceFirst("^\\d+[\\.)]?\\s*", "").trim();
                    String result = buffer.replaceAll("[\"']", ""); //remove quotes
                    ClipBoard.copyToClipboard(result);
                }
                break;

            case 2:
        
                // Check if there's at least one line5
                if (lines.length > 1) {
                     // regex to Only remove the bullet if the first line starts with a numerical bullet
                     String buffer= lines[1].replaceFirst("^\\d+[\\.)]?\\s*", "").trim();
                     String result = buffer.replaceAll("[\"']", ""); //remove quotes

                     ClipBoard.copyToClipboard(result);
                    }
                break;
            case 3:
        
                // Check if there's at least one line
                if (lines.length > 1) {
                     // regex to Only remove the bullet if the first line starts with a numerical bullet
                     String buffer= lines[2].replaceFirst("^\\d+[\\.)]?\\s*", "").trim();
                     String result = buffer.replaceAll("[\"']", ""); //remove quotes

                     ClipBoard.copyToClipboard(result);
                    }
                break;
            default:
                output.setText("No text to copy right now");

        }
    }



    // Method to transform text based on selected type
    private void transformText(String input, String type, TextArea output) {
        StringBuilder prompt = new StringBuilder();
        boolean hasText = false; //to check if input TextArea has text
        switch (type) {
            case "AI":
                if(input == null || input.trim().isEmpty()){
                    output.setText("use AI button to converse with Aero AI");
                    break;
                }
                else{
                prompt.append(input);
                hasText = true;
                break;
                }
            case "polite":
                if(input == null || input.trim().isEmpty()){
                    output.setText("use polite Button to make the sentence polite.");
                    break;
                }
                else{
                prompt.append("make 3 iterations of this to be Polite :").append(input);
                hasText = true;
                break;
                }
            case "flirty":
                 if(input == null || input.trim().isEmpty()){
                    output.setText("use flitry button to make the sentence flirty.");
                    break;
                }
                else{
                prompt.append("make 3 iterations of this to be Flirty :").append(input);
                hasText = true;
                break;
                }
            case "witty":
                if(input == null || input.trim().isEmpty()){
                output.setText("use witty button to make the sentence Witty.");
                break;
                }
                else{
                prompt.append("make 3 iterations of this to be Witty :").append(input);
                hasText = true;
                break;
                }
            default:
                if(input == null || input.trim().isEmpty()){
                output.setText("use this button to conver with jurassic2 AI");
                hasText = false;
                break;
                }
        }
        if (hasText){
        // task is created to run process in the background, so it won't hamper java fx UI becasue it runs on a different thread.
        //IT IS USED TO SEND DATA TO BACKEND.
        Task<Void> task = new Task<Void>() {
                    //--for debugging-- checking thread this program is running on, turns out this still runs on the javaFX application thread
                    static{
                        Thread current = Thread.currentThread();
                    System.out.println("Current Thread called using static: " + current);
                }
            
            @Override
            protected Void call() throws Exception {
                try {
                            //--for debugging-- checking thread this program is running on, this runs on a different thread
                             Thread current = Thread.currentThread();
                             System.out.println("Current Thread for sending to Backend: " + current);
                    // Send the input text to the Spring Boot backend
                    String transformedText = sendToBackend(prompt.toString());

                    // Once the response is received, update the output text area
                    updateMessage(transformedText);
                } catch (Exception e) {
                    // Handle any exceptions (network issues, etc.)
                    updateMessage("Error: " + e.getMessage());
                }

                
                return null;
            }
            
        };

        // Update the output text area when the task completes
        task.messageProperty().addListener((observable, oldMessage, newMessage) -> output.setText(newMessage));


        // Start the task on a separate thread
        new Thread(task).start();
    }
    
}

    // Method to send the input text to the Spring Boot backend for processing
    private String sendToBackend(String input) throws Exception {
        // Prepare the API URL (make sure Spring Boot is running locally)
        String apiUrl = "http://localhost:8080/api/transform";  // Replace with your Spring Boot backend URL

        // Create JSON payload for the request
        JSONObject json = new JSONObject();
        json.put("inputText", input);

        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(json.toString()))
                .build();

        // Send the request using HttpClient
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Return the transformed text from the response body
        return response.body();
    
    }

}


//     public static void main(String[] args) {
//         launch(args);
//     }
// }
