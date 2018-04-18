package com.davis.data.organizer.processing;

import com.davis.data.organizer.api.FileMoveInfo;
import com.davis.data.organizer.utils.NameUtils;
import com.davis.data.organizer.utils.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * * This software was created for me rights to this software belong to me and appropriate licenses
 * and * restrictions apply.
 *
 * @author Samuel Davis created on 7/18/17.
 */
public class Restructurer {
  private static final Logger log = LoggerFactory.getLogger(Restructurer.class.getName());
  Map<String, Integer> subDirMap = new HashMap<>();
  private String dirToRecurseIn = null;
  private int loopCount = -1;

  /** Instantiates a new Restructurer. */
  public Restructurer() {}

  /**
   * Execute restructure boolean.
   *
   * @param args the args from the main method
   * @return the boolean
   */
  public List<Path> executeRestructure(String[] args) {
    if (args.length < 1) {
      log.error(
          "You must specify a directory to perform operations against as the first argument. ");
      log.error("Example : java -jar Data-Organizer-1.0.jar /mnt/linux-storage/someImages");
      System.exit(1);
    }
    if (args[0].trim().endsWith("/")) {
      dirToRecurseIn = args[0].trim().substring(0, args[0].length() - 1);
      dirToRecurseIn = StringUtils.substringAfterLast(dirToRecurseIn, "/");
    } else {
      dirToRecurseIn = StringUtils.substringAfterLast(args[0], "/");
    }

    List<Path> paths = PathUtils.getFilePaths(args[0]);
    int count = -1;
    for (Path p : paths) {
      String fullPath = p.toAbsolutePath().toString();
      String partialPath = StringUtils.substringAfter(fullPath, dirToRecurseIn);
      if (partialPath.startsWith("/")) {
        partialPath = partialPath.substring(1, partialPath.length());
      }
      String childDir = StringUtils.substringBefore(partialPath, "/");
      if (subDirMap.get(childDir) == null) {
        subDirMap.put(childDir, count + 1);
        log.info("Adding directory of {} to subDirectoryMap ", childDir);
      }
    }
    log.info("Path specified was {} it contains {} number of files. ", args[0], paths.size());
    if (paths.isEmpty()) {
      throw new IllegalArgumentException("The path specified contained no files, exiting ...");
    }
    List<Path> createdPaths = null;

    try (BufferedWriter bw = new BufferedWriter(new FileWriter("moveInfo"+new Date().toString()+".csv", true))) {

      for (int x = 0; x < paths.size(); x++) {

        Path filePath = paths.get(x);
        FileMoveInfo fMi = null;
        if (args.length > 1) {
          if (args[1] != null && args[1].equalsIgnoreCase("tensorflow")) {
            fMi = createMoveInfoTensorflow(filePath);
          }
        } else {
          fMi = createMoveInfo(filePath);
        }
        writeMoveInformation(bw, fMi);

        //Move File to its Target Location
        PathUtils.moveFileIntoDir(fMi);
        //Rename the moved file once it is there
        PathUtils.renameFile(fMi);
        int percent = ((x * 100) / paths.size());
        log.info("{} % complete with restructuring operations.", percent);
      }
      bw.close();
      log.info("{} % complete with restructuring operations.", 100);
      String targetBaseDir =
          StringUtils.substringAfterLast(Paths.get(args[0]).toAbsolutePath().toString(), "/");
      if (targetBaseDir.trim().equalsIgnoreCase("")) {
        targetBaseDir =
            StringUtils.substringAfterLast(
                StringUtils.substringBeforeLast(
                    Paths.get(args[0]).toAbsolutePath().toString(), "/"),
                "/");
      }
      String finalPath = Paths.get(args[0]).toAbsolutePath().toString();
      finalPath = finalPath.replace(targetBaseDir, "restructured-" + targetBaseDir);

      createdPaths = PathUtils.getFilePaths(finalPath);
      log.info(
          "Files were organized into  {} it contains {} number of files. ",
          finalPath,
          paths.size());
      /* if (createdPaths.size() == paths.size()) {
        return true;
      } else {
        return false;
      }*/
    } catch (IOException e) {
      log.error("Error creating file appender object");
    }

    return createdPaths;
  }


  private void writeMoveInformation(BufferedWriter bufferedWriter, FileMoveInfo fileMoveInfo) throws IOException {
    bufferedWriter.write(fileMoveInfo.getOriginalPath().toString()+"#!!!#");
    bufferedWriter.write(fileMoveInfo.getTargetPath().toString());
    bufferedWriter.newLine();
    bufferedWriter.flush();
  }

