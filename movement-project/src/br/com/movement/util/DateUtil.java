package br.com.movement.util;

import java.util.Calendar;

/**
 * Utilit�rio gen�rico para para trabalhar com datas.
 * 
 * @author Welinton Carvalho.
 * @since 15/10/2014.
 * @version 1.0.
 * */
public class DateUtil {

	/**
	 * M�todo que verifica a quantidade de m�ses entre um m�s e outro.
	 */
	public static int verifyDifQuantityBeteweenTwoMonths(String initialMonth, String endMonth) {
		int quantity = (intValueOfMonth(endMonth) + 1) - (intValueOfMonth(initialMonth) + 1);
		return quantity;
	}

	/**
	 * M�todo para pegar o valor inteiro correspondente a cada m�s.
	 * 
	 * @param monthName Nome do m�s.
	 * @return Valor inteiro reference ao m�s.
	 */
	private static int intValueOfMonth(String monthName) {

		switch (monthName) {

		case "Janeiro":
			return Calendar.JANUARY;

		case "Fevereiro":
			return Calendar.FEBRUARY;

		case "Mar�o":
			return Calendar.MARCH;

		case "Abril":
			return Calendar.APRIL;

		case "Maio":
			return Calendar.MAY;

		case "Junho":
			return Calendar.JUNE;

		case "Julho":
			return Calendar.JULY;

		case "Agosto":
			return Calendar.AUGUST;

		case "Setembro":
			return Calendar.SEPTEMBER;

		case "Outubro":
			return Calendar.OCTOBER;

		case "Novembro":
			return Calendar.NOVEMBER;

		case "Dezembro":
			return Calendar.DECEMBER;

		default:
			break;
		}

		return -1;
	}
}
