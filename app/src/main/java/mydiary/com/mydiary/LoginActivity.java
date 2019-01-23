package mydiary.com.mydiary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mydiary.com.mydiary.helper.SQLiteHandler;
import mydiary.com.mydiary.helper.SessionManager;

public class LoginActivity extends Activity implements View.OnClickListener {

    //private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText et_email, et_password;
    private Button btn_cancel, btn_login;

    private TextView tv_register;

    SQLiteHandler db;
    SessionManager session;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpUI();
    }

    private void setUpUI() {

        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);

        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_login = (Button)findViewById(R.id.btn_login);

        tv_register = (TextView)findViewById(R.id.tv_register);

        btn_cancel.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        tv_register.setOnClickListener(this);

        //call session;
        session = new SessionManager(getApplicationContext());

        //call db - SQLiteHandler
        db = new SQLiteHandler(getApplicationContext()); //call db


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_login :
                login();
                break;

            case R.id.btn_cancel :
                finish();
                break;
            case R.id.tv_register :
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login() {

        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            checkLogin(email, password);

        } else {
            Toast.makeText(getApplicationContext(), "Please enter the required data", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLogin(final String email, final String password) {

        String tag_string_req = "req_login";

        pDialog.setMessage("Logging In..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        //finish();
                    } else {
                        Log.d("objError", jObj.toString());
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {

        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {

        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
