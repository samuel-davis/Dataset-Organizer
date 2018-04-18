import com.davis.data.organizer.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

/**
 * This software was created for
 * rights to this software belong to
 * appropriate licenses and restrictions apply.
 *
 * @author Samuel Davis created on 7/25/17.
 */
public class FullMainExecution {
    @Before
    public void createTestFileStructure() {
        String dirs = Main.class.getClassLoader().getResource("currentDirs.txt").getPath();
        Path filePath = Paths.get(dirs);

        Charset charset = Charset.forName("ISO-8859-1");
        try {
            List<String> lines = Files.readAllLines(filePath, charset);

            for (String line : lines) {
                File f = new File("target/testDataSet/" + line);
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testMain(){
        String[] args = {"target/testDataSet"};
        try {
            Main.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @After
    public void cleanUp() {
        try {
            Path rootPath = Paths.get("target/testDataSet");
            Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    // .peek(System.out::println)
                    .forEach(File::delete);
            Path rootPath2 = Paths.get("target/restructured-testDataSet");
            Files.walk(rootPath2, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    //.peek(System.out::println)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
