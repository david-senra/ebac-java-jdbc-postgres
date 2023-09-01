package dsenra.dao;

import dsenra.dao.generic.IGenericDao;
import dsenra.domain.Produto;


public interface IProdutoDao extends IGenericDao<Produto> {
    void atualizar(Produto produto1, Produto produto2);
}
