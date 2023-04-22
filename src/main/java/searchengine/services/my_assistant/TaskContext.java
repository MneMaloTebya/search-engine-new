package searchengine.services.my_assistant;

import searchengine.services.page_parser.RecursiveTask;

import java.util.ArrayList;
import java.util.List;

public class TaskContext {

    private static List<RecursiveTask> tasks = new ArrayList<>();

    public static void addTask(RecursiveTask task) {
        tasks.add(task);
    }

    public static void removeAll() {
        tasks.clear();
    }

    public static List<RecursiveTask> getTasks() {
        return tasks;
    }
}
