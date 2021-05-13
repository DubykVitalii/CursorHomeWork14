import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public final static String directory = "src/main/resources";
    public final static int NUMBER_OFTEN_USED_WORDS = 4;

    public static void main(String[] args) throws IOException {
        var deleteSwearWord = new StringBuilder();
        var resultWord = new StringBuilder();
        var counterTotalWords = 0;
        var counterDeleteWords = 0;
        var pathFileData = Path.of(directory, "data.txt");
        var pathResultWords = Path.of(directory, "dataresult.txt");
        var pathDeletedWords = Path.of(directory, "deletedwords.txt");
        /**
         * Counting the total number of words in the text
         * Exclude obscene words or words less than 3 characters long
         * Counting the number of words that need to be excluded
         * */
        try (var scanner = new Scanner(pathFileData)) {
            while (scanner.hasNext()) {
                String word = scanner.next().replace(",", "").replace(".", "");
                counterTotalWords++;
                if (word.length() >= 3 && !filterSwearWords(word)) {
                    resultWord.append(word).append("\n");
                } else {
                    counterDeleteWords++;
                    deleteSwearWord.append(word).append("\n");
                }
            }
        }
        /**
         * Write filtered words to a file
         * */
        Files.writeString(pathResultWords, resultWord);
        /**
         * Creating a hash map, key - word, value - the number of repetitions of the word in the text
         * */
        HashMap<String, Integer> words = new HashMap<>();
        try (var scanner = new Scanner(pathResultWords)) {
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (words.containsKey(word)) {
                    var valueInt = words.get(word) + 1;
                    words.put(word, valueInt);
                } else {
                    words.put(word, 1);
                }
            }
        }
        /**
         * Printing NUMBER_OFTEN_USED_WORDS words that are most common in the text
         * */
        words.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(NUMBER_OFTEN_USED_WORDS).forEach(System.out::println);
        /**
         * Write deleted words to a file
         * */
        Files.writeString(pathDeletedWords, deleteSwearWord);
        System.out.println("Deleted words:" + counterDeleteWords);
        System.out.println("Total words:" + counterTotalWords);
    }
    /**
     * Check for swear words
     */
    private static boolean filterSwearWords(String word) throws IOException {
        var pathFileSwearWords = Path.of(directory, "swearwords.txt");
        var isSwearWord = false;
        try (var scanner = new Scanner(pathFileSwearWords)) {
            while (scanner.hasNext()) {
                String swearword = scanner.next().replace(",", "");
                if (word.equals(swearword)) {
                    isSwearWord = true;
                }
            }
        }
        return isSwearWord;
    }
}
