/*
 * ConnexionAPI - implementation of the Java Native Interface
 *
 * @author: hjbflyer
 *
 * Copyright(c) hjbflyer 2017
 */

#include <jni.h>
#include <iostream>
#include <3DconnexionClient/ConnexionClientAPI.h>
#include "include/de_hjbflyer_connexion3dapi_ConnexionApi.h"

#define NO_OF_AXIS 6  // x, y, z, rx, ry, rz

// global references - we use the prefix 'g_' for global references
// ===========================================================================================================
JavaVM *g_virtualMachine;					// Java VM
jobject g_callingObj;						// instance of the ConnexionAPI
jmethodID g_midMessageAxisHandler;			// method ID for the message axis handler
jmethodID g_midForMessageButtonHandler;		// method ID for the message button handler
jmethodID g_midForAddedHandler;				// method ID for the device added handler
jmethodID g_midForRemovedHandler;			// method ID for the device removed handler
jmethodID g_midForPrefsHandler;				// method ID for the device removed handler
jmethodID g_midForDeviceAddedHandler;
jmethodID g_midForDeviceRemovedHandler;
int g_clientID;								// identifies the client

// Forward declarations of message handlers
// ===========================================================================================================
static void InternalMessageHandler(unsigned int productID, unsigned int messageType, void *messageArgument);
static void InternalAddedHandler(unsigned int productID);
static void InternalRemovedHandler(unsigned int productID);

// forward declarations of utility procedures;
// ===========================================================================================================
void cstr2pstr(const char *cstr, char *pstr);      	// convert c-string to Pascal string
void pstr2cstr(const char *pstr, char *cstr);		// convert Pascal string to c-string
void fillPrefs(jclass prefsClass, jobject prefsObj, JNIEnv* env, ConnexionDevicePrefs* pprefs); // fill prefs Object
void fillIntArray(unsigned int productID, unsigned int messageType, JNIEnv* env, jmethodID mid, int16_t values[6]);
JNIEnv * getEnvAndcheckVersionAndAttachTread();

/*
 * ***********************************************************************************************************
 * setConnexionHandlers
 *
 * Class:     de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:    setConnexionHandlers
 * Signature: (Z)I
 *
 * This is the first call into the ConnexionAPI. It stores some global values for later use and sets
 * the different ConnexionHandlers (callbacks from the driver).
 * ***********************************************************************************************************
 */
JNIEXPORT jint JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_setConnexionHandlers(JNIEnv *env,
		jobject callingObj, jboolean useSeparateThread) {

	// get reference to virtual machine
	int status = env->GetJavaVM(&g_virtualMachine);
	// reference to calling class
	g_callingObj = env->NewGlobalRef(callingObj);
	// get class of the callingObj
	jclass callingClass = env->GetObjectClass(callingObj);
	// get the method id's of the jave code
	g_midMessageAxisHandler = env->GetMethodID(callingClass, "messageHandlerAxisCallback", "(II[I)V");
	g_midForMessageButtonHandler = env->GetMethodID(callingClass, "messageHandlerButtonCallback", "(IIJ)V");
	g_midForPrefsHandler = env->GetMethodID(callingClass, "messageHandlerPrefsCallback",
			"(IILde/hjbflyer/connexion3dapi/ConnexionDevicePrefs;)V");
	g_midForDeviceAddedHandler = env->GetMethodID(callingClass, "deviceAddedHandler", "(I)V");
	g_midForDeviceRemovedHandler = env->GetMethodID(callingClass, "deviceRemovedHandler", "(I)V");

	// now set the handlers and return the error code
	return SetConnexionHandlers(InternalMessageHandler, InternalAddedHandler, InternalRemovedHandler, useSeparateThread);
}

/*
 * ***********************************************************************************************************
 * registerConnexionClient
 *
 * Class:     de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:    registerConnexionClient
 * Signature: (ILjava/lang/String;IJ)I
 *
 * This call registers the client and returns an ID which should be used on other calls to identify the client
 * We do pass the ID to java.
 * ***********************************************************************************************************
 */
JNIEXPORT void JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_registerConnexionClient(JNIEnv *env,
		jobject callingObj, jint bundleSig, jstring execName, jint takeOver, jlong mask) {

	// get exex name and convert it to a pascal string
	const char *execNameCStr = env->GetStringUTFChars(execName, NULL);
	char execNamePStr[65];
	cstr2pstr(execNameCStr, execNamePStr);
	g_clientID = RegisterConnexionClient(bundleSig, (uint8_t *) execNamePStr, takeOver, mask);
}

