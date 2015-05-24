import java.util.*;
import java.util.stream.*;
import java.io.*;

public class Sistema
{
	private GregorianCalendar dataAtual;
	private List<Usuario> todosUsuarios;
	private List<Livro> todosLivros;
	private List<Emprestimo> emprestimosAtivos;
	private int totalEmprestimosAtivos;

	//construtor, recebendo a data a ser usada:
	public Sistema(Date data)
	{
		this.dataAtual = new GregorianCalendar();
		this.dataAtual.setTime(data);
		todosLivros = new ArrayList<Livro>();
		todosUsuarios = new ArrayList<Usuario>();
		emprestimosAtivos = new ArrayList<Emprestimo>();
		this.totalEmprestimosAtivos = 0;
	}

	public void setDataDoSistema(Date data) //seta data no sistema
	{
		this.dataAtual.setTime(data);
	}

	public GregorianCalendar getData() //pega a data so sistema
	{
		return this.dataAtual;
	}

	public void addUsuario(Usuario user) throws UserCodeAlreadyExistsException
	{
		if(this.todosUsuarios
			.stream()
			.anyMatch(x -> x.getCodigo().equals(user.getCodigo()))
			)
		{
			throw new UserCodeAlreadyExistsException();
		}

		this.todosUsuarios.add(user);
	}

	public void removeUsuario(String codigo) throws NoSuchElementException
	{
		Optional<Usuario> optional = this.todosUsuarios
			.stream()
			.filter( x -> x.getCodigo().equals(codigo))
			.findAny();
		Usuario u = optional.get();
		System.out.println("Removido: " + u.getCodigo());
		todosUsuarios.remove(u);
	}

	public void listarUsuarios()
	{
		this.todosUsuarios
			.stream()
			.forEach(x->
			{
				System.out.println("Nome: " + x.getNome());
				System.out.println("Endereco: " + x.getEndereco());
				System.out.println("Codigo: " + x.getCodigo() + "\n");
			});
	}

	public void addLivro(Livro livro) throws BookCodeAlreadyExistsException
	{
		if(this.todosLivros
			.stream()
			.anyMatch(x -> x.getCodigo().equals(livro.getCodigo()))
			)
		{
			throw new BookCodeAlreadyExistsException();
		}
		this.todosLivros.add(livro);
	}

	public void removeLivro(String codigo) throws NoSuchElementException
	{
		Optional<Livro> optional = this.todosLivros
			.stream()
			.filter( x -> x.getCodigo().equals(codigo))
			.findAny();
		Livro livro = optional.get();
		System.out.println("Removido: " + livro.getCodigo());
		todosLivros.remove(livro);
	}

	public void listarLivros()
	{
		this.todosLivros
			.stream()
			.forEach(x->
			{
				System.out.println("Titulo : " + x.getTitulo());
				System.out.println("Autor : " + x.getAutor());
				System.out.println("Editora : " + x.getEditora());
				System.out.println("Ano: " + x.getAno());
				System.out.println("Assunto: " + x.getAssunto());				
				System.out.println("Codigo: " + x.getCodigo()+ "\n");				
			});
	}

	public void realizarEmprestimo(String codigoUsuario, String codigoLivro) throws UserBlockedException, NoSuchElementException, 
																	UserHasMaxNumberOfBookException, BookUnavailableException
	{
		this.verificacao(codigoUsuario, codigoLivro);
	}

	public void verificacao(String codigoUsuario, String codigoLivro) throws UserBlockedException, NoSuchElementException, 
																	UserHasMaxNumberOfBookException, BookUnavailableException
	{
		Optional<Usuario> optional = this.todosUsuarios
			.stream()
			.filter( x -> x.getCodigo().equals(codigoUsuario))
			.findAny();   //na verdade, "find the only one"
		Usuario user = optional.get();

		if(user.getSuspensoAteEstaData() != null)
		{
			throw new UserBlockedException();
		}
		if(user.getTotalLivrosEmprestados() == user.maxLivros)
		{
			throw new UserHasMaxNumberOfBookException();
		}

		Optional<Livro> opt = this.todosLivros
			.stream()
			.filter( x -> x.getCodigo().equals(codigoLivro))
			.findAny();   //na verdade, "find the only one"
		
		Livro book = opt.get();

		if(book.getEmprestado())
		{
			throw new BookUnavailableException();
		}
		//depois de verificado, empresta livro em caso de sucesso
		this.emprestarLivro(user, book, this.dataAtual);
	}	

