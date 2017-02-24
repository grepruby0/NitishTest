package com.android.test;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.test.custom.RoundedImageView;
import com.android.test.model.DataModel;

import java.io.IOException;

public class UserDetailActivity extends AppCompatActivity {

    RoundedImageView mIvImage;
    TextView mTvUserName, mTvEmail, mTvPhoneNumber, mTvDOB, mTVGender;
    DataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        model = (DataModel) getIntent().getParcelableExtra(UserFormActivity.PARSE_KEY);
        initViews();
        setData();
        setImage();
    }

    private void initViews() {
        mIvImage = (RoundedImageView) findViewById(R.id.ivImage);

        mTvUserName = (TextView) findViewById(R.id.tvUserName);
        mTvEmail = (TextView) findViewById(R.id.tvEmail);
        mTvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
        mTvDOB = (TextView) findViewById(R.id.tvDOB);
        mTVGender = (TextView) findViewById(R.id.tvGender);
    }

    private void setData() {
        mTvUserName.setText(model.getUserName());
        mTvEmail.setText(model.getEmail());
        mTvPhoneNumber.setText(model.getPhoneNumber());
        mTvDOB.setText(model.getDob());
        mTVGender.setText(model.getGender());
    }

    private void setImage() {
        try {
            Uri uri = model.getImagePath();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            mIvImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
