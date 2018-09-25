package br.com.tastyfast.tastyfastservice.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import br.com.tastyfast.tastyfastservice.dao.ReservaDao;
import br.com.tastyfast.tastyfastservice.firebase.EnviaNotificacaoPush;
import br.com.tastyfast.tastyfastservice.model.Cliente;
import br.com.tastyfast.tastyfastservice.model.Reserva;
import br.com.tastyfast.tastyfastservice.model.Restaurante;

@Path("/reserva")
public class ReservaService {

	// Token note 8
	//private final String userDeviceIdKey = "eVU2UMPfndA:APA91bGwHmi91Xh0M5GmyPinlo-6Ledy4hBtZujLEgHPtg5liItaAvsLoMWxGHJemD5PcxOBWSOUl0JujpstZCx7K24McIR2z-aMJhpPcBxsNaSPLxm_U7nqRbP2eRUVwxV36oWQdoVq";

	//Token Emulador 6.0
	//private final String userDeviceIdKey = "dvvilcvEiDM:APA91bHzgmQAgmvXhEFy_bAIRFVXVJr0hItoCb5gmmBUTHc5xQ_LQL716_MOoh3kV2WmkudUko02Y5pyFBoSHA4Li62tbfTrTXcrYOVRbJGuPFXx6gy1GQZ4Ym3I7Mu8IeE13jey8XJX";
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public String cadastrar(Reserva reserva) throws Exception{
		try{
			new ReservaDao().salvar(reserva);
			return new Gson().toJson(reserva);
		} catch(Exception ex){
			System.out.println("problemas ao gravar...\n" + ex.getMessage());
			return "Problemas ao gravar reserva...\n" + ex.getMessage();
		}
	}
	
	@GET
	@Produces("application/json")
	public String listar() throws Exception{
		try{
			List<Reserva> reservas = new ArrayList<>();
			reservas = new ReservaDao().findAll();
			return new Gson().toJson(reservas);
		} catch(Exception ex){
			return "Problemas ao recuperar via webservice...\n" + ex.getMessage();
		}
	}
	
	@GET
	@Path("/{idRestaurante}")
	@Produces("application/json")
	public String findByCode(@PathParam("idRestaurante") String idRestaurante) throws Exception{
		try{
			Restaurante restaurante = new Restaurante();
			restaurante.setIdRestaurante(new Integer(idRestaurante));
			List<Reserva> reservas = new ArrayList<>();
			reservas = new ReservaDao().findReservaByRestauranteCode(restaurante);
			return new Gson().toJson(reservas);
		} catch(Exception ex){
			return "Problemas ao listar reservas...\n" + ex.getMessage();
		}
	}
	
	@GET
	@Path("/historico/{idCliente}")
	@Produces("application/json")
	public String findreservasCliente(@PathParam("idCliente") String idCliente) throws Exception{
		try{
			Cliente cliente = new Cliente();
			cliente.setIdCliente(new Integer(idCliente));
			List<Reserva> reservas = new ArrayList<>();
			reservas = new ReservaDao().findReservaByClienteCode(cliente);
			return new Gson().toJson(reservas);
		} catch(Exception ex){
			ex.printStackTrace();
			return "Problemas ao gerar histórico...\n" + ex.getMessage();
		}
	}
	
	
	@PUT
	@Consumes("application/json")
	@Produces("text/plain")
	public String alterar(Reserva reserva) throws Exception{
		try{
			new ReservaDao().alterar(reserva);
			EnviaNotificacaoPush.pushFCMNotification("A sua reserva no restaurante " + reserva.getRestaurante().getNome() + " foi confirmada!", reserva.getCliente().getTokenAparelho());
			return "Dados alterados com sucesso!";
		} catch(Exception ex){
			System.out.println("Problemas ao alterar:\n" + ex.getMessage());
			return "Problemas ao alterar dados!\n" + ex.getMessage();
		}
	}
	
}
