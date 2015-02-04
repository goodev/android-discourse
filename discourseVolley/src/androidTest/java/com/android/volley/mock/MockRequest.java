/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.volley.mock;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.utils.CacheTestUtils;

import java.util.HashMap;
import java.util.Map;

public class MockRequest extends Request<byte[]> {
    public boolean deliverResponse_called = false;
    public boolean parseResponse_called = false;
    public boolean deliverError_called = false;
    public boolean cancel_called = false;
    private Map<String, String> mPostParams = new HashMap<String, String>();
    private String mCacheKey = super.getCacheKey();
    private Priority mPriority = super.getPriority();

    public MockRequest() {
        super("http://foo.com", null);
    }

    public MockRequest(String url, ErrorListener listener) {
        super(url, listener);
    }

    @Override
    public Map<String, String> getPostParams() {
        return mPostParams;
    }

    public void setPostParams(Map<String, String> postParams) {
        mPostParams = postParams;
    }

    @Override
    public String getCacheKey() {
        return mCacheKey;
    }

    public void setCacheKey(String cacheKey) {
        mCacheKey = cacheKey;
    }

    @Override
    protected void deliverResponse(byte[] response) {
        deliverResponse_called = true;
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        deliverError_called = true;
    }

    @Override
    public void cancel() {
        cancel_called = true;
        super.cancel();
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        parseResponse_called = true;
        return Response.success(response.data, CacheTestUtils.makeRandomCacheEntry(response.data));
    }

}
