import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Insets;
import java.awt.Panel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Mega extends JPanel{

	// static String[] jogo1 = {"03","05","07","13","30","33"};
	// static String[] jogo1 = {"17","57","55","24","52","05"}; //6
	// static String[] jogo1 = {"53","10","21","11","54","09"}; //5
	// static String[] jogo1 = {"17","10","12","52","11","03"}; //4

	static List<Resultados> resultados = new ArrayList<>();
	static List<List<String>> lines;
	static List<List<String>> resultMega;
	static List<List<String>> jogosMega;
	static String msg;
	
	static private JTextArea taskOutput;	
	
	private JProgressBar pbar;
	static final int MY_MINIMUM = 0;
	static final int MY_MAXIMUM = 100;

	public Mega() {
		super(new BorderLayout());
		
		taskOutput = new JTextArea(20,60);
		taskOutput.setMargin(new Insets(100,0,100,100));
		taskOutput.setEditable(false);		

		pbar = new JProgressBar();
		pbar.setMinimum(MY_MINIMUM);
		pbar.setMaximum(MY_MAXIMUM);
		pbar.setStringPainted(true);
		pbar.setPreferredSize(new Dimension(300, 30));
		
		//add(pbar);		
		
		JPanel panel = new JPanel();
		panel.add(pbar);
		
		add(panel, BorderLayout.PAGE_START);
		add(new JScrollPane(taskOutput), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
	}	
	
	public void updateBar(int newValue, List<String> jogo, int nrJogo, List<String> result, int nrConcurso) {
		pbar.setValue(newValue);
		taskOutput.append(String.format("Verificando Jogo Nr: " + nrJogo + " (" + jogo.toString() + ") " + 
		                                "no concurso Nr " + nrConcurso + " (" + result.toString()) + ")\n");		
	}	
	
	public static void main(String[] args) throws FileNotFoundException {
		teste1();
	}

	//*************** Esse exemplo funciona para pegar numero a numero ******************
	// public static void teste2() throws FileNotFoundException {
	// Scanner scanner = new Scanner(new File("../mega/D_MEGA.csv"));
	// scanner.useDelimiter(";");
	// while(scanner.hasNext()) {
	// System.out.println(scanner.next());
	// }
	// scanner.close();
	// }

	// Cria array com os 6 numeros por linha
	public static void teste1() throws FileNotFoundException {

		final Mega it = new Mega();
		
		JFrame frame = new JFrame("Verificando numeros...");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(it);
		frame.pack();
		frame.setVisible(true);		
		
		resultMega = new ArrayList<>();
		jogosMega = new ArrayList<>();
		
		resultMega = abreArquivo("Informe o arquivo de Resultados");
		jogosMega = abreArquivo("Informe o arquivo de Jogos");

		int i = 0;
		int h = 0;
		Double zzz = 1.0;
		Double contLoop = (100.0 /(jogosMega.size() * resultMega.size()));		
		
		for (List<String> jogo : jogosMega) {			
			for (List<String> result : resultMega) {
				final int percent = (int) Math.ceil((zzz * contLoop));
				final int ii = i;
				final int hh = h;
				
				try {
					SwingUtilities.invokeLater(new Runnable() {					
						@Override
						public void run() {
							it.updateBar(percent, jogo, (hh+1), result, (ii+1));						
						}
					});
					java.lang.Thread.sleep(1);
				} catch (InterruptedException e) {
					;
				}			
				
				verificaSorteio(result, (i+1), jogo, (h+1));
				i++;				
				zzz++;			
			}
			h++;
			i = 0;
		}
		
		taskOutput.append(String.format("\n\n\n"));
		

		for (Resultados resultado : resultados) {
			String[] numeros1 = new String[6];
			numeros1 = resultado.getNumeros();
			int x = 0;
			msg = "";
			// msg = String.valueOf(resultado.getConcurso());
			for (String qt : numeros1) {
				if (qt != null) {
					msg += qt + ",";
					x++;
				}
			}
			if (x >= 4) {
				
				taskOutput.append(String.format("Voce acertou " + x + " numeros (" + msg + "), do seu jogo Nr " 
			            + String.valueOf(resultado.getJogo()) + " no concurso "
						+ String.valueOf(resultado.getConcurso()) + "\n"));							
				
				/*
				JOptionPane.showMessageDialog(null, "Voce acertou " + x + " numeros (" + msg + "), do seu jogo Nr " 
			            + String.valueOf(resultado.getJogo()) + " no concurso "
						+ String.valueOf(resultado.getConcurso()));
				*/		
			}
		}
		
		taskOutput.setCaretPosition(taskOutput.getDocument().getLength());
		
		//frame.dispose();
		
		
	}
		
	public static List<List<String>> abreArquivo(String mensagem) throws FileNotFoundException {
		
		String texto = mensagem;
		
		FileDialog dialogo = new FileDialog((JDialog)null, texto);
		dialogo.setMode(FileDialog.LOAD);
		dialogo.setVisible(true);

		// String fileName = arquivo;
		String fileName = dialogo.getDirectory() + dialogo.getFile();
		File file = new File(fileName);

		lines = new ArrayList<>();
		Scanner inputStream;

		inputStream = new Scanner(file);

		while (inputStream.hasNext()) {
			String line = inputStream.next();
			String[] values = line.split(";");
			lines.add(Arrays.asList(values));
		}
		inputStream.close();
		return lines;
	}
	
	public static void verificaSorteio(List<String> result, int contador, List<String> jogo, int contador1) throws FileNotFoundException {
		Resultados resultado1 = new Resultados();		

		// String[] jogo1 = new String[lines.size()];

		//for (List<String> line1 : lines) {
			String[] num = new String[6];
			int z = 0;
			for (int b = 0; b < jogo.size(); b++) {
				for (int zz = 0; zz < result.size(); zz++) {
					if (jogo.get(b).equals(result.get(zz))) {
						num[z] = jogo.get(b);
						z++;
					}
				}
			}
			if (z >= 4) {
				resultado1.setNumeros(num);
				resultado1.setConcurso(contador);
				resultado1.setJogo(contador1);
				resultados.add(resultado1);
				/*
				 * for(String nn:num) { System.out.print(nn); } System.out.println("");
				 */

			}
		//}
	}	
	
}
