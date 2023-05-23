package com.lunatech.mutiny;


import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@ContextMutex
// Priority must be lower than that of WithSessionInterceptor, so that it comes
// 'outside' of the session, thereby making session creation exclusive
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 190)
public class ContextMutexInterceptor extends AbstractUniInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        if (isUniReturnType(context)) {
            return VertxContextExtras.contextMutex(() -> proceedUni(context));
        }
        return context.proceed();
    }

}

