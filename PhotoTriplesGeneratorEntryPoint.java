package edu.cornell.vivo.photolinksgenerator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Entry Point
 * @author Muhammad Javed (Cornell University)
 */
public class PhotoTriplesGeneratorEntryPoint {

	private static final Logger LOGGER = Logger.getLogger(PhotoTriplesGeneratorEntryPoint.class.getName());
	
	
	private static String INPUT_FOLDER;
	private static String OUTPUT_FOLDER;
	private static String OUTPUT_FILENAME_NT = "photo-triple";
	private static String PERSON_URI_NS = "http://scholars.cornell.edu/individual/";
	
	
	public static void main(String[] args) {
		try {
			if(args.length >= 2){
				init(args);
			}else{
				LOGGER.info("Input Parameters not found....");
				LOGGER.info("java -jar PhotoTriplesGeneratorEntryPoint.jar <InputFolderPath> <OutputFolderPath> <PrsonURINamesapce>");
				LOGGER.info("For example...");
				LOGGER.info("java -jar PhotoTriplesGeneratorEntryPoint.jar /mj495/document/photos/input/ /mj495/document/photos/output/ http://scholars.cornell.edu/individual/");
				LOGGER.info("Returning.....");
				
				String ar[] = {
						"/Users/mj495/Documents/PostProcessFiles/TEST-PHOTO/photos",
						"/Users/mj495/Documents/PostProcessFiles/TEST-PHOTO/output"};
				init(ar);
				
			}
			PhotoTriplesGeneratorEntryPoint obj = new PhotoTriplesGeneratorEntryPoint();
			obj.runProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void runProcess() {
		PhotoTriplesGenerator obj = new PhotoTriplesGenerator();
		OUTPUT_FILENAME_NT = OUTPUT_FILENAME_NT+"-"+getCurrentDate()+".nt";
		obj.generateTriples(INPUT_FOLDER, OUTPUT_FOLDER, OUTPUT_FILENAME_NT, PERSON_URI_NS);
	}

	public static void init(String args[]) throws IOException{
		INPUT_FOLDER = args[0];
		OUTPUT_FOLDER = args[1];
		if(args.length > 2){
			PERSON_URI_NS = args[2];
		}
		generateDirectories(OUTPUT_FOLDER);
	}

	private static void generateDirectories(String propFilePath) throws IOException {
		// CREATE NEW DIRECTORIES
		createFolder(new File(OUTPUT_FOLDER+"/"+getCurrentDate()));
	}

	private static void createFolder(File file) {
		if (!file.exists()) {
			if (file.mkdirs()) {
				LOGGER.info(file.getAbsolutePath()+" folder created!");
			} else {
				LOGGER.throwing("PhotoTriplesGeneratorEntryPoint", "createFolder", new Throwable("EXCEPTION: Could not create folder..."));
			}
		}
	}
	
	private static String getCurrentDate() {
		String date = null;
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		date = dateFormatter.format(now);
		LOGGER.info(date);
		return date;
	}
}
