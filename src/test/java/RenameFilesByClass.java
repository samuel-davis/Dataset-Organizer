import com.davis.data.organizer.utils.PathUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This software was created for rights to this software belong to appropriate licenses and
 * restrictions apply.
 *
 * @author Samuel Davis created on 7/25/17.
 */
public class RenameFilesByClass {
  List<String> classes = new ArrayList<>();
  Map<String, Integer> classesAndNumber = new HashMap<>();
  Map<Integer, Integer> imagePerClassCount = new HashMap<>();

  //@Before
  public void setup() {
    classes.add("Aircraft");
    classes.add("Airfield");
    classes.add("Arrays-Radar-Satellites");
    classes.add("Buildings-Landscapes");
    classes.add("Camouflage-Concealment");
    classes.add("Drawings-Renders");
    classes.add("Land-Vehicles-Launchers-Trailers");
    classes.add("Medals");
    classes.add("Electronics-Computers-Machinery");
    classes.add("Explosions-Fire-Smoke-Dust");
    classes.add("Missile-Rockets");
    classes.add("Flags-Banners");
    classes.add("Maps-Satellite-Views");
    classes.add("Naval");
    classes.add("People-Clothing-Firearms");

    int x = 0;
    for (String s : classes) {
      classesAndNumber.put(s, x);
      x = x + 1;
    }
  }

  //@Test
  public void renameFilesByClass() {
    List<Path> files =
        PathUtils.getFilePaths(
            "/mnt/linux-storage/darknetTrainNasicModel/data/restructured-nasic-data");
    for (Path p : files) {
      String fileExtension =
          StringUtils.substringAfterLast(p.getFileName().toAbsolutePath().toString(), ".");
      String name = PathUtils.getFilenameFromPath(p);
      String darkClass = getDarknetClassFromPath(p, classes);
      String fileName =
          darkClass + "_" + String.valueOf(getNumberForClass(darkClass) + "." + fileExtension);

      File alteredNameFile =
          new File("/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/" + fileName);
      try {
        FileUtils.copyFile(p.toFile(), alteredNameFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  //@Test
  public void testCopyDirAndRename() {
    copyDirAndRenameDarknet(
        "/mnt/linux-storage/nasic-data-DNA/Aircraft",
        "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
        "Aircraft");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Airfield",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Airfield");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Arrays, Radar and Satellites",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Arrays-Radar-Satellites");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Buildings and Landscapes",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Buildings-Landscapes");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Camouflage and Concelment",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Camouflage-Concealment");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Convert Format",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Random");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Drawings and Renders",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Drawings-Renders");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Electronics, Computers, and Machinery",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Electronics-Computers-Machinery");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Explosions - Fire - Smoke - Dust",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Explosions-Fire-Smoke-Dust");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Flags and Banners",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Flags-Banners");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Land Vehicles, Launchers, and Trailers",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Land-Vehicles-Launchers-Trailers");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Maps and Satellite Views",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Maps-Satellite-Views");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Medals",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Medals");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Missile and Rockets",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Missile-Rockets");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Naval",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Naval");
    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/People, Clothing and Firearms",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "People-Clothing-Firearms");

    copyDirAndRenameDarknet(
            "/mnt/linux-storage/nasic-data-DNA/Text and Misc. (Needs sub-organization)",
            "/mnt/linux-storage/darknetTrainNasicModel/data/nasic/train/",
            "Random");
  }

  private void copyDirAndRenameDarknet(
      String pathToGet, String pathToCopyTo, String classToNameTo) {
    List<Path> files = PathUtils.getFilePaths(pathToGet);
    int count = 0;
    for (Path p : files) {
      String fileExtension =
          StringUtils.substringAfterLast(p.getFileName().toAbsolutePath().toString(), ".");

      String fileName = classToNameTo + "_" + String.valueOf(count) + "." + fileExtension;

      File alteredNameFile = new File(pathToCopyTo + fileName);
      try {
        FileUtils.copyFile(p.toFile(), alteredNameFile);
        count = count + 1;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private int getNumberForClass(String darkClass) {
    int responseNumber = -1;
    int classNumber = -1;
    if (classesAndNumber.get(darkClass) != null) {
      classNumber = classesAndNumber.get(darkClass);
    }
    if (imagePerClassCount.get(classNumber) != null) {
      responseNumber = imagePerClassCount.get(classNumber);
      imagePerClassCount.put(classNumber, imagePerClassCount.get(classNumber) + 1);
    } else {
      responseNumber = 0;
      imagePerClassCount.put(classNumber, 0);
    }

    return responseNumber;
  }

  private String getDarknetClassFromPath(Path path, List<String> classes) {
    String result = null;
    String classDir =
        path.getParent()
            .subpath(path.getParent().getNameCount() - 1, path.getParent().getNameCount())
            .toString();
    for (String className : classes) {
      if (StringUtils.containsIgnoreCase(className, classDir)) {
        result = className;
        break;
      }
    }
    if (result == null) {
      result = "random";
    }

    return result;
  }
}
