/**
 * Copyright 2012-2013 Continuuity,Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.continuuity.weave.api.logging;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.TimeZone;

/**
 * A {@link LogHandler} that prints the {@link LogEntry} through a {@link PrintWriter}.
 */
public final class PrinterLogHandler implements LogHandler {

  private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'");
      format.setTimeZone(TimeZone.getTimeZone("UTC"));
      return format;
    }
  };

  private final PrintWriter writer;
  private final Formatter formatter;

  /**
   * Creates a {@link PrinterLogHandler} which has {@link LogEntry} written to the given {@link PrintWriter}.
   * @param writer The write that log entries will write to.
   */
  public PrinterLogHandler(PrintWriter writer) {
    this.writer = writer;
    this.formatter = new Formatter(writer);
  }

  @Override
  public void onLog(LogEntry logEntry) {
    String utc = timestampToUTC(logEntry.getTimestamp());

    formatter.format("%s %-5s %s [%s] [%s] %s:%s(%s:%d) - %s\n",
                     utc,
                     logEntry.getLogLevel().name(),
                     logEntry.getLoggerName(),
                     logEntry.getHost(),
                     logEntry.getThreadName(),
                     logEntry.getSourceClassName(),
                     logEntry.getSourceMethodName(),
                     logEntry.getFileName(),
                     logEntry.getLineNumber(),
                     logEntry.getMessage());
    formatter.flush();

    StackTraceElement[] stackTraces = logEntry.getStackTraces();
    if (stackTraces != null) {
      for (StackTraceElement stackTrace : stackTraces) {
        writer.append("\tat ").append(stackTrace.toString());
        writer.println();
      }
      writer.flush();
    }
  }

  private String timestampToUTC(long timestamp) {
    return DATE_FORMAT.get().format(new Date(timestamp));
  }
}
