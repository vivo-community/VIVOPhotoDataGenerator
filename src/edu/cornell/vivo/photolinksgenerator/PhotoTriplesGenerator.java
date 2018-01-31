package edu.cornell.vivo.photolinksgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

public class PhotoTriplesGenerator {

	private static final Logger LOGGER = Logger.getLogger(PhotoTriplesGenerator.class.getName());
	private static Boolean newDataExists = false;
	private String currentDate;
	private String personURINamespace;
	
	public void generateTriples(String photosFolder, String outputFolder, String outputfilename, String ns) {
		personURINamespace = ns;
		currentDate = getCurrentDate();
		
		Model rdfmodel = ModelFactory.createDefaultModel();	

		Property mainImage = rdfmodel.createProperty("http://vitro.mannlib.cornell.edu/ns/vitro/public#mainImage");
		Property mimeType  = rdfmodel.createProperty("http://vitro.mannlib.cornell.edu/ns/vitro/public#mimeType");
		Property filename  = rdfmodel.createProperty("http://vitro.mannlib.cornell.edu/ns/vitro/public#filename");

		Property modTime          = rdfmodel.createProperty("http://vitro.mannlib.cornell.edu/ns/vitro/0.7#modTime");
		Property thumbnailImage   = rdfmodel.createProperty("http://vitro.mannlib.cornell.edu/ns/vitro/public#thumbnailImage");
		Property downloadLocation = rdfmodel.createProperty("http://vitro.mannlib.cornell.edu/ns/vitro/public#downloadLocation");
		Property directDownloadUrl = rdfmodel.createProperty("http://vitro.mannlib.cornell.edu/ns/vitro/public#directDownloadUrl");

		Resource File = rdfmodel.createResource("http://vitro.mannlib.cornell.edu/ns/vitro/public#File");
		Resource FileByteStream = rdfmodel.createResource("http://vitro.mannlib.cornell.edu/ns/vitro/public#FileByteStream");

		File inputFolder = new File(photosFolder);
		if(!inputFolder.isDirectory()) {
			System.err.println(inputFolder.getAbsolutePath() +" is not a directoty");
			return;
		}
		
		Set<String> newFoldersToBePushed = new HashSet<String>();
		
		for(File file: inputFolder.listFiles()){
			if(file.isDirectory() || file.getName().startsWith(".")) continue;
			String personURILocalName  = file.getName().substring(0, file.getName().indexOf("."));
			// if image is processed, there must be a folder in file directory
			if(personAlreadyProcessed(personURILocalName, new File(outputFolder))){
				LOGGER.info(personURILocalName + " already processed..continuing!");
				continue; // already exists
			}else{
				newFoldersToBePushed.add(personURILocalName.substring(0, 3));
				newDataExists = true;
			}
			
			Resource person = rdfmodel.createResource(personURINamespace+personURILocalName);
			Resource person_mainImage = rdfmodel.createResource(personURINamespace+personURILocalName+"_mainImage");
			person.addProperty(mainImage, person_mainImage);
			person_mainImage.addProperty(mimeType, "image/jpeg");
			person_mainImage.addProperty(filename, personURILocalName+".jpg");
			person_mainImage.addProperty(RDF.type, File);

			person_mainImage.addProperty(modTime, currentDate);

			Resource person_thumbnailImage = rdfmodel.createResource(personURINamespace+personURILocalName+"_thumbnailImage");
			person_mainImage.addProperty(thumbnailImage, person_thumbnailImage);
		
			person_thumbnailImage.addProperty(mimeType, "image/jpeg");
			person_thumbnailImage.addProperty(filename, personURILocalName+".jpg");
			person_thumbnailImage.addProperty(RDF.type, File);
			person_thumbnailImage.addProperty(modTime, currentDate);

			// PATH 1
			Resource person_thumbnailImage_downloadLocation = rdfmodel.createResource(personURINamespace+"n"+personURILocalName+"tdL77");
			person_thumbnailImage.addProperty(downloadLocation, person_thumbnailImage_downloadLocation);
			String localName = person_thumbnailImage_downloadLocation.getLocalName();
			
			String path = getPath(localName);
			File filedir = new File(new File(outputFolder), "file/"+path);
			filedir.mkdirs();
			Path src = FileSystems.getDefault().getPath(inputFolder.getAbsolutePath(), personURILocalName+".jpg");
			Path tgt = FileSystems.getDefault().getPath(filedir.getAbsolutePath()+"/");
			
			try {
				if(!tgt.resolve(src.getFileName()).toFile().exists()){
					Files.copy(src, tgt.resolve(src.getFileName()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
			person_thumbnailImage_downloadLocation.addProperty(directDownloadUrl, "/file/"+localName+"/"+personURILocalName+".jpg");
			person_thumbnailImage_downloadLocation.addProperty(RDF.type, FileByteStream);
			person_thumbnailImage_downloadLocation.addProperty(modTime, currentDate);

			
			// PATH 2
			Resource person_downloadLocation = rdfmodel.createResource(personURINamespace+"n"+personURILocalName+"dL77");
			person_mainImage.addProperty(downloadLocation, person_downloadLocation);
			String localName2 = person_downloadLocation.getLocalName();
			String path2 = getPath(localName2);
			File filedir2 = new File(new File(outputFolder), "file/"+path2);
			filedir2.mkdirs();
			Path src2 = FileSystems.getDefault().getPath(inputFolder.getAbsolutePath(), personURILocalName+".jpg");
			Path tgt2 = FileSystems.getDefault().getPath(filedir2.getAbsolutePath()+"/");
			try {
				if(!tgt2.resolve(src2.getFileName()).toFile().exists()){
					Files.copy(src2, tgt2.resolve(src2.getFileName()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			person_downloadLocation.addProperty(directDownloadUrl, "/file/"+localName+"/"+personURILocalName+".jpg");
			person_downloadLocation.addProperty(RDF.type, FileByteStream);
			person_downloadLocation.addProperty(modTime, currentDate);	
			
		}
		
		if(newDataExists){
			// save RDF model.
			try {
				LOGGER.info("Save Photo Triples RDF Model...");
				saveRDFModel(rdfmodel, getNewFileName(outputFolder+"/"+outputfilename));
				LOGGER.info("Save Photo Triples RDF Model...completed");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else{
			LOGGER.info("Photo Triples: NO NEW DATA FOUND!!...");
		}
		
	}

	private String getNewFileName(String filePath){
		int i = 1;
		File file = new File(filePath);
		while(file.exists()){
			filePath = file.getAbsolutePath();
			filePath = filePath.substring(0, filePath.lastIndexOf("."))+i+".nt";
			file = new File(filePath);
			i++;
		}
		return filePath;
	}
	
	private boolean personAlreadyProcessed(String netId, File outputFolder) {
		String subNetId = netId.substring(0, 3);
		File folder = new File (outputFolder, "file/"+subNetId);
		return (folder.exists());
	}

	private String getPath(String localName) {
		//System.out.println(localName);
		String path = "";
		int start = 1;
		int end = 4;
		while(localName.length() > 3){
			String split = localName.substring(start, end);
			path = path.concat(split+"/");
			localName = localName.substring(end);
			start = 0;
			end = 3;
		}
		path = path + localName+"/";
		//System.out.println(path);
		return path;
	}

	private void saveRDFModel(Model rdfModel, String filePath) throws FileNotFoundException {
		PrintWriter printer = null;
		printer = new PrintWriter(filePath);
		rdfModel.write(printer, "N-Triples");
		printer.flush();
		printer.close();
	}
	
	private String getCurrentDate() {
		String date = null;
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		date = dateFormatter.format(now);
		LOGGER.info(date);
		return date;
	}
}
