//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.proto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.googlecode.androidannotations.api.SdkVersionHelper;
import com.proto.R.id;

public final class MainActivity_
    extends MainActivity
{

    private Handler handler_ = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private void init_(Bundle savedInstanceState) {
    }

    private void afterSetContentView_() {
        graph = ((LinearLayout) findViewById(id.graph));
        download_txt = ((TextView) findViewById(id.download_txt));
        traffic = ((ProgressBar) findViewById(id.traffic));
        download = ((ProgressBar) findViewById(id.download));
        traffic_txt = ((TextView) findViewById(id.traffic_txt));
        button = ((Button) findViewById(id.button));
        {
            View view = findViewById(id.button);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MainActivity_.this.startDownload(view);
                    }

                }
                );
            }
        }
        init();
        checkConnection();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static MainActivity_.IntentBuilder_ intent(Context context) {
        return new MainActivity_.IntentBuilder_(context);
    }

    @Override
    public void updateTraffic(final double rate) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.updateTraffic(rate);
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void resetTraffic() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.resetTraffic();
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void resetDownload() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.resetDownload();
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void updateDownload(final double progress, final double data) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.updateDownload(progress, data);
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void enableButton() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.enableButton();
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void startDownload() {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.startDownload();
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, MainActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public MainActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

    }

}