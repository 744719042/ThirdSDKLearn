package com.example.thirdplatform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.constant.NetConstants;
import com.example.thirdplatform.imageloader.SimpleImageLoader;
import com.example.thirdplatform.utils.BitmapUtils;
import com.example.thirdplatform.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.example.thirdplatform.utils.DimenUtils.dp2px;

public class DiskLruActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView origin;
    private ImageView ratio;
    private Button loadLocalBitmap;
    private Button loadNetBitmap;
    private ImageView net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk_lru);
        origin = findViewById(R.id.origin);
        ratio = findViewById(R.id.ratio);
        net = findViewById(R.id.net);
        loadLocalBitmap = findViewById(R.id.loadLocalBitmap);
        loadLocalBitmap.setOnClickListener(this);
        loadNetBitmap = findViewById(R.id.loadNetBitmap);
        loadNetBitmap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loadLocalBitmap) {
            try {
                long startTime = System.currentTimeMillis();
                Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open("custom_save_bg.png"));
                Logger.d("Origin Cost time: " + (System.currentTimeMillis() - startTime));
                Logger.d("Original Bitmap Size:" +  bitmap.getByteCount());
                origin.setImageBitmap(bitmap);
                File file = new File(getCacheDir(), "hello.png");
                FileUtils.copy(getAssets().open("custom_save_bg.png"), file);

                startTime = System.currentTimeMillis();
                bitmap = BitmapUtils.ratio(file.getAbsolutePath(), dp2px(50), dp2px(50));
                Logger.d("Ratio Cost time: " + (System.currentTimeMillis() - startTime));
                Logger.d("Ratio Bitmap Size:" +  bitmap.getByteCount());
                ratio.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (v == loadNetBitmap) {
            SimpleImageLoader.getInstance().displayImage(net, NetConstants.getImageUrl("hello.jpg"));
        }
    }

}
