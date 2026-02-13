package dev.orne.log.manager.impl.logback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.EchoEncoder;
import ch.qos.logback.core.spi.LifeCycle;

/**
 * In-memory Logback appender for testing purposes.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2026-02
 * @param <E> The type of log event
 * @since 1.0
 */
public class InMemoryLogbackAppender<E>
extends OutputStreamAppender<E>
implements AutoCloseable {

    /** The in-memory output stream to capture log output. */
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * Creates a new instance.
     * 
     * @param name The name of the appender.
     */
    public InMemoryLogbackAppender(
            final String name) {
        super();
        setName(name);
        setEncoder(new EchoEncoder<>());
        setOutputStream(this.outputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        getEncoder().start();
        getCopyOfAttachedFiltersList().forEach(LifeCycle::start);
        super.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();
        getCopyOfAttachedFiltersList().forEach(LifeCycle::stop);
        getEncoder().stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        stop();
        this.outputStream.close();
    }

    /**
     * Returns the current content of the in-memory log output as a string.
     * 
     * @return The log content.
     */
    public String getOutput() {
        return this.outputStream.toString(StandardCharsets.UTF_8);
    }
}
