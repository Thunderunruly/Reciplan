package comp5216.sydney.edu.au.group11.reciplan.net;

import java.util.HashMap;
import java.util.Map;

public class ApiBuilder {
    Map<String, String> params ;
    Map<String, String> headers ;
    String url;

    public ApiBuilder Params(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    public ApiBuilder Params(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public ApiBuilder Url(String url) {
        this.url = url;
        return this;
    }

    public ApiBuilder() {
        this.setParams();
    }


    private void setParams() {
        this.url = null;
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
    }

}
