package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.util.StringConverter;

public final class FixedPointStringConverter extends StringConverter<Integer>{

	private final int decimals;
	
	public FixedPointStringConverter(int decimals) {
		this.decimals = decimals;
	}
	
	@Override
	public Integer fromString(String arg0) {
		return new BigDecimal(arg0).movePointRight(decimals).setScale(0, RoundingMode.HALF_UP).intValueExact();
	}

	@Override
	public String toString(Integer arg0) {
		return arg0 == null ? "" : new BigDecimal(arg0).movePointLeft(decimals).stripTrailingZeros().toPlainString();
	}

}
