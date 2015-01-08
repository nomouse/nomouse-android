package nomouse.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by nomouse on 2014/11/21.
 */
public class ReflectUtils {
    public static void main(String[] args) {
        try {
            Class cls = Class.forName("nomouse.reflect.ReflectUtils$User");
            Field[] fields = cls.getFields();
            Method[] methods = cls.getMethods();
            Constructor[] constructors = cls.getConstructors();
            Annotation[] annotations = cls.getAnnotations();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class User {
        private int id;
        private String name;
        private boolean sex;
        private Date birth;
    }
}
