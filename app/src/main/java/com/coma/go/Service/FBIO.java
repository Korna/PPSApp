package com.coma.go.Service;

import com.coma.go.Entity.Account;
import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Event;
import com.google.android.gms.tasks.TaskCompletionSource;


import java.util.ArrayList;

/**
 * Created by Koma on 27.09.2017.
 */

public class FBIO {

    /**  создание аккаунта   */
    public static Account createUserInfo(String uid, Account user){
        return null;
    }
    /** создание ивента в категории */
    public static String createEvent(Event event, String categoryName){//можно доп категории ввести в виде .child(CATEGORY_NAME)
        return null;
    }

    public static Dialog createConversation(ArrayList<String> participants
                                            //, ArrayList<String> admins
    ){
        return null;
    }


    public static ArrayList<Event> getMyEvents(String uid){
        return null;
    }



    static Dialog createdDialog = null;
    static boolean found = false;

    /**  пытается найти диалог. вызывает создание нового, если не найдено */
    public static TaskCompletionSource<Dialog> getActualCid(final String senderUid, final String getterUid){//для случая диалога 1vs1
      return null;
    }

    public static TaskCompletionSource<ArrayList<Dialog>> getConversationsByCids(final ArrayList<String> listOfCids){
        return null;
    }


    public static TaskCompletionSource<Dialog> getConversationByCid(String cid){
        return null;
    }


    public static TaskCompletionSource<ArrayList<String>> getMyCids(String uid){
        return null;
    }

    public static ArrayList<Event> getEvents(String category){
        return null;
    }

    public static void deleteEvent(String uid, String category, final String key){



    }



}
