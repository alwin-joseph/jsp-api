/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
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

env.label = "jakartaee-tck-pod-${UUID.randomUUID().toString()}"

default_tcks=["jsp"]
def tcks = params.standalone_tcks != null ? params.standalone_tcks.split() : default_tcks
 
 
def parallelStandaloneTCKMap = tcks.collectEntries {
  ["${it}": generateStandaloneTCKStage(it)]
}
 
def generateStandaloneTCKStage(job) {
  return {
    podTemplate(label: env.label) {
      node(label) {
        stage("${job}") {
          container('jakartaeetck-ci') {
            checkout scm
            unstash 'standalone-bundles'
            sh """
              env
              bash -x ${WORKSPACE}/docker/${job}tck.sh 2>&1 | tee ${WORKSPACE}/${job}tck.log
            """
            archiveArtifacts artifacts: "${job}tck-results.tar.gz,*-junitreports.tar.gz,${job}tck.log",allowEmptyArchive: true
            junit testResults: 'results/junitreports/*.xml', allowEmptyResults: true

          }
        }
      }
    }
  }
}
 
pipeline {
  options {
    durabilityHint('PERFORMANCE_OPTIMIZED')
    buildDiscarder(logRotator(numToKeepStr: '30', artifactDaysToKeepStr: '30'))
  }
  agent {
    kubernetes {
      label "${env.label}"
      defaultContainer 'jakartaeetck-ci'
      yaml """
apiVersion: v1
kind: Pod
metadata:
spec:
  hostAliases:
  - ip: "127.0.0.1"
    hostnames:
    - "localhost.localdomain"
    - "james.local"
  containers:
  - name: jnlp
    env:
      - name: JNLP_PROTOCOL_OPTS
        value: "-XshowSettings:vm -Xmx2048m -Dsun.zip.disableMemoryMapping=true -Dorg.jenkinsci.remoting.engine.JnlpProtocol3.disabled=true"
  - name: jakartaeetck-ci
    image: jakartaee/cts-base:0.1
    command:
    - cat
    tty: true
    imagePullPolicy: Always
    env:
      - name: JAVA_TOOL_OPTIONS
        value: -Xmx6G
    resources:
      limits:
        memory: "10Gi"
        cpu: "2.0"
  - name: james-mail
    image: jakartaee/cts-mailserver:0.1
    command:
    - cat
    ports:
    - containerPort: 1025
    - containerPort: 1143
    tty: true
    imagePullPolicy: Always
    resources:
      limits:
        memory: "2Gi"
        cpu: "0.5"
"""
    }
  }
  parameters {
    string(name: 'GF_BUNDLE_URL', 
           defaultValue: '', 
           description: 'URL required for downloading GlassFish Full/Web profile bundle' )
    string(name: 'GF_VERSION_URL', 
           defaultValue: '', 
           description: 'URL required for downloading GlassFish version details' )
    string(name: 'TCK_BUNDLE_BASE_URL', 
           defaultValue: '', 
           description: 'Base URL required for downloading prebuilt binary TCK Bundle from a hosted location' )
    string(name: 'TCK_BUNDLE_FILE_NAME', 
           defaultValue: 'jakartaeetck.zip', 
           description: 'Name of bundle file to be appended to the base url' )
    choice(name: 'PROFILE', choices: 'FULL\nWEB', 
           description: 'Profile to be used for running CTS either web/full' )
    choice(name: 'DATABASE', choices: 'JavaDB\nOracle\nMySQL', 
           description: 'Database to be used for running CTS. Currently only JavaDB is supported.' )
    choice(name: 'BUILD_TYPE', choices: 'CTS\nSTANDALONE-TCK', 
           description: 'Run the full EE compliance testsuite or a standalone tck' )
    string(name: 'standalone_tcks', defaultValue: 'caj concurrency connector el jacc jaspic jaxr jaxrpc jaxrs jaxws jms jpa jsf jsp jsonb jsonp jstl jta saaj securityapi servlet websocket', 
           description: 'Space separated list of standalone TCKs to build and run') 
    string(name: 'USER_KEYWORDS',
           defaultValue: '',
           description: 'Optional keywords prefixed by joining operator - [&|] for filtering out the tests to run' )
  }
  environment {
    CTS_HOME = "/root"
    ANT_OPTS = "-Djavax.xml.accessExternalStylesheet=all -Djavax.xml.accessExternalSchema=all -Djavax.xml.accessExternalDTD=file,http" 
    MAIL_USER="user01@james.local"
    MAIL_HOST="localhost"
    LANG="en_US.UTF-8"
    DEFAULT_GF_BUNDLE_URL="https://repo1.maven.org/maven2/org/glassfish/main/distributions/glassfish/5.1.0/glassfish-5.1.0.zip"
  }
  stages {
 
    stage('standalone-tck-build') {
      when {
        expression {
          return params.BUILD_TYPE == 'STANDALONE-TCK';
         }
      }
 
      steps {
        container('jakartaeetck-ci') {
          sh """
            env
            bash -x ${WORKSPACE}/docker/build_standalone-tcks.sh ${standalone_tcks} 2>&1 | tee ${WORKSPACE}/build_standalone-tcks.log
          """
          archiveArtifacts artifacts: "standalone-bundles/*.zip,*.version,*.log", allowEmptyArchive: true
          stash includes: 'standalone-bundles/*.zip', name: 'standalone-bundles'
        }
      }
    }

    stage('standalone-tck-run') {
      when {
        expression {
          return params.BUILD_TYPE == 'STANDALONE-TCK';
        }
      }
      steps {
        script {
          parallel parallelStandaloneTCKMap
        }
      }
    }
  }
}
