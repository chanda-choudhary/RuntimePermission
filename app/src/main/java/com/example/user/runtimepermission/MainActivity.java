package com.example.user.runtimepermission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA=125;
    private static final int REQUEST_STORSGE=225;
    private static final int REQUEST_CONTACTS=325;

    private static final int TXT_CAMERA=1;
    private static final int TXT_STORAGE=2;
    private static final int TXT_CONTACTS=3;

    private PermissionUtil permissionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionUtil=new PermissionUtil(this);
    }

    //Check Permission
    private int checkPermission(int permission)
    {
        int status= PackageManager.PERMISSION_DENIED;
        switch (permission)
        {
            case TXT_CAMERA:
                status= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                break;
            case TXT_STORAGE:
                status= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case TXT_CONTACTS:
                status= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
                break;
        }
        return status;
    }

    //Request New Permission
    private void requestPermission(int permission)
    {
        switch (permission)
        {
            case TXT_CAMERA:
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
                break;
            case TXT_STORAGE:
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORSGE);
                break;
            case TXT_CONTACTS:
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CONTACTS);
                break;
        }
    }

    //Display Permission Explanation
    private void showPermissionExplanation(final int permission)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        if(permission==TXT_CAMERA)
        {
            builder.setMessage("This app need to access your device Camera..Please allow");
            builder.setTitle("Camera Permission Needed..");
        }

        else if(permission==TXT_STORAGE)
        {
            builder.setMessage("This app need to write to your device storage..Please allow");
            builder.setTitle("Storage Permission Needed..");
        }
        else if(permission==TXT_CONTACTS)
        {
            builder.setMessage("This app need to access your Contacts..Please allow");
            builder.setTitle("Contacts Permission Needed..");
        }
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(permission==TXT_CAMERA)
                    requestPermission(TXT_CAMERA);
                else if (permission==TXT_STORAGE)
                    requestPermission(TXT_STORAGE);
                else if (permission==TXT_CONTACTS)
                    requestPermission(TXT_CONTACTS);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    //***************************************************************
    public void openCamera(View view) {
        if(checkPermission(TXT_CAMERA)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CAMERA))
            {
                showPermissionExplanation(TXT_CAMERA);
            }
            else if(!permissionUtil.checkPermissionPreference("camera"))
            {
                requestPermission(TXT_CAMERA);
                permissionUtil.updatePermissionPreference("camera");
            }
            else
            {
                Toast.makeText(this,"Please allow camera permission in your app settings",Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction((Settings.ACTION_APPLICATION_DETAILS_SETTINGS));
                Uri uri=Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(this,"You have camera permission",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,ResultActivity.class);
            intent.putExtra("message","You can now take photos and record videos..");
            startActivity(intent);
        }
    }

    public void writeToStorage(View view) {
        if(checkPermission(TXT_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                showPermissionExplanation(TXT_STORAGE);
            }
            else if(!permissionUtil.checkPermissionPreference("storage"))
            {
                requestPermission(TXT_STORAGE);
                permissionUtil.updatePermissionPreference("storage");
            }
            else
            {
                Toast.makeText(this,"Please allow storage permission in your app settings",Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction((Settings.ACTION_APPLICATION_DETAILS_SETTINGS));
                Uri uri=Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(this,"You have storage permission",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,ResultActivity.class);
            intent.putExtra("message","Now you can write to the storage of this device..");
            startActivity(intent);
        }
    }

    public void readContacts(View view) {
        if(checkPermission(TXT_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CONTACTS))
            {
                showPermissionExplanation(TXT_CONTACTS);
            }
            else if(!permissionUtil.checkPermissionPreference("contacts"))
            {
                requestPermission(TXT_CONTACTS);
                permissionUtil.updatePermissionPreference("contacts");
            }
            else
            {
                Toast.makeText(this,"Please allow contacts permission in your app settings",Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction((Settings.ACTION_APPLICATION_DETAILS_SETTINGS));
                Uri uri=Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(this,"You have contacts permission",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,ResultActivity.class);
            intent.putExtra("message","You can now read contacts of this device..");
            startActivity(intent);
        }
    }
    //****************************************************************
}
