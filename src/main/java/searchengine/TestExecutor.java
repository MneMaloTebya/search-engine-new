package searchengine;

import java.util.concurrent.*;

public class TestExecutor {
    public static void main(String[] args) {
        ex();
    }

    public static void ex() {
        Callable<String> task = () -> Thread.currentThread().getName();
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            Future result = service.submit(task);
            try {
                System.out.println(result.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(service.isShutdown());
        service.shutdown();
        System.out.println(service.isShutdown());

        for (int i = 0; i < 5; i++) {
            Future result = service.submit(task);
            try {
                System.out.println(result.get() + "новый цикл");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(service.isShutdown());
        service.shutdown();
        System.out.println(service.isShutdown());
    }
}

