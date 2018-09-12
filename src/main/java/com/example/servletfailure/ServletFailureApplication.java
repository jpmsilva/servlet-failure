package com.example.servletfailure;

import org.apache.catalina.core.StandardContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;

@SpringBootApplication
public class ServletFailureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServletFailureApplication.class, args);
    }

    public static class FailureServlet extends HttpServlet {

        @Override
        public void init() {
            throw new RuntimeException();
        }
    }

    @Bean
    public ServletRegistrationBean<FailureServlet> failureServet() {
        ServletRegistrationBean<FailureServlet> servetServletRegistration = new ServletRegistrationBean<>(new FailureServlet());
        servetServletRegistration.setLoadOnStartup(0);
        return servetServletRegistration;
    }

    @Component
    public class TomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

        @Override
        public void customize(TomcatServletWebServerFactory factory) {
            factory.addContextCustomizers(context -> {
                if (context instanceof StandardContext) {
                    ((StandardContext) context).setFailCtxIfServletStartFails(true);
                }
            });
        }
    }
}
