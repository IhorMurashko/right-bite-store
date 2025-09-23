package com.best_store.right_bite.security.util;


import lombok.experimental.UtilityClass;

import java.util.Date;

/**
 * Utility class for date-based operations related to token validity.
 */
@UtilityClass
public class DateConstructorUtil {

    /**
     * Constructs a new {@link Date} object by adding the given number of seconds
     * to the specified base date.
     *
     * @param from                    base date
     * @param validityPeriodInSeconds number of seconds to add
     * @return resulting expiration date
     */
    public Date dateExpirationGenerator(Date from, long validityPeriodInSeconds) {
        return new Date(from.getTime() + validityPeriodInSeconds * 1000);
    }
}