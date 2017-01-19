package com.kapilmalviya.km_browser;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Kapil Malviya on 12/18/2015.
 */
public class Login extends Fragment implements View.OnClickListener{

    LoginButton loginButton;
    CallbackManager cbm;

    String pname=null;
    String loginid=null;
    SharedPreferences shared;
    String btnText=null;
    public Login() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
            cbm = CallbackManager.Factory.create();


        } catch (Exception e) {
            Toast.makeText(getActivity().getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_login, container, false);
    }

    public String saveToInternelMemory(Bitmap b){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File der = cw.getDir("imageDir",Context.MODE_PRIVATE);
        File myPath = new File(der,"profile.jpg");
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.PNG,100,fos);
        }catch (Exception e){
            Toast.makeText(getActivity().getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
        }finally {
            try {
                fos.close();
            }catch (Exception e){
                Toast.makeText(getActivity().getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return der.getAbsolutePath();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            loginButton = (LoginButton) view.findViewById(R.id.loginfb);
            loginButton.setOnClickListener(this);
            loginButton.setReadPermissions("user_friends");
            // If using in a fragment
            loginButton.setFragment(this);
            // Other app specific specialization

            // Callback registration

            loginButton.registerCallback(cbm, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    try {
                        Profile f = Profile.getCurrentProfile();
                        pname = f.getName().toString();
                        loginid = f.getId().toString();
                        Toast.makeText(getActivity().getBaseContext(), pname, Toast.LENGTH_LONG).show();
                        new MainActivity().profilename.setText(pname);
                        new MainActivity().loginID.setText(loginid);
                        Uri dpurl = f.getProfilePictureUri(127, 127);
                        Picasso.with(getActivity().getBaseContext()).load(dpurl).transform(new CircleTransform()).into(new MainActivity().dp);
                        Bitmap dpBitmap =((BitmapDrawable) new MainActivity().dp.getDrawable()).getBitmap();
                        String path = saveToInternelMemory(dpBitmap);
                        shared = getActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor e = shared.edit();
                        e.putString("name",pname);
                        e.putString("id",loginid);
                        e.putString("url",dpurl.toString());
                        e.putString("internalPath",path);
                        e.putString("login_status","yes");
                        e.commit();
                      //  startActivity(new Intent(getActivity().getBaseContext(),MainActivity.class));
                    }catch (Exception e){
                        //Toast.makeText(getActivity().getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancel() {
                    Toast.makeText(getActivity().getBaseContext(),"cancel from user!!!", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(FacebookException e) {
                    Toast.makeText(getActivity().getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            cbm.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Toast.makeText(getActivity().getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        btnText = loginButton.getText().toString();
        if(btnText.equals("Log in with Facebook")){
            Toast.makeText(getActivity().getBaseContext(),btnText, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity().getBaseContext(),btnText+"Successfull!!!", Toast.LENGTH_LONG).show();
          //  startActivity(new Intent(getActivity().getBaseContext(),MainActivity.class));
        }
      //  Toast.makeText(getActivity().getBaseContext(),btnText, Toast.LENGTH_LONG).show();
    }
}


