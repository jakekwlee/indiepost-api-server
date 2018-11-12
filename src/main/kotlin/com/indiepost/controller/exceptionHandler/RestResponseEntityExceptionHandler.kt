package com.indiepost.controller.exceptionHandler

import com.indiepost.exceptions.ResourceNotFoundException
import com.indiepost.exceptions.UnauthorizedException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
    protected fun handleConflict(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    protected fun handleResourceNotFound(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [UnauthorizedException::class])
    protected fun handleUnauthorized(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.UNAUTHORIZED, request)
    }
}
