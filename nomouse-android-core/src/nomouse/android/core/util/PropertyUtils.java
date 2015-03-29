package nomouse.android.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 载入properties文件
 */
public class PropertyUtils {

    private static void loadProperties(Properties properties, String path) {
        if (properties == null) {
            return;
        }
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            properties.load(reader);
        } catch (Exception e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
}