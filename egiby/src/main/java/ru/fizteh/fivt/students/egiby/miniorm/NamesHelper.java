package ru.fizteh.fivt.students.egiby.miniorm;

import com.google.common.base.CaseFormat;
import com.google.common.primitives.Booleans;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by egiby on 18.12.15.
 */
public class NamesHelper {
    private static final String REGEX = "[A-Za-z0-9_-]*";

    public static Boolean isGood(String name) {
        return name.matches(REGEX);
    }

    public static String convert(String name) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }

    private static Map<Class, String> toSQL = new HashMap<Class, String>();

    private static void createMap() {
        toSQL.put(Integer.class, "INTEGER");
        toSQL.put(Booleans.class, "BOOLEAN");
        toSQL.put(Byte.class, "TINYINT");
        toSQL.put(Short.class, "SMALLINT");
        toSQL.put(Long.class, "BIGINT");
        toSQL.put(BigDecimal.class, "DECIMAL");
        toSQL.put(Double.class, "DOUBLE");
        toSQL.put(Float.class, "REAL");
        toSQL.put(Time.class, "TIME");
        toSQL.put(Date.class, "DATE");
        toSQL.put(Timestamp.class, "DATETIME");
        toSQL.put(Character.class, "CHAR");
        toSQL.put(String.class, "VARCHAR"); // not very good, but what can I do?
        toSQL.put(UUID.class, "UUID");
        toSQL.put(Array.class, "ARRAY");
    }

    public static String getSQLTypeName(Class type) {
        if (toSQL.isEmpty()) {
            createMap();
        }

        if (toSQL.containsKey(type)) {
            return toSQL.get(type);
        } else {
            return "OTHER";
        }
    }
}
