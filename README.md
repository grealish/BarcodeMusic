# BarcodeMusic App (Alpha) 
======
This app an app that allows your to search for music by scanning the barcode of a real album and searching this music online. It makes use of the SanditSDK camera BarcodeScanner and a REST client to lookup the albums EAN-13 barcode number. Currently a Alpha build in continnous developpment, please see the feature roadmap below

<p align="center">
<img src"https://raw.githubusercontent.com/grealish/BarcodeMusic/master/raw/barcodemusic-scanactivity-v0.1.png" alt="Scanning Activity" />
</p>

## Feature Roadmap

* Scans Music Album CDs and DVD's that have a Barcode EAN-13
* Uses Scandit Product API to lookup barcode details
* Displays the Album name 
* Searches Music API (Rovi) to get album statics and album art
* Optionally Intents the Sonos App to play the featured single or top track in the album
* Saves a history of searches locally
* Search your history from the ActionBar

## Development

### Build

Do the regular Git pull, check that gradle is working on your system already or if you have android studio it's possible to build in the UI

````
git clone git@github.com:grealish/BarcodeMusic.git
cd BarcodeMusic/
gradle clean build
cd app/build/outputs/apk/ "you find the apk files here"
````
### Testing

More to come, adding gradle test tools

### TODO:

* Refactor AsyncTask
* Add ActionBar Search
* Add Barcode Settings
* Consolidate Scans to EAN-13 and UPC
* Add unittests

## Referances

* SDK http://scandit.com/
* API http://scandit.com/
* Music API http://developer.rovicorp.com/
* Android Studio (IntelliJ)


## Contact

* Grealish http://twitter.com/grealish