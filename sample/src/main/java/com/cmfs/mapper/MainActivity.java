package com.cmfs.mapper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Model model = new Model();
        model.setStrValue("ModelStringValue");
        model.setIntValue(12345);

        TargetModel targetModel = Mappers.source(model)
                .to(TargetModel.class);

        Log.d(TAG, "onCreate: TargetModel.str = " + targetModel.getStr());
        Log.d(TAG, "onCreate: TargetModel.i = " + targetModel.getI());
    }
}
