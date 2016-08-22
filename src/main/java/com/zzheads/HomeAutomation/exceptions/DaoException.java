package com.zzheads.HomeAutomation.exceptions;//

// HomeAutomation
// com.zzheads.HomeAutomation.exceptions created by zzheads on 21.08.2016.
//
public class DaoException extends Exception {
    private Exception originalException;

    public DaoException(Exception originalException, String message) {
        super(message);
        this.originalException = originalException;
    }

}
