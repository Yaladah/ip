package chatBot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import TaskFunctions.*;

public class Drive {
    protected String path;

    public Drive(String path) {
        this.path = path;
    }

    public List<Task> load() throws DukeException {
        List<Task> taskList = new ArrayList<>();
        try {
            File file = new File("./data/data.txt");
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while(scanner.hasNextLine()) {
                String taskInfo = scanner.nextLine();
                String taskType = taskInfo.split(" ")[0];
                switch (taskType) {
                case "[T]":
                    TodoTask todoTask = new TodoTask(taskInfo.split(" ")[1]);
                    if (taskInfo.split(" ")[1] == "[X]") {
                        todoTask.check();
                    }
                    taskList.add(todoTask);
                    break;

                case "[D]":
                    String deadlineName = taskInfo.split(" /by ")[0];
                    String deadlineTime = taskInfo.split(" /by ")[1];
                    DeadlineTask deadlineTask = new DeadlineTask(deadlineName, deadlineTime);
                    if (taskInfo.split(" ")[1] == "[X]") {
                        deadlineTask.check();
                    }
                    taskList.add(deadlineTask);

                case "[E]":
                    String eventName = taskInfo.split(" /from ")[0];
                    String eventDate = taskInfo.split(" /from ")[1];
                    String startTime = eventDate.split(" /to ")[0];
                    String endTime = eventDate.split(" /to ")[1];
                    EventTask eventTask = new EventTask(eventName, startTime, endTime);
                    if (taskInfo.split(" ")[1] == "[X]") {
                        eventTask.check();
                    }
                    taskList.add(eventTask);

                default:
                    throw new DukeException("Problems encountered while loading data.");
                }
            }
        } catch (FileNotFoundException f) {
            new File(path, "data.txt");
        } catch (Exception e) {
            throw new DukeException("Oops something went wrong...");
        }
        return taskList;
    }
}
