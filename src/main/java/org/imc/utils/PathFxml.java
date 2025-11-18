package org.imc.utils;
import java.nio.file.Paths;

public class PathFxml {
    public static String patBase(){
        return Paths.get("src/main/java/org/imc/view").toAbsolutePath().toString();
    }
}
