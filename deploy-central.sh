#!/usr/bin/env bash
VERSION=3.3.2-java8

# parent pom
mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=pom.xml \
-Dfile=pom.xml

# core
mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=generator-plugin-java8/pom.xml \
-Dfile=generator-plugin-java8/target/stjs-generator-plugin-java8-$VERSION.jar

mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=generator-plugin-java8/pom.xml \
-Dfile=generator-plugin-java8/target/stjs-generator-plugin-java8-$VERSION-javadoc.jar \
-Dclassifier=javadoc

mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=generator-plugin-java8/pom.xml \
-Dfile=generator-plugin-java8/target/stjs-generator-plugin-java8-$VERSION-sources.jar \
-Dclassifier=sources

# maven plugin
mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=maven-plugin/pom.xml \
-Dfile=maven-plugin/target/stjs-maven-plugin-$VERSION.jar


mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=maven-plugin/pom.xml \
-Dfile=maven-plugin/target/stjs-maven-plugin-$VERSION-sources.jar \
-Dclassifier=sources


# test-helper
mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=generator-plugin-java8/pom.xml \
-Dfile=generator-plugin-java8/target/stjs-generator-plugin-java8-$VERSION.jar

mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=generator-plugin-java8/pom.xml \
-Dfile=generator-plugin-java8/target/stjs-generator-plugin-java8-$VERSION-javadoc.jar \
-Dclassifier=javadoc

mvn gpg:sign-and-deploy-file \
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
-DrepositoryId=ossrh \
-DpomFile=generator-plugin-java8/pom.xml \
-Dfile=generator-plugin-java8/target/stjs-generator-plugin-java8-$VERSION-sources.jar \
-Dclassifier=sources
