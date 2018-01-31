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
### Input Folder
Input Folder should contain the person photos in jpg format.

### Output Folder
At the end of jar file execution, output folder should contain the 1) .nt triple file (that should go in triplestore) and 2) a folder named as "file". The subdirectories of the file folder should be copied in VIVO (/file_storage_root/a~n/). 
#### Do not copy the complete file folder, but only the subdirectories.

### Default Person URI
The default person URI is set to ``` http://scholars.cornell.edu/individual/ ```
