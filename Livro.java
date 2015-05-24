import java.util.*;

public abstract class Livro
{
	protected String titulo, autor, assunto, editora, codigo;
	protected int ano;
	protected boolean emprestado;

	public Livro(String codigo, String titulo, String autor, String editora, int ano, String assunto)
	{	
		this.codigo = codigo;
		this.titulo = titulo;
		this.autor = autor;
		this.assunto = assunto;
		this.editora = editora;
		this.ano = ano;
		this.emprestado = false;
	}

	//Sets
	public void setTitulo(String titulo)
	{
		this.titulo = titulo;	
	}
	public void setAutor(String autor)
	{
		this.autor = autor;	
	}
	public void setAssunto(String assunto)
	{
		this.assunto = assunto;	
	}
	public void setEditora(String editora)
	{
		this.editora = editora;	
	}
	public void setAno(int  ano)
	{	
		this.ano = ano;	
	}
	public void setEmprestado(boolean condicao)
	{	
		this.emprestado = condicao;	
	}
	
	//gets
	public String getTitulo()
	{
		return this.titulo;	
	}
	public String getAutor()
	{
		return this.autor;	
	}
	public String getAssunto()
	{
		return this.assunto;	
	}
	public String getEditora()
	{
		return this.editora;	
	}
	public int getAno()
	{	
		return this.ano;	
	}
	public String getCodigo()
	{	
		return this.codigo;
	}
	public boolean getEmprestado()
	{	
		return this.emprestado;
	}

}

class LivroTexto extends Livro  //livro texto
{
	public LivroTexto(String codigo, String titulo, String autor, String editora, int ano,String assunto)
	{
		super(codigo, titulo, autor,editora,ano,assunto);
	}
}

class LivroGeral extends Livro  //livro geral
{
	public LivroGeral(String codigo, String titulo, String autor, String editora, int ano, String assunto)
	{
		super(codigo, titulo, autor, editora, ano, assunto);
	}
}