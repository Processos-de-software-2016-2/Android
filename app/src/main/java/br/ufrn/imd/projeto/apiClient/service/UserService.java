package br.ufrn.imd.projeto.apiClient.service;

import java.util.List;

import br.ufrn.imd.projeto.dominio.Match;
import br.ufrn.imd.projeto.dominio.Skill;
import br.ufrn.imd.projeto.dominio.Skill_ID;
import br.ufrn.imd.projeto.dominio.StateLogin;
import br.ufrn.imd.projeto.dominio.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ronaldo on 02/12/2016.
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

    @GET("/login/{email}/{password}")
    Call<StateLogin> getInfoLogin(@Path("email") String email, @Path("password") String password);

    @GET("/user/{id}/skills")
    Call<List<Skill>> getSkillsUser(@Path("id") int id);

    @POST("users/skills")
    Call<Void> insert_skill_user(@Body Skill_ID skill_id);

    @DELETE("users/skills/{id_user}/{id_skill}")
    Call<Void> delete_skill_user(@Path("id_user") int id_user, @Path("id_skill") int id_skill);

    @DELETE("users/interests/{id_user}/{id_interest}")
    Call<Void> delete_interest_user(@Path("id_user") int id_user, @Path("id_interest") int id_interest);

    @GET("skill/autocomplete/{name}")
    Call<List<Skill>> skill_by_name(@Path("name") String name);

    @POST("users/interests")
    Call<Void> insert_interest_user(@Body Skill_ID skill_id);

    @GET("/user/{id}/interests")
    Call<List<Skill>> getInterestsUser(@Path("id") int id);

    @GET("skill/{id_skill}/users")
    Call<List<User>> get_users_by_skill(@Path("id_skill") int id_skill);

    @GET("interest/{id_interest}/users")
    Call<List<User>> get_users_by_interest(@Path("id_interest") int id_interest);

    @POST("matches")
    Call<Void> perform_match(@Body Match body);
}
