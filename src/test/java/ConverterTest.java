import com.davis.data.organizer.processing.ImageConverter;
import com.davis.data.organizer.Main;
import com.davis.data.organizer.utils.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
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
 * * This software was created for me rights to this software belong to me and appropriate licenses
 * and * restrictions apply.
 *
 * @author Samuel Davis created on 7/20/17.
 */
public class ConverterTest {
  private static final String FORMAT = "jpg";

  @Before
  public void createTestFileStructure() {
    String dirs = Main.class.getClassLoader().getResource("converter.list").getPath();
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
  public void testConvert() throws IOException {
    ImageConverter converter = new ImageConverter();
    Path imageToConvert = Paths.get("src/test/resources/dog.png");
    Path result = converter.convertFormat(imageToConvert, "jpg", false);
    Assert.assertNotNull(result);
    Files.deleteIfExists(result);
  }

  @Test
  public void testConverter() {
    String[] args = {"target/testDataSet"};
    ImageConverter converter = new ImageConverter();
    List<Path> imagesToConvert = PathUtils.getFilePaths(args[0]);
    for (Path p : imagesToConvert) {

      String fileExtension = StringUtils.substringAfterLast(p.getFileName().toString(), ".");
      if (fileExtension.equalsIgnoreCase("jpeg")) {
        Path pn = converter.convertFormat(p, FORMAT, true);
        Assert.assertNotNull(pn);
      } else if (fileExtension.equalsIgnoreCase("png")) {
        Path pn = converter.convertFormat(p, FORMAT, true);
        Assert.assertNotNull(pn);
      } else if (fileExtension.equalsIgnoreCase("bmp")) {
        Path pn = converter.convertFormat(p, FORMAT, true);
        Assert.assertNotNull(pn);
      } else if (fileExtension.equalsIgnoreCase("wbmp")) {
        Path pn = converter.convertFormat(p, FORMAT, true);
        Assert.assertNotNull(pn);
      } else if (fileExtension.equalsIgnoreCase("gif")) {
        Path pn = converter.convertFormat(p, FORMAT, true);
        Assert.assertNotNull(pn);
      } else if (fileExtension.equalsIgnoreCase("jpg")) {
        //do nothing
      } else {
        System.out.println(
            "File of "
                + p.toAbsolutePath().toString()
                + " will not be converted because it is not a image. ");
      }
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
