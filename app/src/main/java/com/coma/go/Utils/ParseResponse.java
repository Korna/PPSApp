package com.coma.go.Utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Koma on 19.12.2017.
 */

public class ParseResponse {
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN = "^(.*)=(.*);(.*)=(.*)=(.*)$";
           // "^.*=(.*);$";

    public boolean isValid(final String hex) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(hex);
        return !matcher.matches();
    }

    public String getSessionId(final String hex) throws IOException{
        String str = "";
        try {
            str = hex.split(";")[0];
        }catch(NullPointerException npe){
            npe.printStackTrace();
            throw new IOException("Cant parse header");
        }catch (IndexOutOfBoundsException iobe){
            iobe.printStackTrace();
            throw new IOException("Cant parse header");
        }
        return str;
    }
    //когда получаю инфу о карточке курьера, то надо распарсить для показа инфу
    public String dateParser(String date){
        String newDate = "";
        Pattern pattern = Pattern.compile("^(\\d+)-(\\d+)-(\\d+)(.*)$");
        if (pattern.matcher(date).matches()) {
            Matcher matcher = pattern.matcher(date);
            matcher.find();
            newDate = matcher.group(3);

            newDate += "." + matcher.group(2);
            newDate += "." + matcher.group(1);
            //Log.d("dateIs:", newDate);
        }else{
            Logger.e("dateParser", "cant parse by rule:" + date);
            newDate = date;
        }
        return newDate;
    }

    //когда курьер отправляет данные о паспорте, он вводит их в порядке ДД.ММ.ГГГГ, но на сервер надо отправить в виде ММ.ДД.ГГГГ
    public String dateParserSend(String date){
        String newDate = "";
        Pattern pattern = Pattern.compile("^(\\d+).(\\d+).(\\d+)$");
        if (pattern.matcher(date).matches()) {
            Matcher matcher = pattern.matcher(date);
            matcher.find();
            newDate = matcher.group(2);

            newDate += "." + matcher.group(1);
            newDate += "." + matcher.group(3);
           // Log.d("dateIs:", newDate);
        }
        return newDate;
    }
}
