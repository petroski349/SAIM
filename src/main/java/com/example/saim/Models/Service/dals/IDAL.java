package com.example.saim.Models.Service.dals;
import com.example.saim.Models.Entitys.Usuario;

import java.util.List;

public interface IDAL <T>{
    public boolean create(T Usuario);
    public boolean updateSenha(T Usuario);
    public boolean deleate(T Usuario);
    public T filterGet(int id);
    public List<T> filterGet(String filtro);
    public Usuario getByLogin(String login);
}
