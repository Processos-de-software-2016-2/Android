package br.ufrn.imd.projeto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ronaldo on 22/11/2016.
 */

public interface UserService {

    public static final String BASE_URL = "http://10.0.2.2:7000/";
    //public static final String BASE_URL = "http://192.168.43.211:7000/";
    @GET("users")
    Call<List<User>> listUsers();

    @POST("user")
    Call<Void> register_user(@Body User body);

    @GET("user/email/{em}")
    Call<List<User>> getUserByEmail(@Path("em") String email);

}
