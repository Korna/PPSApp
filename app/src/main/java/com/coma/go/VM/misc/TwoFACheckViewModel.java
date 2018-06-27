package com.coma.go.VM.misc;

import android.app.Application;
import android.support.annotation.NonNull;

import com.coma.go.Misc.App;
import com.coma.go.VM.ActionViewModel;


/**
 * Created by Koma on 24.03.2018.
 */

public class TwoFACheckViewModel extends ActionViewModel<Boolean> {


    public TwoFACheckViewModel(@NonNull Application application) {
        super(application);
    }

    public void check(String email){
       action(App.getApp().getComponent().webApi().token2fCheck(email));
    }

}
