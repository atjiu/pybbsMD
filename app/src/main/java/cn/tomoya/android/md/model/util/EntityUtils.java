package cn.tomoya.android.md.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class EntityUtils {

    private EntityUtils() {}

    public static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
//            .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
            .create();

}
