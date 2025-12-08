package dev.orne.log.manager;

import org.apiguardian.api.API;

/**
 * The period to roll the log file.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public enum RollingPeriod {

    /** Rollover at the top of each hour. */
    HOURLY,
    /** Rollover daily. */
    DAILY,
    /** Rollover at the first day of each week. */
    WEEKLY,
    /** Rollover at the beginning of each month. */
    MONTHLY,
    ;
}
