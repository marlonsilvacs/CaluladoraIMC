package org.imc.service;

import jakarta.persistence.EntityManager;
import org.imc.model.Pessoa;
import org.imc.repository.JPAUtil;

import java.util.List;

public class PessoaService {

    public void salvar(Pessoa pessoa) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(pessoa);
        em.getTransaction().commit();
        em.close();
    }

    public void atualizar(Pessoa pessoa) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(pessoa);
        em.getTransaction().commit();
        em.close();
    }

    public void excluir(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Pessoa pessoa = em.find(Pessoa.class, id);

        if (pessoa != null) {
            em.getTransaction().begin();
            em.remove(pessoa);
            em.getTransaction().commit();
        }
        em.close();
    }

    public List<Pessoa> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pessoa p", Pessoa.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public String classificarIMC(double imc) {
        if (imc < 18.5) return "Abaixo do Peso";
        if (imc < 24.9) return "Peso Normal";
        if (imc < 29.9) return "Sobrepeso";
        if (imc < 34.9) return "Obesidade Grau 1";
        if (imc < 39.9) return "Obesidade Grau 2";
        return "Obesidade Grau 3";
    }
}