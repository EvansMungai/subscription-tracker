package com.subscriptiontracker.subscription_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

@SpringBootApplication
public class SubscriptionTrackerApplication {

 private static ConfigurableApplicationContext springContext;
    private static String[] args;

    public static void main(String[] args) {
        SubscriptionTrackerApplication.args = args;
        
        // Start Spring Boot in a separate thread
        new Thread(() -> {
            springContext = SpringApplication.run(SubscriptionTrackerApplication.class, args);
        }).start();
        
        // Launch JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Wait for Spring Boot to start
        waitForSpringBoot();
        
        // Create WebView to display the web dashboard
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        
        // Load the dashboard from localhost
        webEngine.load("http://localhost:8080/dashboard");
        
        // Configure the stage
        primaryStage.setTitle("Subscription Tracker Dashboard");
        primaryStage.setScene(new Scene(webView, 1200, 800));
        primaryStage.setMaximized(true);
        
        // Handle window close
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            if (springContext != null) {
                springContext.close();
            }
            System.exit(0);
        });
        
        primaryStage.show();
    }

    private void waitForSpringBoot() {
        // Simple wait mechanism - in production, you'd want a more sophisticated approach
        try {
            Thread.sleep(3000); // Wait 3 seconds for Spring Boot to start
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void stop() {
        if (springContext != null) {
            springContext.close();
        }
    }
}
