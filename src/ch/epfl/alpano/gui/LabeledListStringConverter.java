package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.util.StringConverter;

public final class LabeledListStringConverter extends StringConverter<Integer>{

	private final List<String> list;
	
	public LabeledListStringConverter(String... strings) {
		list = new ArrayList<>();
		for(String s : strings){
			list.add(s);
		}
	}
	
	@Override
	public Integer fromString(String arg0) {
		return list.indexOf(arg0);
	}

	@Override
	public String toString(Integer arg0) {
		return list.get(arg0);
	}

}
