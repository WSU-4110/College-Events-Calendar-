//package com.example.campusconnect.services;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//
//import com.example.campusconnect.R;
//import com.shashank.sony.fancydialoglib.FancyAlertDialog;
//import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
//import com.shashank.sony.fancydialoglib.Icon;
//
//public class CheckInternetConnection {
//
//    private Context ctx;
//
//    public CheckInternetConnection(Context context) {
//        ctx = context;
//    }
//
// Function Call:
//        new CheckInternetConnection(this).checkConnection();
//    public void checkConnection() {
//        if (isInternetConnected() == false) {
//            new FancyAlertDialog.Builder((Activity) ctx)
//                    .setBackgroundColor(R.color.Wheat)
//                    .setIcon(R.drawable.internetconnection, Icon.Visible)
//                    .setTitle("No Internet")
//                    .setMessage("Kindly connect to a WIFI network or enable Mobile Data")
//                    .setPositiveBtnText("Connect Now")
//                    .setNegativeBtnText("Recheck")
//                    .setPositiveBtnBackground(R.color.colorPrimaryDark)
//                    .OnPositiveClicked(new FancyAlertDialogListener() {
//                        @Override
//                        public void OnClick() {
//                            // open setting
//                            Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
//                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            ctx.startActivity(dialogIntent);
//                        }
//                    })
//                    .OnNegativeClicked(new FancyAlertDialogListener() {
//                        @Override
//                        public void OnClick() {
//                            // restart application on recheck
//                            System.exit(0);
//                        }
//                    })
//                    .isCancellable(false).build();
//        }
//    }
//
//    private boolean isInternetConnected() {
//        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
//    }
//}
