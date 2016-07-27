package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.MDC;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MDCEventLoopGroup implements EventLoopGroup {

    private Map<String, String> mdcContext;
    private EventLoopGroup delegate;

    public final EventLoopGroup fromThread(EventLoopGroup delegate) {
        return new MDCEventLoopGroup(MDC.getCopyOfContextMap(), delegate);
    }

    private MDCEventLoopGroup(Map<String, String> mdcContext, EventLoopGroup delegate) {
        this.mdcContext = mdcContext;
        this.delegate = delegate;
    }

    @Override
    public boolean isShuttingDown() {
        return delegate.isShuttingDown();
    }

    @Override
    public Future<?> shutdownGracefully() {
        return delegate.shutdownGracefully();
    }

    @Override
    public Future<?> shutdownGracefully(long l, long l1, TimeUnit timeUnit) {
        return delegate.shutdownGracefully(l, l1, timeUnit);
    }

    @Override
    public Future<?> terminationFuture() {
        return delegate.terminationFuture();
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public EventLoop next() {
        return delegate.next();
    }

    @Override
    public Iterator<EventExecutor> iterator() {
        return delegate.iterator();
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return delegate.submit(runnable);
    }

    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return delegate.invokeAll(tasks);
    }

    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return delegate.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.invokeAny(tasks, timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Runnable runnable, T t) {
        return delegate.submit(runnable, t);
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return delegate.submit(callable);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable runnable, long l, TimeUnit timeUnit) {
        return delegate.schedule(runnable, l, timeUnit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long l, TimeUnit timeUnit) {
        return delegate.schedule(callable, l, timeUnit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long l, long l1, TimeUnit timeUnit) {
        return delegate.scheduleAtFixedRate(runnable, l, l1, timeUnit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long l, long l1, TimeUnit timeUnit) {
        return delegate.scheduleWithFixedDelay(runnable, l, l1, timeUnit);
    }

    @Override
    public ChannelFuture register(Channel channel) {
        return delegate.register(channel);
    }

    @Override
    public ChannelFuture register(Channel channel, ChannelPromise channelPromise) {
        return delegate.register(channel, channelPromise);
    }

    @Override
    public void execute(Runnable runnable) {
        delegate.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, String> oldMdcContext = MDC.getCopyOfContextMap();
                setContextMap(mdcContext);
                try {
                    runnable.run();
                } finally {
                    setContextMap(oldMdcContext);
                }

            }
        });
    }

    private void setContextMap(Map<String, String> context) {
        if (context == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(context);
        }
    }
}
