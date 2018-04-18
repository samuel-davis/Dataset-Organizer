package com.davis.data.organizer;

import com.davis.data.organizer.processing.ImageConverter;
import com.davis.data.organizer.processing.Restructurer;
import com.davis.data.organizer.recording.RecordWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** The type Main. */
public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class.getName().toString());

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws IOException the io exception
   */
  public static void main(String[] args) throws IOException {
    List<Path> structuredFiles = new Restructurer().executeRestructure(args);
    log.info("Successfully restructured {} number of files ", structuredFiles.size());
    List<Path> convertedFiles = new ImageConverter().doConversionForList(structuredFiles);
    log.info("Successfully converted {} number of files ", convertedFiles.size());
  }
}