  private FileMoveInfo createMoveInfoTensorflow(Path originalFile) {
    FileMoveInfo.FileMoveInfoBuilder builder = new FileMoveInfo.FileMoveInfoBuilder();
    builder.originalFilename(originalFile.getFileName().toString());
    builder.originalPath(originalFile);
    builder.originalParentDir(originalFile.getParent().toAbsolutePath().toString());

    String[] pathNames = originalFile.toAbsolutePath().toAbsolutePath().toString().split("\\/");
    boolean triggered = false;
    String targetDir = null;
    StringBuilder newPathing = new StringBuilder();
    for (String name : pathNames) {
      if (triggered) {
        //newPathing.append("/").append(name);
        targetDir = name;
        break;
      } else {
        if (name.equals(dirToRecurseIn)) {
          newPathing.append("/").append(name);
          triggered = true;
        } else {
          if (!name.equalsIgnoreCase("/") && !name.trim().equalsIgnoreCase("")) {
            newPathing.append("/").append(name);
          }
        }
      }
    }
    String targetParentDir =
        newPathing.toString().replace(dirToRecurseIn, "restructured-" + dirToRecurseIn + "/")
            + NameUtils.sanitizeDirName(targetDir);

    String targetFileName = NameUtils.sanitizeName(originalFile.getFileName().toString());
    builder.targetParentDir(targetParentDir);
    builder.targetFileName(targetFileName);
    builder.targetPathWithOriginalName(
        Paths.get(targetParentDir + "/" + originalFile.getFileName().toString()));
    builder.targetPath(Paths.get(targetParentDir + "/" + targetFileName));

    return builder.build();
  }

  private FileMoveInfo createMoveInfo(Path originalFile) {
    FileMoveInfo.FileMoveInfoBuilder builder = new FileMoveInfo.FileMoveInfoBuilder();
    builder.originalFilename(originalFile.getFileSystem().toString());
    builder.originalPath(originalFile);
    builder.originalParentDir(originalFile.getParent().toAbsolutePath().toString());
    int numberOfDirsFromRoot = 0;
    //this will tell us how far from root our target directory is.
    for (int x = 0; x < originalFile.getNameCount(); x++) {
      if (originalFile.getName(x).toString().equals(dirToRecurseIn)) {
        //numberOfDirsFromRoot = numberOfDirsFromRoot + 1;
        break;
      } else {
        numberOfDirsFromRoot = numberOfDirsFromRoot + 1;
      }
    }
    String targetDirPath = originalFile.subpath(0, numberOfDirsFromRoot).toString();
    if (!targetDirPath.startsWith("/")) {
      targetDirPath = "/" + targetDirPath;
    }
    //this will rename every directory above the file and below the target directory
    boolean specifiedDir = true;
    for (int x = numberOfDirsFromRoot; x < originalFile.getNameCount() - 1; x++) {
      if (specifiedDir) {
        specifiedDir = false;
        targetDirPath =
            targetDirPath
                + "/"
                + "restructured-"
                + NameUtils.sanitizeDirName(originalFile.getName(x).toString())
                + "/";
      } else {
        targetDirPath =
            targetDirPath + NameUtils.sanitizeDirName(originalFile.getName(x).toString()) + "--";
      }
    }
    if (targetDirPath.endsWith("--")) {
      targetDirPath = targetDirPath.substring(0, targetDirPath.length() - 2);
    }

    String targetFileName = NameUtils.sanitizeName(originalFile.getFileName().toString());
    builder.targetParentDir(targetDirPath);
    builder.targetFileName(targetFileName);
    builder.targetPathWithOriginalName(
        Paths.get(targetDirPath + "/" + originalFile.getFileName().toString()));
    builder.targetPath(Paths.get(targetDirPath + "/" + targetFileName));

    return builder.build();
  }

  private FileMoveInfo getNewTargetPath(Path originalPath) {
    FileMoveInfo.FileMoveInfoBuilder builder = new FileMoveInfo.FileMoveInfoBuilder();
    String originalFileName = originalPath.getFileName().toString();
    //Set current Name
    builder.originalFilename(originalFileName);
    builder.originalParentDir(originalPath.getParent().toString());
    //Remove Stupid Chars and Fix the Name
    builder.targetFileName(NameUtils.sanitizeName(originalFileName));

    String targetParentDir = "";
    String[] originalParentDirs = originalPath.toAbsolutePath().toString().split("\\/");
    int loopCount = 0;
    String containingDirectory = "";
    for (int x = 0; x < originalParentDirs.length - 1; x++) {
      if (originalParentDirs[x].equalsIgnoreCase(dirToRecurseIn)) {
        containingDirectory = containingDirectory + originalParentDirs[x] + "/";
        loopCount = loopCount + 1;
        break;
      } else {
        containingDirectory = containingDirectory + originalParentDirs[x] + "/";
        loopCount = loopCount + 1;
      }
    }
    if (originalParentDirs.length - loopCount > 1) {

      for (int x = loopCount; x < originalParentDirs.length - 1; x++) {
        targetParentDir = targetParentDir + NameUtils.sanitizeDirName(originalParentDirs[x]) + "-";
      }
      if (targetParentDir.lastIndexOf('-') == targetParentDir.length() - 1) {
        targetParentDir = targetParentDir.substring(0, targetParentDir.length() - 1);
      }
      targetParentDir = containingDirectory + targetParentDir;
      targetParentDir = targetParentDir.replace(dirToRecurseIn, "restructured-" + dirToRecurseIn);
      builder.targetParentDir(targetParentDir);
    } else {
      builder.targetParentDir(containingDirectory + "/" + NameUtils.sanitizeName(originalFileName));
    }

    return builder.build();
  }
}
