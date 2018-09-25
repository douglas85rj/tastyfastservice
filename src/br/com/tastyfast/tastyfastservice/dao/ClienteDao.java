package br.com.tastyfast.tastyfastservice.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Query;

import br.com.tastyfast.tastyfastservice.config.MD5;
import br.com.tastyfast.tastyfastservice.model.Cliente;

public class ClienteDao {
	
	private final EntityManagerFactory factory;
	private final EntityManager manager;
	private Query query;
	
	public ClienteDao() {
		factory = Persistence.createEntityManagerFactory("tastyfastservice");
		manager = factory.createEntityManager();
	}
	
	public void salvar(Cliente cliente){
		try{
			manager.getTransaction().begin();
			cliente.setSenha(new MD5().criptografa(cliente.getSenha()));
			manager.persist(cliente);
			manager.getTransaction().commit();
			System.out.println("Cliente gravado com sucesso!");
		} catch(Exception ex){
			System.out.println("Problemas ao gravar cliente!\n" + ex.getMessage());
		}
	}
	
	public void alterar(Cliente cliente){
		try{
			manager.getTransaction().begin();
			manager.merge(cliente);
			manager.getTransaction().commit();
			System.out.println("Alterou o cliente...");
		} catch(Exception ex){
			System.out.println("Problemas ao alterar dados...\n" + ex.getMessage());
		}
	}
	
	public List<Cliente> findAll(){
		try{
			return manager.createQuery("from Cliente").getResultList();
		} catch(Exception ex){
			System.out.println("Problemas ao listar clientes...\n" + ex.getMessage());
			return null;
		}
	}
	
	public Cliente login(Cliente usuarioApp){
		try{
			query = (Query) manager.createQuery("from Cliente where email = :param1 and senha = :param2");
			query.setString("param1", usuarioApp.getEmail());
			query.setString("param2", usuarioApp.getSenha());
			Cliente clienteLogado = (Cliente) query.uniqueResult();
			return clienteLogado;
		} catch(Exception ex){
			System.out.println("Problemas ao localizar usuários(Clientes)!");
			return null;
		}
	}

}
