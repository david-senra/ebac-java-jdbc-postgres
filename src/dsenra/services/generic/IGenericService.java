package dsenra.services.generic;


import dsenra.domain.IPersistente;

public interface IGenericService<T extends IPersistente> {
    T buscar(T objeto);
    boolean atualizar(T objeto1, T objeto2);
}