/*
 * ***********************************************************************************************************
 * cleanUpConnexionHandler
 *
 * Class:     de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:    cleanUpConnexionHandler
 * Signature: ()V
 *
 * Tell the driver to discard the handlers
 * ***********************************************************************************************************
 */
JNIEXPORT void JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_cleanUpConnexionHandler(JNIEnv *env,
		jobject callingObject) {
	CleanupConnexionHandlers();
}
/*
 * ***********************************************************************************************************
 * unregisterConnexionClient
 *
 * Class:		de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:		unregisterConnexionClient
 * Signature:	()V
 *
 * Unregister the client
 * ***********************************************************************************************************
 */
JNIEXPORT void JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_unregisterConnexionClient(JNIEnv *env,
		jobject callingObj) {
	UnregisterConnexionClient(g_clientID);
	g_clientID = 0;
}

/*
 * ***********************************************************************************************************
 * Class:     de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:    clientControl
 * Signature: (IILjava/lang/Integer;)I
 * ***********************************************************************************************************
 */
JNIEXPORT jint JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_connexionClientControl(JNIEnv *env,
		jobject callingObject, jint message, jint param, jobject ret) {
	int32_t returnValue;
	int err = ConnexionClientControl(g_clientID, kConnexionCtlGetDeviceID, g_clientID, &returnValue);
	jclass  clazz = env->FindClass("java/lang/Integer");
	jmethodID mid = env->GetMethodID(clazz, "<init>", "(I)V");
	env->CallVoidMethod(ret, mid, returnValue);
	return err;
}

/*
 * ***********************************************************************************************************
 * setConnexionClientMask
 *
 * Class:     de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:    setConnexionClientMask
 * Signature: (IJ)V
 *
 * ***********************************************************************************************************
 */
JNIEXPORT void JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_setConnexionClientMask(JNIEnv *env,
		jobject callingObj, jlong mask) {
	SetConnexionClientMask(g_clientID, mask);
}

/*
 * ***********************************************************************************************************
 * setConnexionClientButtonMask
 *
 * Class:     de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:    setConnexionClientButtonMask
 * Signature: (IJ)V
 * ***********************************************************************************************************
 */
JNIEXPORT void JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_setConnexionClientButtonMask(JNIEnv *env,
		jobject callingObj, jlong buttonMask) {
	SetConnexionClientButtonMask(g_clientID, buttonMask);
}

/*
 * ***********************************************************************************************************
 * connexionGetCurrentDevicePrefs
 *
 * Class:     de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:    connexionGetCurrentDevicePrefs
 * Signature: (JLde/hjbflyer/connexion3dapi/ConnexionDevicePrefs;)I
 *
 * Request current device prefs
 * ***********************************************************************************************************
 */
JNIEXPORT jint JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_connexionGetCurrentDevicePrefs(JNIEnv *env,
		jobject callingObj, jobject prefsObj) {
	int32_t deviceID;
	ConnexionClientControl(g_clientID, kConnexionCtlGetDeviceID, 0, &deviceID);
	ConnexionDevicePrefs prefs;
	ConnexionDevicePrefs *pprefs = &prefs;
	int error = ConnexionGetCurrentDevicePrefs(deviceID, &prefs);
	jclass prefsClass = env->GetObjectClass(prefsObj);
	fillPrefs(prefsClass, prefsObj, env, pprefs);
	return 0;
}

/*
 * ***********************************************************************************************************
 * Class:     de_hjbflyer_connexion3dapi_ConnexionAPI
 * Method:    connexionSetButtonLabels
 * Signature: ([Ljava/lang/String;I)I
 * ***********************************************************************************************************
 */
JNIEXPORT jint JNICALL Java_de_hjbflyer_connexion3dapi_ConnexionAPI_connexionSetButtonLabels(
		JNIEnv *env, jobject callingObject, jobjectArray labels, jint size) {

	//return ConnexionSetButtonLabels(labels, size);
	return 0;
}

/*
 * ***********************************************************************************************************
 * ***********************************************************************************************************
 * The following sections contains the call backs from the device driver
 * ***********************************************************************************************************
 * ***********************************************************************************************************
 *
 */

/*
 * ***********************************************************************************************************
 * InternalMessageHandler
 * ***********************************************************************************************************
 */
