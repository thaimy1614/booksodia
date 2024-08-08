package com.thai.profile.configuration;

import jakarta.annotation.Nonnull;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    public static class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(
                @Nonnull Throwable ex,
                @Nonnull Method method,
                @Nonnull Object... params
        ) {
            System.err.println("Exception message - " + ex.getMessage());
            System.err.println("Method name - " + method.getName());
            for (Object param : params) {
                System.err.println("Parameter value - " + param);
            }
        }
    }
}
