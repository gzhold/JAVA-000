package io.kimmking.rpcfx.api;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public interface SerializableFunction<T> extends Serializable {

    default String getName() throws Exception {
        Method method = this.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(true);
        SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
        return serializedLambda.getImplMethodName();
    }

}
