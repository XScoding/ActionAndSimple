package com.xs.simple.ext;



import com.xs.simple.Call;
import com.xs.simple.CallAdapter;
import com.xs.simple.util.TypeUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * Action CallAdapter
 *
 * Created by xs code on 2019/3/27.
 */

public class ActionCallAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(final Type retrunType) {
        if (TypeUtil.typeClass(retrunType,SimpleAction.class)) {
            return new CallAdapter<Object, Object>() {
                @Override
                public Type getResponseType() {
                    if (retrunType instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) retrunType;
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        Type paramType = actualTypeArguments[0];
                        if (paramType instanceof WildcardType) {
                            return ((WildcardType) paramType).getUpperBounds()[0];
                        }
                        return paramType;
                    }
                    throw new IllegalArgumentException("must be return " + retrunType.getClass().getName() + "<?>");
                }

                @Override
                public Object adapter(Call<Object> call) {
                    return new SimpleAction<Object>(call);
                }
            };
        }
        return null;
    }
}
