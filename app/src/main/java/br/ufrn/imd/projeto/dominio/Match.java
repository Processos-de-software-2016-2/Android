package br.ufrn.imd.projeto.dominio;

/**
 * Created by ronaldo on 02/12/2016.
 */

public class Match {
    public String id_user_not;
    public String id_user_has;
    public String id_skill;

    public Match(String id_user_not, String id_user_has, String id_skill){
        this.id_user_not = id_user_not;
        this.id_user_has = id_user_has;
        this.id_skill = id_skill;
    }
}
