package br.ufrn.imd.projeto.apiClient;

import java.util.List;

import br.ufrn.imd.projeto.dominio.User;

/**
 * Created by ronaldo on 28/11/2016.
 */

public interface Search_callback {
    public void users_search_callback(List<User> users_search, int id);
}
