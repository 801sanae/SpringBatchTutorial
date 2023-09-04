package com.example.springbatchtutorial.common;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * packageName    : com.example.springbatchtutorial.common
 * fileName       : DataShareBeanUtils
 * author         : kmy
 * date           : 2023/09/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/04        kmy       최초 생성
 */

@Slf4j
public class DataShareBeanUtils {
    public static int CASE_LOWER = 0;
    public static int CASE_UPPER = 1;

    /**
     * <p>
     * 2개의 DataShareBean을 merge(병합)할 때 사용.
     * </p>
     *
     * @param preDataShareBean
     *            DataShareBean.
     * @param nextDataShareBean
     *            DataShareBean.
     * @param option
     *            (1.option="0" --> 동일한 key는 두번째 DataShareBean의 value로 덮어쓴다./ 2.option="1" --> 동일한 key는 해당 value를 배열로 처리한다.)
     * @return merged DataShareBean.
     * @comment 중복 key 에 해당하는 value를 배열로 처리할 때는 return되는 DataShareBean에서 중복 key 에 해당하는 value는 ArrayList 로 반환받는다.(*주의)
     */
    public static DataShareBean merge(DataShareBean preDataShareBean, DataShareBean nextDataShareBean, int option) {

        DataShareBean rtn = null;
        try {
            // 두개의 DataShareBean 중 하나라도 null 이거나 size()가 0이라면 null 이 아닌 size()가 0인 DataShareBean를 return 한다.
            if (preDataShareBean == null || preDataShareBean.size() < 1 || nextDataShareBean == null || nextDataShareBean.size() < 1 || option > 1) {
                rtn = new DataShareBean();
            }
            else {
                // 각각의 DataShareBean 에 담긴 key,value 를 쌍으로 ArrayList 에 담는다.
                ArrayList preKeyList = preDataShareBean.getKeys();
                ArrayList preValList = preDataShareBean.getValues();
                ArrayList nextKeyList = nextDataShareBean.getKeys();
                ArrayList nextValList = nextDataShareBean.getValues();

                // key,value 를 담을 DataShareBean 인스턴스 생성.
                rtn = new DataShareBean();

                // 옵션값 체크
                if (option == 0) { // overwrite merge.
                    // 첫째 DataShareBean 추가
                    for (int i = 0; i < preKeyList.size(); i++) {
                        rtn.put((Object) preKeyList.get(i), (Object) preValList.get(i));
                    }
                    // 둘째 DataShareBean 추가
                    for (int i = 0; i < nextKeyList.size(); i++) {
                        rtn.put((Object) nextKeyList.get(i), (Object) nextValList.get(i));
                    }
                }
                else { // array merge.
                    ArrayList preKeyTemp = new ArrayList(); // 중복 key 를 제외한 첫번째 key ArrayList
                    ArrayList nextKeyTemp = new ArrayList(); // 중복 key 를 제외한 두번째 key ArrayList
                    ArrayList preValTemp = new ArrayList(); // 중복 key 를 제외한 첫번째 value ArrayList
                    ArrayList nextValTemp = new ArrayList(); // 중복 key 를 제외한 두번째 value ArrayList
                    DataShareBean dupTemp = new DataShareBean(); // 중복 key 에 해당하는 key,value 를 저장할 DataShareBean
                    ArrayList dupValTemp = null; // 중복 key 에 해당하는 value 를 저장할 ArrayList

                    // merge 준비.
                    boolean check = false;
                    int jVal = 0;
                    for (int i = 0; i < preKeyList.size(); i++) {
                        for (int j = 0; j < nextKeyList.size(); j++) {
                            if (((String) preKeyList.get(i)).equals((String) nextKeyList.get(j))) {
                                check = true;
                                jVal = j;
                                break;
                            }
                        }

                        if (!check) {
                            preKeyTemp.add((Object) preKeyList.get(i));
                            preValTemp.add((Object) preValList.get(i));
                        }
                        else {
                            dupValTemp = new ArrayList();
                            dupValTemp.add((Object) preValList.get(i));
                            dupValTemp.add((Object) nextValList.get(jVal));
                            dupTemp.put((Object) preKeyList.get(i), dupValTemp);
                            dupValTemp = null;
                        }
                        check = false;
                    }

                    boolean check2 = false;
                    if (dupTemp != null && dupTemp.size() > 0) {
                        ArrayList dupKeyList = dupTemp.getKeys();
                        for (int i = 0; i < nextKeyList.size(); i++) {
                            for (int k = 0; k < dupTemp.size(); k++) {
                                if (((String) nextKeyList.get(i)).equals((String) dupKeyList.get(k))) {
                                    check2 = true;
                                    break;
                                }
                            }

                            if (!check2) {
                                nextKeyTemp.add((Object) nextKeyList.get(i));
                                nextValTemp.add((Object) nextValList.get(i));
                            }
                            check2 = false;
                        }
                    }
                    else {
                        nextKeyTemp = nextKeyList;
                        nextValTemp = nextValList;
                    }

                    // 첫째 ArrayList 추가
                    for (int i = 0; i < preKeyTemp.size(); i++) {
                        rtn.put((Object) preKeyTemp.get(i), (Object) preValTemp.get(i));
                    }
                    // 둘째 ArrayList 추가
                    for (int i = 0; i < nextKeyTemp.size(); i++) {
                        rtn.put((Object) nextKeyTemp.get(i), (Object) nextValTemp.get(i));
                    }
                    // 중복 key 에 따른 array value 를 담은 DataShareBean 추가
                    if (dupTemp != null && dupTemp.size() > 0) {
                        for (int i = 0; i < dupTemp.size(); i++) {
                            ArrayList dupKeyList = dupTemp.getKeys();
                            ArrayList dupValList = dupTemp.getValues();
                            rtn.put((Object) dupKeyList.get(i), (Object) dupValList.get(i));
                        }
                    }
                }
            }
        }
        catch (Exception e) {
        }
        return rtn;
    }

