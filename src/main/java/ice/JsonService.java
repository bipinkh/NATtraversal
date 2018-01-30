package ice;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;



/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */

//this is just used to share the sdp information for now
public class JsonService {

    public static void storeJson(String key, String sdp) throws IOException {

        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/json_file")));
        JSONObject root = new JSONObject(contents);
        String val_older = root.getString(key);
        //Compare values
        if(!sdp.equals(val_older))
        {
            //Update value in object
            root.put(key,sdp);

            //Write into the file
            try (FileWriter file = new FileWriter("src/main/resources/json_file"))
            {
                file.write(root.toString());
                System.out.println("Successfully updated json object to file...!!");
                file.close();
            }
        }

    }

    public static String readJson(String key) throws IOException {
        //Read from file
        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/json_file")));
        JSONObject root = new JSONObject(contents);
        return root.getString(key);

    }


}
