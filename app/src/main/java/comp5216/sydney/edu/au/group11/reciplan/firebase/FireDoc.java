package comp5216.sydney.edu.au.group11.reciplan.firebase;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;

public class FireDoc {
    private final FirebaseFirestore database;
    private final FirebaseAuth auth;
    private Context context;
    private String id;
    private Map<String, Object> keys;

    public FireDoc(Context context){
        this.context = context;
        keys = new HashMap<>();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
    }

    public boolean checkLogin() {
        if(auth.getCurrentUser() != null) {
            return true;
        }
        return false;
    }

    public void updateToFirebase(Map<String, Object> keys, CallBack callBack) {
        database.collection("reciplan")
                .add(keys)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        callBack.onResponse((task));
                    }
                    else {
                        callBack.onFail("Fail to set username");
                    }
                });
    }

    public void setMapById() {
        database.collection("reciplan").document(id)
                .set(keys)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                    }
                    else {
                        Toast.makeText(context, "update Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void update() {
        database.collection("reciplan")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot document:task.getResult()) {
                            if (auth.getCurrentUser().getUid().equals(document.get("uid"))) {
                                keys = document.getData();
                                id = document.getId();
                            }
                        }
                    }
                    else {
                        Toast.makeText(context, "Fail to connect to database", Toast.LENGTH_SHORT).show();
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
                    keys.put("uid",auth.getCurrentUser().getUid());
                    if(task.isSuccessful()) {
                        callBack.onResponse(task);
                    }
                    else {
                        callBack.onFail("Fail to login");
                    }
                });
    }

    public void setKey(String name,Object obj) {
        keys.put(name, obj);
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getKeys() {
        return keys;
    }
}
