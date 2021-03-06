package ro.dcsi.internship;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvRow;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Catalin on 7/3/2017.
 */
public class CsvReader {
  private String filename;
  private final char separator;

  public CsvReader(String filename) {
    this.filename = filename;
    separator = ';';
  }

  public CsvReader(String filename, char separator) {
    this.filename = filename;
    this.separator = separator;
  }

  public List<ExtendedUser> readUsers() {
    //Opening file
    try (FileReader reader = new FileReader(new File(filename))) {

      //Init reader for FastCSV
      de.siegmar.fastcsv.reader.CsvReader csv = new de.siegmar.fastcsv.reader.CsvReader();

      //Csv Options
      csv.setContainsHeader(true);
      csv.setFieldSeparator(separator);

      //Creating container
      CsvContainer container = csv.read(reader);

      //Reading headers
      List<String> headers = new ArrayList<>(container.getHeader());

      //User list
      List<ExtendedUser> users = new ArrayList<>();
      Map<String, String> mapBuffer;

      //Read Users
      for (CsvRow row : container.getRows()) {
        mapBuffer = new HashMap<>();

        for (int i = 0; i < row.getFieldCount(); i++) {
          mapBuffer.put(headers.get(i), row.getField(i));
        }
        String id = mapBuffer.containsKey("_id") ? mapBuffer.get("_id") : Integer.toString(row.hashCode());
        ExtendedUser user = new ExtendedUser(id, mapBuffer);
        users.add(user);
      }
      reader.close();
      return users;

    } catch (IOException e) {
      System.err.print("Open for read, file error");
      throw new RuntimeException("Error opening file for read", e);
    }
  }
}
