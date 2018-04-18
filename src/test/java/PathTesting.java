import com.davis.data.organizer.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 *  * This software was created for me rights to this software belong to me and appropriate licenses and * restrictions apply.
 *
 * @author Samuel Davis created on 7/21/17.
 */
public class PathTesting {

  private static final Logger log = LoggerFactory.getLogger(PathTesting.class.getName());

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
  public void pathTests() {
    Path p0 = Paths.get("src/test/resources/dog.jpg");
    Path p = Paths.get(p0.toAbsolutePath().toString());
    log.info("FileName  {}", p.getFileName()); // Gives me just the filename
    log.info("File System {}", p.getFileSystem()); // Determines the FileSystem
    log.info("Name Count {}", p.getNameCount()); // Gives me the count of every element within the path
    log.info("Last Name Element {}", p.getName(p.getNameCount() -1)); // Gives me the file portion of the Path
    log.info("Parent {}", p.getParent()); // Gives Parent Directory
    log.info("Root {}", p.getRoot()); // Returns Root of Entire FileSystem
    log.info("Absolute Path {}", p.toAbsolutePath()); //Entire Path
    log.info("URI {}", p.toUri()); //Uri to access file
    log.info("toString {}", p.toString()); //Entire Path
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
