package br.com.movement.entity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Entidade Movimento que difine cada movimentação feita por uma filial.
 * 
 * @author Welinton Carvalho.
 * @since 15/10/2014.
 * @version 1.0.
 */
public class Movement implements Comparable<Movement> {

	/**
	 * Nome da filial.
	 */
	private String subsidiaryName;

	/**
	 * Data de referência da movimentação.
	 */
	private String refDate;

	/**
	 * Valor da movimentação.
	 */
	private double movementValue;
	
	/**
	 * Valor percentual de crescimento.
	 */
	private double percentOfGrowthRate;

	/**
	 * Construtor padrão.
	 */
	public Movement() {
		this.subsidiaryName = "";
		this.refDate = "";
		this.movementValue = 0;
	}

	/**
	 * Método get para recuperar o nome da filial responsável pela movimentação.
	 * 
	 * @return o subsidiaryName.
	 */
	public String getSubsidiaryName() {
		return subsidiaryName;
	}

	/**
	 * Método set para difinir o nome da filial responsável pela movimentação.
	 * 
	 * @param subsidiaryName para sertar o subsidiaryName.
	 */
	public void setSubsidiaryName(String subsidiaryName) {
		this.subsidiaryName = subsidiaryName;
	}

	/**
	 * Método get para recuperar o mês de referêcia para a movimentação.
	 * 
	 * @return o refDate.
	 */
	public String getRefDate() {
		return refDate;
	}

	/**
	 * Método set para difinir o mês de referêcia para a movimentação.
	 * 
	 * @param refDate para sertar o refDate.
	 */
	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}

	/**
	 * Método get para recuperar o valor da movimentação.
	 * 
	 * @return o movementValue.
	 */
	public double getMovementValue() {
		return movementValue;
	}

	/**
	 * Método set para difinir o valor da movimentação.
	 * 
	 * @param movementValue
	 *            para sertar o movementValue.
	 */
	public void setMovementValue(double movementValue) {
		this.movementValue = movementValue;
	}
	
	/**
	 * Método get para recuperar  o valor percentual de acrécimo/decrécimo da movimentação.
	 * 
	 * @return o percentOfGrowthRate.
	 */
	public double getPercentOfGrowthRate() {
		return percentOfGrowthRate;
	}

	/**
	 * Método set para difinir o valor percentual de acrécimo/decrécimo da movimentação.
	 * 
	 * @param percentOfGrowthRate para sertar o percentOfGrowthRate.
	 */
	public void setPercentOfGrowthRate(double percentOfGrowthRate) {
		this.percentOfGrowthRate = percentOfGrowthRate;
	}

	@Override
	public String toString() {
		return this.subsidiaryName.isEmpty() ? "Filial sem nome" : this.subsidiaryName
						.concat(" - ")
						.concat(this.refDate.isEmpty() ? "Mês não informado" : this.refDate)
						.concat(" - ")
						.concat(String.valueOf(this.movementValue).isEmpty() ? "0" : NumberFormat.getCurrencyInstance().format(this.movementValue));
	}
		
	/**
	 * Método de sáida formatada para mostrar a filial com seu valor percentual de aumento em vendas.
	 * 
	 * @return Texto formatado.	 
	 */
	public String toStringPercentualOfGrowthRate() {
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');
		decimalFormatSymbols.setGroupingSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
		
		return this.subsidiaryName.isEmpty() ? "Filial sem nome" : this.subsidiaryName
						.concat(" - ")
						.concat(this.refDate.isEmpty() ? "Mês não informado" : this.refDate)
						.concat(" - ")
						.concat(String.valueOf(this.percentOfGrowthRate).isEmpty() ? decimalFormat.format(0) : decimalFormat.format(this.percentOfGrowthRate)).concat("%");
	}

	/**
	 * Método sobrescrito para ordenação das movimentações com base no seu valor.
	 */
	@Override
	public int compareTo(Movement anotherMovement) {
		if (this.movementValue < anotherMovement.movementValue) {
			return -1;
		}
		if (this.movementValue > anotherMovement.movementValue) {
			return 1;
		}
		return 0;
	}
}
