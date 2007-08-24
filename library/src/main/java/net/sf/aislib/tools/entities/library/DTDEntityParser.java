package net.sf.aislib.tools.entities.library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wutka.dtd.DTDEntity;
import com.wutka.dtd.DTDParser;

/**
 * Simple parser for DTD entities with saved order of entities.
 *
 * @author Andrzej Luszczyk, AIS.PL
 */
public class DTDEntityParser extends DTDParser {

  private List entities;

  /**
   * Constructor extended by initialization of entities list.
   *
   * @param in - The file to read.
   * @param trace - True if the parser should print out tokens as it reads them (used for debugging the parser).
   * @throws IOException
   */
  public DTDEntityParser(File in, boolean trace) throws IOException {
    super(in, trace);
    entities = new ArrayList();
  }

  /**
   * @see com.wutka.dtd.DTDParser#parseEntityDef(com.wutka.dtd.DTDEntity)
   */
  protected void parseEntityDef(DTDEntity entity) throws IOException {
    super.parseEntityDef(entity);
    entities.add(entity);
  }

  /**
   * Gets <code>entities</code> read from DTD file.
   *
   * @return List of all entities read from DTD file.
   */
  public List getEntities() {
    return entities;
  }

}