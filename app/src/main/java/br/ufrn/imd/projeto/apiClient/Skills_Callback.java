package br.ufrn.imd.projeto.apiClient;

import android.app.Application;

import java.util.List;

import br.ufrn.imd.projeto.dominio.Skill;

/**
 * Created by ronaldo on 02/12/2016.
 */

public interface Skills_Callback {
    void set_skills_callback(List<Skill> list, Application app);
}

