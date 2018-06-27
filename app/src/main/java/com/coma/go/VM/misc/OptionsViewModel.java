package com.coma.go.VM.misc;

import android.app.Application;
import android.support.annotation.NonNull;

import com.coma.go.Entity.Options;
import com.coma.go.Misc.App;
import com.coma.go.VM.ActionViewModel;


/**
 * Created by Koma on 24.03.2018.
 */

public class OptionsViewModel extends ActionViewModel<Options> {


    public OptionsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getOptions(){
       action(App.getApp().getComponent().webApi().getOptions());
    }

}
