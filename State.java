import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// This is an example of how to save and restore
// state to a file via serialization
public class State implements Serializable {
  public String name;
  public int recordDays;
  public int totalDeaths;
  public int succ;
  public int unSucc;

  public String toString () {
    return "\nRecord days travelled: " + recordDays + "\nTotal people killed: " + totalDeaths + "\nTotal successful journeys: " + succ + "\nTotal unsuccessful journeys: " + unSucc;
  }

  // Save non-transient state to a local file
  // If "name" is "marc", then the local file we'll save the
  // state to will be called "marcState.ser"
  public boolean save () {
    if (name == null)
      return false;

    String fileName = name + "State.ser";
    
    try {
      FileOutputStream fos = new FileOutputStream(fileName);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(this);
      oos.close();
      fos.close();
      return true;
    } catch (IOException e) {
      System.err.println(e);
      return false;
    }
  }

  // Returns a State class from serialized state stored in the
  // file name + "State.ser", or null if unable to deserialize 
  public static State restore (String name) {
    String fileName = name + "State.ser";
    
    try {
		  FileInputStream fis = new FileInputStream(fileName);
      ObjectInputStream ois = new ObjectInputStream(fis);
      State s = (State) ois.readObject();
	    ois.close();
	    fis.close();
      return s;
	  } catch(Exception e) { // IOException, ClassNotFoundException
	    return null;
	  }
  }
}