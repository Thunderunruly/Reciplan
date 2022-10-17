package comp5216.sydney.edu.au.group11.reciplan.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
import comp5216.sydney.edu.au.group11.reciplan.ui.like.LikeItem;

public class FireDoc {
    private final FirebaseFirestore database;
    private final FirebaseAuth auth;
    private final Context context;
    private Map<String, Object> keys;
    private String dailyId;
    private Map<String, Object> daily;

    public FireDoc(Context context){
        this.context = context;
        keys = new HashMap<>();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
    }

    public boolean checkLogin() {
        return auth.getCurrentUser() != null;
    }



    public<T> void addToDatabase(Map<String, Object> keys, CallBack<T> callBack) {
        database.collection("reciplan").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .set(keys)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        callBack.onResponse((T) task);
                    }
                    else {
                        callBack.onFail("Fail to set username");
                    }
                });
    }

    public void setMapById() {
        database.collection("reciplan").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .set(keys)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                    }
                    else {
                        Toast.makeText(context, "update Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void update(CallBack callBack) {
        if(checkLogin()) {
            database.collection("reciplan").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            keys = task.getResult().getData();
                            callBack.onResponse(task);
                        }
                        else {
                            callBack.onFail("Fail to connect to database");
                        }
                    });
        }
        else {
            callBack.onFail("login");
        }
    }
    public void updateLikes(CallBack<ArrayList<LikeItem>> callBack) {
        database.collection("reciplan").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("likes")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        ArrayList<LikeItem> items = new ArrayList<>();
                        for(QueryDocumentSnapshot document: task.getResult()) {
                            Log.d("name",document.get("title").toString());
                            items.add(new LikeItem(Integer.parseInt(document.get("id") + ""),
                                    Objects.requireNonNull(document.get("title")).toString(),
                                    Objects.requireNonNull(document.get("image")).toString(),
                                    Double.parseDouble(document.get("calories") + "")));
                        }
                        callBack.onResponse(items);
                    }
                    else {
                        callBack.onFail("Fail to connect to database");
                    }
                });
    }

    public void register(String emailAddress, String password, CallBack callBack) {
        auth.createUserWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        callBack.onResponse(task);
                    }
                    else {
                        callBack.onFail("Fail to Register.");
                    }
                });
    }

    public void signIn(String emailAddress, String password, String username, CallBack callBack) {
        auth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(task -> {
                    keys.put("username",username);
                    keys.put("email",emailAddress);
                    keys.put("image","https://spoonacular.com/recipeimages/716429-312x231.jpg");
                    if(task.isSuccessful()) {
                        callBack.onResponse(task);
                    }
                    else {
                        callBack.onFail("Fail to login");
                    }
                });
    }

    public String getStatus() {
        for(String name: keys.keySet()) {
            if(name.equals("status")) {
                return (String) keys.get(name);
            }
        }
        return null;
    }

    public void setKey(String name,Object obj) {
        keys.put(name, obj);
    }

    public Map<String, Object> getKeys() {
        return keys;
    }

    public void checkDaily(CallBack callBack) {
        database.collection("reciplan").document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        if(task.getResult().getString("dailyId") == null) {
                            addDaily(new CallBack<String>() {
                                @Override
                                public void onResponse(String data) {
                                    getDaily(data, new CallBack<Map<String, Object>>() {
                                        @Override
                                        public void onResponse(Map<String, Object> data) {
                                            // final
                                            callBack.onResponse(data);
                                        }

                                        @Override
                                        public void onFail(String msg) {
                                            callBack.onFail(msg);
                                        }
                                    });
                                }

                                @Override
                                public void onFail(String msg) {
                                    callBack.onFail(msg);
                                }
                            });
                        }
                        else {
                            getDaily(task.getResult().getString("dailyId"), new CallBack<Map<String, Object>>() {
                                @Override
                                public void onResponse(Map<String, Object> data) {
                                    callBack.onResponse(data);
                                }

                                @Override
                                public void onFail(String msg) {
                                    callBack.onFail(msg);
                                }
                            });
                        }
                    }
                    else {
                        callBack.onFail("Fail to connect database");
                    }
                });
    }

    private void getDaily(String dailyId, CallBack<Map<String, Object>> callBack) {
        database.collection("reciplan").document(auth.getCurrentUser().getUid())
                .collection("daily").document(dailyId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        daily = (Map<String, Object>) task.getResult();
                        callBack.onResponse((Map<String, Object>) task.getResult());
                    }
                    else {
                        callBack.onFail("Fail to get daily item from database");
                    }
                });
    }

    private void addDaily(CallBack<String> callBack) {
        database.collection("reciplan").document(auth.getCurrentUser().getUid())
                .collection("daily")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot document:task.getResult()) {
                            callBack.onResponse(document.getId());
                        }
                    }
                    else {
                        callBack.onFail("Fail to backup daily");
                    }
                });
    }

    public void checkLike(int id) {
    }

    public void updateToFireBase(CallBack callBack) {
        database.collection("reciplan").document(auth.getCurrentUser().getUid())
                .set(keys)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        callBack.onResponse(task);
                    }
                    else {
                        callBack.onFail("Fail to backup");
                    }
                });
    }
}
