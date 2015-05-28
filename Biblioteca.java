import java.util.*;
import java.util.stream.*;

//import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Dialog;

public class Biblioteca extends Application{

	public void start(Stage s) throws Exception{
		GregorianCalendar now = new GregorianCalendar();

		Sistema sist = new Sistema(now.getTime());

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		//grid.setPadding(new Insets(0, 0, 0, 0));
		//String d = new String(now.get(Calendar.DAY_OF_MONTH));
		Integer y = now.get(Calendar.YEAR);
		Integer m = now.get(Calendar.MONTH);
		Integer d = now.get(Calendar.DAY_OF_MONTH);
		//Label date = new Label(d.toString() + "/" + m.toString() + "/" + y.toString());
		Button date = new Button();
		date.setText(d.toString() + "/" + m.toString() + "/" + y.toString());
		date.setStyle("-fx-font: 14 arial; -fx-font-weight: bold;-fx-border-width: 0px;-fx-border-style: solid;");
		/*date.setOnAction((ActionEvent event) -> {
			TextInputDialog dialog = new TextInputDialog("walter");
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Look, a Text Input Dialog");
			dialog.setContentText("Please enter your name:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
			    System.out.println("Your name: " + result.get());
			}

		});
		*/
		grid.add(date, 68, 2);
		Scene scene = new Scene(grid, 800, 600);
		s.setTitle("Biblioteca");
		s.setScene(scene);
		s.show();  



	}

	public static void main(String[] args) throws Exception
	{
		GregorianCalendar now = new GregorianCalendar();

		Sistema sist = new Sistema(now.getTime());

		Professor prof = new Professor("Smith", "Rua 12", "PHD101");
		Aluno al = new Aluno("John", "Rua 15", "JR15");
		LivroGeral lg = new LivroGeral("DNS22", "Duna", "Frank Herbert", "Aleph", 1965, "Ficcao Cientifica");
		//sist.addLivro(lg);
		//sist.addUsuario(prof);
		//sist.addUsuario(al);

		sist.carregaArquivoUsuarios();
		sist.carregaArquivoLivros(); 
		sist.carregaEmprestados(); 

		//sist.realizarEmprestimo("PHD101", "DNS22", now);
		System.out.println("=====================");
		sist.listarUsuarios();
		System.out.println("=====================");
		sist.listarLivros();
		System.out.println("=====================");
		sist.listarEmprestimos();
		System.out.println("=====================");
		sist.salvaArquivoUsuarios();
		sist.salvaArquivoLivros();
		sist.salvaArquivoEmprestimos();

		//System.out.println(now.get(Calendar.DAY_OF_MONTH)+"/"+Calendar.MONTH+"/"+Calendar.YEAR);
		System.out.println(now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.YEAR));
		//launch(args);
		//System.out.println("ᶲ");
		System.exit(0);
	}
}


