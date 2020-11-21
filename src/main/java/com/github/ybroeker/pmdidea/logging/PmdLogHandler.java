package com.github.ybroeker.pmdidea.logging;

import java.util.logging.*;

import org.slf4j.LoggerFactory;


class PmdLogHandler extends Handler {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PmdLogHandler.class);

    public PmdLogHandler() {
        Formatter formatter = new Formatter() {
            @Override
            public String format(final LogRecord record) {
                return super.formatMessage(record);
            }
        };
        this.setFormatter(formatter);
    }

    @Override
    public void publish(final LogRecord record) {
        if (LOGGER.isDebugEnabled()) {
            final String message = this.getFormatter().format(record);
            LOGGER.debug("{}: {}", record.getLoggerName(), message);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
