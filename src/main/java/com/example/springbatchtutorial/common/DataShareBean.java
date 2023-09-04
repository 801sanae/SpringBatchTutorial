package com.example.springbatchtutorial.common;

/**
 * packageName    : com.example.springbatchtutorial.common
 * fileName       : DataShareBean
 * author         : kmy
 * date           : 2023/09/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/04        kmy       최초 생성
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <p>
 * DTO entity 객체.
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
public class DataShareBean extends ConcurrentHashMap {

//    private static final long serialVersionUID = 1L;

    /** ResultSet 에서 컬럼 이름을 key로 해서 그 값을 DataShareBean 에 저장 */
    public void parseResultSet(ResultSet rs) throws SQLException {

        ResultSetMetaData md = rs.getMetaData();
        int size = md.getColumnCount();
        String columnName = null;
        for (int i = 1; i <= size; i++) {
            columnName = (md.getColumnName(i)).toLowerCase();
            if (columnName != null) {
                /*
                 * Logger.debug.println("[" + i + "]");
                 */
                setValue(columnName, rs.getString(i));
            }
        }
    }

    /** LDAP ResultSet 에서 컬럼 이름을 key로 해서 그 값을 DataShareBean 에 저장 */
    // public void parseLdapResultSet(Enumeration en) throws LDAPException {
    //
    // while ( en.hasMoreElements() ) {
    // LDAPAttribute anAttr = (LDAPAttribute)en.nextElement();
    // String attrName = anAttr.getName();
    // Enumeration enumVals = anAttr.getStringValues();
    //
    // while ( enumVals.hasMoreElements() ) {
    // String aVal = ( String )enumVals.nextElement();
    // setValue(attrName, aVal);
    // }
    // }
    //
    // }

    /** ResultSet 에서 컬럼 이름과 인덱스를 key로 해서 그 값을 DataShareBean 에 저장 */
    public void parseResultSet(int iIndex, ResultSet rs) throws SQLException {

        ResultSetMetaData md = rs.getMetaData();
        int size = md.getColumnCount();

        // String sColumnName = null;
        // String sColumnValue = null;
        for (int i = 1; i <= size; i++) {
            setValue(new StringBuffer((md.getColumnName(i)).toLowerCase()).append(iIndex).toString(), rs.getString(i));
        }
    }
    public String nvl(String str) {
        return str == null ? "" : str ;
    }
    /**
     * Request 에서 parameter 를 뽑아서 DataShareBean 에 저장.
     */
    public void parseRequest(HttpServletRequest request) throws ServletException {

        Enumeration en = request.getParameterNames();
        String param = null;
        String[] values = null;
        while (en.hasMoreElements()) {
            param = (String) en.nextElement();
            values = request.getParameterValues(param);
            if (values.length == 1) {
                if (!"".equals(nvl(param))) {
                    setValue(param, values[0]);
                }
            }
            else {
                ArrayList list = new ArrayList();

                for (int i = 0; i < values.length; i++) {
                    list.add(values[i]);
                }

                String[] temp = new String[list.size()];
                for (int j = 0; j < list.size(); j++) {
                    temp[j] = (String) list.get(j);
                }
                if (!"".equals(nvl(param))) {
                    if (!"".equals(nvl(param))) {
                        setValue(param, temp);
                    }
                }
            }
        }
    }

    public void setValue(String sKey, String sValue) {
        if (sValue != null)
            put(sKey, sValue);
    }

    public void setValue(String sKey, String sValues[]) {
        put(sKey, sValues);
    }

    public void setValue(String sKey, byte[] yValues) {
        String sValue = null;

        if (yValues != null)
            sValue = new String(yValues);

        put(sKey, sValue);
    }

    public void setValue(String sKey, byte yValue) {
        put(sKey, Byte.toString(yValue));
    }

    public void setValue(String sKey, char[] cValues) {
        String sValue = null;

        if (cValues != null)
            sValue = new String(cValues);

        put(sKey, sValue);
    }

    public void setValue(String sKey, char cValue) {
        put(sKey, String.valueOf(cValue));
    }

    public void setValue(String sKey, float fValue) {
        put(sKey, String.valueOf(fValue));
    }

    public void setValue(String sKey, boolean bValue) {
        put(sKey, String.valueOf(bValue));
    }

    public void setValue(String sKey, short tValue) {
        put(sKey, String.valueOf(tValue));
    }

    public void setValue(String sKey, int iValue) {
        put(sKey, String.valueOf(iValue));
    }

    public void setValue(String sKey, long lValue) {
        put(sKey, String.valueOf(lValue));
    }

    public void setValue(String sKey, double dValue) {
        put(sKey, String.valueOf(dValue));
    }

    public void setValue(String sKey, java.util.Date value) {
        String sValue = null;
        if (value != null){
            sValue = value.toString();
        }
        put(sKey, sValue);
    }

    public void setValue(String sKey, Vector value) {
        put(sKey, value);
    }

    public void setValue(String sKey, ArrayList value) {
        put(sKey, value);
    }

    public void setValue(String sKey, Hashtable value) {
        put(sKey, value);
    }

    public void setValue(String sKey, DataShareBean value) {
        put(sKey, value);
    }

    public void setValue(String sKey, ConcurrentHashMap value) {
        put(sKey, value);
    }

    /* 2003-07-16 */
    public void setValue(String sKey, Object value) {
        put(sKey, value);
    }

    public String getTrimString(String sKey) {

        String sValue = "";
        Object obj = null;

        try {
            obj = get(sKey);
            if (obj instanceof String) {
                sValue = ((String) obj).trim();
            }
            else if (obj instanceof String[]) {
                sValue = ((String[]) obj)[0].trim();
            }
            else {
                sValue = "";
            }
        }
        catch (Exception e) {
            sValue = "";
        }

        return sValue;
    }

    public String getString(String sKey) {

        String sValue = "";
        Object obj = null;

        try {
            obj = get(sKey);
            if (obj instanceof String) {
                sValue = ((String) obj);
            }
            else if (obj instanceof String[]) {
                sValue = ((String[]) obj)[0];
            } else if (obj instanceof Long) {
                sValue =  Long.toString((Long) obj);
            }
            else {
                sValue = "";
            }
        }
        catch (Exception e) {
            sValue = "";
            System.err.println(e.getStackTrace());
        }

        return sValue;
    }

    /**
     * DataShareBean 에 저장된 String[] 를 return 하는 method. String 이 저장되어 있을 경우에는 length 가 1인 String[] 를 return.
     */
    public String[] getStrings(String sKey) {

        String sValues[] = null;
        Object obj = null;

        try {
            obj = get(sKey);
            if (obj instanceof String) {
                sValues = new String[1];
                sValues[0] = (String) obj;
            }
            else {
                sValues = (String[]) obj;
            }
        }
        catch (Exception e) {
        }

        return sValues;
    }

    public byte getByte(String sKey) {

        byte yResult = (byte) 0;

        try {
            yResult = Byte.parseByte((String) get(sKey));
        }
        catch (Exception e) {
        }

        return yResult;
    }

    public byte[] getBytes(String sKey) {

        byte[] yResults = null;

        try {
            yResults = ((String) get(sKey)).getBytes();
        }
        catch (Exception e) {
        }

        return yResults;
    }

    public char getChar(String sKey) {

        char cResult = (char) 0;

        try {
            cResult = ((String) get(sKey)).charAt(0);
        }
        catch (Exception e) {
        }

        return cResult;
    }

    public char[] getChars(String sKey) {

        char[] cResults = null;

        try {
            cResults = ((String) get(sKey)).toCharArray();
        }
        catch (Exception e) {
        }

        return cResults;
    }

    public float getFloat(String sKey) {

        float fResult = 0;

        try {
            fResult = Float.parseFloat((String) get(sKey));
        }
        catch (Exception e) {
        }

        return fResult;
    }

    public boolean getBoolean(String sKey) {

        boolean bResult = false;

        try {
            String sVal = (String) get(sKey);

            if (sVal.equals("true")) {
                bResult = true;
            }
            else if (sVal.equals("false")) {
                bResult = false;
            }
            else {
                bResult = Boolean.getBoolean(sVal);
            }
        }
        catch (Exception e) {
        }

        return bResult;
    }

    public short getShort(String sKey) {

        short tResult = 0;

        try {
            tResult = Short.parseShort((String) get(sKey));
        }
        catch (Exception e) {
        }

        return tResult;
    }

    // 2011.03.08 get(sKey) Integer Type 으로 Return 할 경우 Integer 로 Casting
    public int getInt(String sKey) {

        int iResult = 0;

        try {
            if (get(sKey) instanceof String) {
                iResult = Integer.parseInt((String) get(sKey));
            }
            else if (get(sKey) instanceof BigDecimal) {
                BigDecimal big = (BigDecimal) get(sKey);
                iResult = big.intValue();
            }
            else {
                iResult = (Integer) get(sKey);
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }

        return iResult;
    }

    public long getLong(String sKey) {

        long lResult = 0;

        try {
            if (get(sKey) instanceof String) {
                lResult = Long.parseLong((String) get(sKey));
            }
            else {
                lResult = (Long) get(sKey);
            }

        }
        catch (Exception e) {
        }

        return lResult;
    }

    public String getStringLongValue(String sKey) {

        long lResult = 0;

        try {
            if (get(sKey) instanceof String) {
                lResult = Long.parseLong((String) get(sKey));
            }
            else {
                lResult = (Long) get(sKey);
            }

        }
        catch (Exception e) {
        }

        return Long.toString(lResult);
    }

    public double getDouble(String sKey) {

        double dResult = 0;

        try {
            // get(sKey) BigDecimal로 return
            // BigDecimal을 double 로 변경
            // 2011.03.05 String type 일 경우 parseDouble, 다른 Class 일 경우 BigDecimal
            if (get(sKey) instanceof String) {
                dResult = Double.parseDouble((String) get(sKey));
            }
            else if (get(sKey) instanceof BigDecimal) {
                BigDecimal big = (BigDecimal) get(sKey);
                dResult = big.doubleValue();
            }
            else {
                dResult = (Double) get(sKey);
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }

        return dResult;
    }

    /**
     *
     * @mehod name : DataShareBean-getDate
     * @writer : Administrator
     * @regdate : 2011. 3. 13.
     * @return : java.util.Date
     * @param sKey
     * @return
     * @comment : 2011.03.13 return Data Class 일 경우 Date 로 캐스팅 추가
     */

    public java.util.Date getDate(String sKey) {

        java.util.Date result = null;

        try {
            if (get(sKey) instanceof Date) {
                result = (Date) get(sKey);
            }
            else {
                String sDate = (String) get(sKey);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                ParsePosition pos = new ParsePosition(0);
                result = formatter.parse(sDate, pos);
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }

        return result;
    }

    public Vector getVector(String sKey) {

        Vector vResult = null;

        try {
            vResult = (Vector) get(sKey);
            if (vResult == null)
                vResult = new Vector();
        }
        catch (Exception e) {
            vResult = new Vector();
        }

        return vResult;
    }

    public ArrayList getArrayList(String sKey) {

        ArrayList alResult = null;

        try {
            alResult = (ArrayList) get(sKey);
            if (alResult == null)
                alResult = new ArrayList();
        }
        catch (Exception e) {
            alResult = new ArrayList();
        }

        return alResult;
    }

    public Hashtable getHashtable(String sKey) {

        Hashtable value = null;

        try {
            value = (Hashtable) get(sKey);
            if (value == null)
                value = new Hashtable();
        }
        catch (Exception e) {
            value = new Hashtable();
        }

        return value;
    }

    public DataShareBean getEntity(String sKey) {

        DataShareBean value = null;

        try {
            value = (DataShareBean) get(sKey);
            if (value == null)
                value = new DataShareBean();
        }
        catch (Exception e) {
            value = new DataShareBean();
        }

        return value;
    }

    public ConcurrentHashMap getHashMap(String sKey) {

        ConcurrentHashMap value = null;

        try {
            value = (ConcurrentHashMap) get(sKey);
            if (value == null)
                value = new ConcurrentHashMap();
        }
        catch (Exception e) {
            value = new ConcurrentHashMap();
        }

        return value;
    }

    public Object getObject(String sKey) {

        Object value = null;

        try {
            value = get(sKey);
            if (value == null)
                value = new Object();
        }
        catch (Exception e) {
            value = new Object();
        }

        return value;
    }

    public Object remove(Object sKey) {
        ConcurrentHashMap hm = this;
        DataShareBean rtn = (DataShareBean) hm.remove(sKey);
        return rtn;
    }

    public String getKey(String sValue) {

        String sResult = null;

        Set keySet = entrySet();
        Object lists[] = keySet.toArray();

        String sKey = null;
        Object value = null;
        for (int i = 0; i < lists.length; i++) {
            sKey = (String) (((Map.Entry) lists[i]).getKey());
            value = get(sKey);
            if (value instanceof String && ((String) value).trim().equals(sValue)) {
                sResult = (String) sKey;
                break;
            }
        }

        return sResult;
    }

    public String getKey(String sValue, String sKeyPrefix) {

        String sResult = null;

        Set keySet = entrySet();
        Object lists[] = keySet.toArray();

        String sKey = null;
        Object value = null;

        for (int i = 0; i < lists.length; i++) {
            sKey = (String) (((Map.Entry) lists[i]).getKey());
            value = get(sKey);
            if (sKey.startsWith(sKeyPrefix) && value instanceof String && ((String) value).trim().equals(sValue)) {
                sResult = (String) sKey;
                break;
            }
        }

        return sResult;
    }

    public ArrayList getStringKeys() {
        int count = 0;
        ConcurrentHashMap hm = this;
        ArrayList keys = new ArrayList();
        for (Iterator i = hm.keySet().iterator(); i.hasNext();) {
            Object key = (Object) i.next();
            Object value = (Object) hm.get(key);
            if (value instanceof String) {
                keys.add(key);
                count++;
            }
        }

        return keys;
    }

    public ArrayList getKeys() {
        int count = 0;
        ConcurrentHashMap hm = this;
        ArrayList keys = new ArrayList();
        for (Iterator i = hm.keySet().iterator(); i.hasNext();) {
            Object key = (Object) i.next();
            keys.add(key);
            count++;
        }

        return keys;
    }

    public ArrayList getValues() {
        int count = 0;
        ConcurrentHashMap hm = this;
        ArrayList values = new ArrayList();
        ArrayList keys = getKeys();
        for (int i = 0; i < keys.size(); i++) {
            Object value = (Object) hm.get(keys.get(i));
            values.add(value);
            count++;
        }

        return values;
    }

    public ArrayList getStringKeys(ConcurrentHashMap hm) {
        int count = 0;
        ArrayList keys = new ArrayList();
        for (Iterator i = hm.keySet().iterator(); i.hasNext();) {
            Object key = (Object) i.next();
            if (key instanceof String) {
                keys.add(key);
                count++;
            }
        }

        return keys;
    }

    public ArrayList getStringValues() {
        int count = 0;
        ConcurrentHashMap hm = this;
        ArrayList values = new ArrayList();
        ArrayList keys = getStringKeys(hm);
        for (int i = 0; i < keys.size(); i++) {
            Object value = (Object) hm.get(keys.get(i));
            if (value instanceof String) {
                values.add(value);
                count++;
            }
        }

        return values;
    }

}