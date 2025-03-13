package com.vict.framework.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * try_file 配置
 */
@Controller
public class ErrorHandlerController implements ErrorController {

    /**
     * error handling.
     *
     * @return home controller
     */
    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.OK)
    public String error() {
        return "/web/index.html";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
