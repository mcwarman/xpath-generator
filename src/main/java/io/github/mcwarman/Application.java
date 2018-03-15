package io.github.mcwarman;

import org.apache.commons.cli.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author mwarman
 */
public class Application {

  private Options options;

  private String file;

  private static final String HELP_ARG = "help";
  private static final String FILE_ARG = "file";

  Application(){
    options = new Options();
    options.addOption("h",HELP_ARG, false, "Displays this.." );
    options.addRequiredOption("f", FILE_ARG, true, "(required) The file");
  }

  public static void main(String[] args) throws Exception {
    Application projectMigrator = new Application();
    projectMigrator.run(args);
  }

  void run(String[] args) throws ParserConfigurationException, SAXException, TransformerException, IOException {
    arguments(args);
    run(file);
  }

  void arguments(String[] args){
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine line = parser.parse(options, args);
      if(line.hasOption(HELP_ARG)){
        usage();
      }
      file = line.getOptionValue(FILE_ARG);
    } catch (ParseException e) {
      System.err.println("Parsing failed.  Reason: " + e.getMessage());
      usage();
    }
  }

  void run(String filePath) throws IOException, ParserConfigurationException, SAXException, TransformerException {
    String xml = XmlUtils.resolveXincludes(readFile(filePath, StandardCharsets.UTF_8));
    for (String s : XpathUtils.getXpaths(xml)){
      System.out.println(s);
    }
  }


  void usage(){
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "xpath-generator", options );
    System.exit(1);
  }

  String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

}
