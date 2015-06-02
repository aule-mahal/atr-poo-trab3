package trab3;

import java.util.*;

public class Emprestimo
{	
	private String codigoDoUsuario;
	private String nomeUsuario;
	private String codigoDoLivro;
	private String nomeLivro;
	private GregorianCalendar dataDevolucao;
	private GregorianCalendar dataEmprestimo;
	private boolean atrasado;

	public Emprestimo(Usuario user, Livro book, GregorianCalendar dataAtual)
	{
		this.codigoDoUsuario = user.getCodigo();
		this.codigoDoLivro = book.getCodigo();
		this.nomeUsuario = user.getNome();
		this.nomeLivro = book.getTitulo();
		this.dataEmprestimo = dataAtual;
		this.dataDevolucao = user.getDataDevolucao(dataAtual);
		this.atrasado = false;
	}

	//Sets:
	public void setAtrasado(GregorianCalendar hoje) //verfica se emprestimo esta atrasado, setando como true o atributo
	{
		if(hoje.getTime().after(this.dataDevolucao.getTime())) //se HOJE está depois da data de devolucao, quer dizer que está atrasado
		{
			this.atrasado = true;
		}
	}

	//Gets:
	public String getCodigoDoUsuario()
	{
		return this.codigoDoUsuario;
	}
	public String getCodigoDoLivro()
	{
		return this.codigoDoLivro;
	}
	public String getNomeUsuario()
	{
		return this.nomeUsuario;
	}
	public String getNomeLivro()
	{
		return this.nomeLivro;
	}
	public GregorianCalendar getDataEmprestimo(){
		return this.dataEmprestimo;
	}
	public GregorianCalendar getDataDevolucao()
	{
		return this.dataDevolucao;
	}

	public void setDataDevolucao(GregorianCalendar dataDevolucao){
		this.dataDevolucao = dataDevolucao;
	}

	public boolean getAtrasado()
	{
		return this.atrasado;
	}
}
