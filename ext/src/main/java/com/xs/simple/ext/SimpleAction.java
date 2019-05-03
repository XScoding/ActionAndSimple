package com.xs.simple.ext;

import com.xs.action.Action;
import com.xs.action.Call;
import com.xs.simple.SimpleResponse;

/**
 * simple action
 *
 * Created by xs code on 2019/3/27.
 */

public class SimpleAction<R> extends Action<com.xs.simple.Call<R>,R> {


    /**
     * constuctor
     *
     * @param http
     */
    public SimpleAction(com.xs.simple.Call<R> http) {
        super(http);
    }

    @Override
    public void act(com.xs.simple.Call<R> http, final Call<R> call) {
        SimpleResponse<R> response = http.execute();
        if (response.isSuccessful()) {
            call.success(response.body());
        } else {
            call.fail(response.getError());
        }
    }

    @Override
    protected void cancel() {
        super.cancel();
        if (t != null) {
            t.cancel();
        }
    }
}
