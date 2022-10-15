package comp5216.sydney.edu.au.group11.reciplan.net;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.ui.daily.DailyItem;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiClient {
    private static ApiClient instance;

    // TODO

    /***
     *
     * 构建 retrofit请求
     * */
    private final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiService.HOST)
            .build();


    /**
     * 获取service实例
     *
     * @return get或者post请求接口
     */
    public ApiService getService() {
        return retrofit.create(ApiService.class);
    }

    /**
     * 创建请求单例
     *
     * @return
     */
    public static ApiClient getInstance() {

        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                }
            }
        }
        return instance;
    }

    /**
     * get请求 带token
     *
     * @param builder    request构建的参数 包含 url header params
     * @param onCallback rquest 回调
     * @param classOf    指定请求的model类型
     *                   <p>
     *                   <p>
     *                   _ = userId，_t = 请求时间，token = MD5(token+_t)
     */

    public <T> void doGet(ApiBuilder builder, final CallBack<T> onCallback, final Class classOf, final Context context) {

        ApiService service = getService();

        Call<ResponseBody> call = service.get(checkHeaders(builder.headers), builder.url, checkParams(builder.params));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("result:", response.toString());
                Object o = null;
                try {
                    if (response.body() == null) {
                        String err = response.errorBody().string();
                        onCallback.onFail("Fail");
                        Log.i("err", err);
                    } else {
                        String string = "{\"data\":"+response.body().string()+"}";

                        o = new Gson().fromJson(string, classOf);
                        onCallback.onResponse((T) o);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onCallback.onFail("Fail");
                } catch (Exception e) {
                    e.printStackTrace();
                    onCallback.onFail("Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onCallback.onFail(t.getMessage());
                Log.d("NET---", t.getMessage());
            }
        });
    }

    /**
     * get请求 不带token
     *
     * @param builder    request构建的参数 包含 url header params
     * @param onCallback rquest 回调
     * @param classOf    指定请求的model类型
     */

    public <T> void doGetSaveSession(ApiBuilder builder, final CallBack<T> onCallback, final Class classOf, final Context context) {

        ApiService service = getService();
        Call<ResponseBody> call = service.get(checkHeaders(builder.headers), builder.url, checkParams(builder.params));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Object o = null;
                try {
                    if (response.body() != null) {
                        o = new Gson().fromJson(response.body().string(), classOf);
                        onCallback.onResponse((T) o);

                    } else {
                        onCallback.onFail("network connection");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Headers headers = response.headers();
                List<String> cookies = headers.values("Set-Cookie");
                if (cookies.size() > 0) {
                    String s = cookies.get(0);
                    String session = s.substring(0, s.indexOf(";"));
                    Log.i("save session", session);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onCallback.onFail(t.getMessage());
                Log.d("NET---", t.getMessage());
            }
        });
    }

    public static Map<String, String> checkHeaders(Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getValue() == null) {
                headers.put(entry.getKey(), "");
            }
        }
        return headers;
    }

    public static Map<String, String> checkParams(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                params.put(entry.getKey(), "");
            }
        }
        return params;
    }
}
