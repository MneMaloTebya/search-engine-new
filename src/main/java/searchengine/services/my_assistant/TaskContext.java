package searchengine.services.my_assistant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import searchengine.services.indexing.IndexingServiceImpl;
import searchengine.services.page_parser.RecursiveTask;

import java.util.ArrayList;
import java.util.List;

public class TaskContext {

    private List<RecursiveTask> tasks = new ArrayList<>();
//    private static boolean poolIsStopped = false;

    private static final Log log = LogFactory.getLog(IndexingServiceImpl.class);

//    public boolean isPoolIsStopped() {
//        return poolIsStopped;
//    }
//
//    public void setPoolIsStopped(boolean poolIsStopped) {
//        this.poolIsStopped = poolIsStopped;
//    }

    public void addTask(RecursiveTask task) {
        tasks.add(task);
    }

//    public void waitRecursiveTask() {
//        if (poolIsStopped) {
//            tasks.forEach(ForkJoinTask::isCancelled);
//        }
//    }

    public void stopRecursiveTask() {
        tasks.forEach(t -> {
            t.cancel(true);
            log.debug("Таска: " + t + " завершена: " + t.isCancelled());
        });
        log.debug("Количество задач: " + tasks.size());
//        tasks.clear();
    }
}
