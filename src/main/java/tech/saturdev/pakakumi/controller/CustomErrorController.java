package tech.saturdev.pakakumi.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    private ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(produces = "text/html")
    public String handleError(HttpServletRequest request, Model model) {
        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(new ServletWebRequest(request),
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.STACK_TRACE,
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.BINDING_ERRORS));

        model.addAttribute("status", attributes.get("status"));
        model.addAttribute("error", attributes.get("error"));
        model.addAttribute("message", attributes.get("message"));
        model.addAttribute("path", attributes.get("path"));

        return "error";
    }

}