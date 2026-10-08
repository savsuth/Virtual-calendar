package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for CSVImporter. These tests verify the behavior of the importer under many scenarios
 * including valid input, empty files, malformed rows, and mixed valid and invalid input.
 */
public class CSVImporterTest {

  ICalendarManager manager = new CalendarManager();
  IMultiCalendarService service = new MultiCalendarService(manager);

  @Before
  public void setUp() throws Exception {

    service.createCalendar("DUMMY", "UTC");
    service.useCalendar("DUMMY");
  }

  @Test
  public void importData() throws Exception {
    CSVImporter importer = new CSVImporter();
    File tempFile = File.createTempFile("validData", ".csv");
    FileWriter writer = new FileWriter(tempFile);
    writer.write(
        "Subject,Start Date,Start Time,End Date,End Time,AllDay,Description,Location,Private\n");
    writer.write("Meeting,2025-04-10,10:00,2025-04-10,11:00,false,Description,Office,false\n");
    writer.close();
    ICalendarModel model = manager.getCalendar("DUMMY").getCalendarModel();
    String result = importer.importData(model, tempFile.getAbsolutePath());
    assertEquals("Imported 1 events.", result);
  }

  @Test
  public void importEmptyFile() throws Exception {
    CSVImporter importer = new CSVImporter();
    File tempFile = File.createTempFile("emptyData", ".csv");
    FileWriter writer = new FileWriter(tempFile);
    writer.write("");
    writer.close();
    ICalendarModel model = manager.getCalendar("DUMMY").getCalendarModel();
    String result = importer.importData(model, tempFile.getAbsolutePath());
    assertEquals("Error: CSV file is empty.", result);
  }

  @Test
  public void importInvalidFieldCount() throws Exception {
    CSVImporter importer = new CSVImporter();
    File tempFile = File.createTempFile("invalidField", ".csv");
    FileWriter writer = new FileWriter(tempFile);
    writer.write(
        "Subject,Start Date,Start Time,End Date,End Time,AllDay,Description,Location,Private\n");
    writer.write("Meeting,2025-04-10,10:00\n");
    writer.close();
    ICalendarModel model = manager.getCalendar("DUMMY").getCalendarModel();
    String result = importer.importData(model, tempFile.getAbsolutePath());
    assertEquals("Imported 0 events.\n1 errors:\nLine 2: Invalid number of fields.\n", result);
  }

  @Test
  public void importInvalidDateFormat() throws Exception {
    CSVImporter importer = new CSVImporter();
    File tempFile = File.createTempFile("invalidDate", ".csv");
    FileWriter writer = new FileWriter(tempFile);
    writer.write(
        "Subject,Start Date,Start Time,End Date,End Time,AllDay,Description,Location,Private\n");
    writer.write("Meeting,invalid-date,10:00,2025-04-10,11:00,false,Description,Office,false\n");
    writer.close();
    ICalendarModel model = manager.getCalendar("DUMMY").getCalendarModel();
    String result = importer.importData(model, tempFile.getAbsolutePath());
    assertTrue(result.contains("could not be parsed"));
  }

  @Test
  public void importMixedData() throws Exception {
    CSVImporter importer = new CSVImporter();
    File tempFile = File.createTempFile("mixedData", ".csv");
    FileWriter writer = new FileWriter(tempFile);
    writer.write(
        "Subject,Start Date,Start Time,End Date,End Time,AllDay,Description,Location,Private\n");

    writer.write("Meeting,2025-04-10,10:00,2025-04-10,11:00,false,Description,Office,false\n");

    writer.write("Event,2025-04-11,12:00\n");
    writer.close();
    ICalendarModel model = manager.getCalendar("DUMMY").getCalendarModel();
    String result = importer.importData(model, tempFile.getAbsolutePath());
    assertEquals("Imported 1 events.\n1 errors:\nLine 3: Invalid number of fields.\n", result);
  }

  @Test
  public void importReadsQuotedFieldsWrittenByExporter() throws Exception {
    File tempFile = File.createTempFile("quoted", ".csv");
    tempFile.deleteOnExit();
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write("Subject,Start Date,Start Time,End Date,End Time,AllDayEvent,"
          + "Description,Location,Private\n");
      writer.write("\"Lunch, \"\"team\"\"\",2025-06-02,12:00,2025-06-02,13:00,false,"
          + "\"a, b\",Cafe,false\n");
    }
    CalendarModel model = new CalendarModel();
    String result = new CSVImporter().importData(model, tempFile.getAbsolutePath());
    assertEquals("Imported 1 events.", result);
    assertEquals("Lunch, \"team\"", model.getAllEvents().get(0).getSubject());
    assertEquals("a, b", model.getAllEvents().get(0).getDescription());
  }
}