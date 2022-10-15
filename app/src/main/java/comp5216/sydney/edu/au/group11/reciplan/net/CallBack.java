package comp5216.sydney.edu.au.group11.reciplan.net;


public interface CallBack<T> {

    void onResponse(T data);

    void onFail(String msg);

}
