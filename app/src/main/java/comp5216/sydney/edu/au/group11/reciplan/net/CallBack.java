package comp5216.sydney.edu.au.group11.reciplan.net;


public interface CallBack<T> {

    public void onResponse(T data);

    public void onFail(String msg);

}