	public void emprestarLivro(Usuario user, Livro book, GregorianCalendar dataAtual) 
	{
		Emprestimo emprest = new Emprestimo(user, book, dataAtual);
		user.incTotalLivrosEmprestados();
		book.setEmprestado(true);
		this.addEmprestimo(emprest);
	}

	public void addEmprestimo(Emprestimo emprest)
	{
		this.emprestimosAtivos.add(emprest);
		this.totalEmprestimosAtivos++;
	}

	public void listarEmprestimos()
	{
		this.emprestimosAtivos
			.stream()
			.forEach(x->
			{
				System.out.print("Usuário: " + x.getNomeUsuario());
				System.out.println(" (" + x.getCodigoDoUsuario() + ")");
				System.out.print("Livro: " + x.getNomeLivro());
				System.out.println(" (" + x.getCodigoDoLivro() + ")");
				System.out.println("Devolver em: " + x.getDataDevolucao().getTime().toString() + "\n");
			});
	}

	public void registrarDevolucao(String codigoLivro) throws NoSuchElementException
	{
		Optional<Emprestimo> opt = this.emprestimosAtivos
			.stream()
			.filter(x -> x.getCodigoDoLivro().equals(codigoLivro))
			.findAny();

		Emprestimo emprest = opt.get(); //pode lancar excecao
		this.removeEmprestimo(emprest);
	}

	public void removeEmprestimo(Emprestimo emprest)
	{
		Optional<Usuario> opt = this.todosUsuarios
			.stream()
			.filter( x -> x.getCodigo().equals(emprest.getCodigoDoUsuario()))
			.findAny();
		Usuario user = opt.get();
		user.decTotalLivrosEmprestados();

		Optional<Livro> optional = this.todosLivros
			.stream()
			.filter( x -> x.getCodigo().equals(emprest.getCodigoDoLivro()))
			.findAny();

		Livro book = optional.get();
		book.setEmprestado(false);

		this.totalEmprestimosAtivos--;
		emprestimosAtivos.remove(emprest);
	}

	public void suspenderUsuario(Usuario user, Emprestimo emprest)//calcula quantos dias de suspensao pro usuário
	{
		int diasDeSuspensao = this.dataAtual.get(Calendar.DAY_OF_YEAR)
		+ 365*(this.dataAtual.get(Calendar.YEAR) - emprest.getDataDevolucao().get(Calendar.YEAR)) 
		- emprest.getDataDevolucao().get(Calendar.DAY_OF_YEAR);

		if(user.getSuspensoAteEstaData() == null) //usuario nao possuia atraso antes
		{
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(this.dataAtual.getTime());
			cal.add(Calendar.DAY_OF_MONTH, diasDeSuspensao);
			user.setSuspensoAteEstaData(cal);
		}
		else
		{
			GregorianCalendar cal = user.getSuspensoAteEstaData();
			cal.add(Calendar.DAY_OF_MONTH, diasDeSuspensao);
			user.setSuspensoAteEstaData(cal);
		}
		
	}

	public void atualizaSistema() //Atuliza sistema, verificando se ha emprestimos vencidos
	{
		this.emprestimosAtivos
			.stream()
			.forEach( x -> x.setAtrasado(this.dataAtual));

		this.emprestimosAtivos
			.stream()
			.filter( x -> x.getAtrasado() == true) //acha emprestimo atrasado
			.forEach( x -> 
			{	
				Optional<Usuario> op =  this.todosUsuarios //procura pelo usuario correspondene ao atraso
					.stream()
					.filter( y -> y.getCodigo().equals(x.getCodigoDoUsuario()))
					.findAny();

				Usuario user = op.get();
				this.suspenderUsuario(user, x);
			}
			);

		this.todosUsuarios
			.stream()
			.filter( x -> x.getSuspensoAteEstaData() != null)
			.filter( x -> x.getSuspensoAteEstaData().before(this.dataAtual))//se a data de suspensao está antes da data atual, nao esta mais suspenso
			.forEach( x -> x.setSuspensoAteEstaData(null));
	}
}
