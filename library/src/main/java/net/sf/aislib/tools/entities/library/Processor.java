package net.sf.aislib.tools.entities.library;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * Reads a DTD with application entities and put it into class as static fields.
 *
 * @author AIS Development Team
 * @version $Revision: 1.1 $
 */
public class Processor {

  private boolean velocityInitailized = false;

  private static final String defaultTemplate = "net/sf/aislib/tools/entities/library/class.template";

  private File source;
  private File destination;
  private String packageName;
  private String className = "Entities";
  private String template = defaultTemplate;

  /**
   * Sets source file (DTD file with entities declarations).
   *
   * @param file source File.
   */
  public void setSource(File file) {
    source = file;
  }

  /**
   * Sets destination file - Java class which will be generated.
   *
   * @param file destination File.
   */
  public void setDestinationDirectory(File file) {
    destination = file;
  }

  public void setTemplate(String template) {
	this.template = template;
  }

  /**
   * @param packageName The packageName to set.
   */
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  /**
   * Sets package and class name, gets entities from <code>dtdFileName</code> and runs Velocity.
   *
   * @throws ParseEntityException
   */
  public void process() throws ParseEntityException {
    Map values = new HashMap();

    // get class name from destination file
    values.put("className", className);
    values.put("packageName", packageName);
    values.put("entities", parseEntities(source));

    String packagePath = packageName.replace('.', File.separatorChar);
    File outDir = new File(destination, packagePath);
    if (!outDir.exists()) {
      outDir.mkdirs();
    }
    File outFile = new File(outDir, className + ".java");
    runVelocity(outFile, values);
  }

  private List parseEntities(File sourceFile) throws ParseEntityException {
    try {
      DTDEntityParser parser = new DTDEntityParser(sourceFile, false);
      parser.parse();

      return parser.getEntities();
    } catch (Exception e) {
      throw new ParseEntityException(e);
    }
  }

  /**
   * Runs Velocity engine and creates output file from template file.
   *
   * @param outFile a <code>File</code> value
   * @param values a <code>Map</code> value
   * @exception ParseEntityException if an error occurs
   */
  private void runVelocity(File outFile, Map values) throws ParseEntityException {
    if (!velocityInitailized) {
      try {
        Properties p = new Properties();
        p.setProperty(RuntimeConstants.RESOURCE_LOADER, "jar");
        p.setProperty("jar.resource.loader.class",
            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
        velocityInitailized = true;
      } catch (Exception e) {
        throw new ParseEntityException(e);
      }
    }

    try {
      FileWriter writer = new FileWriter(outFile);
      VelocityContext context = new VelocityContext(values);
      try {
        Velocity.mergeTemplate(template, "ISO-8859-1", context, writer);
      } catch (ResourceNotFoundException e) {
        throw new ParseEntityException(e);
      } catch (ParseErrorException e) {
        throw new ParseEntityException(e);
      } catch (MethodInvocationException e) {
        throw new ParseEntityException(e);
      } catch (Exception e) {
        throw new ParseEntityException(e);
      } finally {
        writer.close();
      }
    } catch (IOException e) {
      throw new ParseEntityException(e);
    }
  }

  public void setClassName(String className) {
    this.className = className;
  }

  private class ParseEntityException extends Exception {

    /**
     * Creates a new <code>ParseEntityException</code> instance.
     *
     * @param cause a <code>Throwable</code> value
     */
    public ParseEntityException(
        Throwable cause) {
      super(cause);
    }

    /**
     * Creates a new <code>ParseEntityException</code> instance.
     *
     * @param message a <code>String</code> value
     */
    public ParseEntityException(
        String message) {
      super(message);
    }
  }

}