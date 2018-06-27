package com.coma.go.VM.misc;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;


import com.coma.go.Misc.App;
import com.coma.go.Model.CreatedTokenKey;
import com.coma.go.VM.ActionViewModel;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Created by Koma on 24.03.2018.
 */

public class TwoFACreateViewModel extends ActionViewModel<CreatedTokenKey> {


    public TwoFACreateViewModel(@NonNull Application application) {
        super(application);
    }

    public void create(){
       action(App.getApp().getComponent().webApi().token2fCreate());
    }

    public void copyToClipBoard(String text){
        ClipboardManager myClipboard = (ClipboardManager) getApplication().getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
    }

    public String pasteFromClipboard(){
        ClipboardManager clipboard = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";

        // If it does contain data, decide if you can handle the data.
        if (!(clipboard.hasPrimaryClip())) {
        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
            // since the clipboard has data but it is not plain text
        } else {
            //since the clipboard contains plain text.
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

            // Gets the clipboard as text.
            pasteData = item.getText().toString();
        }
        return pasteData;
    }

}
