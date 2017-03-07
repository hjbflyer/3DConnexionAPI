# #############################################################################################
# Makefile
#
# This makefile has been written for building the native library (*.jnilib) on OS X, but it
# can be easily adapted to other operating systems.
# 
# Author: hjbflyer
# #############################################################################################
# define main variables
JNI_NAME = ConnexionAPI
PACKAGE := de.hjbflyer.connexion3dapi
# #############################################################################################
LDFLAGS = -framework 3DconnexionClient -dynamiclib 
INCLUDE_DIRS = -I$(JAVA_HOME)/include/darwin -I$(JAVA_HOME)/include
INCLUDE_FRAMEWORKS = -F/Library/Frameworks
# #############################################################################################
JNI_DIR      := $(subst .,/,$(PACKAGE))
JAVA_SRC	:= $(wildcard $(JNI_DIR)/*.java)
JAVA_OBJS   := $(JAVA_SRC:.java=.class)
JNI_HEADER	:= $(subst .,_,$(PACKAGE))_$(JNI_NAME).h
# #############################################################################################
NATIVE_LIB	:= lib$(JNI_NAME).jnilib
CLASS_PATH = ../bin
# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)
# #############################################################################################
# build all
# #############################################################################################
all: testjava clean $(NATIVE_LIB) $(JNI_NAME).jar

# #############################################################################################
# test if java is setup?
# #############################################################################################
testjava:
	@if [ ! -e $(JAVA_HOME) ]; then \
		echo "Please set JAVA_HOME to point to a SDK!"; \
		exit 1; \
	fi

# #############################################################################################
# compile java into class files
# #############################################################################################
%.class:	%.java
	$(JAVA_HOME)/bin/javac -C ../bin $<
	
# #############################################################################################
# generate the C/C++ header file from java code
# #############################################################################################
$(JNI_HEADER):	$(JNI_DIR)/$(JNI_NAME).class
	$(JAVA_HOME)/bin/javah -d include -classpath $(CLASS_PATH)  $(PACKAGE).$(JNI_NAME)

# #############################################################################################
# build the jar file
# #############################################################################################
$(JNI_NAME).jar:	$(JAVA_OBJS)
	$(JAVA_HOME)/bin/jar cvfe  $@  $(PACKAGE).$(JNI_NAME)Demo -C ../bin/ . $^
		
# #############################################################################################
# build the dynamic native library
# #############################################################################################
$(NATIVE_LIB):	$(JNI_HEADER) $(JNI_NAME).cpp
	g++ -v $(INCLUDE_DIRS) \
	    $(LDFLAGS) \
	    -o $@ $(JNI_NAME).cpp
	
# #############################################################################################
# build java doc
# #############################################################################################
documentation:
	$(JAVA_HOME)/bin/javadoc -d ../documentation -sourcepath ../src $(PACKAGE)
	
# #############################################################################################
# remove all generated files
# #############################################################################################
clean:
	rm -f $(JAVA_OBJS) \
	      include/$(JNI_HEADER) \
	      $(NATIVE_LIB) \
	      $(JNI_NAME).jar
	rm -rf ../documentation
