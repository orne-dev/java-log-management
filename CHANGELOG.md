# Orne Log Managemenet Changelog

## 1.0.0 - *Unreleased*

**Initial Release**

### Added

- Log management API
    - Logging system agnostic logging configuration beans 
        - `Level` immutable bean for log levels
        - `Logger` immutable bean for loggers
        - `Appender` immutable bean for appenders
        - `FileRollingPolicy` immutable bean for file rolling policies
        - `FileRollingPolicy` immutable bean for file rolling policies
        - `SizeBasedFileRollingPolicy` immutable bean for size based file rolling policies
        - `RollingPeriod` enumeration for supported file rolling periods
        - `TimeBasedFileRollingPolicy` immutable bean for size based file rolling policies
        - `SizeAndTimeBasedFileRollingPolicy` immutable bean for size and time based file rolling policies
