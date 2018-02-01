# VIVO Photo Data Generator
This code generate triples and file directories required for person photos to be displayed in VIVO.

## How to run jar file.

Run the jar file as below:
```
java -jar VIVOPhotoLinksGenerator.jar <InputFolderPath> <OutputFolderPath> <PrsonURINamesapce>
```
For example

```
java -jar VIVOPhotoLinksGenerator.jar /mj495/document/photos/input/ /mj495/document/photos/output/ http://vivo.cornell.edu/individual/
```
### Input Folder
Input Folder should contain the person photos in jpg format. The jpg file name must match with the localname of a person's URI. For example if a person's URI is http://scholars.cornell.edu/individual/mj495 , then jpg filename must be mj495.jpg. This is how we link a peron to its image.

### Output Folder
At the end of jar file execution, output folder should contain the 1) .nt triple file (that should go in triplestore) and 2) a folder named as "file". The subdirectories of the "file" folder should be copied in VIVO (/file_storage_root/a~n/). 
#### Do not copy the complete "file" folder, but only the subdirectories.

### Default Person URI
If no URI is given as Java run parameters, the default person URI is set to ``` http://scholars.cornell.edu/individual/ ```


## How to run jar file periodically

There may be the cases when new persons are added in a VIVO instance and one wants to add their photos. All one needs to do is to add their photos (with correct image names) in input folder. The program will automatically check which photos has already been processed (by looking into output/file folder. At the end of the jar file execution, a new triple file will be added in the output folder.
Currently the file directories will be added in existing output/file folder. This part of code can be updated in furture.
