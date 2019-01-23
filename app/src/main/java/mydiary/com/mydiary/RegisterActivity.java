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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import mydiary.com.mydiary.helper.SQLiteHandler;
import mydiary.com.mydiary.helper.SessionManager;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText et_name, et_email, et_password;
    private Button btn_cancel, btn_register;

    private TextView tv_login;

    SQLiteHandler db;
    SessionManager session;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpUI();
    }

    private void setUpUI() {

        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_register = (Button) findViewById(R.id.btn_register);

        tv_login = (TextView) findViewById(R.id.tv_login);

        //call session;
        session = new SessionManager(getApplicationContext());

        //call db - SQLiteHandler
        db = new SQLiteHandler(getApplicationContext()); //call db


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btn_cancel.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        tv_login.setOnClickListener(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_register:
                register();
                break;

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.tv_login:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

        }
    }

    private void register() {
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            registerUser(name, email, password);

        } else {
            Toast.makeText(getApplicationContext(), "Please enter the required data", Toast.LENGTH_SHORT).show();
        }

    }

    private void registerUser(final String name, final String email, final String password) {

        //string to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ....");
        showDialog();

        //Start registering process.
        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                AppConfig.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();

                        //get json object;
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            boolean error = obj.getBoolean("error");

                            if (!error) {
                                String uid = obj.getString("uid");

                                JSONObject user = obj.getJSONObject("user");
                                String name = user.getString("name");
                                String email = user.getString("email");
                                String password = user.getString("password");

                                //inserting user;
                                db.addUser(name, email, uid, password);

                                Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                            } else {
                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = obj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                                hideDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Registration Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
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
