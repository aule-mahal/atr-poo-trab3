import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.StageStyle;
import javafx.scene.layout.*;
import javafx.scene.effect.*;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.beans.binding.*;
import javafx.scene.Group;
import javafx.geometry.*;
import java.util.*;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;

/*
	TODO:

		##>>>> ALTERAR SEPARADORES DE TODOS OS ARQUIVOS PARA VIRGULA <<<<##

		LISTAR USUARIO, LIVRO, EMPRESTIMOS -- OK
		---->> Arrumar formatação da listagem de livros
	

		CADASTROS
		-->> CADASTRO USUARIO -- OK
		-->> CADASTRO LIVRO   -- OK
		

		EFETUAR EMPRESTIMO -- OK
		EFETUAR DEVOLUCAO  -- OK
		
		BUSCAS ????
		-------->> Busca Usuario
		----->> Busca por codigo
		----->> Busca por nome
		--->> Editar Dados
		--->> Deletar Usuario
		-------->> Busca Livro
		----->> Busca por codigo
		----->> Busca por titulo
		----->> Busca por autor
		--->> Editar Dados
		--->> Deletar Livro

		OPCOES
		-->> Criar Aba
		---->> Alterar Data


		TROCAR PRINTLNs POR DIALOG BOX
*/

public class TelaBiblioteca extends Application
{
	private TabPane painelAbas = new TabPane();
	private HBox box  = new HBox();
	private Tab abaCadastro = new Tab("Cadastrar");
	private Tab abaBuscar =   new Tab("   Buscar  ");
	private Tab abaEmpreDevo =  new Tab("Empréstimo/Devolução");
	private Tab abaListar =   new Tab("   Listar   ");
	private Tab abaOpcoes = new Tab("Opcoes"); //Criar pagian
	private ChoiceBox escolhaTipoUsuario;

	public static void main(String[] args)
	{
		launch(args);
	}

