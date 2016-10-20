/*
 * PropertyUtil.java Date created: 22.01.2008
 * Last modified by: $Author: mike $
 * $Revision: 3644 $ $Date: 2008-09-15 22:38:31 +0900 (월, 15 9월 2008) $
 */

package net.sf.infrared2.server.util;

import java.io.*;

/**
 * Class SerializeHelper ...
 *
 * @author gzgonikov
 * Created on 11.04.2008
 */
public class SerializeHelper {

    /** Field FILE_SEPARATOR  */
    public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
    /** Field LIVE_DATA_FILE_NAME  */
    public static final String LIVE_DATA_FILE_NAME = "src"+FILE_SEPARATOR+"test"+FILE_SEPARATOR+"resources"+FILE_SEPARATOR+"livePerfData.ser";

    /** Field LIVE_DATA_FILE_NAME  */
    public static final String LIVE_DATA_FILE_NAME_DIVIDED = "src"+FILE_SEPARATOR+"test"+FILE_SEPARATOR+"resources"+FILE_SEPARATOR+"livePerfDataDivided.ser";

    /** Field ARCHIVE_DATA_FILE_NAME  */
    public static final String ARCHIVE_DATA_FILE_NAME = "src"+FILE_SEPARATOR+"test"+FILE_SEPARATOR+"resources"+FILE_SEPARATOR+"archivePerfData.ser";
    /** Field CONFIG_FILE  */
    public static final String CONFIG_FILE = "src"+FILE_SEPARATOR+"test"+FILE_SEPARATOR+"resources"+FILE_SEPARATOR+"config.ser";
    /** Field REPORT_CONFIG_FILE  */
    public static final String REPORT_CONFIG_FILE = "src"+FILE_SEPARATOR+"test"+FILE_SEPARATOR+"resources"+FILE_SEPARATOR+"reportConfig.ser";

    /**
     * Method serializeToFile make an object to be serialized into file.
     *
     * @param object of type Object
     * @param fileName of type String
     */
    public static void serializeToFile(Object object, String fileName) {
        try {
            // Serialize to a file
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(object);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method deserializeFromFile make a file to be serialized into object.
     *
     * @param fileName of type String
     * @return Object
     */
    public static Object deserializeFromFile(String fileName) {
        Object object = null;
        try {
            // Deserialize from a file
            File file = new File(fileName);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            // Deserialize the object
            object = in.readObject();
            in.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

}
