package colab.vt.whiteboard.component.state;

import java.awt.AlphaComposite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlphaComposites
{
	private HashMap<String,AlphaComposite> namesToAlphaComposites = new HashMap<String, AlphaComposite>();
	private  HashMap<AlphaComposite,String> alphaCompositesToNames = new HashMap<AlphaComposite,String>();
	private  ArrayList<String> names = new ArrayList<String>();
	
	public AlphaComposites()
	{
		addNameAlphaComposite("Clear",AlphaComposite.Clear);
		addNameAlphaComposite("Dst",AlphaComposite.Dst);
		addNameAlphaComposite("DstAtop",AlphaComposite.DstAtop);
		addNameAlphaComposite("DstIn",AlphaComposite.DstIn);
		addNameAlphaComposite("DstOut",AlphaComposite.DstOut);
		addNameAlphaComposite("DstOver",AlphaComposite.DstOver);
		addNameAlphaComposite("Src",AlphaComposite.Src);
		addNameAlphaComposite("SrcAtop",AlphaComposite.SrcAtop);
		addNameAlphaComposite("SrcIn",AlphaComposite.SrcIn);
		addNameAlphaComposite("SrcOut",AlphaComposite.SrcOut);
		addNameAlphaComposite("SrcOver",AlphaComposite.SrcOver);
		addNameAlphaComposite("Xor",AlphaComposite.Xor);
	}

	private  void addNameAlphaComposite(String name, AlphaComposite alphaComposite)
	{
		namesToAlphaComposites.put(name, alphaComposite);
		alphaCompositesToNames.put(alphaComposite, name);
		names.add(name);
	}
	
	public  AlphaComposite fromName(String name)
	{
		return namesToAlphaComposites.get(name);
	}
	
	public  String toName(AlphaComposite alphaComposite)
	{
		return alphaCompositesToNames.get(alphaComposite);
	}
	
	public  List<String> getNames()
	{
		return names;
	}
}
