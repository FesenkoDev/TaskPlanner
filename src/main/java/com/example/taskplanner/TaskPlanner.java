package com.example.taskplanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.*;

public class TaskPlanner extends Application {
    //private final ArrayList<String> tasks = new ArrayList<>();
    private final ListView<String> taskListView = new ListView<>();
    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Planner");
        connectToDatabase();
        loadTasksFromDatabase();

        Button addTaskButton = new Button("Додати задачу");
        Button deleteTaskButton = new Button("Видалити задачу");

        addTaskButton.setOnAction(e -> openAddTaskWindow());
        deleteTaskButton.setOnAction(e -> deleteSelectedTask());

        VBox buttonBox = new VBox(10);
        buttonBox.getChildren().addAll(addTaskButton, deleteTaskButton);

        BorderPane layout = new BorderPane();
        layout.setLeft(buttonBox);
        layout.setRight(taskListView);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/taskplanner";
            String user = "root";
            String password = "43_IMSCoRPIO_57";

            connection = DriverManager.getConnection(url, user, password);

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "description TEXT)");
        } catch (SQLException e) {System.out.println("Ошибка при подключении к БД: " + e.getMessage());}
    }

    private void saveTaskToDatabase(String title, String description) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tasks (title, description) VALUES (?, ?)");
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {System.out.println("Ошибка при сохранении данных в БД: " + e.getMessage());}
    }

    private void loadTasksFromDatabase() {
        taskListView.getItems().clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT title, description FROM tasks");
            while(resultSet.next()) {
                taskListView.getItems().add(resultSet.getString("title") + " - " + resultSet.getString("description"));
            }
        } catch (SQLException e) {System.out.println("Ошибка при загрузке данных из БД: " + e.getMessage());}
    }

    private void openAddTaskWindow() {
        Stage addTaskStage = new Stage();
        addTaskStage.setTitle("Додати задачу");

        TextField taskTitle = new TextField();
        taskTitle.setPromptText("Назва задачі");

        TextArea taskDescription = new TextArea();
        taskDescription.setPromptText("Опис задачі");

        Button saveButton = new Button("Зберегти");
        saveButton.setOnAction(e -> {
            String title = taskTitle.getText();
            String description = taskDescription.getText();
            if(!title.isEmpty()) {
                saveTaskToDatabase(title, description);
                loadTasksFromDatabase();
                addTaskStage.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(taskTitle, taskDescription, saveButton);

        Scene scene = new Scene(layout, 300, 200);
        addTaskStage.setScene(scene);
        addTaskStage.show();
    }

    private void deleteSelectedTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if(selectedTask != null) {
            String title = selectedTask.split(" - ")[0];
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tasks WHERE title = ?");
                preparedStatement.setString(1, title);
                preparedStatement.executeUpdate();
                loadTasksFromDatabase();
            } catch (SQLException e) {System.out.println("Ошибка при удалении данных из БД: " + e.getMessage());}
        }
    }
}