package nomouse.core.android.inject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * @author nomouse
 *         <p/>
 *         注入工具，通过注解简化 findViewById的使用
 */
public class InjectUtils {

    /**
     * 检索目标Activity布局文件，将其中中所有含有注解标记的成员对象注入到target相应的成员中
     *
     * @param target
     */
    public static void inject(Activity target) {

        Context context = target;
        Class<?> targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();

        // 遍历目标的所有成员
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectView.class)) {

                InjectView injectView = field
                        .getAnnotation(InjectView.class);
                int resId = injectView.value();

                // 无注解的情况下，直接使用成员名
                if (resId == 0) {
                    String fieldName = field.getName();
                    resId = context.getResources().getIdentifier(fieldName,
                            "id", context.getPackageName());
                }

                try {
                    View view = target.findViewById(resId);
                    field.setAccessible(true);
                    field.set(target, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 检索目标Dialog布局文件，将其中中所有含有注解标记的成员对象注入到target相应的成员中
     *
     * @param target
     */
    public static void inject(Dialog target) {

        Context context = target.getContext();
        Class<?> targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();

        // 遍历目标的所有成员
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                try {
                    InjectView injectView = field
                            .getAnnotation(InjectView.class);
                    int resId = injectView.value();

                    // 无注解的情况下，直接使用成员名
                    if (resId == 0) {
                        String fieldName = field.getName();
                        resId = context.getResources().getIdentifier(fieldName,
                                "id", context.getPackageName());
                    }

                    View view = target.findViewById(resId);
                    field.setAccessible(true);
                    field.set(target, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 检索source布局文件，将其中中所有含有注解标记的成员对象注入到target相应的成员中
     *
     * @param target
     */
    public static void inject(Object target, View source) {

        Context context = source.getContext();
        Class<?> targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();

        // 遍历目标的所有成员
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                try {
                    InjectView injectView = field
                            .getAnnotation(InjectView.class);
                    int resId = injectView.value();

                    // 无注解的情况下，直接使用成员名
                    if (resId == 0) {
                        String fieldName = field.getName();
                        resId = context.getResources().getIdentifier(fieldName,
                                "id", context.getPackageName());
                    }

                    View view = source.findViewById(resId);
                    field.setAccessible(true);
                    field.set(target, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
