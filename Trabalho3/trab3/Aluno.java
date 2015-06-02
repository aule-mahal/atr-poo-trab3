package trab3;

public class Aluno extends Usuario
{
	public Aluno(String nome, String endereco, String codigo)
	{
		super(nome, endereco, codigo);
		this.diasDeEmprestimo = 15; 
		this.maxLivros = 4;
	}

}
