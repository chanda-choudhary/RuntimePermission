package com.example.user.runtimepermission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA=125;
    private static final int REQUEST_STORSGE=225;
    private static final int REQUEST_CONTACTS=325;
    private static final int REQUEST_GROUP_PERMISSION=425;
    private static final int REQUEST_READ_STORSGE=525;



    private static final int TXT_CAMERA=1;
    private static final int TXT_STORAGE=2;
    private static final int TXT_CONTACTS=3;
    private static final int TXT_READ_STORAGE=4;

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
            case TXT_READ_STORAGE:
                status=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
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
            case TXT_READ_STORAGE:
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_STORSGE);
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
        else if (permission==TXT_READ_STORAGE)
        {
            builder.setMessage("This app need to read from your device storage..Please allow");
            builder.setTitle("Read Storage Permission Needed..");
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
                else if (permission==TXT_READ_STORAGE)
                    requestPermission(TXT_READ_STORAGE);
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

    //Request Group Permission
    public void requestGroupPermission(ArrayList<String> permissions)
    {
        String[] permissionList=new String[permissions.size()];
        permissions.toArray(permissionList);

        ActivityCompat.requestPermissions(MainActivity.this,permissionList,REQUEST_GROUP_PERMISSION);
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

    //Read External Storage
    public void readExternalStorage(View view) {
        if(checkPermission(TXT_READ_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                showPermissionExplanation(TXT_READ_STORAGE);
            }
            else if(!permissionUtil.checkPermissionPreference("read storage"))
            {
                requestPermission(TXT_READ_STORAGE);
                permissionUtil.updatePermissionPreference("read storage");
            }
            else
            {
                Toast.makeText(this,"Please allow read storage permission in your app settings",Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction((Settings.ACTION_APPLICATION_DETAILS_SETTINGS));
                Uri uri=Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(this,"You have read storage permission",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,ResultActivity.class);
            intent.putExtra("message","Now you can read from the storage of this device..");
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

    public void requestAllPermission(View view) {
        ArrayList<String> permissionsNeeded=new ArrayList<>();
        ArrayList<String> permissionsAvailable=new ArrayList<>();
        permissionsAvailable.add(Manifest.permission.CAMERA);
        permissionsAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsAvailable.add(Manifest.permission.READ_CONTACTS);

        for (String permission:permissionsAvailable)
        {
            if (ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED)
                permissionsNeeded.add(permission);
        }
        requestGroupPermission(permissionsNeeded);
    }
    //****************************************************************


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_CAMERA:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"You have Camera permission.",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(this,ResultActivity.class);
                    intent.putExtra("message","You can now take photos and record videos.");
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this,"Camera Permission is denied.Turn off Camera modules of the app.",Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_CONTACTS:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"You have Contacts permission.",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(this,ResultActivity.class);
                    intent.putExtra("message","Now you can read Contacts availabe on this device.");
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this,"Read Contacts Permission is denied.Turn off Read Contacts modules of the app.",Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_STORSGE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"You have Storage permission.",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(this,ResultActivity.class);
                    intent.putExtra("message","Now you can write to the storage of this device.");
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this,"Write External Storage Permission is denied.Turn off Storage modules of the app.",Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_READ_STORSGE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"You have Storage permission.",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(this,ResultActivity.class);
                    intent.putExtra("message","Now you can read storage from this device.");
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this,"Read External Storage Permission is denied.Turn off Storage modules of the app.",Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_GROUP_PERMISSION:
                String result="";
                int i=0;
                for (String permis:permissions)
                {
                    String status="";
                    if(grantResults[i]==PackageManager.PERMISSION_GRANTED)
                        status="Granted";
                    else
                        status="Denied";
                    result=result+"\n"+permis+" : "+status;
                    i++;
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Group Permission Details");
                builder.setMessage(result);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                Toast.makeText(this,result,Toast.LENGTH_LONG).show();
                break;
        }

    }


}
