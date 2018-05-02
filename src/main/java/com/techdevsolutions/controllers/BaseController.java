package com.techdevsolutions.controllers;

import com.techdevsolutions.beans.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.logging.Logger;

public class BaseController {
    protected Logger logger = Logger.getLogger(BaseController.class.getName());

    public Long getTimeTook(HttpServletRequest request) {
        Long startTime = (Long) request.getAttribute("requestStartTime");
        return new Date().getTime() - startTime;
    }

    public ResponseEntity generateErrorResponse(HttpServletRequest request, HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(request.getServletPath(),
                status.getReasonPhrase(),
                status.toString(),
                message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
