/**
 * Created by Chek Tien Tan on 23/3/17.
 * NOTE: please mess with this file to your hearts content.
 */

#include <jni.h>
#include <string>
#include <sstream>
#include <math.h>
#include "sha256.h"

extern "C" {
    static const char *TAG = "SHANATIVE";

    /**
     * Generate salt.
     * - each salt is a hash of the hash iteration count
     */
    static const std::string getSalt(const int i) {
        std::stringstream ss;
        ss << i;
        return sha256(ss.str());
    }

    JNIEXPORT jstring JNICALL Java_edu_singaporetech_shactivity_SHActivity_shaMe(JNIEnv *env, jobject instance, jstring message, jstring count){
        const char *str = env->GetStringUTFChars(message, 0);
        std::string digestText = sha256(str+getSalt(0));

        int total = pow(2,count);
		
        for(int i=1; i<total; i++){
            digestText = sha256(digestText+getSalt(i));
        }
        env->ReleaseStringUTFChars(message,str);
        return env->NewStringUTF(digestText.c_str());
    }
}