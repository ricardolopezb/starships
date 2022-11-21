package starships.utils;

public class IdGenerator {
    private static Integer id = 0;
    public static String generateId(){
        return String.valueOf(id++);
    }
}
