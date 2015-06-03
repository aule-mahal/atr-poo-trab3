import java.util.*;

public abstract class Usuario
{
	protected String nome, endereco, codigo; //Código que identifica unicamente cada usuario
	protected int diasDeEmprestimo, totalLivrosEmprestados, maxLivros; //quantos dias cada emprestimo dura, total de emprestimos e maximo de livros por usuario
	protected GregorianCalendar suspensoAteEstaData; //data ate a qual esta suspenso
																						
	public Usuario(String nome, String endereco, String codigo)
	{
		this.nome = nome;
		this.endereco = endereco;
		this.codigo = codigo;
		this.suspensoAteEstaData = null;
		this.totalLivrosEmprestados = 0;
	}

	protected GregorianCalendar getDataDevolucao(GregorianCalendar hoje) //retorna data de devolucao de livro emprestado HOJE
	{
		System.out.println("++HOJE: "+hoje.getTime().toString());
		GregorianCalendar devolucao = new GregorianCalendar();
		devolucao.setTime(hoje.getTime());	
		//System.out.println("DIAS DE EMPRESTIMO "+this.diasDeEmprestimo);
		devolucao.add(Calendar.DAY_OF_MONTH, this.diasDeEmprestimo); //acrescenta o tanto de dias de emprestimos à data atual  
		devolucao.set(Calendar.HOUR_OF_DAY, 23); //seta a hora como 23:59:59 para devolver
		devolucao.set(Calendar.MINUTE, 59);
		devolucao.set(Calendar.SECOND, 59);
		if(devolucao.get(Calendar.DAY_OF_WEEK) == 1) //se a devolucao cair no domingo
			devolucao.add(Calendar.DAY_OF_MONTH, 1); //adiciona mais um dia

		//System.out.println("DEVOLUCAO "+devolucao.getTime().toString());
		return devolucao;
	}

	public String getCodigo()
	{
		return this.codigo;
	}
	public String getNome()
	{
		return this.nome;
	}
	public String getEndereco()
	{
		return this.endereco;
	}
	public int getTotalLivrosEmprestados()
	{
		return this.totalLivrosEmprestados;
	}
	public int getMaxLivros()
	{
		return this.maxLivros;
	}
	public GregorianCalendar getSuspensoAteEstaData()
	{
		return this.suspensoAteEstaData;
	}


	public void setSuspensoAteEstaData(GregorianCalendar suspensao)
	{
		this.suspensoAteEstaData = suspensao;
	}

	protected void incTotalLivrosEmprestados() //incrementa total de livros emprestados
	{
		this.totalLivrosEmprestados++;
	}

	protected void decTotalLivrosEmprestados() //decrementa total de livros emprestados
	{
		this.totalLivrosEmprestados--;
	}

}

class Aluno extends Usuario
{
	public Aluno(String nome, String endereco, String codigo)
	{
		super(nome, endereco, codigo);
		this.diasDeEmprestimo = 15; //<<<<---------------------mudar------------
		this.maxLivros = 4;
	}

}

class Professor extends Usuario
{
	public Professor(String nome, String endereco, String codigo)
	{
		super(nome, endereco, codigo);
		this.diasDeEmprestimo = 60;
		this.maxLivros = 6;
	}

}

class Comunidade extends Usuario
{ 
	public Comunidade(String nome, String endereco, String codigo)
	{
		super(nome, endereco, codigo);
		this.diasDeEmprestimo = 15;
		this.maxLivros = 2;
	}

}
