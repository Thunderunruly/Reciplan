package comp5216.sydney.edu.au.group11.reciplan.net;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.entity.DataBean;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
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
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                onCallback.onFail(t.getMessage());
                Log.d("NET---", t.getMessage());
            }
        });
    }

    public void dailyGet(ApiBuilder builder, final CallBack<DailyItem> onCallback) {
        ApiService service = getService();
        Call<ResponseBody> call = service.get(checkHeaders(builder.headers), builder.url, checkParams(builder.params));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                DailyItem item = new DailyItem();
                try {
                    if(response.body() != null) {
                        String string = response.body().string();
                        JSONObject object = new JSONObject(string);
                        JSONArray array = object.getJSONArray("results");
                        JSONObject result = array.getJSONObject(0);
                        item.setId(result.getInt("id"));
                        item.setTitle(result.getString("title"));
                        item.setImage(result.getString("image"));
                        item.setImageType(result.getString("imageType"));
                        JSONObject nutrition = result.getJSONObject("nutrition");
                        JSONArray arr = nutrition.getJSONArray("nutrients");
                        for(int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String name = obj.getString("name");
                            if(name.equals("Calories")) {
                                item.setCalories((double)obj.get("amount"));
                                item.setUnit(obj.getString("unit"));
                            }
                        }
                        onCallback.onResponse(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onCallback.onFail("Error");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                onCallback.onFail(t.getMessage());
                Log.d("NET---", t.getMessage());
            }
        });
    }

    public void normalGet(ApiBuilder builder, final CallBack<DataEntity> onCallback) {
        ApiService service = getService();
        Call<ResponseBody> call = service.get(checkHeaders(builder.headers), builder.url, checkParams(builder.params));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                DataEntity data = new DataEntity();
                List<DataBean> bean = new ArrayList<>();
                try {
                    if(response.body() != null) {
                        String string = response.body().string();
                        JSONObject object = new JSONObject(string);
                        JSONArray array = object.getJSONArray("results");
                        for(int i = 0; i < array.length();i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            int id = object1.getInt("id");
                            String title = object1.getString("title");
                            String image = object1.getString("image");
                            String imageType = object1.getString("imageType");
                            DataBean dataBean = new DataBean();
                            dataBean.setId(id);
                            dataBean.setTitle(title);
                            dataBean.setImage(image);
                            dataBean.setImageType(imageType);
                            bean.add(dataBean);
                        }
                        data.setData(bean);
                        onCallback.onResponse(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onCallback.onFail("Error");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
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
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

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

    public void summaryGet(ApiBuilder builder, final CallBack<String> onCallback) {
        ApiService service = getService();
        Call<ResponseBody> call = service.get(checkHeaders(builder.headers), builder.url, checkParams(builder.params));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject object = new JSONObject(string);
                    String summary = object.getString("summary");
                    onCallback.onResponse(summary);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onCallback.onFail(t.getMessage());
                Log.d("NET---", t.getMessage());
            }
        });
    }
}
