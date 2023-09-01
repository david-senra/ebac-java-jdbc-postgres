package dsenra.dao.generic;

import dsenra.domain.IPersistente;
import dsenra.exception.ObjetoNaoEncontradoException;

import java.util.Collection;
import java.util.Map;

public abstract class GenericDao<T extends IPersistente> implements IGenericDao<T> {

    private final SingletonMap singletonMap;
    public abstract Class<T> getTipoClasse();

    public GenericDao() {
        this.singletonMap = SingletonMap.getInstance();
    }

    @Override
    public boolean cadastrar(T objeto) throws ObjetoNaoEncontradoException {
        Map<Long, T> mapaInterno = (Map<Long, T>) singletonMap.getMap().get(getTipoClasse());
        T objetoEncontrado = mapaInterno.get(objeto.getId());
        if (objetoEncontrado == null) {
            mapaInterno.put(objeto.getId(), objeto);
            return true;
        }
        else throw new RuntimeException();
    }

    @Override
    public T buscar(T objeto) throws ObjetoNaoEncontradoException {
        Map<Long, T> mapaInterno = (Map<Long, T>) this.singletonMap.getMap().get(getTipoClasse());
        T objetoEncontrado = mapaInterno.get(objeto.getId());
        if (objetoEncontrado == null) {
            throw new RuntimeException();
        }
        return objetoEncontrado;
    }

    @Override
    public boolean excluir(T objeto) throws ObjetoNaoEncontradoException {
        Map<Long, T> mapaInterno = (Map<Long, T>) this.singletonMap.getMap().get(getTipoClasse());
        T objetoEncontrado = mapaInterno.get(objeto.getId());
        if (objetoEncontrado == null) {
            throw new RuntimeException();
        }
        else mapaInterno.remove(objeto.getId());
        return true;
    }

    public abstract void atualizar(T objeto, T objetoCadastrado);

    @Override
    public Collection<T> listaElementos() {
        Map<Long, T> mapaInterno = (Map<Long, T>) singletonMap.getMap().get(getTipoClasse());
        return mapaInterno.values();
    }
}
