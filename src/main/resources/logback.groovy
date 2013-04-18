//
// Built on Thu Apr 18 23:56:05 CEST 2013 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.WARN

appender("CONSOLE_APPENDER", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{24} - %msg %xEx%n"
  }
}
appender("DETAIL_APPENDER", RollingFileAppender) {
  file = System.getProperty("user.home") + "/bitj/bitj.log"
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "log/multibit_debug.%d{yyyy-MM-dd}.%i.gz"
    maxHistory = 30
    timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
      maxFileSize = "1MB"
    }
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg %xEx%n"
  }
}
root(INFO, ["CONSOLE_APPENDER", "DETAIL_APPENDER"])
logger("org.multibit", WARN)
logger("com.google.bitcoin", WARN)
logger("com.google.bitcoin.core.BitcoinSerializer", WARN)
