package TaskFunctions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    List<Task> taskList;

    public TaskManager() {
        taskList = new ArrayList<>(100);
    }

    public TaskManager(List<Task> inputList) {
        this.taskList = inputList;
    }

    public List<? extends Task> getList() {
        return taskList;
    }

    public Task get(int i) {
        return taskList.get(i);
    }

    public int getSize() {
        return taskList.size();
    }

    public void remove(int i) {
        taskList.remove(i);
    }

    public void add(Task task) throws IOException {
        taskList.add(task);
        try {
            BufferedWriter io = new BufferedWriter(new FileWriter("./data/data.txt", true));
            io.newLine();
            io.append(task.toString());
            io.close();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
    public static void update(TaskManager newList) throws IOException {
        PrintWriter writer = new PrintWriter("./data/data.txt");
        writer.close();
        List<Task> updatedList = (List<Task>) newList.getList();
        BufferedWriter bwriter = new BufferedWriter(new FileWriter("./data/data.txt", true));
        for (Task t : updatedList) {
            try {
                bwriter.newLine();
                bwriter.append(t.toString());
            } catch (IOException io) {
                throw new IOException(io.getMessage());
            }
        }
    }
}
