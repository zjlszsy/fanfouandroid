package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by wendyzhou on 4/12/2017.
 */

public class NewStatusActivity extends Activity {
    public static final String FRIENDS_LIST = "Friend List";
    public static final Character TOKENIZER = '@';
    public static final Character TOKEN_TERMINATOR  = ' ';
    public MultiAutoCompleteTextView inputEditText;
    public static final String API = "userFanfouAPI";
    private static final int ACTIVITY_RESULT_CODE_SELECT_PICTURE = 1;
    File imgFile = null;
    private static final String INTENT_TITLE = "Select Picture";
    private static final String INTENT_TYPE = "image/*";


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.newstatus);
        inputEditText = (MultiAutoCompleteTextView) findViewById(R.id.newStatusText);
        ArrayList<String> friendList = getIntent().getStringArrayListExtra(FRIENDS_LIST);

        String[] friendArray = friendList.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, friendArray);
        inputEditText.setAdapter(adapter);
        inputEditText.setThreshold(1);
        inputEditText.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public int findTokenStart(CharSequence charSequence, int i) {
                while (i > 0 && charSequence.charAt(i - 1) != TOKENIZER) {
                    i--;
                }
                return i;
            }

            @Override
            public int findTokenEnd(CharSequence charSequence, int i) {
                int len = charSequence.length();
                while (i < len) {
                    if (charSequence.charAt(i) == TOKEN_TERMINATOR) {
                        return i;
                    } else {
                        i++;
                    }
                }
                return len;
            }

            @Override
            public CharSequence terminateToken(CharSequence charSequence) {
                int i = charSequence.length();

                while (i > 0 && charSequence.charAt(i - 1) == TOKEN_TERMINATOR) {
                    i--;
                }
                if (i > 0 && charSequence.charAt(i - 1) == TOKEN_TERMINATOR) {
                    return charSequence;
                } else {
                    if (charSequence instanceof Spanned) {
                        SpannableString sp = new SpannableString(charSequence + " ");
                        TextUtils.copySpansFrom((Spanned) charSequence, 0, charSequence.length(), Object.class, sp, 0);
                        return sp;
                    } else {
                        return charSequence + " ";
                    }
                }
            }
        });

        Button uploadPhoto = (Button) findViewById(R.id.upload_photo);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType(INTENT_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        INTENT_TITLE), ACTIVITY_RESULT_CODE_SELECT_PICTURE);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ACTIVITY_RESULT_CODE_SELECT_PICTURE
                    && data != null && data.getData() != null) {
                try {
                    Uri selectedImageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    ImageView photoView = (ImageView) findViewById(R.id.loaded_image);
                    photoView.setImageBitmap(bitmap);


                    imgFile = new File(this.getCacheDir(), "imgfile");
                    imgFile.createNewFile();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitMapData = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(imgFile);
                    fos.write(bitMapData);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void toPost(View v) {
        FanfouAPI api = getIntent().getParcelableExtra(API);
        PostStatusRequest postStatusRequest = new PostStatusRequest();
        postStatusRequest.statusText =  inputEditText.getText().toString();
        postStatusRequest.photo = imgFile;
        try {
            postStatusRequest.execute(api);
        } catch (Exception e) {
            Log.e("Exception", "Issue");
        }
        Intent newTimeline = new Intent(this, DisplayTimelineActivity.class);
        newTimeline.putExtra(DisplayTimelineActivity.API, api);
        startActivity(newTimeline);
    }
}
