package chatBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import TaskFunctions.*;

public class Duke {
    public static String projName = " ____        _        \n"
                                    + "|  _ \\ _   _| | _____ \n"
                                    + "| | | | | | | |/ / _ \\\n"
                                    + "| |_| | |_| |   <  __/\n"
                                    + "|____/ \\__,_|_|\\_\\___|\n";
    private TaskManager taskList;
    private Drive drive;

//    private void addTask(Task task, String name) {
//        taskList.add(task);
//        System.out.println("Item added: " + name);
//    }
    public Duke(String filePath) {
        this.drive = new Drive(filePath);
        try {
            taskList = new TaskManager(drive.load());
        } catch (DukeException e) {
            System.out.println("File does not exist, creating a new one now!");
            taskList = new TaskManager();
        }
    }

    public boolean readInput(String input) throws DukeException, IOException {
        String firstInput = input.split(" ")[0];

        switch(firstInput) {
            case "list":
                System.out.println("Here are the tasks you asked for!");
                for (int i = 0; i < taskList.getSize(); i += 1) {
                    int currItem = i + 1;
                    System.out.println(currItem + ": " + taskList.get(i));
                }
                System.out.println("You now have " + taskList.getSize() + " items in your list.");
                return true;

            case "bye":
                System.out.println("It was a pleasure to help, goodbye!");
                return false;

            case "mark":
                if (input.split(" ").length < 2) {
                    throw new DukeException("Mark? Mark what?");
                }
                try {
                    int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                    Task selectedTask = taskList.get(taskIndex);
                    System.out.println("Done! I've marked this task as done :D");
                    selectedTask.check();
                    System.out.println(selectedTask);
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    throw new DukeException("Oops, that task number does not exist");
                }

            case "unmark":
                if (input.split(" ").length < 2) {
                    throw new DukeException("Unmark? Unmark what?");
                }
                try {
                    int untaskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                    Task unselectedTask = taskList.get(untaskIndex);
                    System.out.println("This task is apparently not done huh D:");
                    unselectedTask.unCheck();
                    System.out.println(unselectedTask);
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    throw new DukeException("Oops, that task number does not exist");
                }

            case "todo":
                try {
                    String todoTaskName = input.substring(5);
                    TodoTask todoTask = new TodoTask(todoTaskName);
//                    addTask(todoTask, todoTaskName);
                    taskList.add(todoTask);
                    return true;
                } catch (StringIndexOutOfBoundsException e) {
                    throw new DukeException("Oops, you can't enter an empty task!");
                } catch (IOException e) {
                    throw new DukeException(e.getMessage());
                }

        case "deadline":
                String deadlineDetails = input.substring(9);
                if (deadlineDetails.split(" /by ").length < 2) {
                    throw new DukeException("Wait a minute, you're missing something! Could be the name or date...");
                }
                String deadlineName = deadlineDetails.split(" /by ")[0];
                String deadlineDate = deadlineDetails.split(" /by ")[1];
                DeadlineTask deadlineTask = new DeadlineTask(deadlineName, deadlineDate);
                taskList.add(deadlineTask);
                return true;

            case "event":
                String eventDetails = input.substring(6);
                if (eventDetails.split(" /from ").length < 2 || eventDetails.split(" /to ").length < 2) {
                    throw new DukeException("Hold up, you might be missing something here buddy!");
                }
                String eventName = eventDetails.split(" /from ")[0];
                String eventDate = eventDetails.split(" /from ")[1];
                String eventStart = eventDate.split(" /to ")[0];
                String eventEnd = eventDate.split(" /to ")[1];
                EventTask eventTask = new EventTask(eventName, eventStart, eventEnd);
                taskList.add(eventTask);
                return true;

            case "delete":
                if (input.split(" ").length < 2) {
                    throw new DukeException("Delete? Delete what?");
                }
                try {
                    int deleteIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                    Task deleteTask = taskList.get(deleteIndex);
                    System.out.println("Done! " + deleteTask + " has been deleted for good.");
                    taskList.remove(deleteIndex);
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    throw new DukeException("Oops, that task number does not exist");
                }


            default:
                throw new DukeException("Oops I do not recognise this command...");
        }
    }

    public static void main(String[] args) throws DukeException {
        System.out.println("Yo! The name is\n" + projName);
        System.out.println("How might I help you today?");
        Scanner scanner = new Scanner(System.in);
        Duke duke = new Duke("data/data.txt");

        boolean cont = true;

        while (cont) {
            String input = scanner.nextLine();
            try {
                cont = duke.readInput(input);
            } catch (DukeException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }
}
