package ghost;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Config {

    private String mapFileName;
    private int lives;
    private int speed;
    private int frightenedLength;
    private List<Integer> modeLengths;
    
    /**
     * @param configFileName - the file name of the json config file
     * Initializes new ArrayList and loads config settings with given file name
     */
    public Config(String configFileName) {
        this.modeLengths = new ArrayList<Integer>();
        this.loadConfigFile(configFileName);
    }

    /**
     * @return mapFileName
     */
    public String getMapFileName() {
        return this.mapFileName;
    }

    /**
     * @return lives
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * @return speed
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * @return frightenedLength
     */
    public int getFrightenedLength() {
        return this.frightenedLength;
    }

    /**
     * @return a List with modeLengths
     */
    public List<Integer> getModeLengths() {
        return this.modeLengths;
    }
    
    /**
     * @param configFileName - the file name of the json config file
     * Reads JSON config file and loads data to relevant variables.
     * Outputs error if config file was invalid.
     */
    public void loadConfigFile(String configFileName) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(configFileName));
            JSONObject jsonObject = (JSONObject) obj;
            this.mapFileName = jsonObject.get("map").toString();
            this.lives = Integer.parseInt(jsonObject.get("lives").toString());
            this.speed = Integer.parseInt(jsonObject.get("speed").toString());
            this.frightenedLength = Integer.parseInt(jsonObject.get("frightenedLength").toString());
            JSONArray jsonArray = (JSONArray) jsonObject.get("modeLengths");
            for (int i = 0; i < jsonArray.size(); i++) {
                this.modeLengths.add(Integer.parseInt(jsonArray.get(i).toString()));
            }
        } catch (Exception e) {
            System.out.println("Error: File was either not found or incorrect parameters were given.");
        }
    }

}