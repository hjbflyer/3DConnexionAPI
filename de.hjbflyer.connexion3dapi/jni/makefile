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
LDFLAGS = -framework 3DconnexionClient -dynamiclib -lc++
INCLUDE_DIRS = -I $(JAVA_HOME)/include/darwin -I $(JAVA_HOME)/include -I Frameworks
# #############################################################################################
JNI_DIR     := $(subst .,/,$(PACKAGE))
JAVA_SRC    := $(wildcard ../src/$(JNI_DIR)/*.java)
JAVA_OBJS   := $(JAVA_SRC:.java=.class)
JNI_HEADER	:= $(subst .,_,$(PACKAGE))_$(JNI_NAME).h
# #############################################################################################
NATIVE_LIB	:= lib$(JNI_NAME).dylib
CLASS_PATH = ../bin
# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

OS := $(shell uname)
ifeq ($(OS),Darwin)
DEST_DIR:=../os/macosx/x86_64/
endif

.PHONY: clean testjava all

# #############################################################################################
# build all
# #############################################################################################
all: testjava ../os $(NATIVE_LIB) $(JNI_NAME).jar

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
$(JNI_DIR)/$(JNI_NAME).class:	$(JAVA_SRC)
	$(JAVA_HOME)/bin/javac  -cp ../src -d ../bin $(JAVA_SRC)
	
# #############################################################################################
# generate the C/C++ header file from java code
# #############################################################################################
$(JNI_HEADER):	$(JNI_DIR)/$(JNI_NAME).class
	$(JAVA_HOME)/bin/javah -d include -classpath $(CLASS_PATH)  $(PACKAGE).$(JNI_NAME)

# #############################################################################################
# build the jar file
# #############################################################################################
$(JNI_NAME).jar:	$(JNI_HEADER)
	$(JAVA_HOME)/bin/jar cvfe  $@  $(PACKAGE).$(JNI_NAME)Demo -C ../bin/ . 
		
# #############################################################################################
# build os directory structure if not there
# #############################################################################################
../os:
	mkdir ../os
	mkdir ../os/macosx
	mkdir ../os/macosx/x86_64
	mkdir ../os/linux
	mkdir ../os/linux/x86_64
	mkdir ../os/win32
	mkdir ../os/win32/x86_64

# #############################################################################################
# build the dynamic native library
# #############################################################################################
$(NATIVE_LIB):	$(JNI_HEADER) $(JNI_NAME).cpp
	gcc -v $(CFLAGS) $(JNI_NAME).cpp $(INCLUDE_DIRS) $(LDFLAGS) -o $@
	cp $(NATIVE_LIB) $(DEST_DIR)

# #############################################################################################
# build java doc
# #############################################################################################
documentation:
	$(JAVA_HOME)/bin/javadoc -d ../documentation -sourcepath ../src $(PACKAGE)
	
# #############################################################################################
# remove all generated files
# #############################################################################################
clean:
	rm -f ../bin/$(JNI_DIR)/*.class \
	    include/$(JNI_HEADER) \
	    $(NATIVE_LIB) \
	    $(JNI_NAME).jar
	rm -rf ../documentation
