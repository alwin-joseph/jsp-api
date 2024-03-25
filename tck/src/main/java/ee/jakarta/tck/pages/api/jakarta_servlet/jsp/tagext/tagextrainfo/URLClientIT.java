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

/*
 * @(#)URLClient.java	1.2 10/09/02
 */

package ee.jakarta.tck.pages.api.jakarta_servlet.jsp.tagext.tagextrainfo;


import ee.jakarta.tck.pages.common.client.AbstractUrlClient;
import ee.jakarta.tck.pages.common.util.JspTestUtil;
import ee.jakarta.tck.pages.common.tags.tck.SimpleTag;

/**
 * Test client for TagExtraInfo. If the test fails, a translation error will be
 * generated and a ValidationMessage array will be returned.
 */
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

    setContextRoot("/jsp_tagextrainfo_web");

    }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagextrainfo_web.war");
    archive.addClasses(TagExtraInfoTEI.class,
            JspTestUtil.class,
            SimpleTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagextrainfo_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagextrainfo.tld", "tagextrainfo.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagExtraInfoDefaultImplTest.jsp")), "TagExtraInfoDefaultImplTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagExtraInfoEmptyReturnTest.jsp")), "TagExtraInfoEmptyReturnTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagExtraInfoNonEmptyReturnTest.jsp")), "TagExtraInfoNonEmptyReturnTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagExtraInfoNullReturnTest.jsp")), "TagExtraInfoNullReturnTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagExtraInfoTest
   * 
   * @assertion_ids:
   * JSP:JAVADOC:264;JSP:JAVADOC:265;JSP:JAVADOC:266;JSP:JAVADOC:267
   * 
   * @test_Strategy: Validate the following: - TagExtraInfo.getTagInfo() returns
   * a non-null value as the container called TagExtraInfo.setTagInfo() prior to
   * calling validate. - A null or an emtpy array of ValidationMessage returned
   * by validate does not cause a translation error. - A non-empty array of
   * ValiationMessages causes a translation error. - The default implementation
   * of TagExtraInfo.validate() calls isValid(). If isValid() returns false, a
   * default ValidationMessage array is returned.
   */
  @Test
  public void tagExtraInfoTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagextrainfo_web/TagExtraInfoNullReturnTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagextrainfo_web/TagExtraInfoEmptyReturnTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagextrainfo_web/TagExtraInfoNonEmptyReturnTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagextrainfo_web/TagExtraInfoDefaultImplTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }
}
