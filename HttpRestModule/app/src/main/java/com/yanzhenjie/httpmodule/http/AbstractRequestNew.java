/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.httpmodule.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.RestRequest;

/**
 * Created by Yan Zhenjie on 2016/12/18.
 */
public abstract class AbstractRequestNew<T> extends RestRequest<Result<T>> {

    public AbstractRequestNew(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public Result<T> parseResponse(Headers responseHeaders, byte[] responseBody) throws Throwable {
        int responseCodee = responseHeaders.getResponseCode();

        String responseBodyString = com.yolanda.nohttp.rest.StringRequest.parseResponseString(responseHeaders, responseBody);
        String data = null;
        if (responseCodee == 200) {
            if (!TextUtils.isEmpty(responseBodyString)) {
                JSONObject jsonObject = JSON.parseObject(responseBodyString);
                if (jsonObject.getIntValue("error") == 1) { // 业务真正的成功。
                    data = jsonObject.getString("data");
                } else {
                    String error = jsonObject.getString("message");
                    return new Result<>(false, null, responseHeaders, error);
                }
            } else {
                return new Result<>(false, null, responseHeaders, "服务器数据格式错误，请稍后重试！");
            }
        } else {
            return new Result<>(false, null, responseHeaders, "服务器发生错误，请稍后重试！");
        }

        // 成功的处理。
        try {
            T result = getResult(data);
            return new Result<>(true, result, responseHeaders, null);
        } catch (Exception e) {
            return new Result<>(false, null, responseHeaders, "服务器数据格式错误，请稍后重试！");
        }
    }

    protected abstract T getResult(String data) throws Exception;
}
