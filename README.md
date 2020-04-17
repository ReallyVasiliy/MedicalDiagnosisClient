# MedicalDiagnosisClient
Quick Android app for medical diagnosis, currently just using Endless Medical API (http://www.endlessmedical.com/OurAPI).

## About
I came across the Endless Medical API and decided to turn it into a quick app. I wanted to make it easy to contribute to this project. It uses the most widely-understood Android patterns. It's written in Java and is based on Android MVP starter (https://github.com/androidstarters/android-starter).

This is a work in progress -- it's functional, but isn't a complete app yet.

## How it works
* Click "BEGIN" to start a new session (yes, every time you launch the app, it's a new session -- we don't save anything on the device, and do not store any personally-identifiable information about users)
* Read and accept the terms. Accepting the terms is required to make the session usable.
* Enter "features" (aka symptoms). These could also be diagnostic results or medical history.
* Switch to the "analyze" tab to view a list of diseases.


## Next steps
* The API provides more information during the analysis step. For example, we can obtain a list of suggested tests to improve diagnosis. We should display such information.
* Display suggestions for features (/v1/dx/GetSuggestedFeatures_PatientProvided endpoint)
* Display errors
* (Maybe) Session ID persistence. Not a huge fan of this, as the user's phone could be compromised and potentially leak diagnostic data.
