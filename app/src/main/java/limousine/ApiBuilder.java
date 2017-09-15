package limousine;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by An Van Thinh on 9/1/2017.
 */

public class ApiBuilder {
    private final static String URL = "http://192.168.1.112:2244/";

    private static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static ApiRetrofit getService() {
        return getRetrofit().create(ApiRetrofit.class);
    }
}
