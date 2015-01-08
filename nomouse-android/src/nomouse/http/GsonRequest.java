package nomouse.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import java.util.Map;

/**
 * 整合Volley和Gson，在非UI线程中反序列化json
 *
 * @param <T>
 * @author nomouse
 */
public class GsonRequest<T> extends Request<T> {

    private final Listener<T> listener;

    public GsonRequest(String url, Listener<T> listener,
                       ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return super.getBodyContentType();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return super.getBody();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
