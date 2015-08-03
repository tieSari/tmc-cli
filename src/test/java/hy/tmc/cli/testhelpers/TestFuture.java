package hy.tmc.cli.testhelpers;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.listeners.ResultListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class TestFuture<T> implements ListenableFuture<T> {

    private final T value;
    
    public TestFuture(T value) {
        this.value = value;
    }
    
    @Override
    public void addListener(Runnable r, Executor exctr) {
        r.run();
    }

    @Override
    public boolean cancel(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public T get() throws InterruptedException, ExecutionException {
        return value;
    }

    @Override
    public T get(long l, TimeUnit tu) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
