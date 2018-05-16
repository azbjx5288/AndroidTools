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
public abstract class ModuleRequest<T> extends RestRequest<Result<T>> {

    public ModuleRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public Result<T> parseResponse(Headers responseHeaders, byte[] responseBody) throws Throwable {
        int responseCode = responseHeaders.getResponseCode();

        String result = null;
        if (responseCode == 200) {
            result = com.yolanda.nohttp.rest.StringRequest.parseResponseString(responseHeaders, responseBody);
        } else {
            return new Result<>(false, null, responseHeaders, "服务器发生错误");
        }

        String childResult = null;
        try {
            if (!TextUtils.isEmpty(result)) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getIntValue("error") == 1) {
                    childResult = jsonObject.getString("data");
                } else {
                    return new Result<>(false, null, responseHeaders, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            return new Result<>(false, null, responseHeaders, "服务器数据格式错误");
        }

        try {
            T t = getResult(childResult);
            return new Result<>(true, t, responseHeaders, null);
        } catch (Exception e) {
            return new Result<>(false, null, responseHeaders, "数据解析失败");
        }
    }

    protected abstract T getResult(String responseBody) throws Exception;
}