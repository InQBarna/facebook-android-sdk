package com.facebook;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
import com.facebook.android.*;

/**
 * This class addresses the issue of a potential window leak during
 * dialog authorization if the Activity containing the dialog is destroyed
 * (e.g. if the user rotates the device).
 * <p/>
 * Add this activity to your AndroidManifest.xml to ensure proper handling
 * of dialog authorization.
 */
public class LoginActivity extends Activity {
    private Dialog loginDialog;
    private Dialog errorDialog;

    @Override
    public void onResume() {
        super.onResume();
        int permissionCheck = checkCallingOrSelfPermission(Manifest.permission.INTERNET);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("AndroidManifest Error")
                   .setMessage("WebView login requires INTERNET permission")
                   .setCancelable(true)
                   .setPositiveButton(R.string.com_facebook_dialogloginactivity_ok_button,
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   finish();
                               }
                           })
                   .setOnCancelListener(new DialogInterface.OnCancelListener() {
                       @Override
                       public void onCancel(DialogInterface dialogInterface) {
                           finish();
                       }
                   });
            errorDialog = builder.create();
            errorDialog.show();
            setResult(Activity.RESULT_CANCELED);
            return;
        }

        Bundle parameters = new Bundle();
        String permissions = getIntent().getStringExtra("scope");
        if (!Utility.isNullOrEmpty(permissions)) {
            parameters.putString(ServerProtocol.DIALOG_PARAM_SCOPE, permissions);
        }

        // The call to clear cookies will create the first instance of CookieSyncManager if necessary
        Utility.clearFacebookCookies(this);

        WebDialog.OnCompleteListener listener = new WebDialog.OnCompleteListener() {
            @Override
            public void onComplete(Bundle values, FacebookException error) {
                if (values != null) {
                    // Ensure any cookies set by the dialog are saved
                    CookieSyncManager.getInstance().sync();
                    setResultAndFinish(Activity.RESULT_OK, values);
                } else {
                    Bundle bundle = new Bundle();
                    if (error instanceof FacebookDialogException) {
                        FacebookDialogException dialogException = (FacebookDialogException) error;
                        bundle.putInt(Session.WEB_VIEW_ERROR_CODE_KEY, dialogException.getErrorCode());
                        bundle.putString(Session.WEB_VIEW_FAILING_URL_KEY, dialogException.getFailingUrl());
                    } else if (error instanceof FacebookOperationCanceledException) {
                        setResultAndFinish(Activity.RESULT_CANCELED, null);
                    }
                    bundle.putString("error", error.getMessage());
                    setResultAndFinish(Activity.RESULT_OK, bundle);
                }
            }

            private void setResultAndFinish(int resultCode, Bundle bundle) {
                if (bundle != null) {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(resultCode, intent);
                } else {
                    setResult(resultCode);
                }
                finish();
            }
        };

        WebDialog dialog = WebDialog.createAuthDialog(this, getIntent().getStringExtra("client_id"), 0);
        dialog.setOnCompleteListener(listener);
        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }
}
