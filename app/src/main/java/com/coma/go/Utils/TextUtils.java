package com.coma.go.Utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Koma on 16.04.2018.
 */

public class TextUtils {



    public static boolean checkInputDate(String string){
        Log.i("checkDate", string);
        boolean error = true;
        Pattern pattern = Pattern.compile("(\\d+)\\.?(\\d*)\\.?(\\d*)");
        Matcher matcher = pattern.matcher(string);

        int d = -1;
        int m = -1;
        int y = -1;
        boolean matches = matcher.matches();
        try {
            d = Integer.valueOf(matcher.group(1));
            m = Integer.valueOf(matcher.group(2));
            y = Integer.valueOf(matcher.group(3));
        }catch (IllegalStateException ise){
            Log.w("checkDate:", ise.toString());
        }catch (ArrayIndexOutOfBoundsException aof){
            Log.w("checkDate:", aof.toString());
        }catch (NumberFormatException nfe){
            Log.w("checkDate:", nfe.toString());
        }

        // Log.d("checkDate:", d + " " + m + " " + y + " " + matches);

        if(matches){
            if(d < 32){
                error = false;
                if(m < 13){
                    error = false;
                    if((y < 2100) || y == -1)
                        error = false;
                    else
                        error = true;
                }else
                    error = true;

            }

        }

        return error;
    }
}
