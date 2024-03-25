/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package ee.jakarta.tck.pages.api.jakarta_servlet.jsp.jsptagexception;


import ee.jakarta.tck.pages.common.client.AbstractUrlClient;
import ee.jakarta.tck.pages.common.util.JspTestUtil;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {




  public URLClientIT() throws Exception {

    setContextRoot("/jsp_jsptagexc_web");
    setTestJsp("JspTagExceptionTest");

    }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_jsptagexc_web.war");
    archive.addClasses(JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_jsptagexc_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagExceptionTest.jsp")), "JspTagExceptionTest.jsp");


    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: jspTagExceptionDefaultCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:116
   * 
   * @test_Strategy: Validate default constructor of JspTagException
   */
  @Test
  public void jspTagExceptionDefaultCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspTagExceptionDefaultCtorTest");
    invoke();
  }

  /*
   * @testName: jspTagExceptionMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:115
   * 
   * @test_Strategy: Validate contructor taking single string argument as the
   * message of the Exception.
   */
  @Test
  public void jspTagExceptionMessageCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspTagExceptionMessageCtorTest");
    invoke();
  }

  /*
   * @testName: jspTagExceptionCauseCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:118
   * 
   * @test_Strategy: Validate constructor taking a Throwable signifying the root
   * cause of the this JspTagException.
   */
  @Test
  public void jspTagExceptionCauseCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspTagExceptionCauseCtorTest");
    invoke();
  }

  /*
   * @testName: jspTagExceptionCauseMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:117
   * 
   * @test_Strategy: Validate constructor taking both a message and a Throwable
   * signifying the root cause of the JspTagException.
   */
  @Test
  public void jspTagExceptionCauseMessageCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspTagExceptionCauseMessageCtorTest");
    invoke();
  }
}
