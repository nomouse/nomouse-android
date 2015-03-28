package nomouse.core.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * gson工具类，配合gson lib使用
 *
 * @author nomouse
 */
public class GsonUtils {

    private static final Logger logger = LoggerFactory.getLogger("GsonUtils");

    public static String EMPTY_JSON_OBJECT = "{}";

    public static String EMPTY_JSON_ARRAY = "[]";

    private static GsonBuilder builder;

    private static Gson defaultGson;

    static {

        builder = new GsonBuilder();

        defaultGson = builder.serializeNulls().create();
    }

    private GsonUtils() {
    }

    /**
     * json序列化
     *
     * @param target 对象
     * @return 失败会返回{}或者[]
     */
    public static String toJson(Object target) {
        String result;
        try {
            result = defaultGson.toJson(target);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (target instanceof Collection<?>
                    || target instanceof Iterator<?>
                    || target instanceof Enumeration<?>
                    || target.getClass().isArray()) {
                result = EMPTY_JSON_ARRAY;
            } else {
                result = EMPTY_JSON_OBJECT;
            }

        }
        return result;
    }

    /**
     * json序列化,标识了@Expose注解的字段不序列化
     *
     * @param target 对象
     * @return 失败会返回{}或者[]
     */
    public static String toJsonWithoutExpose(Object target) {
        String result;
        try {
            result = builder.excludeFieldsWithoutExposeAnnotation().create()
                    .toJson(target);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (target instanceof Collection<?>
                    || target instanceof Iterator<?>
                    || target instanceof Enumeration<?>
                    || target.getClass().isArray()) {
                result = EMPTY_JSON_ARRAY;
            } else {
                result = EMPTY_JSON_OBJECT;
            }

        }
        return result;
    }

    /**
     * json反序列化
     *
     * @param source 源
     * @param cls    目标类型，不包含带泛型的集合类型
     * @param <T>    目标泛型
     * @return T的实例或者null
     */
    public static <T> T fromJson(String source, Class<T> cls) {
        T result = null;

        try {
            result = defaultGson.fromJson(source, cls);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    /**
     * json反序列化
     *
     * @param source 源
     * @param type   目标类型，不包含带泛型的集合类型
     * @param <T>    目标泛型
     * @return T的实例或者null
     */
    public static <T> T fromJson(String source, TypeToken<T> type) {
        T result = null;

        try {
            result = defaultGson.fromJson(source, type.getType());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    public static GsonBuilder getBuilder() {
        return builder;
    }

}
