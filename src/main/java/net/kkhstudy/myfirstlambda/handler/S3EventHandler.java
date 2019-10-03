package net.kkhstudy.myfirstlambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

public class S3EventHandler implements RequestHandler<S3Event, Object> {
    @Override
    public Object handleRequest(S3Event s3Event, Context context) {
        return null;
    }
}