static void InternalMessageHandler(unsigned int productID, unsigned int messageType, void *messageArgument) {

	ConnexionDeviceState *state;
	ConnexionDevicePrefs prefs;
	ConnexionDevicePrefs *pprefs = &prefs;
	jclass prefsClass;
	jobject prefsObj;
	jobject pprefsObj;
	JNIEnv *env;
	uint32_t signature;
	int32_t deviceID;
	short int error;
	int l;
	char execName[64];

	char buff[64];
	std::string s;

	if (NULL == g_virtualMachine) {
		std::cerr << "g vm = null" << std::endl;
		return;
	}
	env = getEnvAndcheckVersionAndAttachTread();
	if (NULL != env) {

		// NOTE for ConnexionMessageHandlerProc:
		// when messageType == kConnexionMsgDeviceState, messageArgument points to ConnexionDeviceState with size kConnexionDeviceStateSize
		// when messageType == kConnexionMsgPrefsChanged, messageArgument points to the target application signature with size sizeof(uint32_t)
		prefsClass = env->FindClass("de/hjbflyer/connexion3dapi/ConnexionDevicePrefs");
		prefsObj = env->NewObject(prefsClass, env->GetMethodID(prefsClass, "<init>", "()V"));
		switch (messageType) {
		case kConnexionMsgDeviceState:
			state = (ConnexionDeviceState*) messageArgument;
			if (state->client == g_clientID) {

				switch (state->command) {
				case kConnexionCmdHandleAxis:
					fillIntArray(productID, messageType, env, g_midMessageAxisHandler, state->axis);
					break;

				case kConnexionCmdHandleButtons:
					env->CallVoidMethod(g_callingObj, g_midForMessageButtonHandler, productID, messageType,
							state->buttons);
					break;
				}

			}
			if (env->ExceptionCheck()) {
				env->ExceptionDescribe();
			}
			break;

		case kConnexionMsgPrefsChanged:
			ConnexionClientControl(g_clientID, kConnexionCtlGetDeviceID, 0, &deviceID);
			error = ConnexionGetCurrentDevicePrefs(deviceID, &prefs);
			if (error == 0) {
				fillPrefs(prefsClass, prefsObj, env, &prefs);
				env->CallVoidMethod(g_callingObj, g_midForPrefsHandler, productID, messageType, prefsObj);
			}
			break;

		default:
			// other messageTypes can happen and should be ignored
			break;
		}
	}
	g_virtualMachine->DetachCurrentThread();
}

/*
 * ***********************************************************************************************************
 * InternalAddedHandler
 * ***********************************************************************************************************
 */
static void InternalAddedHandler(unsigned int productID) {
	JNIEnv *env;
	env = getEnvAndcheckVersionAndAttachTread();
	if (env != NULL) {
		env->CallVoidMethod(g_callingObj, g_midForDeviceAddedHandler, productID);
	}

}

/*
 * ***********************************************************************************************************
 * InternalRemovedHandler
 * ***********************************************************************************************************
 */
static void InternalRemovedHandler(unsigned int productID) {
	JNIEnv *env;
	env = getEnvAndcheckVersionAndAttachTread();
	if (env != NULL) {
		env->CallVoidMethod(g_callingObj, g_midForDeviceRemovedHandler, productID);
	}
}


/*
 * ***********************************************************************************************************
 * get Env, check  version an try to attach thread
 * ***********************************************************************************************************
 */
JNIEnv * getEnvAndcheckVersionAndAttachTread() {
	JNIEnv *env;
	int getEnvStat = g_virtualMachine->GetEnv((void **) &env, JNI_VERSION_1_8);
	if (getEnvStat == JNI_EDETACHED) {
		if (g_virtualMachine->AttachCurrentThread((void **) &env, NULL) != 0) {
			std::cerr << "??? Failed to attach current thread" << std::endl;
			return NULL;
		}
	} else if (getEnvStat == JNI_OK) {
		//
	} else if (getEnvStat == JNI_EVERSION) {
		std::cerr << "JNI version not supported" << std::endl;
		return NULL;
	}
	return env;
}

/*
 * ***********************************************************************************************************
 * fill an int array
 * ***********************************************************************************************************
 */
void fillIntArray(unsigned int productID, unsigned int messageType, JNIEnv* env, jmethodID mid, int16_t values[6]) {

	jintArray intarray = env->NewIntArray(NO_OF_AXIS);
	int data[NO_OF_AXIS];
	for (int i = 0; i < NO_OF_AXIS; i++) {
		data[i] = values[i];
	}
	env->SetIntArrayRegion(intarray, 0, NO_OF_AXIS, data);
	env->CallVoidMethod(g_callingObj, mid, productID, messageType, intarray);
}

/*
 * ***********************************************************************************************************
 * fill the ConnexionDevicePrefs from structure
 * ***********************************************************************************************************
 */
