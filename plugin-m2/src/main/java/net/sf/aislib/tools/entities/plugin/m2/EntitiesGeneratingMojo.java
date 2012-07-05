package net.sf.aislib.tools.entities.plugin.m2;

import java.io.File;

import net.sf.aislib.tools.entities.library.Processor;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Generate class file with constants.
 *
 * @author pikus
 */
@Mojo(defaultPhase = LifecyclePhase.GENERATE_SOURCES, name = "generate")
public class EntitiesGeneratingMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project.build.directory}/generated-sources/entities")
  private File outputDirectory;

  @Parameter(required = true)
  private File sourceFile;

  @Parameter
  private String resultName = "Entities";

  @Parameter(required = true)
  private String packageName;

  @Parameter(defaultValue = "${project}", required = true)
  private MavenProject project;

  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      Processor processor = new Processor();
      processor.setSource(sourceFile);
      processor.setClassName(resultName);
      processor.setPackageName(packageName);
      processor.setDestinationDirectory(outputDirectory);
      processor.process();

      project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public void setSourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  public void setResultName(String resultName) {
    this.resultName = resultName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }
}
