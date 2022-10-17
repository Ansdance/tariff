package de.angelasensio.tariff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class TariffControllerAdvice {

    @ResponseBody
    @ExceptionHandler(CoverageOutOfRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String coverageOutOfRangeHandler(CoverageOutOfRangeException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(PriceNotFoundException ex) {
        return ex.getMessage();
    }


}
