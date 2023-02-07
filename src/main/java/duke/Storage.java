package duke;

import task.DeadlineTask;
import task.EventTask;
import task.Task;
import task.TaskList;
import task.TodoTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Storage class handles the interaction between local hard drive and Duke.
 */
public class Storage {
    File file;
    private final static String SAVED_PATH = "data/tasks.txt";

    /**
     * Constructor for Storage instance.
     *
     * @param filePath Path to locate file relative to project root.
     */
    public Storage(String filePath) {
        file = new File(filePath);
    }

    /**
     * Method to write current TaskList onto file on hard drive.
     *
     * @param taskList TaskList with tasks in Duke.
     * @throws IOException Thrown when file system encounters an error.
     * @throws DukeException Thrown when unexpected behaviour occurs with Duke.
     */
    public void save(TaskList taskList) throws IOException, DukeException {
        if (!file.getParentFile().exists()) { //check if directory exists, else make one
            if (!file.getParentFile().mkdirs()) {
                throw new DukeException("Directories can't be made? Or did something go wrong...");
            }
        }

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new DukeException("This file both doesn't exist and cannot be made D:");
            }
        }

        FileWriter fileWriter = new FileWriter(file);

        for (Task task : taskList.asList()) {
            fileWriter.write(task.toString());
            fileWriter.write(System.lineSeparator());
        }

        fileWriter.close();
//        Ui.showSavedDataMessage();
    }

    /**
     * Retrieves data from a previous session of Duke, if it exists.
     *
     * @return An array of Tasks.
     * @throws DukeException Thrown when unexpected behaviour occurs with Duke.
     */
    public Task[] load() throws DukeException {
        File f = new File(SAVED_PATH);

        try {
            if (!f.exists()) { //file didn't exist yet
                Ui.showNewUserMessage();
                return new Task[0];
            } else {
                System.out.println("Welcome back!");
                System.out.println("Here's the tasks you have:");
                Scanner scanner = new Scanner(f);
                ArrayList<Task> taskList = new ArrayList<>();

                while (scanner.hasNextLine()) {
                    Task task;
                    String currTask = scanner.nextLine();
                    String taskType = currTask.split(" ")[0];
                    String taskCompletion = currTask.substring(4,7);
                    Boolean complete = Objects.equals(taskCompletion, "[X]");
                    String taskDetails;

                    if (complete) {
                        taskDetails = currTask.split(" \\[X] ")[1];
                    } else {
                        taskDetails = currTask.split(" \\[ ] ")[1];
                    }

                    switch (taskType) {
                    case "[T]":
                        task = new TodoTask(taskDetails, complete);
                        break;

                    case "[D]":
                        String deadlineName = taskDetails.split(" \\(by: ")[0];
                        String date = taskDetails.split(" \\(by: ")[1].split("\\)")[0];
                        LocalDate deadLine = LocalDate.parse(date);
                        task = new DeadlineTask(deadlineName, deadLine, complete);
                        break;

                    case "[E]":
                        String eventName = taskDetails.split(" \\(from:")[0];
                        String eventPeriod = taskDetails.split("\\(from: ")[1];
                        String startStr = eventPeriod.split(" to: ")[0];
                        String endStr = eventPeriod.split(" to: ")[1].split("\\)")[0];
                        LocalDate start = LocalDate.parse(startStr);
                        LocalDate end = LocalDate.parse(endStr);
                        task = new EventTask(eventName, start, end, complete);
                        break;

                    default:
                        task = new Task(taskDetails);
                    }

                    taskList.add(task);
                }

                for (int i = 0; i < taskList.size(); i += 1) {
                    int currItem = i + 1;
                    System.out.println(currItem + ": " + taskList.get(i));
                }

                Ui.showWelcomePrompt();
                return taskList.toArray(new Task[0]);
            }
        } catch (IOException io) {
            throw new DukeException("Something went wrong while trying to load...");
        }
    }
}
