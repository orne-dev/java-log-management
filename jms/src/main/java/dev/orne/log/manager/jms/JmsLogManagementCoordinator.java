package dev.orne.log.manager.jms;

/*-
 * #%L
 * Orne Log Management - JMS
 * %%
 * Copyright (C) 2022 - 2025 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.util.Objects;
import java.util.UUID;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementCoordinator;
import dev.orne.log.manager.LogManagementEngine;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppenderConfig;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;

/**
 * Implementation of log management coordinator that coordinates changes
 * through a JMS topic.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class JmsLogManagementCoordinator
implements LogManagementCoordinator, MessageListener, AutoCloseable {

    private static final String JMS_SEND_ERROR = "Error sending log management JMS coordination message";

    /** The logger of the class. */
    private static final Logger LOG = LoggerFactory.getLogger(JmsLogManagementCoordinator.class);

    /** The message type JMS property. */
    public static final String MESSAGE_TYPE_PROPERTY = "messageType";
    /** The message type value. */
    public static final String MESSAGE_TYPE = "orne-log-management";
    /** The sender ID JMS property. */
    public static final String SENDER_ID_PROPERTY = "senderId";
    /** The event type JMS property. */
    public static final String EVENT_TYPE_PROPERTY = "eventType";
    /** The set level event type. */
    public static final String SET_LEVEL_EVENT = "set-level";
    /** The create appender event type. */
    public static final String CREATE_APPENDER_EVENT = "create-appender";
    /** The delete appender event type. */
    public static final String DELETE_APPENDER_EVENT = "delete-appender";
    /** The attach appender event type. */
    public static final String ATTACH_APPENDER_EVENT = "attach-appender";
    /** The detach appender event type. */
    public static final String DETACH_APPENDER_EVENT = "detach-appender";
    /** The logger name JMS property. */
    public static final String LOGGER_PROPERTY = "logger";
    /** The level name JMS property. */
    public static final String LEVEL_PROPERTY = "level";
    /** The appender name JMS property. */
    public static final String APPENDER_PROPERTY = "appender";

    /** The log management engine. */
    private final LogManagementEngine engine;
    /** The JMS context. */
    private final JMSContext context;
    /** The JMS topic. */
    private final Topic topic;
    /** The sender ID. */
    private final String senderId;
    /** The consumer ID. */
    private final String consumerId;
    /** The JMS messages consumer. */
    private final JMSConsumer consumer;

    /**
     * Creates a new instance.
     * 
     * @param engine   The log management engine.
     * @param config   The log management configuration.
     * @param context  The JMS context.
     * @param topic    The JMS topic to use for coordination.
     * @param senderId The sender ID to identify messages from this instance.
     */
    public JmsLogManagementCoordinator(
            final LogManagementEngine engine,
            final LogManagementConfig config,
            final JMSContext context,
            final Topic topic,
            final String senderId) {
        this.engine = Objects.requireNonNull(engine, "Log management engine cannot be null");
        this.context = Objects.requireNonNull(context, "JMS Context cannot be null");
        this.topic = Objects.requireNonNull(topic, "JMS Topic cannot be null");
        this.senderId = Objects.requireNonNull(senderId, "Sender ID cannot be null");
        this.consumerId = UUID.randomUUID().toString();
        this.consumer = context.createSharedDurableConsumer(
                topic,
                this.consumerId,
                MESSAGE_TYPE_PROPERTY + " = '" + MESSAGE_TYPE + "'");
        this.consumer.setMessageListener(this);
    }

    /**
     * Returns the log management engine.
     * 
     * @return The log management engine.
     */
    protected LogManagementEngine getEngine() {
        return this.engine;
    }

    /**
     * Returns the JMS context.
     * 
     * @return The JMS context.
     */
    protected JMSContext getContext() {
        return this.context;
    }

    /**
     * Returns the JMS topic.
     * 
     * @return The JMS topic.
     */
    protected Topic getTopic() {
        return this.topic;
    }

    /**
     * Returns the sender ID.
     * 
     * @return The sender ID.
     */
    protected String getSenderId() {
        return this.senderId;
    }

    /**
     * Returns the consumer ID.
     * 
     * @return The consumer ID.
     */
    protected String getConsumerId() {
        return this.consumerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(
            final Message message) {
        try {
            final String eventType = message.getStringProperty(EVENT_TYPE_PROPERTY);
            final String sourceSenderId = message.getStringProperty(SENDER_ID_PROPERTY);
            LOG.info("Received JMS log management message of type {} from sender {}", eventType, sourceSenderId);
            switch (eventType) {
                case SET_LEVEL_EVENT: {
                    setLevel(message);
                    break;
                }
                case CREATE_APPENDER_EVENT: {
                    createAppender(message);
                    break;
                }
                case DELETE_APPENDER_EVENT: {
                    deleteAppender(message);
                    break;
                }
                case ATTACH_APPENDER_EVENT: {
                    attachAppender(message);
                    break;
                }
                case DETACH_APPENDER_EVENT: {
                    detachAppender(message);
                    break;
                }
                default:
                    LOG.warn("Received JMS log management message with unknown event type: {}", eventType);
            }
        } catch (final JMSException | LogManagementException | RuntimeException e) {
            LOG.error("Error processing JMS log management message", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLevel(
            final String logger,
            final @Nullable String level)
    throws LogManagementException {
        try {
            final Message message = this.context.createMessage();
            message.setStringProperty(MESSAGE_TYPE_PROPERTY, MESSAGE_TYPE);
            message.setStringProperty(EVENT_TYPE_PROPERTY, SET_LEVEL_EVENT);
            message.setStringProperty(LOGGER_PROPERTY, logger);
            message.setStringProperty(LEVEL_PROPERTY, level);
            this.context.createProducer().send(this.topic, message);
        } catch (final JMSException e) {
            throw new LogManagementException(JMS_SEND_ERROR, e);
        }
    }

    /**
     * Sets the level based on the received JMS message.
     * 
     * @param message   The JMS message.
     * @throws JMSException               If an error occurs processing the JMS message.
     * @throws LogManagementException     If an error occurs changing the log level.
     */
    protected void setLevel(
            final Message message)
    throws JMSException, LogManagementException {
        final String logger = message.getStringProperty(LOGGER_PROPERTY);
        final String level = message.getStringProperty(LEVEL_PROPERTY);
        this.engine.setLevel(logger, level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAppender(
            final ManagedAppenderConfig config)
    throws LogManagementException {
        try {
            final Message message = this.context.createObjectMessage(config);
            message.setStringProperty(MESSAGE_TYPE_PROPERTY, MESSAGE_TYPE);
            message.setStringProperty(EVENT_TYPE_PROPERTY, CREATE_APPENDER_EVENT);
            this.context.createProducer().send(this.topic, message);
        } catch (final JMSException e) {
            throw new LogManagementException(JMS_SEND_ERROR, e);
        }
    }

    /**
     * Creates a appender based on the received JMS message.
     * 
     * @param message   The JMS message.
     * @throws JMSException               If an error occurs processing the JMS message.
     * @throws LogManagementException     If an error occurs creating the appender.
     */
    protected void createAppender(
            final Message message)
    throws JMSException, LogManagementException {
        final ManagedAppenderConfig config = message.getBody(ManagedAppenderConfig.class);
        this.engine.createAppender(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAppender(
            final String appender)
    throws LogManagementException {
        try {
            final Message message = this.context.createMessage();
            message.setStringProperty(MESSAGE_TYPE_PROPERTY, MESSAGE_TYPE);
            message.setStringProperty(EVENT_TYPE_PROPERTY, DELETE_APPENDER_EVENT);
            message.setStringProperty(APPENDER_PROPERTY, appender);
            this.context.createProducer().send(this.topic, message);
        } catch (final JMSException e) {
            throw new LogManagementException(JMS_SEND_ERROR, e);
        }
    }

    /**
     * Deletes a appender based on the received JMS message.
     * 
     * @param message   The JMS message.
     * @throws JMSException               If an error occurs processing the JMS message.
     * @throws LogManagementException     If an error occurs deleting the appender.
     */
    protected void deleteAppender(
            final Message message)
    throws JMSException, LogManagementException {
        final String appender = message.getStringProperty(APPENDER_PROPERTY);
        this.engine.deleteAppender(appender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachAppender(
            final String logger,
            final String appender)
    throws LogManagementException {
        try {
            final Message message = this.context.createMessage();
            message.setStringProperty(MESSAGE_TYPE_PROPERTY, MESSAGE_TYPE);
            message.setStringProperty(EVENT_TYPE_PROPERTY, ATTACH_APPENDER_EVENT);
            message.setStringProperty(APPENDER_PROPERTY, appender);
            message.setStringProperty(LOGGER_PROPERTY, logger);
            this.context.createProducer().send(this.topic, message);
        } catch (final JMSException e) {
            throw new LogManagementException(JMS_SEND_ERROR, e);
        }
    }

    /**
     * Attaches a appender based on the received JMS message.
     * 
     * @param message   The JMS message.
     * @throws JMSException               If an error occurs processing the JMS message.
     * @throws LogManagementException     If an error occurs attaching the appender.
     */
    protected void attachAppender(
            final Message message)
    throws JMSException, LogManagementException {
        final String logger = message.getStringProperty(LOGGER_PROPERTY);
        final String appender = message.getStringProperty(APPENDER_PROPERTY);
        this.engine.attachAppender(logger, appender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detachAppender(
            final String logger,
            final String appender)
    throws LogManagementException {
        try {
            final Message message = this.context.createMessage();
            message.setStringProperty(MESSAGE_TYPE_PROPERTY, MESSAGE_TYPE);
            message.setStringProperty(EVENT_TYPE_PROPERTY, DETACH_APPENDER_EVENT);
            message.setStringProperty(APPENDER_PROPERTY, appender);
            message.setStringProperty(LOGGER_PROPERTY, logger);
            this.context.createProducer().send(this.topic, message);
        } catch (final JMSException e) {
            throw new LogManagementException(JMS_SEND_ERROR, e);
        }
    }

    /**
     * Detaches a appender based on the received JMS message.
     * 
     * @param message   The JMS message.
     * @throws JMSException               If an error occurs processing the JMS message.
     * @throws LogManagementException     If an error occurs detaching the appender.
     */
    protected void detachAppender(
            final Message message)
    throws JMSException, LogManagementException {
        final String logger = message.getStringProperty(LOGGER_PROPERTY);
        final String appender = message.getStringProperty(APPENDER_PROPERTY);
        this.engine.detachAppender(logger, appender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        this.context.unsubscribe(this.consumerId);
        this.consumer.close();
        this.context.close();
    }
}
