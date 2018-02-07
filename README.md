# VIVO Photo Data Generator
The Java code generate triples and file directories required for person photos to be displayed in VIVO. There is also a self-contained runnable jar file that can be used separately.

## Depenedency : 
Jena Library (https://jena.apache.org/)

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
Input Folder should contain the person photos in jpg format. The jpg file name must match with the localname of a person's URI. For example if a person's URI is http://scholars.cornell.edu/individual/mj495 , then jpg filename must be mj495.jpg. This is how we create a person URI and link the person URI to the image.

### Output Folder
At the end of jar file execution, output folder should contain 1) a new .nt triple file (that should go in triplestore) and 2) a folder - named as current date (for example, "2018-02-07"). The subdirectories of the newly created folder should be copied in VIVO (/file_storage_root/a~n/). 
#### Do not copy the complete folder, but only the subdirectories.

### Default Person URI
If no URI is given as Java run parameters, the default person URI is set to ``` http://scholars.cornell.edu/individual/ ```


## How to run jar file periodically

There may be the cases when new persons are added in a VIVO instance and one wants to add their photos. All one needs to do is to add their photos (with correct photo names) in input folder. The program will automatically check which photos has already been processed (by looking into subdirectories of output folder . At the end of the jar file execution, a new .nt triple file will be added in the output folder and a new (if it does not already exist) subdirectory - named as current date. (for example, if current date is Feb 9th 2018, the folder name would be "2018-02-09"). 

## Contact 
Muhammad Javed (mj495@cornell.edu) - Tech Lead (Scholars@Cornell)

