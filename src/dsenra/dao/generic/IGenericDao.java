package dsenra.dao.generic;

import dsenra.domain.IPersistente;
import dsenra.exception.ObjetoNaoEncontradoException;

import java.util.Collection;
import java.util.Map;

public interface IGenericDao <T extends IPersistente> {
    boolean cadastrar(T objeto) throws ObjetoNaoEncontradoException;
    T buscar(T objeto) throws ObjetoNaoEncontradoException;
    boolean excluir(T objeto) throws ObjetoNaoEncontradoException;
    void atualizar(T objeto1, T objeto2);
    Collection<T> listaElementos();
}
