# VIVOPhotoDataGenerator
This code generate triples and file directory required for person photos to be displayed in VIVO.

Run the jar file as below:
```
java -jar PhotoTriplesGeneratorEntryPoint.jar <InputFolderPath> <OutputFolderPath> <PrsonURINamesapce>
```
For example

```
java -jar PhotoTriplesGeneratorEntryPoint.jar /mj495/document/photos/input/ /mj495/document/photos/output/ http://vivo.cornell.edu/individual/
```

** The default person URI is set to ``` http://scholars.cornell.edu/individual/ ```
