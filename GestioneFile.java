	/*  
		Classe di utility per caricamento e salvataggio su file dei formati: Json, XML, txt e Binario.
    	Realizzata da Cristian Borelli e Giovany Flores.        
   		Gennaio 2017  4A Info.                                  
	*/


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.google.gson.*;

public abstract class GestioneFile {

	/*Dove ci sono parametri di tipo Class<?> si deve passare nel richiamo della funzione: 
	NomeClasse.class o NomeClass.getClass()  
	
	Tutte le classi che vengono serializzate in XML devono avere le seguenti annotazioni:
	@XmlRootElement(name="Nome classe")
	@XmlAccessorType(XmlAccessType.FIELD)
	
	*/

	public static boolean ObjectToXML(Object obj, String path) {
	
		try {
			JAXBContext context;
			context = JAXBContext.newInstance(obj.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(obj, new File(path));
			return true;
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public static Object XMLToObject(Class<?> classOfElement,String path) 
	{
		 JAXBContext jaxbContext;
		try {
				jaxbContext = JAXBContext.newInstance(classOfElement);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				return jaxbUnmarshaller.unmarshal(new File(path));
			
		} catch (JAXBException e) {
				e.printStackTrace();
				return null;	
			}
	}

	public static boolean ObjectToJson(Object obj, String path) {
		
     Gson gson = new Gson();

     String json = gson.toJson(obj);

     try {
        FileWriter writer = new FileWriter(path);
        writer.write(json);
        writer.close();
        
        return true;
        
        } catch (IOException e) {
           e.printStackTrace();
           return false;
      }
	}

	public static Object JsonToObject(Class<?> classOfObj, String path)
	{
		Gson gson = new Gson();
	
		try {
			return gson.fromJson(new FileReader(path), classOfObj);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	/*Questo metodo serve per deserializzare le collection che lavorano con parametri generici, come ArrayList, TreeMap etc... 
	 *Type è una interfaccia in cui sono definiti tutti i tipi nella programmazione Java.
	 *Link per maggiori informazioni: https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Type.html
	 *
	 *Nel richiamo di tale funzione per ricavare il tipo della collection si deve importare la libreria 
	 *com.google.gson.reflect.TypeToken e scrivere nel richiamo della funzione: 
	 *new TypeToken<Collection<ObjectOfCollection>>(){}.getType()	*/
	
	public static Object JsonToCollection(Type typeOfCollection, String path)
	{
		Gson gson = new Gson();
	
		try {
			return gson.fromJson(new FileReader(path), typeOfCollection);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static boolean ObjectToBin(Object obj, String path) {
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path));
			stream.writeObject(obj);
			stream.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static Object BinToObject(String path) {
		
		ObjectInputStream stream = null;
		try {
			stream = new ObjectInputStream(new FileInputStream(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			Object obj = stream.readObject();
			stream.close();
			return obj;
			
			
		} catch (ClassNotFoundException ex) {
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// La classe dell'oggetto passato come parametro deve implementare l'interfaccia "Serializable"
	public static boolean objectToTxt(Object obj, String path) {

		FileOutputStream f;
	
		try {
			f = new FileOutputStream(new File(path));
			ObjectOutputStream o;
			o = new ObjectOutputStream(f);
			o.writeObject(obj);
			o.close();
			f.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static Object TxtToObject(String path) {
		FileInputStream fi;
		try {
			fi = new FileInputStream(new File(path));
			ObjectInputStream oi = new ObjectInputStream(fi);
			Object obj = oi.readObject();

			oi.close();
			fi.close();
			
			return obj;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	
	}
	

}
