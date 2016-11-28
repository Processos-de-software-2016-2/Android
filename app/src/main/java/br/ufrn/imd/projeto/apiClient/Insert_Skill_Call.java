package br.ufrn.imd.projeto.apiClient;

/**
 * Created by ronaldo on 27/11/2016.
 */

public interface Insert_Skill_Call {
    public void Skill_Insert(int id_skill, int id_user);

    /**
     * Created by ronaldo on 28/11/2016.
     */

    interface Skill_Interest_Id_Call {
        public void skill_interest_callback(int id_skill_interest);
    }
}
