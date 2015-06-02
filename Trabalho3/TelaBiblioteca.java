import trab3.*;
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


public class TelaBiblioteca extends Application
{
	private TabPane painelAbas = new TabPane();
	private HBox box  = new HBox();
	private Tab abaCadastro = new Tab("Cadastrar");
	private Tab abaEmpreDevo = new Tab("Empréstimo/Devolução");
	private Tab abaListar =  new Tab("   Listar   ");
	private Tab abaOpcoes = new Tab("Opções");
	private ChoiceBox escolhaTipoUsuario;
	private Boolean checkHist = false; //Flag para "viagem no tempo", se true, nao cadastra, empresta ou devolve

	public static void main(String[] args)
	{
		launch(args);
	}

	public void start(Stage stage) throws Exception
	{
		GregorianCalendar now = new GregorianCalendar();
		Sistema sist = new Sistema(now.getTime());

		sist.carregaArquivoUsuarios();
		
		sist.carregaArquivoLivros();

		sist.carregaEmprestados();

		sist.carregaEmprestimosInativos();

		new Thread(()->
		{	
			while(true){
				try
				{	
					sist.atualizaSistema();
					GregorianCalendar tmp = sist.getData();
					tmp.add(Calendar.MINUTE, 5);
					sist.setDataDoSistema(tmp.getTime());
					Thread.sleep(1000*60*5); //5 minutos entre uma atualizacao e outra
				}
				catch(Exception ex)
				{
					return;
				}
			}
		}).start();
			

		configuraAbaCadastrar(sist);
		configuraAbaListar(sist);
		configuraAbaEmpreDevo(sist);
		configuraAbaOpcoes(sist);

		painelAbas.getTabs().addAll(abaCadastro, abaEmpreDevo, abaListar, abaOpcoes);
		stage.setScene(new Scene(painelAbas));
		
		final GregorianCalendar cal = sist.getData();
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), 
			(event) ->  
     		{  	
     			
     			cal.add(Calendar.SECOND, 1);
	          	
	          	stage.setTitle("DEDALUS 2.0   [" + cal.getTime().toString()+"]");
	          	stage.setWidth(650);
				stage.setHeight(550);
	          	stage.show();
	        })); 

		timeline.setCycleCount(Animation.INDEFINITE);  
		timeline.play();  

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
			Alert dialogoOk;
			

			//Verifica se todos os campos foram preenchidos
			if(checkHist){
				dialogoOk = new Alert(Alert.AlertType.WARNING);
				dialogoOk.setContentText("Verificação do histórico:\nAlterações não são permitidas.");
			}
			else if(putNome.getText().contentEquals("") || putEnd.getText().contentEquals("") || putCode.getText().contentEquals(""))
			{
				dialogoOk = new Alert(Alert.AlertType.WARNING);
				dialogoOk.setContentText("Há campo(s) vazio(s)!");
				dialogoOk.setTitle("Info");
				dialogoOk.setHeaderText("Atenção!");
			}
			else
			{
				if(rbAluno.isSelected())
				{
					try
					{
						sist.addUsuario(new Aluno(putNome.getText(), putEnd.getText(), putCode.getText()));
						sist.salvaArquivoUsuarios();
						dialogoOk = new Alert(Alert.AlertType.INFORMATION);
						dialogoOk.setContentText("Aluno cadastrado com sucesso!");
					}
					catch(Exception ex)
					{
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText(ex.getMessage());
					}
				}
				else if(rbProfessor.isSelected())
				{
					try
					{
						sist.addUsuario(new Professor(putNome.getText(), putEnd.getText(), putCode.getText()));
						sist.salvaArquivoUsuarios();
						dialogoOk = new Alert(Alert.AlertType.INFORMATION);
						dialogoOk.setContentText("Professor cadastrado com sucesso!");
					}
					catch(Exception ex)
					{
						
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText(ex.getMessage());
					}
				}
				else
				{
					try
					{
						sist.addUsuario(new Comunidade(putNome.getText(), putEnd.getText(), putCode.getText()));
						sist.salvaArquivoUsuarios();
						dialogoOk = new Alert(Alert.AlertType.INFORMATION);
						dialogoOk.setContentText("Pessoa da Comunidade cadastrada com sucesso!");
					}
					catch(Exception ex)
					{
						//TROCAR POR ALERT BOX
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText(ex.getMessage());
					}
				}
				
			}
			dialogoOk.showAndWait();
			putNome.clear();
			putCode.clear();
			putEnd.clear();
		});

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
			Alert dialogoOk;
			if(checkHist){
				dialogoOk = new Alert(Alert.AlertType.WARNING);
				dialogoOk.setContentText("Verificação do histórico:\nAlterações não são permitidas.");
			}
			else if(putTitulo.getText().contentEquals("") || putAutor.getText().contentEquals("") || putAssunto.getText().contentEquals("") ||
				putAno.getText().contentEquals("") || putCodeLivro.getText().contentEquals("") || putEditora.getText().contentEquals(""))
			{
				dialogoOk = new Alert(Alert.AlertType.WARNING);
				dialogoOk.setContentText("Há campo(s) vazio(s)!");
				dialogoOk.setTitle("Info");
				dialogoOk.setHeaderText("Atenção!");
			}
			else
			{
				if(rbTexto.isSelected())
				{
					try
					{
						sist.addLivro(new LivroTexto(putCodeLivro.getText(), putTitulo.getText(), putAutor.getText(),
						putEditora.getText(), Integer.parseInt(putAno.getText()), putAssunto.getText()));
						sist.salvaArquivoLivros();
						dialogoOk = new Alert(Alert.AlertType.INFORMATION);
						dialogoOk.setContentText("Livro texto cadastrado com sucesso!");
					}
					catch(NumberFormatException ex)
					{
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText("Campo com valor invalido\n" + ex.getMessage());
					}
					catch(Exception ex)
					{
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText(ex.getMessage());
					}
				}
				else
				{
					try
					{
						sist.addLivro(new LivroGeral(putCodeLivro.getText(), putTitulo.getText(), putAutor.getText(),
						putEditora.getText(), Integer.parseInt(putAno.getText()), putAssunto.getText()));
						sist.salvaArquivoLivros();
						dialogoOk = new Alert(Alert.AlertType.INFORMATION);
						dialogoOk.setContentText("Livro geral cadastrado com sucesso!");
					}
					catch(NumberFormatException ex)
					{
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText("Campo com valor invalido\n" + ex.getMessage());
					}
					catch(Exception ex)
					{	
						dialogoOk = new Alert(Alert.AlertType.ERROR);
						dialogoOk.setContentText(ex.getMessage());
					}
				}
			}
			dialogoOk.showAndWait();
			putAutor.clear();
			putTitulo.clear();
			putEditora.clear();
			putCodeLivro.clear();
			putAssunto.clear();
			putAno.clear();
		});
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
						texto.appendText(" "  + x);
					});
			}
			else if (rbListarEmprest.isSelected()) //testa se selecionou book pra listar
			{	
				texto.clear();
				List<String> llemp = new ArrayList<String>();
				llemp = sist.listarEmprestimos();
				llemp.stream()
					.forEach(x ->{
						texto.appendText(" " + x );
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
			Alert dialogoOk;
			if(checkHist){
				dialogoOk = new Alert(Alert.AlertType.WARNING);
				dialogoOk.setContentText("Verificação do histórico:\nAlterações não são permitidas.");
			}
			else if(putCodeBook.getText().contentEquals("") || putCodeUser.getText().contentEquals("")){
				dialogoOk = new Alert(Alert.AlertType.WARNING);
				dialogoOk.setContentText("Insira o código do usuário e do livro!");
			}
			else{

				try{
					sist.realizarEmprestimo(putCodeUser.getText(), putCodeBook.getText(), sist.getData());
					sist.salvaArquivoEmprestimos();
					sist.atualizaSistema();
					dialogoOk = new Alert(Alert.AlertType.INFORMATION);
					dialogoOk.setContentText("Empréstimo feito com sucesso!");
										
				}
				catch(BookUnavailableException nbook){
					dialogoOk = new Alert(Alert.AlertType.ERROR);
					dialogoOk.setContentText("ERRO: "+nbook.getMessage());
				}
				catch(Exception ex){

					dialogoOk = new Alert(Alert.AlertType.ERROR);
					dialogoOk.setContentText("ERRO: "+ex.getMessage());					
					
				}

				
			}
			dialogoOk.showAndWait();
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
			Alert dialogoOk;
			
			if(checkHist){
				dialogoOk = new Alert(Alert.AlertType.WARNING);
				dialogoOk.setContentText("Verificação do histórico:\nAlterações não são permitidas.");
			}
			else if(putCodeBookDevolv.getText().contentEquals(""))
			{
				dialogoOk = new Alert(Alert.AlertType.WARNING);
				dialogoOk.setContentText("Insira o código do livro!");
			}
			else{
				
				try{

					sist.registrarDevolucao(putCodeBookDevolv.getText(), sist.getData());
					sist.atualizaSistema();
					sist.salvaArquivoEmprestimos();
					sist.salvaArquivoEmprestimosInativos();
					sist.salvaArquivoUsuarios();
					sist.salvaArquivoLivros();

					dialogoOk = new Alert(Alert.AlertType.INFORMATION);
					dialogoOk.setContentText("Livro devolvido com sucesso!");
					
				}
				catch(Exception ex){
					dialogoOk = new Alert(Alert.AlertType.WARNING);
					dialogoOk.setContentText("Esse livro não existe ou não foi emprestado!");
				}
				
			}
			dialogoOk.showAndWait();
			putCodeBookDevolv.clear();
		});

		VBox vertical = new VBox();
		vertical.setSpacing(20);
		vertical.setAlignment(Pos.TOP_CENTER);
		vertical.getChildren().addAll(linha1, linha2, linha3, linha4, linha5, linha7, linha8);

		abaEmpreDevo.setContent(vertical);
		abaEmpreDevo.setClosable(false);
	}

	private void configuraAbaOpcoes(Sistema sist)
	{
		Label titulo = new Label("\n\t\t====== Alterar data do sistema ======\n\n");
		Label usarDataAtual = new Label("\t\tOu use data e hora atuais:\n");
		Label alterarData = new Label("\t\tInserir data:\n");
		TextField inputData = new TextField("DD/MM/AAAA HH:MM:SS");
		inputData.setMinWidth(180);
		Button btSetadata = new Button("Alterar data");
		Button btSetaDataAtual = new Button("Alterar para data e hora atuais");
		HBox linha0 = new HBox();
		linha0.getChildren().add(titulo);
		
		HBox linha1 = new HBox();
		linha1.getChildren().add(alterarData);

		HBox linha2 = new HBox();
		linha2.setSpacing(20);
		linha2.getChildren().addAll(new Label("\t   "), inputData, btSetadata);
		HBox linha3 = new HBox();
		linha3.setSpacing(20);
		linha3.getChildren().addAll(usarDataAtual);
		HBox linha4 = new HBox();
		linha4.setSpacing(20);
		linha4.getChildren().addAll(new Label("\t   "), btSetaDataAtual);
		VBox principal = new VBox();
		principal.setSpacing(8);
		principal.getChildren().addAll(linha0, linha1, linha2, linha3, linha4);
		abaOpcoes.setClosable(false);
		abaOpcoes.setContent(principal);

		btSetadata.setOnAction((event) -> 
		{
			Alert dialogoSetData;
			if(inputData.getText().contentEquals("DD/MM/AAAA HH:MM:SS")){

				dialogoSetData = new Alert(Alert.AlertType.WARNING);
				dialogoSetData.setContentText("Insira a nova data!");

			}
			else{

				
				//Parse String
				String raw = inputData.getText();
				String[] sep = raw.split(" ");
				String[] newDate = sep[0].split("/");
				String[] newHour = sep[1].split(":");
				
				
				GregorianCalendar newCal = new GregorianCalendar(Integer.parseInt(newDate[2]), Integer.parseInt(newDate[1])-1, Integer.parseInt(newDate[0]), 
												Integer.parseInt(newHour[0]), Integer.parseInt(newHour[1]), Integer.parseInt(newHour[2]));
					
				GregorianCalendar relative = new GregorianCalendar();
				if(newCal.getTime().before(relative.getTime()))
					this.checkHist = true;
				else
					this.checkHist = false;

				sist.setDataDoSistema(newCal.getTime());
				sist.atualizaSistema();

				dialogoSetData = new Alert(Alert.AlertType.INFORMATION);
				dialogoSetData.setContentText("Data do sistema atualizada para: \n"+sist.getData().getTime().toString());
				
			}
			dialogoSetData.showAndWait();
			inputData.setText("DD/MM/AAAA HH:MM:SS");
			
		});

		btSetaDataAtual.setOnAction((event) -> 
		{
			
			GregorianCalendar osTime = new GregorianCalendar();
			sist.setDataDoSistema(osTime.getTime());
			this.checkHist = false;

			Alert dialogoSetData = new Alert(Alert.AlertType.INFORMATION);
			dialogoSetData.setContentText("Data do sistema atualizada.");
			dialogoSetData.showAndWait();
		});


	}


}

