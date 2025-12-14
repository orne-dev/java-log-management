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
        - `SizeBasedFileRollingPolicy` immutable bean for size based file rolling policies
        - `RollingPeriod` enumeration for supported file rolling periods
        - `TimeBasedFileRollingPolicy` immutable bean for time based file rolling policies
        - `SizeAndTimeBasedFileRollingPolicy` immutable bean for size and time based file rolling policies
        - `FileRollingPolicyTemplate` immutable bean for file rolling policy templates
        - `ManagedAppender` immutable bean for managed appenders
        - `ManagedAppenderConfig` bean for configuration for new managed appenders 
    - Log management exceptions
        - `LogManagementException` exception for internal log management errors
        - `IllegalLevelException` exception for invalid or unsupported log level errors
        - `LoggerNotFoundException` exception for logger not found errors
        - `AppenderNotFoundException` exception for appender not found errors
        - `UnmanagedAppenderException` exception for unmanaged appender errors
        - `InvalidAppenderConfigException` exception for misconfigured appender errors
        - `InvalidFileRollingConfigException` exception for misconfigured file rolling policy errors
        - `UnsupportedFileRollingPolicyException` exception for unsupported file rolling policy errors
    - `LogManagementConfig` interface for log management system configuration
    - `LogManagementEngine` interface for logging systems communication layer
    - `LogManagementCoordinator` interface for log management changes coordinator
    - `LogManager` interface for log management operations
- Built-in `LogManager` implementation
    - `LogManagerImpl` default implementation
- Built-in `LogManagementCoordinator` implementations
    - `NoOpLogManagementCoordinator` local JVM only log management coordinator
    - `JmsLogManagementCoordinator` JMS topic based log management coordinator
- Built-in `LogManagementEngine` implementations
    - `LogbackManagementEngine` Logback logging system engine