	public void start(Stage stage) throws Exception
	{
		GregorianCalendar now = new GregorianCalendar();
		System.out.println("NOW " + now.getTime());
		Sistema sist = new Sistema(now.getTime());



		sist.carregaArquivoUsuarios();
		sist.carregaArquivoLivros();
		sist.carregaEmprestados();
		sist.atualizaSistema();

		configuraAbaCadastrar(sist);
		configuraAbaBuscar(sist);
		configuraAbaListar(sist);
		configuraAbaEmpreDevo(sist);
		painelAbas.getTabs().addAll(abaCadastro, abaEmpreDevo, abaBuscar, abaListar);
		stage.setScene(new Scene(painelAbas));

		/*final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() 
		{  
     		public void handle(ActionEvent event) 
     		{  
	          	final GregorianCalendar cal = new GregorianCalendar();  
	          	clock.setText(cal.getTime().toString());
	          	System.out.println(cal.getTime().toString());
	          	stage.setTitle("DEDALUS 2.0  -  Hora do Sistema: <" + cal.getTime().toString()+">");
	          	stage.setWidth(650);
				stage.setHeight(550);
	          	stage.show();
    	 	}  
		}));  */

		//GregorianCalendar tempo;
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), 
			(event) ->  
     		{  

	          	final GregorianCalendar cal = new GregorianCalendar();
	          	//sist.setDataDoSistema(cal.getTime());
	          	stage.setTitle("DEDALUS 2.0   [" + cal.getTime().toString()+"]");
	          	stage.setWidth(650);
				stage.setHeight(550);
	          	stage.show();
	        })); 

		timeline.setCycleCount(Animation.INDEFINITE);  
		timeline.play();  
		
		//stage.setWidth(650);
		//stage.setHeight(550);
		//stage.show();
	}


	private void configuraAbaCadastrar(Sistema sist)
	{

		HBox linha1 = new HBox();
		linha1.setSpacing(10);
		VBox v = new VBox();
		v.setSpacing(10);
		VBox v2 = new VBox();
		v2.setSpacing(20);
		linha1.getChildren().add(new Label("   \tTipo de usuário:"));
		
		ToggleGroup rbGroup = new ToggleGroup();
		RadioButton rbAluno = new RadioButton("Aluno");
		rbAluno.setSelected(true);
		rbAluno.setToggleGroup(rbGroup);
		RadioButton rbProfessor = new RadioButton("Professor");
		rbProfessor.setToggleGroup(rbGroup);
		RadioButton rbComunidade = new RadioButton("Comunidade");
		rbComunidade.setToggleGroup(rbGroup);
		linha1.getChildren().addAll(rbAluno, rbProfessor, rbComunidade);		
		
		HBox linha2 = new HBox();
		linha2.setSpacing(10);
		linha2.getChildren().add(new Label("\tNome:     "));
		TextField putNome = new TextField();
		putNome.setMinWidth(300);
		linha2.getChildren().add(putNome);

		HBox linha3 = new HBox();
		linha3.setSpacing(10);
		linha3.getChildren().add(new Label("\tEndereço:"));
		TextField putEnd = new TextField();
		putEnd.setMinWidth(300);
		linha3.getChildren().add(putEnd);

		HBox linha4 = new HBox();
		linha4.setSpacing(25);
		linha4.getChildren().add(new Label("\tCódigo:    "));
		TextField putCode = new TextField();
		putCode.setMinWidth(100);

		Button btCadastrarUsuario = new Button("Cadastrar");
		linha4.getChildren().addAll(putCode, btCadastrarUsuario);

		Button btnErro = new Button("Mostrar diálogo de error");

		btCadastrarUsuario.setOnAction( (event) ->
		{
			/*Escrever aqui codigo correspondente e apagar o print
				Dica: pegar as strings dos text fields (ache na net o metodo, deve ser algum getText)
				e chamar o construtor do usuario, 
				depois inserir no sistema
				nao esqueça de resetar os text fields depois
			*/
			//Verifica se todos os campos foram preenchidos
			if(putNome.getText().contentEquals("") || putEnd.getText().contentEquals("") || putCode.getText().contentEquals("")){
				System.out.println("CAMPO VAZIO");
				//Trocar por alert box
			}
			else{
				Alert dialogoOk;
				if(rbAluno.isSelected()){
					
					try{
						sist.addUsuario(new Aluno(putNome.getText(), putEnd.getText(), putCode.getText()));
						sist.salvaArquivoUsuarios();
						dialogoOk = new Alert(Alert.AlertType.INFORMATION);
						dialogoOk.setContentText("Aluno cadastrado com sucesso!");
										

						//Alert pop-up -> mensagem (Cadastro efetuado com sucesso)
					}
					catch(Exception ex){
						//TROCAR POR ALERT BOX
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText("Aluno já existente!");
						System.out.println("Erro: " + ex.getMessage());
					}
					dialogoOk.showAndWait();
				}
				else if(rbProfessor.isSelected()){
					try{
						sist.addUsuario(new Professor(putNome.getText(), putEnd.getText(), putCode.getText()));
						sist.salvaArquivoUsuarios();
						dialogoOk = new Alert(Alert.AlertType.INFORMATION);
						dialogoOk.setContentText("Professor cadastrado com sucesso!");
					}
					catch(Exception ex){
						
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText("Professor já existente!");
						System.out.println("Erro: " + ex.getMessage());
					}
					dialogoOk.showAndWait();
				}
				else if(rbComunidade.isSelected()){
					try{
						sist.addUsuario(new Comunidade(putNome.getText(), putEnd.getText(), putCode.getText()));
						sist.salvaArquivoUsuarios();
						dialogoOk = new Alert(Alert.AlertType.INFORMATION);
						dialogoOk.setContentText("Pessoa da Comunidade cadastrada com sucesso!");
					}
					catch(Exception ex){
						//TROCAR POR ALERT BOX
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText("Pessoa da Comunidade já existente!");
						System.out.println("Erro: " + ex.getMessage());
					}
					dialogoOk.showAndWait();
				}
			}
			putNome.clear();
			putCode.clear();
			putEnd.clear();

			System.out.println("Cadastrar usuario!!");
		}
		);

		v.getChildren().addAll(linha1, linha2, linha3, linha4);

		v2.getChildren().add(new Label(""));
		v2.getChildren().add(linha1);
		v2.getChildren().add(v);

		Separator sep = new Separator();
		v.getChildren().addAll(new Label("\n"), sep);

		HBox linha5 = new HBox();
		linha5.setSpacing(10);

		VBox vv = new VBox();
		vv.setSpacing(15);
		linha5.getChildren().add(new Label("   \tTipo de livro:"));
		
		ToggleGroup rbGroupLivro = new ToggleGroup();
		RadioButton rbTexto = new RadioButton("Texto");
		rbTexto.setSelected(true);
		rbTexto.setToggleGroup(rbGroupLivro);
		RadioButton rbGeral = new RadioButton("Geral");
		rbGeral.setToggleGroup(rbGroupLivro);
		linha5.getChildren().addAll(rbTexto, rbGeral);		
		
		HBox linha6 = new HBox();
		linha6.setSpacing(10);
		linha6.getChildren().add(new Label("\tTitulo:"));
		TextField putTitulo = new TextField();
		putTitulo.setMinWidth(250);
		linha6.getChildren().add(putTitulo);

		linha6.getChildren().add(new Label("Autor:"));
		TextField putAutor = new TextField();
		putAutor.setMinWidth(200);
		linha6.getChildren().add(putAutor);

		HBox linha7 = new HBox();
		linha7.setSpacing(10);
		linha7.getChildren().add(new Label("\tEditora: "));
		TextField putEditora = new TextField();
		putEditora.setMaxWidth(70);
		linha7.getChildren().add(putEditora);
		linha7.getChildren().add(new Label("Ano: "));
		TextField putAno = new TextField();
		putAno.setMaxWidth(55);
		linha7.getChildren().add(putAno);
		linha7.getChildren().add(new Label("Assunto: "));
		TextField putAssunto = new TextField();
		putAssunto.setMinWidth(235);
		linha7.getChildren().add(putAssunto);

		HBox linha8 = new HBox();
		linha8.setSpacing(20);
		linha8.getChildren().add(new Label("\tCódigo: "));
		TextField putCodeLivro = new TextField();
		putCodeLivro.setMinWidth(200);
		linha8.getChildren().addAll(putCodeLivro);
		Button btCadastrarLivro = new Button("Cadastrar");
		linha8.getChildren().addAll(btCadastrarLivro);

		vv.getChildren().addAll(linha5, linha6, linha7, linha8);

		v2.getChildren().add(vv);

		abaCadastro.setContent(v2);
		abaCadastro.setClosable(false);

		btCadastrarLivro.setOnAction( (event) ->
		{
			/*Escrever aqui codigo correspondente e apagar o print
				Dica: pegar as strings dos text fields (ache na net, deve ser algum getText)
				e chamar o construtor do livro, 
				depois inserir no sistema
			*/
			if(putTitulo.getText().contentEquals("") || putAutor.getText().contentEquals("") || putAssunto.getText().contentEquals("") ||
				putAno.getText().contentEquals("") || putCodeLivro.getText().contentEquals("") || putEditora.getText().contentEquals("")){
				System.out.println("CAMPO VAZIO");
				//Trocar por alert box
			}
			else{
				//public Livro(String codigo, String titulo, String autor, String editora, int ano, String assunto)
				if(rbTexto.isSelected()){
					try{
						sist.addLivro(new LivroTexto(putCodeLivro.getText(), putTitulo.getText(), putAutor.getText(),
									putEditora.getText(), Integer.parseInt(putAno.getText()), putAssunto.getText()));
						sist.salvaArquivoLivros();
						//Alert pop-up -> mensagem (Cadastro efetuado com sucesso)
					}
					catch(Exception ex){
						//TROCAR POR ALERT BOX
						System.out.println("Erro: " + ex.getMessage());
					}
				}
				else if(rbGeral.isSelected()){
					try{
						sist.addLivro(new LivroGeral(putCodeLivro.getText(), putTitulo.getText(), putAutor.getText(),
									putEditora.getText(), Integer.parseInt(putAno.getText()), putAssunto.getText()));
						sist.salvaArquivoLivros();
						//Alert pop-up -> mensagem (Cadastro efetuado com sucesso)
					}
					catch(Exception ex){
						//TROCAR POR ALERT BOX
						System.out.println("Erro: " + ex.getMessage());
					}
				}
			}
			putAutor.clear();
			putTitulo.clear();
			putEditora.clear();
			putCodeLivro.clear();
			putAssunto.clear();
			putAno.clear();
			System.out.println("Cadastrar livro!!");
		}
		);

	}

	private void configuraAbaBuscar(Sistema sist)
	{

		/*
			BUSCA POR CODIGO -- RETORNA DIALOG BOX COM ACESSO COMPLETO (EDICAO E DELECAO) DE USUARIO/LIVRO
			BUSCA POR NOME/AUTOR/TITULO -- RETORNA LISTA DE 
		
		*/


		ToggleGroup groupUser = new ToggleGroup();
		RadioButton rbUserCode = new RadioButton("Buscar por Código");
		rbUserCode.setSelected(true);
		rbUserCode.setToggleGroup(groupUser);

		RadioButton rbUserName = new RadioButton("Buscar por Nome");
		rbUserName.setToggleGroup(groupUser);

		HBox linha1 = new HBox();
		linha1.getChildren().add(new Label("\n\t\t\t==== USUÁRIO ===="));

		HBox linha2 = new HBox();
		linha2.setSpacing(10);
		linha2.getChildren().add(new Label("\t"));
		linha2.getChildren().addAll(rbUserCode, rbUserName);


		HBox linha3 = new HBox();
		linha3.setSpacing(10);
		linha3.getChildren().add(new Label("\tDado do usuário:  "));

		TextField putSearchUser = new TextField();
		linha3.getChildren().add(putSearchUser);

		Button btBuscarUsuario = new Button("Buscar Usuário");
		HBox linha4 = new HBox();
		linha4.setSpacing(175);
		linha4.getChildren().add(new Label(""));
		linha4.getChildren().add(btBuscarUsuario);
		
		btBuscarUsuario.setOnAction((event) -> 
		{
			System.out.println("BUSCANDO USUARIO");
			if(putSearchUser.getText().contentEquals("")){
				System.out.println("CAMPO VAZIO");
			}
			else{
				if(rbUserCode.isSelected()){ //Busca por codigo
								System.out.println("BUSCANDO POR CODIGO");

				}
				else{	//Busca por nome
								System.out.println("BUSCANDO POR NOME");

				}	
			}

			/*
				Abrir Resultado em Dialog Box
			*/

			putSearchUser.clear();
		});

		//BUSCA LIVRO
		/*
		HBox linha5 = new HBox();
		linha5.getChildren().add(new Label("\n\t\t\t==== LIVRO ===="));


		ToggleGroup groupLivro = new ToggleGroup();
		RadioButton rbBookCode = new RadioButton("Buscar por Código");
		rbBookCode.setSelected(true);
		rbBookCode.setToggleGroup(groupLivro);

		RadioButton rbBookTitle = new RadioButton("Buscar por Titulo");
		rbUserName.setToggleGroup(groupUser);

		HBox linha6 = new HBox();
		linha6.setSpacing(10);

		

		HBox linha7 = new HBox();
		linha7.setSpacing(10);
		linha7.getChildren().add(new Label("\tDado de Busca:  "));

		TextField putTitleBook = new TextField();
		linha7.getChildren().add(putTitleBook);


		Button btBuscarLivro = new Button("Buscar Livro");
		HBox linha8 = new HBox();
		linha9.setSpacing(175);
		linha9.getChildren().add(new Label(""));
		linha9.getChildren().add(btBuscarLivro);
		
		btBuscarLivro.setOnAction((event) -> 
		{
			System.out.println("BUSCANDO LIVRO");
			
			/*
				Abrir Resultado em Dialog Box
			*/
				
				/*
			if(putCodeBook.getText().contentEquals("") && putTitleBook.getText().contentEquals("") && putAuthorName.getText().contentEquals("")){
				System.out.println("CAMPOS VAZIOS");
			}
			else{
				if(!putCodeBook.getText().contentEquals("")){ //Busca Por Codigo

				}
				else if(!putTitleBook.getText().contentEquals("")){ //Busca por titulo

				}
				else{	//Busca por autor

				}
			}
		
			*/
			/*
			putCodeBook.clear();
			putTitleBook.clear();
			putAuthorName.clear();
		});
		*/

		VBox v3 = new VBox();
		v3.setSpacing(20);
		v3.setAlignment(Pos.TOP_CENTER);
		//v3.getChildren().addAll(linha1, linha2, linha3, linha4, linha5, linha6, linha7, linha8, linha9);
		v3.getChildren().addAll(linha1, linha2, linha3, linha4);
		abaBuscar.setContent(v3);
		abaBuscar.setClosable(false);
	}

	private synchronized void configuraAbaListar(Sistema sist)
	{
		ToggleGroup groupListar = new ToggleGroup();
		RadioButton rbListarUser = new RadioButton("Listar usuários");
		rbListarUser.setSelected(true);
		rbListarUser.setToggleGroup(groupListar);

		RadioButton rbListarBook = new RadioButton("Listar livros");
		rbListarBook.setToggleGroup(groupListar);

		RadioButton rbListarEmprest = new RadioButton("Listar empréstimos");
		rbListarEmprest.setToggleGroup(groupListar);

		TextArea texto = new TextArea();
		texto.setMaxSize(1000, 1000);
		texto.setMinSize(600, 600);
		texto.setEditable(false);

		Button btListar = new Button("     OK     ");
		HBox linha1 = new HBox();
		linha1.setAlignment(Pos.TOP_CENTER);
		linha1.setSpacing(10);
		linha1.getChildren().addAll(rbListarUser, rbListarBook, rbListarEmprest, btListar);

		btListar.setOnAction((event) -> 
		{

			//System.out.println(sist.getData().getTime().toString());
			
			if(rbListarUser.isSelected()) //testa se selecionou user pra listar
			{	
				texto.clear();
				List<String> lusu = new ArrayList<String>();
				lusu = sist.listarUsuarios();
				lusu.stream()
					.forEach(x ->{
						texto.appendText(" "+x);
					});
			}	
			else if(rbListarBook.isSelected()) //testa se selecionou book pra listar
			{	
				texto.clear();
				List<String> lliv = new ArrayList<String>();
				lliv = sist.listarLivros();
				lliv.stream()
					.forEach(x ->{
						texto.appendText(" "  +x);
					});
			}
			else if (rbListarEmprest.isSelected()) //testa se selecionou book pra listar
			{	
				texto.clear();
				List<String> llemp = new ArrayList<String>();
				llemp = sist.listarEmprestimos();
				llemp.stream()
					.forEach(x ->{
						texto.appendText(" "+x);
					});
			}
			
			
		});

		
		VBox vertical = new VBox();
		vertical.setSpacing(20);
		vertical.setAlignment(Pos.TOP_CENTER);
		vertical.getChildren().add(new Label("\n"));
		vertical.getChildren().addAll(linha1, texto);
		abaListar.setContent(vertical);
		abaListar.setClosable(false);
	

	}

	private void configuraAbaEmpreDevo(Sistema sist)
	{
		HBox linha1 = new HBox();
		linha1.getChildren().add(new Label("\n\t\t\t==== EMPRÉSTIMO ===="));

		HBox linha2 = new HBox();
		linha2.setSpacing(10);
		linha2.getChildren().add(new Label("\tCódigo do usuário:"));

		TextField putCodeUser = new TextField();
		linha2.getChildren().add(putCodeUser);

		HBox linha3 = new HBox();
		linha3.setSpacing(10);
		linha3.getChildren().add(new Label("\tCódigo do livro:     "));

		TextField putCodeBook = new TextField();
		linha3.getChildren().add(putCodeBook);

		Button btEmprestar = new Button("Realizar empréstimo");
		HBox linha4 = new HBox();
		linha4.setSpacing(175);
		linha4.getChildren().add(new Label(""));
		linha4.getChildren().add(btEmprestar);
		
		btEmprestar.setOnAction((event) -> 
		{	
			
			if(putCodeBook.getText().contentEquals("") || putCodeUser.getText().contentEquals("")){
				System.out.println("CAMPO VAZIO");
			}
			else{

				Alert dialogoOk;
				try{
//<<<<<<< HEAD
					System.out.println("DATA SISTEMA EMPRESTA "+sist.getData().getTime().toString());
//=======

//>>>>>>> Janelas
					sist.realizarEmprestimo(putCodeUser.getText(), putCodeBook.getText(), sist.getData());
					sist.salvaArquivoEmprestimos();
					sist.atualizaSistema();
					dialogoOk = new Alert(Alert.AlertType.INFORMATION);
					dialogoOk.setContentText("Cadastro feito com sucesso!");
										
				}
				catch(Exception ex){

					dialogoOk = new Alert(Alert.AlertType.ERROR);
					dialogoOk.setContentText("Livro já emprestado!");					
					System.out.println("ERRO: " + ex.getMessage());
				}

				dialogoOk.showAndWait();
			}

			putCodeBook.clear();
			putCodeUser.clear();
		});

		HBox linha5 = new HBox();
		linha5.getChildren().add(new Label("\n\n\t\t\t==== DEVOLUÇÃO ====\n"));
		HBox linha7 = new HBox();
		linha7.setSpacing(10);
		linha7.getChildren().add(new Label("\tCódigo do livro:     "));
		TextField putCodeBookDevolv = new TextField();
		linha7.getChildren().add(putCodeBookDevolv);

		Button btDevolver = new Button("     Devolver livro     ");
		HBox linha8 = new HBox();
		linha8.setSpacing(175);
		linha8.getChildren().add(new Label(""));
		linha8.getChildren().add(btDevolver);

		btDevolver.setOnAction((event) -> 
		{
			if(putCodeBookDevolv.getText().contentEquals("")){
				System.out.println("CAMPO VAZIO");
			}
			else{

				Alert dialogoOk;
				try{
					sist.registrarDevolucao(putCodeBookDevolv.getText());
					sist.atualizaSistema();
					sist.salvaArquivoEmprestimos();
					sist.salvaArquivoUsuarios();
					sist.salvaArquivoLivros();
					dialogoOk = new Alert(Alert.AlertType.INFORMATION);
					dialogoOk.setContentText("Livro devolvido com sucesso!");
					
				}
				catch(Exception ex){
					
					dialogoOk = new Alert(Alert.AlertType.WARNING);
					dialogoOk.setContentText("Esse livro não existe ou não foi emprestado!");
					dialogoOk.showAndWait();
					System.out.println("Erro: "+ex.getMessage());
				}
				dialogoOk.showAndWait();
			}

			putCodeBookDevolv.clear();
			System.out.println("Devolver livro");
		});

		VBox vertical = new VBox();
		vertical.setSpacing(20);
		vertical.setAlignment(Pos.TOP_CENTER);
		vertical.getChildren().addAll(linha1, linha2, linha3, linha4, linha5, linha7, linha8);

		abaEmpreDevo.setContent(vertical);
		abaEmpreDevo.setClosable(false);
	}

}

/* Coisas uteis:

	box.getChildren().add(new Button("Um botão de conteúdo"));
	Button botao = new Button("outro botão de conteúdo");
	botao.setEffect(new GaussianBlur()); //embaçado
	botao.setDisable(true); //inclicável
	box.getChildren().add(botao);

	painelAbas.setSide(Side.BOTTOM);

*/
