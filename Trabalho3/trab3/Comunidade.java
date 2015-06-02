package trab3;

public class Comunidade extends Usuario
{ 
	public Comunidade(String nome, String endereco, String codigo)
	{
		super(nome, endereco, codigo);
		this.diasDeEmprestimo = 15;
		this.maxLivros = 2;
	}

}
