package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;
	private Integer anno;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	String forma = this.cmbBoxForma.getValue();
    	this.txtResult.appendText(this.model.creaGrafo(anno, forma));
    	
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {
    	this.txtResult.clear();
    	try {
    		this.anno = Integer.parseInt(this.txtAnno.getText());
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Inserire un anno compreso tra 1910 e 2014!\n");
    		return;
    	}
    	if(anno<1910 || anno > 2014) {
    		this.txtResult.appendText("Inserire un anno compreso tra 1910 e 2014!\n");
    		return;
    	}
    	this.cmbBoxForma.getItems().addAll(this.model.getForme(anno));
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	Integer t1;
    	Integer alfa;
    	try {
    		t1 = Integer.parseInt(this.txtT1.getText());
    		alfa = Integer.parseInt(this.txtAlfa.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Inserire un numero!\n");
    		return;
    	}
    	if(t1<0 || t1>365) {
    		this.txtResult.appendText("Inserire un numero positivo minore o uguale a 365 in T1\n");
    		return;
    	}
    	if(alfa<0 || alfa>100) {
    		this.txtResult.appendText("Inserire un numero positivo minore o uguale a 100 in alfa\n");
    		return;
    	}
    	this.txtResult.appendText(this.model.simula(anno, this.cmbBoxForma.getValue(), t1, alfa));

    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
