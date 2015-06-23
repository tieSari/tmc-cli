package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.util.concurrent.TimeUnit;

public class StopProcess extends Command<Boolean> {

    private ListeningExecutorService threadPool;

    public StopProcess(ListeningExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void checkData() throws ProtocolException {
    }

    public ListeningExecutorService getThreadPool() {
        return threadPool;
    }

    @Override
    public Optional<String> parseData(Object data) {
        Boolean result = (Boolean) data;
        if (result) {
            return Optional.of("Processes killed.");
        }
        return Optional.of("Failed to kill processes.");
    }

    @Override
    public Boolean call() throws Exception {
        threadPool.shutdown();
        threadPool.awaitTermination(3, TimeUnit.SECONDS);
        threadPool.shutdownNow();
        return threadPool.isShutdown();
    }
}
