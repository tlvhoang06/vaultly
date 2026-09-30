package com.hoang.vaultly.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ErrorCode {
    // User Exception
    INVALID_CREDENTIALS(1001, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_EXISTS(1003, "Username existed", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_SAME_AS_OLD(1004, "New password is similar to old password", HttpStatus.BAD_REQUEST),

    // JWT Exception
    INVALID_TOKEN(1101, "Invalid Token", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(1102, "Expired Token", HttpStatus.BAD_REQUEST),

    // Fund Exception
    FUND_NOT_FOUND(1201, "Fund not found", HttpStatus.NOT_FOUND),


    // Fund Member Exception
    FUND_MEMBER_NOT_FOUND(1301, "Fund member not found", HttpStatus.NOT_FOUND),
    MEMBER_NOT_IN_FUND(1302, "Member does not belong to fund", HttpStatus.BAD_REQUEST),
    MEMBER_ROLE_EXISTS(1303, "Member already had this role", HttpStatus.BAD_REQUEST),
    MEMBER_ROLE_NOT_FOUND(1304, "Member does not have this role", HttpStatus.NOT_FOUND),
    CANNOT_REMOVE_OWNER(1305, "Fund has to have at least 1 owner", HttpStatus.BAD_REQUEST),
    CANNOT_ASSIGN_OWNER(1306, "Fund has to have at most 1 owner", HttpStatus.BAD_REQUEST),
    ALREADY_FUND_MEMBER(1307, "User is already in this fund", HttpStatus.BAD_REQUEST),
    MEMBER_MUST_HAVE_ONE_ROLE(1308, "Member must have at least one role", HttpStatus.BAD_REQUEST);
    int code;
    String message;
    HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
