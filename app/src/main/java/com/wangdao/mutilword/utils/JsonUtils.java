package com.wangdao.mutilword.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by yxd on 2016/4/20.
 */
public class JsonUtils {
    public static String fromAssetsToString(Context context ,String jsonName, String codeFormat) {
        String result = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(jsonName), codeFormat);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            result = stringBuilder.toString();
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
