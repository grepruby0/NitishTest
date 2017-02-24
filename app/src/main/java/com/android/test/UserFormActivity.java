package com.android.test;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.test.custom.RoundedImageView;
import com.android.test.model.DataModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserFormActivity extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    public static final String PARSE_KEY = "com.android.text.parse_key";

    RoundedImageView mIvImage;
    EditText mEtUserName, mEtEmail, mEtPhoneNumber, mEtDOB;
    RadioGroup mRadioGroup;
    RadioButton mMale, mFemale;
    Button mSubmit;
    DataModel dataModel;

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    private static final int PICK_IMAGE = 100;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog dobDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        dataModel = new DataModel();
        initViews();
    }

    private void initViews() {

        mIvImage = (RoundedImageView) findViewById(R.id.ivImage);
        mIvImage.setOnClickListener(this);

        mEtUserName = (EditText) findViewById(R.id.etUserName);
        mEtEmail = (EditText) findViewById(R.id.etEmail);
        mEtPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        mEtDOB = (EditText) findViewById(R.id.etDOB);
        mEtDOB.setOnClickListener(this);
        initDobDialog();

        mRadioGroup = (RadioGroup) findViewById(R.id.radio);
        mRadioGroup.setOnCheckedChangeListener(this);
        mMale = (RadioButton) findViewById(R.id.male);
        dataModel.setGender(MALE);
        mFemale = (RadioButton) findViewById(R.id.female);

        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
    }

    private void initDobDialog() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mEtDOB.setError(null);
                mEtDOB.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImage:
                onUserImageOnClickListener();
                break;
            case R.id.etDOB:
                mEtDOB.setError(null);
                onDOBOnClickListener();
                break;
            case R.id.submit:
                onSubmitButtonClickedListener();
                break;
        }
    }

    private void onDOBOnClickListener() {
        dobDatePickerDialog.show();
    }

    private void onUserImageOnClickListener() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                dataModel.setImagePath(uri);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mIvImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSubmitButtonClickedListener() {
        if (hasValue(mEtUserName) && hasValue(mEtDOB)
                && (hasImagePath())
                && isEmailValid(mEtEmail)
                && isValidPhoneNumber(mEtPhoneNumber)
                ) {
            dataModel.setUserName(mEtUserName.getText().toString());
            dataModel.setEmail(mEtEmail.getText().toString());
            dataModel.setPhoneNumber(mEtPhoneNumber.getText().toString());
            dataModel.setDob(mEtDOB.getText().toString());
            //Toast.makeText(this, "Go to details screen", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,UserDetailActivity.class);
            intent.putExtra(PARSE_KEY,dataModel);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.male:
                dataModel.setGender(MALE);
                break;
            case R.id.female:
                dataModel.setGender(FEMALE);
                break;
        }
    }

    private boolean hasValue(EditText... editTexts) {
        try {
            boolean isHasValue = true;
            for (EditText editText : editTexts) {
                if ((editText == null ||
                        editText.length() < 1) && isHasValue) {
                    editText.setError("Please enter value");
                    isHasValue = false;
                    return isHasValue;
                }
            }
            return isHasValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasImagePath() {
        boolean isImagePath = false;
        try {
            isImagePath = (dataModel.getImagePath() != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isImagePath) {
            Toast.makeText(this, "Please select user image", Toast.LENGTH_LONG).show();
        }
        return isImagePath;
    }

    public static boolean isEmailValid(EditText email) {

        boolean isValid = false;
        if (email != null && email.length() > 0) {
            String emailStr = email.getText().toString();

            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = emailStr;

            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                isValid = true;
            }
        }
        if (!isValid) {
            email.setError("Please enter valid email address");
        }
        return isValid;
    }

    private boolean isValidPhoneNumber(EditText etPhoneNumber) {
        boolean isValidNumber = false;
        if (etPhoneNumber != null && etPhoneNumber.length() > 0) {
            CharSequence phoneNumberStr = etPhoneNumber.getText();
            if (!TextUtils.isEmpty(phoneNumberStr)) {
                isValidNumber = Patterns.PHONE.matcher(phoneNumberStr).matches();
            }
        }
        if (!isValidNumber) {
            etPhoneNumber.setError("Please enter valid phone number");
        }
        return isValidNumber;
    }
}
