package org.imc.service;

public class ImcService {

    public void validarCampos(String nome, String alturaStr, String pesoStr) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }

        validarAlturaEPeso(alturaStr, pesoStr);
    }

    public void validarAlturaEPeso(String alturaStr, String pesoStr) {
        if (alturaStr == null || alturaStr.isBlank() ||
                pesoStr == null || pesoStr.isBlank()) {
            throw new IllegalArgumentException("Altura e peso são obrigatórios.");
        }

        double altura;
        double peso;

        try {
            altura = Double.parseDouble(alturaStr.replace(",", "."));
            peso = Double.parseDouble(pesoStr.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Altura e peso devem ser números válidos.");
        }

        if (altura <= 0 || peso <= 0) {
            throw new IllegalArgumentException(
                    "Altura e peso devem ser MAIORES que zero (não podem ser 0 nem negativos)."
            );
        }
    }

    public double calcularImc(double peso, double altura) {
        double imc = peso / (altura * altura);
        return Math.round(imc * 100.0) / 100.0;
    }
}

