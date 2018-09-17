package io.github.mcwarman;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author mwarman
 */
public class XpathUtilsTest {

  @Test
  public void getXPath() throws Exception {
    List<String> s = XpathUtils.getXpaths("<foo><foo1>Foo Test 1</foo1><services><another1><unique-id>value</unique-id><test1>Foo Test 2.0</test1><test1>${data}</test1></another1></services><foo3>Foo Test 3</foo3><foo4>Foo Test 4</foo4></foo>");
    assertEquals(6, s.size());
    assertEquals("/foo/foo1", s.get(0));
  }

  @Test
  public void getXPathWithAttributes() throws Exception {
    List<String> s = XpathUtils.getXpaths("<foo><foo1 hello=\"world\">Foo Test 1</foo1><services><another1><unique-id>value</unique-id><test1>Foo Test 2.0</test1><test1>${data}</test1></another1></services><foo3>Foo Test 3</foo3><foo4>Foo Test 4</foo4></foo>");
    assertEquals(7, s.size());
    assertEquals("/foo/foo1/@hello", s.get(0));
  }

  @Test
  public void getXPathWithPosition() throws Exception {
    List<String> s = XpathUtils.getXpaths("<master_data><record><type>0</type></record><record><type>0</type></record><record><type>1</type></record></master_data>");
    assertEquals(3, s.size());
    assertEquals("/master_data/record[1]/type", s.get(0));
    assertEquals("/master_data/record[2]/type", s.get(1));
    assertEquals("/master_data/record[3]/type", s.get(2));
  }

  @Test
  public void getXPathWithNameSpace() throws Exception {
    List<String> s = XpathUtils.getXpaths("<foo xmlns=\"https://warman.io/DoesNotExist.xsd\"><foo1>Foo Test 1</foo1><services><another1><unique-id>value</unique-id><test1>Foo Test 2.0</test1><test1>${data}</test1></another1></services><foo3>Foo Test 3</foo3><foo4>Foo Test 4</foo4></foo>");
    assertEquals(6, s.size());
    assertEquals("/foo/foo1", s.get(0));
  }


  @Test
  public void getXPathWithNameSpaceWithNamedPrefix() throws Exception {
    Map<String, String> ns = Collections.singletonMap("https://warman.io/DoesNotExist.xsd", "ns");
    List<String> s = XpathUtils.getXpaths(
        "<foo xmlns=\"https://warman.io/DoesNotExist.xsd\"><foo1>Foo Test 1</foo1><services><another1><unique-id>value</unique-id><test1>Foo Test 2.0</test1><test1>${data}</test1></another1></services><foo3>Foo Test 3</foo3><foo4>Foo Test 4</foo4></foo>"
        ,ns);
    assertEquals(6, s.size());
    assertEquals("/ns:foo/ns:foo1", s.get(0));
  }


}