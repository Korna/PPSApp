package com.coma.go.VM.misc;

import android.app.Application;
import android.support.annotation.NonNull;

import com.coma.go.Misc.App;
import com.coma.go.VM.ActionViewModel;


/**
 * Created by Koma on 24.03.2018.
 */

public class TwoFAActivateViewModel extends ActionViewModel<Void> {


    public TwoFAActivateViewModel(@NonNull Application application) {
        super(application);
    }

    public void activate(String code){
       action(App.getApp().getComponent().webApi().token2fActivate(code));
    }

}
