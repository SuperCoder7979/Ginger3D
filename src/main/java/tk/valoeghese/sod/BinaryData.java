package tk.valoeghese.sod;

import java.io.*;
import java.util.*;

import tk.valoeghese.sod.exception.SODParseException;

public class BinaryData implements Iterable<Map.Entry<String, DataSection>>
{
	public static BinaryData read(File file) throws SODParseException
	{
		try (DataInputStream dis = new DataInputStream(new FileInputStream(file)))
		{
			long magic = dis.readLong();
			if (magic != 0xA77D1E)
			{ throw new SODParseException("Not a valid SOD file!"); }
			return Parser.parse(dis);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			//throw new SODParseException("Error in parsing file " + file.toString());
			return new BinaryData();
		}
	}

	private final Map<String, DataSection> sections;

	public BinaryData()
	{ this.sections = new HashMap<>(); }

	public DataSection get(String name)
	{ return this.sections.get(name); }

	public DataSection getOrCreate(String name)
	{ return this.sections.computeIfAbsent(name, k -> new DataSection()); }

	@Override
	public Iterator<Map.Entry<String, DataSection>> iterator()
	{ return this.sections.entrySet().iterator(); }

	public void put(String name, DataSection section)
	{ this.sections.put(name, section); }

	public boolean write(File file)
	{
		try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file)))
		{
			Parser.write(this, dos);
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
