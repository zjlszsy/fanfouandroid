package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

import java.io.File;
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
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    File imgFile = null;


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
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                ImageView photoView = (ImageView) findViewById(R.id.loaded_image);
                imgFile = new  File(selectedImagePath);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                photoView.setImageBitmap(myBitmap);
            }
        }
    }

    public String getPath(Uri contentUri) {
        return CompatUtils.getPath(this, contentUri);

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
