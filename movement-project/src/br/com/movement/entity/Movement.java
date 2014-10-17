package br.com.movement.entity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Entidade Movimento que difine cada movimenta��o feita por uma filial.
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
	 * Data de refer�ncia da movimenta��o.
	 */
	private String refDate;

	/**
	 * Valor da movimenta��o.
	 */
	private double movementValue;
	
	/**
	 * Valor percentual de crescimento.
	 */
	private double percentOfGrowthRate;

	/**
	 * Construtor padr�o.
	 */
	public Movement() {
		this.subsidiaryName = "";
		this.refDate = "";
		this.movementValue = 0;
	}

	/**
	 * M�todo get para recuperar o nome da filial respons�vel pela movimenta��o.
	 * 
	 * @return o subsidiaryName.
	 */
	public String getSubsidiaryName() {
		return subsidiaryName;
	}

	/**
	 * M�todo set para difinir o nome da filial respons�vel pela movimenta��o.
	 * 
	 * @param subsidiaryName para sertar o subsidiaryName.
	 */
	public void setSubsidiaryName(String subsidiaryName) {
		this.subsidiaryName = subsidiaryName;
	}

	/**
	 * M�todo get para recuperar o m�s de refer�cia para a movimenta��o.
	 * 
	 * @return o refDate.
	 */
	public String getRefDate() {
		return refDate;
	}

	/**
	 * M�todo set para difinir o m�s de refer�cia para a movimenta��o.
	 * 
	 * @param refDate para sertar o refDate.
	 */
	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}

	/**
	 * M�todo get para recuperar o valor da movimenta��o.
	 * 
	 * @return o movementValue.
	 */
	public double getMovementValue() {
		return movementValue;
	}

	/**
	 * M�todo set para difinir o valor da movimenta��o.
	 * 
	 * @param movementValue
	 *            para sertar o movementValue.
	 */
	public void setMovementValue(double movementValue) {
		this.movementValue = movementValue;
	}
	
	/**
	 * M�todo get para recuperar  o valor percentual de acr�cimo/decr�cimo da movimenta��o.
	 * 
	 * @return o percentOfGrowthRate.
	 */
	public double getPercentOfGrowthRate() {
		return percentOfGrowthRate;
	}

	/**
	 * M�todo set para difinir o valor percentual de acr�cimo/decr�cimo da movimenta��o.
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
						.concat(this.refDate.isEmpty() ? "M�s n�o informado" : this.refDate)
						.concat(" - ")
						.concat(String.valueOf(this.movementValue).isEmpty() ? "0" : NumberFormat.getCurrencyInstance().format(this.movementValue));
	}
		
	/**
	 * M�todo de s�ida formatada para mostrar a filial com seu valor percentual de aumento em vendas.
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
						.concat(this.refDate.isEmpty() ? "M�s n�o informado" : this.refDate)
						.concat(" - ")
						.concat(String.valueOf(this.percentOfGrowthRate).isEmpty() ? decimalFormat.format(0) : decimalFormat.format(this.percentOfGrowthRate)).concat("%");
	}

	/**
	 * M�todo sobrescrito para ordena��o das movimenta��es com base no seu valor.
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
