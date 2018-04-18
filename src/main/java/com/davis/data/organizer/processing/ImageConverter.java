package com.davis.data.organizer.processing;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** The type Image converter. */
public class ImageConverter {
  private static final Logger log = LoggerFactory.getLogger(ImageConverter.class.getName());
  private static final String FORMAT = "jpg";

  public List<Path> doConversionForList(List<Path> restructuredFiles) {
    List<Path> convertedPaths = new ArrayList<>();
    for (int x = 0; x < restructuredFiles.size(); x++) {
      Path p = restructuredFiles.get(x);
      Path convertedFile = null;
      String fileExtension = StringUtils.substringAfterLast(p.getFileName().toString(), ".");
      if (!fileExtension.equals(FORMAT)) {
        if (fileExtension.equalsIgnoreCase("jpeg")) {
          convertedFile = convertFormat(p, FORMAT, true);
        } else if (fileExtension.equalsIgnoreCase("png")) {
          convertedFile = convertFormat(p, FORMAT, true);
        } else if (fileExtension.equalsIgnoreCase("bmp")) {
          convertedFile = convertFormat(p, FORMAT, true);
        } else if (fileExtension.equalsIgnoreCase("wbmp")) {
          convertedFile = convertFormat(p, FORMAT, true);
        } else if (fileExtension.equalsIgnoreCase("gif")) {
          convertedFile = convertFormat(p, FORMAT, true);
        } else if (fileExtension.equalsIgnoreCase("jpg")) {
          convertedFile = convertFormat(p, FORMAT, true);

        } else {
          log.trace(
              "File of {} will not be converted because it is not a image. ",
              p.toAbsolutePath().toString());
        }
      } else {
        log.trace(
            "File of {} will not be converted because it already has the correct format ",
            p.toAbsolutePath().toString());
      }

      if (convertedFile != null) {
        convertedPaths.add(convertedFile);
      }
      int percent = ((x * 100) / restructuredFiles.size());
      log.info("{} % complete with image conversion operations.", percent);
    }
    log.info("{} % complete with image conversion operations.", 100);
    return convertedPaths;
  }

  /**
   * Converts an image to another format in the same directory
   *
   * @param inputImagePath Path of the source image
   * @param formatName the format to be converted to, one of: jpeg, png, bmp, wbmp, and gif
   * @param deleteOriginal should original be deleted
   * @return Path object for the newly created file.
   * @throws IOException if errors occur during writing
   */
  public Path convertFormat(Path inputImagePath, String formatName, boolean deleteOriginal) {
    FileInputStream inputStream = null;
    FileOutputStream outputStream = null;
    String targetPath = getNewPath(inputImagePath, formatName);
    try {

      inputStream = new FileInputStream(inputImagePath.toFile());
      outputStream = new FileOutputStream(targetPath);

      // reads input image from file
      BufferedImage inputImage = ImageIO.read(inputStream);

      // writes to the output image in specified format
      boolean result = ImageIO.write(inputImage, formatName, outputStream);

      if (result) {
        log.trace(
            "Conversion successful for {} new file name is {}",
            inputImagePath.toAbsolutePath().toString(),
            targetPath);
      } else {
        log.trace("Conversion failed for {} ", inputImagePath.toAbsolutePath().toString());
      }
      // needs to close the streams
      outputStream.close();
      inputStream.close();
    } catch (Exception e) {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e1) {
        log.error("Exception encountered {}", e1);
      }
      try {
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (IOException e1) {
        log.error("Exception encountered {}", e1);
      }
    }

    Path convertLocation = Paths.get(targetPath);
    if (convertLocation.toFile().exists()) {
      if (deleteOriginal) {
        if (inputImagePath.toFile().delete()) {
          log.trace(
              "Deletion of original file of {} was successful",
              inputImagePath.toAbsolutePath().toString());
        } else {
          log.trace(
              "Deletion of original file of {} was unsuccessful",
              inputImagePath.toAbsolutePath().toString());
        }
      }
      return convertLocation;
    } else {
      return null;
    }
  }

  private String getNewPath(Path originalPath, String newFormat) {
    String path = originalPath.toAbsolutePath().toString();
    String fixed = StringUtils.substringBeforeLast(path, ".");
    fixed = fixed + "." + newFormat;
    return fixed;
  }
}