void fillPrefs(jclass prefsClass, jobject prefsObj, JNIEnv* env, ConnexionDevicePrefs* pprefs) {
	jmethodID mid = env->GetMethodID(prefsClass, "setType", "(I)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->type);
	mid = env->GetMethodID(prefsClass, "setVersion", "(I)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->version);
	mid = env->GetMethodID(prefsClass, "setDeviceID", "(I)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->deviceID);
	mid = env->GetMethodID(prefsClass, "setMainSpeed", "(I)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->mainSpeed);
	mid = env->GetMethodID(prefsClass, "setZoomOnY", "(I)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->zoomOnY);
	mid = env->GetMethodID(prefsClass, "setDominant", "(I)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->dominant);
	mid = env->GetMethodID(prefsClass, "setGamma", "(J)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->gamma);
	mid = env->GetMethodID(prefsClass, "setIntersect", "(J)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->intersect);
	mid = env->GetMethodID(prefsClass, "setAppSignature", "(J)V");
	env->CallVoidMethod(prefsObj, mid, pprefs->appSignature);
	mid = env->GetMethodID(prefsClass, "setAppName", "(Ljava/lang/String;)V");
	char execNameCStr[65];
	pstr2cstr((char*) (pprefs->appName), execNameCStr);
	jstring name = env->NewStringUTF(execNameCStr);
	env->CallVoidMethod(prefsObj, mid, name);

	jintArray intarray = env->NewIntArray(NO_OF_AXIS);
	mid = env->GetMethodID(prefsClass, "setMapV", "([I)V");
	int data[NO_OF_AXIS];
	for (int i = 0; i < NO_OF_AXIS; i++) {
		data[i] = pprefs->mapV[i];
	}
	env->SetIntArrayRegion(intarray, 0, NO_OF_AXIS, data);

	intarray = env->NewIntArray(NO_OF_AXIS);
	mid = env->GetMethodID(prefsClass, "setMapH", "([I)V");
	for (int i = 0; i < NO_OF_AXIS; i++) {
		data[i] = pprefs->mapH[i];
	}
	env->SetIntArrayRegion(intarray, 0, NO_OF_AXIS, data);

	intarray = env->NewIntArray(NO_OF_AXIS);
	mid = env->GetMethodID(prefsClass, "setEnabled", "([I)V");
	for (int i = 0; i < NO_OF_AXIS; i++) {
		data[i] = pprefs->enabled[i];
	}
	env->SetIntArrayRegion(intarray, 0, NO_OF_AXIS, data);

	intarray = env->NewIntArray(NO_OF_AXIS);

	mid = env->GetMethodID(prefsClass, "setReversed", "([I)V");
	for (int i = 0; i < NO_OF_AXIS; i++) {
		data[i] = pprefs->reversed[i];
	}
	env->SetIntArrayRegion(intarray, 0, NO_OF_AXIS, data);

	mid = env->GetMethodID(prefsClass, "setSpeed", "([I)V");
	for (int i = 0; i < NO_OF_AXIS; i++) {
		data[i] = pprefs->speed[i];
	}
	env->SetIntArrayRegion(intarray, 0, NO_OF_AXIS, data);

	mid = env->GetMethodID(prefsClass, "setSensitivity", "([I)V");
	for (int i = 0; i < NO_OF_AXIS; i++) {
		data[i] = pprefs->sensitivity[i];
	}
	env->SetIntArrayRegion(intarray, 0, NO_OF_AXIS, data);

	jlongArray longarray = env->NewLongArray(NO_OF_AXIS);
	mid = env->GetMethodID(prefsClass, "setScale", "([J)V");
	long longData[NO_OF_AXIS];
	for (int i = 0; i < NO_OF_AXIS; i++) {
		longData[i] = pprefs->scale[i];
	}
	env->SetLongArrayRegion(longarray, 0, NO_OF_AXIS, longData);
}

/*
 * ***********************************************************************************************************
 * convert C string to Pascal string
 * ***********************************************************************************************************
 */
void cstr2pstr(const char *cstr, char *pstr) {
	int i;
	for (i = 0; cstr[i]; i++) {
		pstr[i + 1] = cstr[i];
	}
	pstr[0] = i;
}

/*
 * ***********************************************************************************************************
 * convert Pascal string to C string
 * ***********************************************************************************************************
 */
void pstr2cstr(const char *pstr, char *cstr) {
	int i;
	for (i = 0; i < pstr[0]; i++) {
		cstr[i] = pstr[i + 1];
	}
	cstr[i] = 0;
}
