package org.imc.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.imc.model.Pessoa;
import org.imc.service.ImcService;
import org.imc.service.PessoaService;

import java.util.List;

public class ImcController {

    @FXML private TextField txtNome;
    @FXML private TextField txtAltura;
    @FXML private TextField txtPeso;
    @FXML private Label labelResultado;

    @FXML private TableView<Pessoa> tabelaDados;
    @FXML private TableColumn<Pessoa, Number> colId;
    @FXML private TableColumn<Pessoa, String> colNome;
    @FXML private TableColumn<Pessoa, Number> colAltura;
    @FXML private TableColumn<Pessoa, Number> colPeso;
    @FXML private TableColumn<Pessoa, Number> colImc;

    private final PessoaService pessoaService = new PessoaService();
    private final ImcService imcService = new ImcService();

    private Pessoa pessoaSelecionada;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()));

        colNome.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNome()));

        colAltura.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getAltura()));

        colPeso.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPeso()));

        colImc.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getImc()));

        tabelaDados.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                pessoaSelecionada = newV;
                txtNome.setText(newV.getNome());
                txtAltura.setText(String.valueOf(newV.getAltura()));
                txtPeso.setText(String.valueOf(newV.getPeso()));
                labelResultado.setText(String.format("%.2f", newV.getImc()));
            }
        });
    }



    @FXML
    public void onSalvar(ActionEvent event) {
        try {
            imcService.validarCampos(txtNome.getText(), txtAltura.getText(), txtPeso.getText());

            double altura = Double.parseDouble(txtAltura.getText().replace(",", "."));
            double peso = Double.parseDouble(txtPeso.getText().replace(",", "."));

            double imcArredondado = imcService.calcularImc(peso, altura);

            Pessoa p = new Pessoa();
            p.setNome(txtNome.getText());
            p.setAltura(altura);
            p.setPeso(peso);
            p.setImc(imcArredondado);
            p.setClassificacao(pessoaService.classificarIMC(imcArredondado));

            pessoaService.salvar(p);

            limparCampos();

        } catch (IllegalArgumentException e) {
            exibirErro("Dados inválidos", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Erro ao salvar", e.getMessage());
        }
    }

    @FXML
    public void onAlterar(ActionEvent event) {
        try {
            if (pessoaSelecionada == null) {
                exibirErro("Alterar", "Selecione um registro na tabela");
                return;
            }

            imcService.validarCampos(txtNome.getText(), txtAltura.getText(), txtPeso.getText());

            double altura = Double.parseDouble(txtAltura.getText().replace(",", "."));
            double peso = Double.parseDouble(txtPeso.getText().replace(",", "."));

            double imcArredondado = imcService.calcularImc(peso, altura);

            pessoaSelecionada.setNome(txtNome.getText());
            pessoaSelecionada.setAltura(altura);
            pessoaSelecionada.setPeso(peso);
            pessoaSelecionada.setImc(imcArredondado);
            pessoaSelecionada.setClassificacao(pessoaService.classificarIMC(imcArredondado));

            pessoaService.atualizar(pessoaSelecionada);

            limparCampos();

        } catch (IllegalArgumentException e) {
            exibirErro("Dados inválidos", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Erro ao alterar", e.getMessage());
        }
    }

    @FXML
    public void onExcluir(ActionEvent event) {
        try {
            if (pessoaSelecionada == null) {
                exibirErro("Excluir", "Selecione um registro para excluir");
                return;
            }

            pessoaService.excluir(pessoaSelecionada.getId());

            limparCampos();
            pessoaSelecionada = null;

        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Erro ao excluir", e.getMessage());
        }
    }

    @FXML
    public void onCarregar(ActionEvent event) {
        carregarTabela();
    }

    @FXML
    public void onCalcular(ActionEvent event) {
        try {
            imcService.validarAlturaEPeso(txtAltura.getText(), txtPeso.getText());

            double altura = Double.parseDouble(txtAltura.getText().replace(",", "."));
            double peso = Double.parseDouble(txtPeso.getText().replace(",", "."));

            double imcArredondado = imcService.calcularImc(peso, altura);

            labelResultado.setText(String.format("%.2f", imcArredondado));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado de IMC");
            alert.setHeaderText("Classificação");
            alert.setContentText(pessoaService.classificarIMC(imcArredondado));
            alert.show();

        } catch (IllegalArgumentException e) {
            exibirErro("Dados inválidos", e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro de cálculo", "Ocorreu um erro ao calcular o IMC.");
        }
    }


    private void carregarTabela() {
        try {
            List<Pessoa> lista = pessoaService.buscarTodos();
            ObservableList<Pessoa> obs = FXCollections.observableArrayList(lista);
            tabelaDados.setItems(obs);
            System.out.println("Carregando tabela. Registros: " + lista.size());
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Erro ao carregar tabela", e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtAltura.clear();
        txtPeso.clear();
        labelResultado.setText("0.00");
        pessoaSelecionada = null;
        tabelaDados.getSelectionModel().clearSelection();
    }

    private void exibirErro(String titulo, String msg) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro  " + titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}


