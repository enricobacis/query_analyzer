package model;

import java.util.ArrayList;
import java.util.HashMap;

public class EncSchemes {
	
	private HashMap<String, ArrayList<String>> schemes;
	
	public EncSchemes(HashMap<String, ArrayList<String>> in)
	{
		schemes = in;
	}
	
	public String showEncSchemes()
	{
		return schemes.toString();
	}

}
