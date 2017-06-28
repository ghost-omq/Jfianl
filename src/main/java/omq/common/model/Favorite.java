package omq.common.model;

import java.util.HashMap;
import java.util.Map;

import omq.common.model.base.BaseFavorite;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Favorite extends BaseFavorite<Favorite> {
	public static final int REF_TYPE_PROJECT = 1;
    public static final int REF_TYPE_SHARE = 2;
    public static final int REF_TYPE_FEEDBACK = 3;

    private static Map<String, Integer> tableToTypeValue = new HashMap<String, Integer>(){{
        put("project", REF_TYPE_PROJECT);
        put("share", REF_TYPE_SHARE);
        put("feedback", REF_TYPE_FEEDBACK);
    }};

    private static Map<Integer, String> typeValueToTable = new HashMap<Integer, String>(){{
        put(REF_TYPE_PROJECT, "project");
        put(REF_TYPE_SHARE, "share");
        put(REF_TYPE_FEEDBACK, "feedback");
    }};

    public static String getRefTable(int refType) {
        return typeValueToTable.get(refType);
    }

    public static int getRefType(String tableName) {
        Integer refType = tableToTypeValue.get(tableName);
        if (refType == null) {
            throw new IllegalArgumentException("tableName 不正确");
        }
        return refType;
    }

    public static void checkRefTypeTable(String refTypeTable) {
        if ( !tableToTypeValue.containsKey(refTypeTable) ) {
            throw new IllegalArgumentException("refType 不正确");
        }
    }

    /**
     * 返回收藏资源的 url
     */
    public String getRefUrl() {
        int refType = getRefType();
        int refId = getRefId();
        if (refType == REF_TYPE_PROJECT) {
            return "/project/" + refId;
        } else if (refType == REF_TYPE_SHARE) {
            return "/share/" + refId;
        } else if (refType == REF_TYPE_FEEDBACK) {
            return "/feedback/" + refId;
        } else {
            throw new RuntimeException("refType 不正确，无法生成 url，reType = " + refType);
        }
    }
}