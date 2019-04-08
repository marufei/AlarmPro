package com.huikezk.alarmpro.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.huikezk.alarmpro.entity.RepairEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/15.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class JsonUtil {
    private static String TAG="JsonUtil";

    /**
     * 维修数据转json
     * @param stringList
     * @return
     */
    public static List<RepairEntity> repair2Json(List<String> stringList){
        List<RepairEntity> list = null;
        try{
            list=new ArrayList<>();
            Gson gson=new Gson();
            for (int i=0;i<stringList.size();i++){
                if (!TextUtils.isEmpty(stringList.get(i))&&stringList.get(i).equals("undefined")){
                    continue;
                }
                RepairEntity repairEntity=gson.fromJson(stringList.get(i),RepairEntity.class);
                list.add(repairEntity);
            }

        }catch (Exception e){
            MyUtils.Loge(TAG,"repair2Json--e:"+e.getMessage());
        }
        return list;
    }
}
