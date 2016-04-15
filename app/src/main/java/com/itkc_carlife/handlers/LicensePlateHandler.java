package com.itkc_carlife.handlers;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/1/6.
 */
public class LicensePlateHandler {

    private static String[] provinces = new String[]{"粤", "京", "津", "沪", "渝",
            "冀", "豫", "云", "辽", "黑",
            "湘", "皖", "鲁", "苏", "赣",
            "浙", "鄂", "桂", "甘", "晋",
            "蒙", "陕", "吉", "闽", "贵",
            "青", "藏", "川", "宁", "新",
            "琼"};

    private static List<String> provincesTextList;
    private static Map<String, String[]> cityCodeMap;

    public static List<String> getProvincesTextList() {
        if (provincesTextList == null) {
            initProvincesTextList();
        }
        return provincesTextList;
    }

    private static void initProvincesTextList() {
        provincesTextList = new ArrayList<String>();
        for (String s : provinces) {
            provincesTextList.add(s);
        }
    }

    public static List<String> getCityCodeList(String[] cityCodes) {
        List<String> list = new ArrayList<String>(cityCodes.length);
        for (String s : cityCodes) {
            list.add(s);
        }
        return list;
    }

    public static List<String> getCityCodeList(String p) {
        if (cityCodeMap == null) {
            initCityCodeMap();
        }

        if (cityCodeMap.containsKey(p)) {
            return getCityCodeList(cityCodeMap.get(p));
        }
        return new ArrayList<>();
    }

    private static void initCityCodeMap() {
        cityCodeMap = new HashMap<String, String[]>();
        cityCodeMap.put(provinces[0], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "VR", "VT", "VS", "VXX", "W", "X", "Y", "Z"});
        cityCodeMap.put(provinces[1], new String[]{"A", "B", "C", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Z"});
        cityCodeMap.put(provinces[2], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R"});
        cityCodeMap.put(provinces[3], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "AX", "BX", "DX"});
        cityCodeMap.put(provinces[4], new String[]{"A", "B", "C", "F", "G", "H"});
        cityCodeMap.put(provinces[5], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "R", "T"});
        cityCodeMap.put(provinces[6], new String[]{"A", "B", "C", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "U"});
        cityCodeMap.put(provinces[7], new String[]{"A", "C", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S"});
        cityCodeMap.put(provinces[8], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "O"});
        cityCodeMap.put(provinces[9], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "U"});
        cityCodeMap.put(provinces[10], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "U"});
        cityCodeMap.put(provinces[11], new String[]{"A", "B", "C", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S"});
        cityCodeMap.put(provinces[12], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R"});
        cityCodeMap.put(provinces[13], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N"});
        cityCodeMap.put(provinces[14], new String[]{"A", "B", "C", "E", "F", "G", "H", "J", "K", "L", "M", "S"});
        cityCodeMap.put(provinces[15], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L"});
        cityCodeMap.put(provinces[16], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "AW"});
        cityCodeMap.put(provinces[17], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R"});
        cityCodeMap.put(provinces[18], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P"});
        cityCodeMap.put(provinces[19], new String[]{"A", "B", "C", "D", "E", "F", "H", "J", "K", "L", "M"});
        cityCodeMap.put(provinces[20], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M"});
        cityCodeMap.put(provinces[21], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "V"});
        cityCodeMap.put(provinces[22], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K"});
        cityCodeMap.put(provinces[23], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K"});
        cityCodeMap.put(provinces[24], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J"});
        cityCodeMap.put(provinces[25], new String[]{"A", "B", "C", "D", "E", "F", "G", "H"});
        cityCodeMap.put(provinces[26], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J"});
        cityCodeMap.put(provinces[27], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "O", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"});
        cityCodeMap.put(provinces[28], new String[]{"A", "B", "C", "D", "E"});
        cityCodeMap.put(provinces[29], new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R"});
        cityCodeMap.put(provinces[30], new String[]{"A", "B", "C", "D", "E", "F"});
    }

    private final static String PROVIONCE_KEY = "province_key";

    public static String getProvinceText(Context context) {
        return SystemHandle.getString(context, PROVIONCE_KEY);
    }

    public static void saveProvinceText(Context context, String p) {
        SystemHandle.saveStringMessage(context, PROVIONCE_KEY, p);
    }

    private final static String CITY_CODE_KEY = "city_code_key";

    public static String getCityCodeText(Context context) {
        return SystemHandle.getString(context, CITY_CODE_KEY);
    }

    public static void saveCityCodeText(Context context, String c) {
        SystemHandle.saveStringMessage(context, CITY_CODE_KEY, c);
    }

}
