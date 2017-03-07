# 3DConnexionAPI
Java api implementation for the Connexion 3D mouse

## Prerequisites
* [Java 8](http://www.java.com) (But code can be easily changed to an earlier version)
* [Eclipse](http://www.eclipse.org) (As development envirenment) 
* [XCode](https://developer.apple.com/xcode/) (For the C/C++ tools on a MAC)

## Building

Building can be done either from command line or from Eclipse. To build from command line and run a small demo, open a terminal. 
```
cd <ProjectDirectory>/jni
make all
java -jar ConnexioAPI.jar 
```

## JavaDoc
To generate the JavaDoc
```
make documentation
```

## Dynamic Library
The initial version of this project does support OS X only. To build for other operating systems, the <b>makefile</b>
has to be adapted.

To use the API you have to include the dynamic library <b>libConnxionAPI.jnilib</b> in your library load path.

## License

Eclipse Public License
http://www.eclipse.org/legal/epl-v10.html
