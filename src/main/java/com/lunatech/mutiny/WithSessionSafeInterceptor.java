package com.lunatech.mutiny;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@WithSessionSafe
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 200)
public class WithSessionSafeInterceptor extends AbstractUniInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        if (isUniReturnType(context)) {
            return SessionOperations.withSessionSafe(s -> proceedUni(context));
        }
        return context.proceed();
    }
}
