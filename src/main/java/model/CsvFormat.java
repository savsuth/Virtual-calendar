package model;

import java.util.ArrayList;
import java.util.List;

/**
 * RFC 4180 field quoting shared by {@link CSVExporter} and {@link CSVImporter}, so a subject,
 * description or location containing a comma, quote or newline survives a round trip and imports
 * cleanly into Google Calendar.
 */
final class CsvFormat {

  private CsvFormat() {
  }

  /**
   * Formats one field, quoting it only when it contains a comma, quote or line break.
   *
   * @param value the raw value; null is written as an empty field
   * @return the field as it should appear in the CSV row
   */
  static String field(String value) {
    if (value == null) {
      return "";
    }
    if (value.contains(",") || value.contains("\"")
        || value.contains("\n") || value.contains("\r")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }

  /**
   * Splits one CSV line into fields, honouring quoted fields and doubled quotes.
   *
   * @param line a single CSV line
   * @return the unquoted field values, including trailing empty fields
   */
  static String[] parseLine(String line) {
    List<String> fields = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean inQuotes = false;
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (inQuotes) {
        if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
          current.append('"');
          i++;
        } else if (c == '"') {
          inQuotes = false;
        } else {
          current.append(c);
        }
      } else if (c == '"') {
        inQuotes = true;
      } else if (c == ',') {
        fields.add(current.toString());
        current.setLength(0);
      } else {
        current.append(c);
      }
    }
    fields.add(current.toString());
    return fields.toArray(new String[0]);
  }
}
