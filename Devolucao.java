import java.util.*;
//PARA SALVAR QUAIS LIVROS FORAM DEVOLVIDOS -- USADO PARA VIAGEM NO TEMPO

public class Devolucao{
	private GregorianCalendar dataDevolucao;
	private String codigoDoLivro;
	private String codigoDoUsuario;
	private Boolean devAtrasado; //Se foi devolvido com atraso

	public Devolucao(String codigoDoLivro, String codigoDoUsuario, GregorianCalendar dataDevolucao){
		this.codigoDoLivro = codigoDoLivro;
		this.codigoDoUsuario = codigoDoUsuario;
		this.dataDevolucao = dataDevolucao;
	}

	public GregorianCalendar getDataDevolucao(){
		return this.dataDevolucao;
	}

	public String getCodigoDoLivro(){
		return this.codigoDoLivro;
	}

	public String getCodigoDoUsuario(){
		return this.codigoDoUsuario;
	}

	public Boolean getDevAtrasado(){
		return this.devAtrasado;
	}

	public void setDevAtrasado(Boolean atraso){
		this.devAtrasado = atraso;
	}
}