    public static DataShareBean remove(DataShareBean DataShareBean, String fname) {

        DataShareBean newDataShareBean = new DataShareBean();
        for (Iterator it = DataShareBean.keySet().iterator(); it.hasNext();) {

            String name = (String) it.next();
            if (name.equals(fname))
                continue;

            Object obj = DataShareBean.get(name);
            newDataShareBean.setValue(name, obj);
        }
        return newDataShareBean;
    }

    public static void mergeTest() {
        DataShareBean temp1 = new DataShareBean();
        DataShareBean temp2 = new DataShareBean();
        String[] bVal = new String[3];
        bVal[0] = "1";
        bVal[1] = "1";
        bVal[2] = "1";
        temp1.put("a", "a");
        temp1.put("b", bVal);
        temp1.put("c", "c");
        temp1.put("d", "d");
        temp2.put("a", "a");
        temp2.put("b", "b");
        temp2.put("d", bVal);
        temp2.put("e", "e");

        DataShareBean rtn1 = merge(temp1, temp2, 0);
        DataShareBean rtn2 = merge(temp1, temp2, 1);

        if (log.isDebugEnabled()) {
            log.debug("merge(temp1, temp2, 0) ::::: \n" + rtn1);
            log.debug("merge(temp1, temp2, 1) ::::: \n" + rtn2);
        }
    }

    /**
     * <p>
     * DataShareBean Key name 대소문자 변환
     * </p>
     *
     * @mehod name : DataShareBeanUtil-convertKeyCase
     * @writer : prehacke
     * @regdate : 2011. 3. 22.
     * @return : DataShareBean
     * @param param
     * @param CASE_TYPE
     * @return
     * @throws Exception
     * @comment :
     */
    public static DataShareBean convertKeyCase(DataShareBean param, int CASE_TYPE) throws Exception {

        DataShareBean tmp = new DataShareBean();
        ArrayList<String> keyList = new ArrayList<String>();
        int keyListSize = 0;

        try {
            keyList = param.getKeys();
            keyListSize = keyList.size();

            for (int i = 0; i < keyListSize; i++) {
                String key = keyList.get(i);

                if (CASE_TYPE == CASE_LOWER)
                    tmp.put(key.toLowerCase(), param.get(key));
                else if (CASE_TYPE == CASE_UPPER)
                    tmp.put(key.toUpperCase(), param.get(key));
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }

        return tmp;
    }
}