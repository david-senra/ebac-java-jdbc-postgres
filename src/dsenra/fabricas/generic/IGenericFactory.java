package dsenra.fabricas.generic;

import dsenra.domain.IPersistente;

public interface IGenericFactory<T extends IPersistente> {
    boolean cadastrar(T objeto);
    boolean excluir(T objeto);
}
