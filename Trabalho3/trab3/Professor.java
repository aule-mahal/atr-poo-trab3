package trab3;

public class Professor extends Usuario
{
	public Professor(String nome, String endereco, String codigo)
	{
		super(nome, endereco, codigo);
		this.diasDeEmprestimo = 60;
		this.maxLivros = 6;
	}

}
