package net.leaderos.shared.error;

public enum Error {

    // general
    USER_NOT_FOUND,

    // store
    INVALID_QUANTITY,
    INSUFFICIENT_BALANCE,
    OUT_OF_STOCK,
    PRODUCT_NOT_FOUND,
    REQUIRED_PRODUCT,
    REQUIRED_LINKED_ACCOUNT,
    DOWNGRADE_NOT_ALLOWED,
    INVALID_VARIABLE,

    // donations
    INVALID_LIMIT,
    INVALID_TYPE,

    // bazaar
    INSERT_ERROR,
    DELETE_ERROR,

    // ticket
    TICKET_NOT_FOUND,

    // credit
    INVALID_AMOUNT,
    NOT_ENOUGH_CREDITS,
    INVALID_TARGET,
    TARGET_USER_NOT_FOUND
    ;

}
