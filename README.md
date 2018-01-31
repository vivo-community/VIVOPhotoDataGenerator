# VIVO Photo Data Generator
This code generate triples and file directories required for person photos to be displayed in VIVO.

Run the jar file as below:
```
java -jar VIVOPhotoLinksGenerator.jar <InputFolderPath> <OutputFolderPath> <PrsonURINamesapce>
```
For example

```
java -jar VIVOPhotoLinksGenerator.jar /mj495/document/photos/input/ /mj495/document/photos/output/ http://vivo.cornell.edu/individual/
```

Input Folder should contain the person photos in jpg format.
Output Folder should contain the 1) .nt triple files and 2) the file directories that need to be copied in VIVO. Do not copy the whole file folder to file_storage_root/a~n/  but only the subdirectories.


** The default person URI is set to ``` http://scholars.cornell.edu/individual/ ```
